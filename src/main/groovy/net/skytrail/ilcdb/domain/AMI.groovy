package net.skytrail.ilcdb.domain

import grails.gorm.Entity;

class AMI implements Entity{
    String label
    int level

    def toMap()
    {
        [label: label,
         level: level]
    }

    public String toString()
    {
        return label
    }
}
