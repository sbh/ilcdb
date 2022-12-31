import grails.transaction.Transactional
import org.joda.time.Interval

@Transactional
class ClientService
{
    def filterStatus(Collection clients, String statusAchieved, String intakeState, Interval interval) {
        Set results = new HashSet()

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
        else if ("lpr" == statusAchieved) {
            if ("opened" == intakeState) results = clients.findAll{it.hasAttemptedLPR(interval)}
            else if ("closed" == intakeState) results = clients.findAll{it.hasAchievedLPR(interval)}
            else results = clients.findAll{it.hasAttemptedLPR(interval) || it.hasAchievedLPR(interval)}
        }
        else if ("citizenship" == statusAchieved) {
            if ("opened" == intakeState) results = clients.findAll{it.hasAttemptedCitizenship(interval)}
            else if ("closed" == intakeState) results = clients.findAll{it.hasAchievedCitizenship(interval)}
            else results = clients.findAll{it.hasAttemptedCitizenship(interval) || it.hasAchievedCitizenship(interval)}
        }
        else if ("daca" == statusAchieved) {
            if ("opened" == intakeState) results = clients.findAll{it.hasAttemptedDACA(interval)}
            else if ("closed" == intakeState) results = clients.findAll{it.hasAchievedDACA(interval)}
            else results = clients.findAll{it.hasAttemptedDACA(interval) || it.hasAchievedDACA(interval)}
        }
        else if ("tps" == statusAchieved) {
            if ("opened" == intakeState) results = clients.findAll{it.hasAttemptedTPS(interval)}
            else if ("closed" == intakeState) results = clients.findAll{it.hasAchievedTPS(interval)}
            else results = clients.findAll{it.hasAttemptedTPS(interval) || it.hasAchievedTPS(interval)}
        }
        else if ("i-90" == statusAchieved) {
            if ("opened" == intakeState) results = clients.findAll{it.hasAttemptedI90(interval)}
            else if ("closed" == intakeState) results = clients.findAll{it.hasAchievedI90(interval)}
            else results = clients.findAll{it.hasAttemptedI90(interval) || it.hasAchievedI90(interval)}
        }
        else if ("eoir" == statusAchieved) {
            if ("opened" == intakeState) results = clients.findAll{it.hasAttemptedEOIR(interval)}
            else if ("closed" == intakeState) results = clients.findAll{it.hasAchievedEOIR(interval)}
            else results = clients.findAll{it.hasAttemptedEOIR(interval) || it.hasAchievedEOIR(interval)}
        }
        else if ("foia" == statusAchieved) {
            if ("opened" == intakeState) results = clients.findAll{it.hasAttemptedFOIA(interval)}
            else if ("closed" == intakeState) results = clients.findAll{it.hasAchievedFOIA(interval)}
            else results = clients.findAll{it.hasAttemptedFOIA(interval) || it.hasAchievedFOIA(interval)}
        }
        else if ("i-102" == statusAchieved) {
            if ("opened" == intakeState) results = clients.findAll{it.hasAttemptedI102(interval)}
            else if ("closed" == intakeState) results = clients.findAll{it.hasAchievedI102(interval)}
            else results = clients.findAll{it.hasAttemptedI102(interval) || it.hasAchievedI102(interval)}
        }
        else if ("i-129f" == statusAchieved) {
            if ("opened" == intakeState) results = clients.findAll{it.hasAttemptedI129F(interval)}
            else if ("closed" == intakeState) results = clients.findAll{it.hasAchievedI129F(interval)}
            else results = clients.findAll{it.hasAttemptedI129F(interval) || it.hasAchievedI129F(interval)}
        }
        else if ("i-130" == statusAchieved) {
            if ("opened" == intakeState) results = clients.findAll{it.hasAttemptedI130(interval)}
            else if ("closed" == intakeState) results = clients.findAll{it.hasAchievedI130(interval)}
            else results = clients.findAll{it.hasAttemptedI130(interval) || it.hasAchievedI130(interval)}
        }
        else if ("i-131" == statusAchieved) {
            if ("opened" == intakeState) results = clients.findAll{it.hasAttemptedI131(interval)}
            else if ("closed" == intakeState) results = clients.findAll{it.hasAchievedI131(interval)}
            else results = clients.findAll{it.hasAttemptedI131(interval) || it.hasAchievedI131(interval)}
        }
        else if ("i-192" == statusAchieved) {
            if ("opened" == intakeState) results = clients.findAll{it.hasAttemptedI192(interval)}
            else if ("closed" == intakeState) results = clients.findAll{it.hasAchievedI192(interval)}
            else results = clients.findAll{it.hasAttemptedI192(interval) || it.hasAchievedI192(interval)}
        }
        else if ("i-360" == statusAchieved) {
            if ("opened" == intakeState) results = clients.findAll{it.hasAttemptedI360(interval)}
            else if ("closed" == intakeState) results = clients.findAll{it.hasAchievedI360(interval)}
            else results = clients.findAll{it.hasAttemptedI360(interval) || it.hasAchievedI360(interval)}
        }
        else if ("i-539" == statusAchieved) {
            if ("opened" == intakeState) results = clients.findAll{it.hasAttemptedI539(interval)}
            else if ("closed" == intakeState) results = clients.findAll{it.hasAchievedI539(interval)}
            else results = clients.findAll{it.hasAttemptedI539(interval) || it.hasAchievedI539(interval)}
        }
        else if ("i-601" == statusAchieved) {
            if ("opened" == intakeState) results = clients.findAll{it.hasAttemptedI601(interval)}
            else if ("closed" == intakeState) results = clients.findAll{it.hasAchievedI601(interval)}
            else results = clients.findAll{it.hasAttemptedI601(interval) || it.hasAchievedI601(interval)}
        }
        else if ("i-751" == statusAchieved) {
            if ("opened" == intakeState) results = clients.findAll{it.hasAttemptedI751(interval)}
            else if ("closed" == intakeState) results = clients.findAll{it.hasAchievedI751(interval)}
            else results = clients.findAll{it.hasAttemptedI751(interval) || it.hasAchievedI751(interval)}
        }
        else if ("i-765" == statusAchieved) {
            if ("opened" == intakeState) results = clients.findAll{it.hasAttemptedI765(interval)}
            else if ("closed" == intakeState) results = clients.findAll{it.hasAchievedI765(interval)}
            else results = clients.findAll{it.hasAttemptedI765(interval) || it.hasAchievedI765(interval)}
        }
        else if ("i-821" == statusAchieved) {
            if ("opened" == intakeState) results = clients.findAll{it.hasAttemptedI821(interval)}
            else if ("closed" == intakeState) results = clients.findAll{it.hasAchievedI821(interval)}
            else results = clients.findAll{it.hasAttemptedI821(interval) || it.hasAchievedI821(interval)}
        }
        else if ("i-824" == statusAchieved) {
            if ("opened" == intakeState) results = clients.findAll{it.hasAttemptedI824(interval)}
            else if ("closed" == intakeState) results = clients.findAll{it.hasAchievedI824(interval)}
            else results = clients.findAll{it.hasAttemptedI824(interval) || it.hasAchievedI824(interval)}
        }
        else if ("i-881" == statusAchieved) {
            if ("opened" == intakeState) results = clients.findAll{it.hasAttemptedI881(interval)}
            else if ("closed" == intakeState) results = clients.findAll{it.hasAchievedI881(interval)}
            else results = clients.findAll{it.hasAttemptedI881(interval) || it.hasAchievedI881(interval)}
        }
        else if ("i-912" == statusAchieved) {
            if ("opened" == intakeState) results = clients.findAll{it.hasAttemptedI912(interval)}
            else if ("closed" == intakeState) results = clients.findAll{it.hasAchievedI912(interval)}
            else results = clients.findAll{it.hasAttemptedI912(interval) || it.hasAchievedI912(interval)}
        }
        else if ("i-914" == statusAchieved) {
            if ("opened" == intakeState) results = clients.findAll{it.hasAttemptedI914(interval)}
            else if ("closed" == intakeState) results = clients.findAll{it.hasAchievedI914(interval)}
            else results = clients.findAll{it.hasAttemptedI914(interval) || it.hasAchievedI914(interval)}
        }
        else if ("i-918" == statusAchieved) {
            if ("opened" == intakeState) results = clients.findAll{it.hasAttemptedI918(interval)}
            else if ("closed" == intakeState) results = clients.findAll{it.hasAchievedI918(interval)}
            else results = clients.findAll{it.hasAttemptedI918(interval) || it.hasAchievedI918(interval)}
        }
        else if ("i-929" == statusAchieved) {
            if ("opened" == intakeState) results = clients.findAll{it.hasAttemptedI929(interval)}
            else if ("closed" == intakeState) results = clients.findAll{it.hasAchievedI929(interval)}
            else results = clients.findAll{it.hasAttemptedI929(interval) || it.hasAchievedI929(interval)}
        }
        else if ("n-336" == statusAchieved) {
            if ("opened" == intakeState) results = clients.findAll{it.hasAttemptedN336(interval)}
            else if ("closed" == intakeState) results = clients.findAll{it.hasAchievedN336(interval)}
            else results = clients.findAll{it.hasAttemptedN336(interval) || it.hasAchievedN336(interval)}
        }
        else if ("n-400" == statusAchieved) {
            if ("opened" == intakeState) results = clients.findAll{it.hasAttemptedN400(interval)}
            else if ("closed" == intakeState) results = clients.findAll{it.hasAchievedN400(interval)}
            else results = clients.findAll{it.hasAttemptedN400(interval) || it.hasAchievedN400(interval)}
        }
        else if ("n-565" == statusAchieved) {
            if ("opened" == intakeState) results = clients.findAll{it.hasAttemptedN565(interval)}
            else if ("closed" == intakeState) results = clients.findAll{it.hasAchievedN565(interval)}
            else results = clients.findAll{it.hasAttemptedN565(interval) || it.hasAchievedN565(interval)}
        }
        else if ("n-600" == statusAchieved) {
            if ("opened" == intakeState) results = clients.findAll{it.hasAttemptedN600(interval)}
            else if ("closed" == intakeState) results = clients.findAll{it.hasAchievedN600(interval)}
            else results = clients.findAll{it.hasAttemptedN600(interval) || it.hasAchievedN600(interval)}
        }
        else if ("none" == statusAchieved)
            clients.each { client ->
                if (client.hasAttemptedNoStatus(interval))
                    results += client
            }
        else if ("any" == statusAchieved) {
            clients.each { client ->
                if (client.hasAttemptedAnyStatus(interval))
                    results += client
            }
        }
        else
            results = clients

        return results
    }
}
