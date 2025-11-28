/**
 * Quick Report Comparison - Paste directly into Grails Console
 *
 * USAGE:
 * 1. Copy this entire file
 * 2. Paste into Grails console (grails console)
 * 3. Results will be displayed and saved to /tmp/
 */

// === CONFIGURATION ===
// Adjust these parameters for your specific test case
START_DATE = new Date(2023 - 1900, 0, 1)   // Jan 1, 2023
END_DATE = new Date(2023 - 1900, 11, 31)   // Dec 31, 2023
MUNICIPALITY = null
MUN_TYPE = "Any"
ATTORNEY = "Any"
HOME_COUNTRY = null

println "\n" + "=" * 80
println "QUICK REPORT COMPARISON"
println "=" * 80
println "Date Range: ${START_DATE} to ${END_DATE}"
println "Municipality: ${MUNICIPALITY ?: 'Any'} (${MUN_TYPE})"
println "Attorney: ${ATTORNEY}"

// Adjust end date to end of day
endDate = new Date(END_DATE.getTime() + 1000L * 24L * 60L * 60L - 1L)

// Build query
CLIENTS_QUERY = """
    select distinct client from
     Client as client
       inner join fetch client.client as person
       inner join fetch person.address as address
       inner join fetch person.placeOfBirth as placeOfBirth
       inner join fetch client.cases as intake
"""

COMPLETED_INTAKES = " ( intake.completionDate >= :startDate AND intake.completionDate <= :endDate ) "
OPENED_INTAKES = " ( intake.startDate >= :startDate AND intake.startDate <= :endDate )"
ONGOING_INTAKES = " ( intake.startDate <= :endDate AND (intake.completionDate is NULL OR intake.completionDate >= :endDate) ) "
COMBINED_INTAKES = COMPLETED_INTAKES + " OR " + OPENED_INTAKES + " OR " + ONGOING_INTAKES

query = CLIENTS_QUERY + "WHERE ( " + COMBINED_INTAKES + " )"

// Build parameters
namedParams = [startDate: START_DATE, endDate: endDate]

println "\n>>> Executing query..."
clients = Client.executeQuery(query, namedParams)

println "\n>>> RESULTS:"
println "    Total Clients: ${clients.size()}"

// Group by intake count
intakeCounts = [:]
clients.each { client ->
    def matchingIntakes = 0
    client.cases?.each { intake ->
        def intakeStartInRange = intake.startDate >= START_DATE && intake.startDate <= endDate
        def intakeCompletedInRange = intake.completionDate && intake.completionDate >= START_DATE && intake.completionDate <= endDate
        def intakeOngoing = intake.completionDate == null || intake.completionDate >= endDate

        if (intakeStartInRange || intakeCompletedInRange || intakeOngoing) {
            matchingIntakes++
        }
    }
    intakeCounts[matchingIntakes] = (intakeCounts[matchingIntakes] ?: 0) + 1
}

println "\n>>> Breakdown by Intake Count:"
intakeCounts.sort().each { count, numClients ->
    println "    ${numClients} clients with ${count} matching intake(s)"
}

// Save client IDs to file for comparison
def outputFile = "/tmp/report_client_ids.txt"
new File(outputFile).withWriter { writer ->
    writer.println "# Report Results - ${new Date()}"
    writer.println "# Date Range: ${START_DATE} to ${END_DATE}"
    writer.println "# Total Clients: ${clients.size()}"
    writer.println "#" + "=" * 70
    clients.sort { it.id }.each { client ->
        writer.println "${client.id}\t${client.client?.toString()}\t${client.cases?.size() ?: 0} intakes"
    }
}

println "\n>>> Client IDs saved to: ${outputFile}"
println "    Compare this file between branches using: diff /tmp/report_client_ids.txt /tmp/report_client_ids_other.txt"

// Show sample of first 10 clients
println "\n>>> Sample Results (first 10):"
clients.take(10).each { client ->
    println "    Client ${client.id}: ${client.client} - ${client.cases?.size() ?: 0} total intakes"
    client.cases?.each { intake ->
        def matchesRange = false
        def reason = ""

        if (intake.startDate >= START_DATE && intake.startDate <= endDate) {
            matchesRange = true
            reason = "opened in range"
        } else if (intake.completionDate && intake.completionDate >= START_DATE && intake.completionDate <= endDate) {
            matchesRange = true
            reason = "completed in range"
        } else if (intake.completionDate == null || intake.completionDate >= endDate) {
            matchesRange = true
            reason = "ongoing"
        }

        if (matchesRange) {
            println "      ✓ Intake ${intake.id}: ${intake.startDate?.format('yyyy-MM-dd')} -> ${intake.completionDate?.format('yyyy-MM-dd') ?: 'ongoing'} (${reason})"
        }
    }
}

println "\n" + "=" * 80
println "NEXT STEPS:"
println "1. Note the total client count: ${clients.size()}"
println "2. Switch branches: git checkout <other-branch>"
println "3. Restart Grails console and paste this script again"
println "4. Compare the two counts and the /tmp/report_client_ids.txt files"
println "=" * 80 + "\n"

// Return clients for further inspection if needed
clients
