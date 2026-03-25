#!/usr/bin/env groovy

/**
 * Report Comparison Script for ILCDB
 *
 * This script helps compare report results between branches to identify discrepancies.
 *
 * USAGE:
 * 1. Run from Grails console or: grails run-script scripts/CompareReportResults.groovy
 * 2. Script will generate a results file with client IDs and intake details
 * 3. Run on both branches, then use compareFiles() to see differences
 *
 * COMPARISON WORKFLOW:
 * - On ilcdb-speedup branch: Run generateReport() -> saves to /tmp/report_speedup.csv
 * - Switch to main branch: Run generateReport() -> saves to /tmp/report_main.csv
 * - Run compareFiles('/tmp/report_main.csv', '/tmp/report_speedup.csv')
 */

import org.joda.time.Interval
import java.text.SimpleDateFormat

// Configuration - adjust these parameters for your test case
def TEST_START_DATE = new Date(2023 - 1900, 0, 1)  // Jan 1, 2023
def TEST_END_DATE = new Date(2023 - 1900, 11, 31)  // Dec 31, 2023
def MUNICIPALITY = null
def MUN_TYPE = "Any"
def ATTORNEY = "Any"
def INTAKE_STATE = "any"
def INTAKE_TYPE = "any"
def STATUS_ACHIEVED = "any"
def HOME_COUNTRY = null

println "=" * 80
println "ILCDB Report Comparison Script"
println "=" * 80

def generateReport(String outputFile = "/tmp/report_results.csv") {
    println "\n>>> Generating report with parameters:"
    println "    Start Date: ${TEST_START_DATE}"
    println "    End Date: ${TEST_END_DATE}"
    println "    Municipality: ${MUNICIPALITY ?: 'Any'}"
    println "    Attorney: ${ATTORNEY}"
    println "    Status: ${STATUS_ACHIEVED}"

    // Adjust end date to end of day (same logic as controller)
    def endDate = new Date(TEST_END_DATE.getTime() + 1000L * 24L * 60L * 60L - 1L)

    // Build the query (mimics ClientController logic)
    def CLIENTS_QUERY = """
        select distinct client from
         Client as client
           inner join fetch client.client as person
           inner join fetch person.address as address
           inner join fetch person.placeOfBirth as placeOfBirth
           inner join fetch client.cases as intake
    """

    def COMPLETED_INTAKES_QUERY = " ( intake.completionDate >= :startDate AND intake.completionDate <= :endDate ) "
    def OPENED_INTAKES_QUERY = " ( intake.startDate >= :startDate AND intake.startDate <= :endDate )"
    def ONGOING_INTAKES_QUERY = " ( intake.startDate <= :endDate AND (intake.completionDate is NULL OR intake.completionDate >= :endDate) ) "
    def COMBINED_INTAKES_QUERY = COMPLETED_INTAKES_QUERY + " OR " + OPENED_INTAKES_QUERY + " OR " + ONGOING_INTAKES_QUERY

    def query = CLIENTS_QUERY + "WHERE ( " + COMBINED_INTAKES_QUERY + " )"

    // Add municipality filter if needed
    if (MUNICIPALITY && MUN_TYPE != "Any") {
        if (MUN_TYPE == "State") {
            query += " and (upper(address.state) = upper(:mun))"
        } else if (MUN_TYPE == "City") {
            query += " and upper(address.city) = upper(:mun)"
        } else if (MUN_TYPE == "County") {
            query += " and upper(address.county) = upper(:mun)"
        }
    }

    // Add attorney filter if needed
    if (ATTORNEY && ATTORNEY != "Any") {
        query += " and intake.attorney.firstName = :attorney"
    }

    // Add home country filter if needed
    if (HOME_COUNTRY && HOME_COUNTRY != "-1") {
        query += " and upper(placeOfBirth.country.name) = :homeCountryName"
    }

    // Build named parameters
    def namedParams = [startDate: TEST_START_DATE, endDate: endDate]
    if (MUNICIPALITY && MUN_TYPE != "Any") {
        namedParams.mun = MUNICIPALITY
    }
    if (ATTORNEY && ATTORNEY != "Any") {
        namedParams.attorney = ATTORNEY
    }
    if (HOME_COUNTRY && HOME_COUNTRY != "-1") {
        namedParams.homeCountryName = Country.get(HOME_COUNTRY).name
    }

    println "\n>>> Executing query..."
    println "    Query: ${query.take(200)}..."

    def clients = Client.executeQuery(query, namedParams)

    println "\n>>> Found ${clients.size()} clients"

    // Write results to CSV file
    def csv = new File(outputFile)
    csv.write("")  // Clear file

    def dateFormat = new SimpleDateFormat("yyyy-MM-dd")

    // Write header
    csv << "ClientID,ClientName,IntakeID,IntakeType,StartDate,CompletionDate,Attorney,City,State,HomeCountry\n"

    // Write each client and their intakes
    def interval = new Interval(TEST_START_DATE.getTime(), endDate.getTime())
    clients.each { client ->
        // Get intakes that match the date criteria
        client.cases?.each { intake ->
            def intakeStartInRange = intake.startDate >= TEST_START_DATE && intake.startDate <= endDate
            def intakeCompletedInRange = intake.completionDate && intake.completionDate >= TEST_START_DATE && intake.completionDate <= endDate
            def intakeOngoing = intake.completionDate == null || intake.completionDate >= endDate

            // Only include intakes that match the query criteria
            if (intakeStartInRange || intakeCompletedInRange || intakeOngoing) {
                csv << "${client.id},"
                csv << "\"${client.client?.toString()?.replaceAll('"', '""')}\","
                csv << "${intake.id},"
                csv << "${intake.intakeType},"
                csv << "${intake.startDate ? dateFormat.format(intake.startDate) : 'null'},"
                csv << "${intake.completionDate ? dateFormat.format(intake.completionDate) : 'null'},"
                csv << "\"${intake.attorney?.toString() ?: ''}\","
                csv << "\"${client.client?.address?.city ?: ''}\","
                csv << "\"${client.client?.address?.state ?: ''}\","
                csv << "\"${client.client?.placeOfBirth?.country?.name ?: ''}\""
                csv << "\n"
            }
        }
    }

    println "\n>>> Results written to: ${outputFile}"
    println "    Total rows: ${clients.size()}"
    println "\n" + "=" * 80

    return [file: outputFile, count: clients.size(), clients: clients]
}

def compareFiles(String file1, String file2) {
    println "\n" + "=" * 80
    println "COMPARING REPORT RESULTS"
    println "=" * 80
    println "File 1: ${file1}"
    println "File 2: ${file2}"

    def lines1 = new File(file1).readLines()
    def lines2 = new File(file2).readLines()

    def set1 = lines1.toSet()
    def set2 = lines2.toSet()

    def onlyIn1 = set1 - set2
    def onlyIn2 = set2 - set1
    def common = set1.intersect(set2)

    println "\n>>> Summary:"
    println "    Total in File 1: ${lines1.size() - 1} (excluding header)"
    println "    Total in File 2: ${lines2.size() - 1} (excluding header)"
    println "    Common: ${common.size() - 1} (excluding header)"
    println "    Only in File 1: ${onlyIn1.size()}"
    println "    Only in File 2: ${onlyIn2.size()}"

    if (onlyIn1.size() > 0) {
        println "\n>>> Records ONLY in File 1 (${file1}):"
        onlyIn1.take(20).each { println "    ${it}" }
        if (onlyIn1.size() > 20) {
            println "    ... and ${onlyIn1.size() - 20} more"
        }
    }

    if (onlyIn2.size() > 0) {
        println "\n>>> Records ONLY in File 2 (${file2}):"
        onlyIn2.take(20).each { println "    ${it}" }
        if (onlyIn2.size() > 20) {
            println "    ... and ${onlyIn2.size() - 20} more"
        }
    }

    // Write diff to file
    def diffFile = "/tmp/report_diff.txt"
    new File(diffFile).withWriter { writer ->
        writer.println "REPORT COMPARISON DIFF"
        writer.println "=" * 80
        writer.println "File 1: ${file1}"
        writer.println "File 2: ${file2}"
        writer.println ""
        writer.println "ONLY IN FILE 1:"
        writer.println "-" * 80
        onlyIn1.each { writer.println it }
        writer.println ""
        writer.println "ONLY IN FILE 2:"
        writer.println "-" * 80
        onlyIn2.each { writer.println it }
    }

    println "\n>>> Full diff written to: ${diffFile}"
    println "=" * 80

    return [onlyIn1: onlyIn1, onlyIn2: onlyIn2, common: common]
}

// Quick run function with current date range
def quickCompare() {
    println "\n>>> Running quick comparison with last year's data..."

    // Generate on current branch
    def result = generateReport("/tmp/report_current_branch.csv")

    println "\n>>> Next steps:"
    println "    1. Switch to the other branch (main or ilcdb-speedup)"
    println "    2. Run: generateReport('/tmp/report_other_branch.csv')"
    println "    3. Run: compareFiles('/tmp/report_current_branch.csv', '/tmp/report_other_branch.csv')"
}

// Run quick comparison by default
quickCompare()

// Return functions for interactive use
return [
    generateReport: this.&generateReport,
    compareFiles: this.&compareFiles,
    quickCompare: this.&quickCompare
]
