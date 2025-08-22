import grails.plugin.springsecurity.annotation.Secured

@Secured(['IS_AUTHENTICATED_FULLY'])
class CaseTypeController {
    static scaffold = true
}
