import grails.transaction.Transactional

@Transactional
class ClientService
{

    def serviceMethod()
    {

    }

    def filterStatus(Collection clients, String whichStatus)
    {
        def results = []
        if ("lpr".equals(whichStatus))
            clients.each {
                if (it.hasAchievedLPR())
                    results += it
            }
        else if ("citizenship".equals(whichStatus))
            clients.each {
                if (it.hasAchievedCitizenship())
                    results += it
            }
        else if ("daca".equals(whichStatus))
            clients.each {
                if (it.hasAchievedDACA())
                    results += it
            }
        else if ("tps".equals(whichStatus))
            clients.each {
                if (it.hasAchievedTPS())
                    results += it
            }
        else if ("none".equals(whichStatus))
            clients.each {
                if (it.hasAchievedNoStatus())
                    results += it
            }
        else if ("any".equals(whichStatus))
            clients.each {
                if (it.hasAchievedAnyStatus())
                    results += it
            }
        else
            results = clients
        return results
    }

    def getEm(Date startDate, Date endDate) {
        def c = Client.createCriteria()
        c.list {
            and {
                gt('firstVisit', startDate)
                lt('firstVisit', endDate)
                cases {
                    and {
                        between('startDate', startDate, endDate)
                        gt('completionDate', endDate)
                    }
                }
            }
        }
    }
}
