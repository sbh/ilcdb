package net.skytrail.ilcdb.domain;
import net.skytrail.ilcdb.domain.AMI
import net.skytrail.ilcdb.domain.Address
import net.skytrail.ilcdb.domain.Appointment
import net.skytrail.ilcdb.domain.BirthPlace
import net.skytrail.ilcdb.domain.CaseResult
import net.skytrail.ilcdb.domain.CaseType
import net.skytrail.ilcdb.domain.Client
import net.skytrail.ilcdb.domain.ClientCase
import net.skytrail.ilcdb.domain.ClientSponsorRelation
import net.skytrail.ilcdb.domain.Conflict
import net.skytrail.ilcdb.domain.Country
import net.skytrail.ilcdb.domain.Note
import net.skytrail.ilcdb.domain.Person
import net.skytrail.ilcdb.domain.Requestmap
import net.skytrail.ilcdb.domain.Role
import net.skytrail.ilcdb.domain.ServiceRecord
import net.skytrail.ilcdb.domain.Sponsor
import net.skytrail.ilcdb.domain.StatusAchieved
import net.skytrail.ilcdb.domain.StatusType
import net.skytrail.ilcdb.domain.User
import net.skytrail.ilcdb.domain.UserRole

import java.util.Date;
import java.text.SimpleDateFormat;
import grails.gorm.Entity;

public class ServiceRecord implements Comparable<ServiceRecord>, Entity {
    static mapping = {
        cache true
    }

    static belongsTo = [ client:Client ]

    Date serviceDate;
    double serviceHours;

    static transients = ["serviceDateString"]

    def toMap() {
        [serviceDate : serviceDate,
         serviceHours: serviceHours]
    }

    String getServiceDateString()
    {
        if (serviceDate instanceof Date)
            return (new SimpleDateFormat("yyyy-MM-dd").format(serviceDate));
        return "";
    }

    int compareTo(obj)
    {
        serviceDate.compareTo(obj.serviceDate)
    }

    public String toString()
    {
        String sd = new SimpleDateFormat("yyyy-MM-dd").format(serviceDate);
        return "${sd}: ${serviceHours} hours";
    }
}
