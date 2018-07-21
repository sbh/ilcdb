import grails.transaction.Transactional
import org.joda.time.Interval

@Transactional
class ClientService
{
    def filterStatus(Collection clients, String statusAchieved, String intakeState, Interval interval)
    {
        def results = []
        if ("staff-advise" == statusAchieved) {
            clients.each { client ->
                if (client.hasStaffAdvise(intakeState, interval))
                    results += client
            }
        }
        else if ("staff-representation" == statusAchieved)
            clients.each { client ->
                if (client.hasStaffRepresentation(intakeState, interval))
                    results += client
            }
        else if ("lpr" == statusAchieved)
            clients.each { client ->
                if (client.hasAttemptedLPR(interval))
                    results += client
            }
        else if ("citizenship" == statusAchieved)
            clients.each { client ->
                if (client.hasAttemptedCitizenship(interval))
                    results += client
            }
        else if ("daca" == statusAchieved)
            clients.each { client ->
                if (client.hasAttemptedDACA(interval))
                    results += client
            }
        else if ("tps" == statusAchieved)
            clients.each { client ->
                if (client.hasAttemptedTPS(interval))
                    results += client
            }
        else if ("none" == statusAchieved)
            clients.each { client ->
                if (client.hasAttemptedNoStatus(interval))
                    results += client
            }
        else if ("any" == statusAchieved)
            clients.each { client ->
                if (client.hasAttemptedAnyStatus(interval))
                    results += client
            }
        else
            results = clients
        
        return results
    }
}
