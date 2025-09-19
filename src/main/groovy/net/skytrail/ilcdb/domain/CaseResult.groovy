package net.skytrail.ilcdb.domain;
import grails.gorm.Entity;

class CaseResult implements Entity {
    String result;
    boolean successfulResult; // For client or ILCBC.
    boolean statusWasAchieved; // An actual immigration status was successfully achieved.

    def toMap() {
        [result           : result,
         successfulResult : successfulResult,
         statusWasAchieved: statusWasAchieved]
    }

    static Collection<String> successfulResults()
    {
        return findAllWhere(successfulResult:true)
    }

    static Collection<String> statusWasAchieved()
    {
        return findAllWhere(statusWasAchieved:true)
    }
}
