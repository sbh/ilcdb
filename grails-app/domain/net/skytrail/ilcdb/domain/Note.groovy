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

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormat;
import grails.gorm.Entity;

class Note implements Comparable<Note>, Entity
{
    private static final DateTimeFormatter briefDateFormat = DateTimeFormat.forPattern("MMM-dd-yyyy");
    
    static mapping =
    {
        cache true
        text type:'text'
    }
    static belongsTo = [ client:Client, intake:ClientCase ]
    
    static transients = ["formattedDate"]
    
    String type    
    
    static constraints = {
        client(nullable:true)
        intake(nullable:true)
        type(inList:["client", "clientCase"])
    }
    
    String text
    Date createDate

    def toMap() {
        [text: text,
         created: createDate]
    }

    String toString() 
    { 
        def regex = (/<(\w|\/\w)[^>]*>/) //Regex to remove tags but leave the contents of tags
        def matcher = (text =~ /$regex/)
        def strippedTags = matcher.replaceAll('')
        return strippedTags.replaceAll("&nbsp;", "")
    }
    
    String getFormattedDate()
    {
        return briefDateFormat.print(createDate.getTime());
    }
    
    int compareTo(otherNote)
    {
        createDate.compareTo(otherNote.createDate)
    }

    public boolean equals(Object other)
    {
        if ( !(other instanceof Note) )
            return false;
            
        return id == ((Note)other).id
    }
    
    public int hashCode()
    {
        return id
    }
}

