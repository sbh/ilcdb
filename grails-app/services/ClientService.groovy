import grails.transaction.Transactional
import org.joda.time.Interval

@Transactional
class ClientService
{
    def statusFuncs =
        [
        "any": [{Client client, Interval interval -> Client.hasAttemptedAnyStatus(client, interval)},
                {Client client, Interval interval -> Client.hasAchievedAnyStatus(client, interval)}],
        "lpr": [{Client client, Interval interval -> Client.hasAttemptedLPR(client, interval)},
                {Client client, Interval interval -> Client.hasAchievedLPR(client, interval)}],
         "citizenship": [{Client client, Interval interval -> Client.hasAttemptedCitizenship(client, interval)},
                         {Client client, Interval interval -> Client.hasAchievedCitizenship(client, interval)}],
         "daca": [{Client client, Interval interval -> Client.hasAttemptedDACA(client, interval)},
                  {Client client, Interval interval -> Client.hasAchievedDACA(client, interval)}],
         "tps": [{Client client, Interval interval -> Client.hasAttemptedTPS(client, interval)},
                 {Client client, Interval interval -> Client.hasAchievedTPS(client, interval)}],
         "i-90": [{Client client, Interval interval -> Client.hasAttemptedI90(client, interval)},
                  {Client client, Interval interval -> Client.hasAchievedI90(client, interval)}],
         "eoir": [{Client client, Interval interval -> Client.hasAttemptedEOIR(client, interval)},
                  {Client client, Interval interval -> Client.hasAchievedEOIR(client, interval)}],
         "foia": [{Client client, Interval interval -> Client.hasAttemptedFOIA(client, interval)},
                  {Client client, Interval interval -> Client.hasAchievedFOIA(client, interval)}],
         "i-102": [{Client client, Interval interval -> Client.hasAttemptedI102(client, interval)},
                   {Client client, Interval interval -> Client.hasAchievedI102(client, interval)}],
         "i-129f": [{Client client, Interval interval -> Client.hasAttemptedI129F(client, interval)},
                    {Client client, Interval interval -> Client.hasAchievedI129F(client, interval)}],
         "i-130": [{Client client, Interval interval -> Client.hasAttemptedI130(client, interval)},
                   {Client client, Interval interval -> Client.hasAchievedI130(client, interval)}],
         "i-131": [{Client client, Interval interval -> Client.hasAttemptedI131(client, interval)},
                   {Client client, Interval interval -> Client.hasAchievedI131(client, interval)}],
         "1-192": [{Client client, Interval interval -> Client.hasAttemptedI192(client, interval)},
                   {Client client, Interval interval -> Client.hasAchievedI192(client, interval)}],
         "i-360": [{Client client, Interval interval -> Client.hasAttemptedI360(client, interval)},
                   {Client client, Interval interval -> Client.hasAchievedI360(client, interval)}],
         "i-539": [{Client client, Interval interval -> Client.hasAttemptedI539(client, interval)},
                   {Client client, Interval interval -> Client.hasAchievedI539(client, interval)}],
         "i-601": [{Client client, Interval interval -> Client.hasAttemptedI601(client, interval)},
                   {Client client, Interval interval -> Client.hasAchievedI601(client, interval)}],
         "i-751": [{Client client, Interval interval -> Client.hasAttemptedI751(client, interval)},
                   {Client client, Interval interval -> Client.hasAchievedI751(client, interval)}],
         "i-765": [{Client client, Interval interval -> Client.hasAttemptedI765(client, interval)},
                   {Client client, Interval interval -> Client.hasAchievedI765(client, interval)}],
         "i-821": [{Client client, Interval interval -> Client.hasAttemptedI821(client, interval)},
                   {Client client, Interval interval -> Client.hasAchievedI821(client, interval)}],
         "i-824": [{Client client, Interval interval -> Client.hasAttemptedI824(client, interval)},
                   {Client client, Interval interval -> Client.hasAchievedI824(client, interval)}],
         "i-881": [{Client client, Interval interval -> Client.hasAttemptedI881(client, interval)},
                   {Client client, Interval interval -> Client.hasAchievedI881(client, interval)}],
         "i-912": [{Client client, Interval interval -> Client.hasAttemptedI912(client, interval)},
                   {Client client, Interval interval -> Client.hasAchievedI912(client, interval)}],
         "i-914": [{Client client, Interval interval -> Client.hasAttemptedI914(client, interval)},
                   {Client client, Interval interval -> Client.hasAchievedI914(client, interval)}],
         "i-918": [{Client client, Interval interval -> Client.hasAttemptedI918(client, interval)},
                   {Client client, Interval interval -> Client.hasAchievedI918(client, interval)}],
         "i-929": [{Client client, Interval interval -> Client.hasAttemptedI929(client, interval)},
                   {Client client, Interval interval -> Client.hasAchievedI929(client, interval)}],
         "n-336": [{Client client, Interval interval -> Client.hasAttemptedN336(client, interval)},
                   {Client client, Interval interval -> Client.hasAchievedN336(client, interval)}],
         "n-400": [{Client client, Interval interval -> Client.hasAttemptedN400(client, interval)},
                   {Client client, Interval interval -> Client.hasAchievedN400(client, interval)}],
         "n-565": [{Client client, Interval interval -> Client.hasAttemptedN565(client, interval)},
                   {Client client, Interval interval -> Client.hasAchievedN565(client, interval)}],
         "n-600": [{Client client, Interval interval -> Client.hasAttemptedN600(client, interval)},
                   {Client client, Interval interval -> Client.hasAchievedN600(client, interval)}],
        "aos":  [{Client client, Interval interval -> Client.hasAttemptedAOS(client, interval)},
                   {Client client, Interval interval -> Client.hasAchievedAOS(client, interval)}]
    ]

    def filterStatus(Collection clients, String statusAchieved, String intakeState, Interval interval) {
        Set results = new HashSet()

        if ("staff-advise" == statusAchieved)
            results = clients.findAll {it.hasStaffAdvise(intakeState, interval)}
        else if ("staff-representation" == statusAchieved)
            results = clients.findAll {it.hasStaffRepresentation(intakeState, interval)}
        else if (statusFuncs.containsKey(statusAchieved)) {
            def functionTuple = statusFuncs.get(statusAchieved)
            def hasAttempted = functionTuple[0]
            def hasAchieved = functionTuple[1]

            switch(intakeState) {
                case "opened":
                    results = clients.findAll{ hasAttempted(it, interval) }
                    break
                case "closed":
                    results = clients.findAll{ hasAchieved(it, interval) }
                    break
                case "ongoing":
                    results = clients.findAll{ it.hasOngoingStaffRepresentation(it, StatusAchieved.Type.fromValue(statusAchieved), interval) }
                    break
                default:
                    results = clients.findAll{ it.hasAttemptedAnyStatus(it, interval) || it.hasAchievedAnyStatus(it, interval) }
                    break
            }
        }
        else if ("none" == statusAchieved)
            results = clients.findAll{it.hasAttemptedNoStatus(it, interval)}
        else if ("any" == statusAchieved)
            clients.findAll {it.hasAttemptedAnyStatus(it, interval)}
        else
            results = clients

        return results
    }
}
