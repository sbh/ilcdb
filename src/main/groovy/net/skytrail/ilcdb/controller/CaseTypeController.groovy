package net.skytrail.ilcdb.controller;
import org.springframework.security.access.annotation.Secured

@Secured(['IS_AUTHENTICATED_FULLY'])
class CaseTypeController {
    static scaffold = true
}
