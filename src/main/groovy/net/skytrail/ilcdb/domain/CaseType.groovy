package net.skytrail.ilcdb.domain;
import grails.gorm.Entity;

class CaseType implements Entity {
    String type;
    boolean deprecated = false;
    String associatedStatus;

    def toMap() {
        [type: type,
        associatedStatus: associatedStatus]
    }

    public boolean equals(Object other)
    {
        if (other instanceof CaseType) return id == ((CaseType)other).id
        else return false
    }

    public int hashCode()
    {
        return id
    }

    public String toString()
    {
        return "${id} : ${type} : ${associatedStatus}"
    }
}
