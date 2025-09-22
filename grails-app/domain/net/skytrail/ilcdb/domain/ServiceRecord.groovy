package net.skytrail.ilcdb.domain;

import gorm.transform.Entity
import java.text.SimpleDateFormat

@Entity
class ServiceRecord implements Comparable<ServiceRecord> {
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
