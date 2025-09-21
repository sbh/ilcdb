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

import java.text.SimpleDateFormat;
import grails.gorm.Entity;

import com.sun.corba.se.impl.oa.poa.POAPolicyMediatorImpl_R_USM.Etherealizer;

class Person implements Comparable<Person>, Entity {
    Long id
    String firstName
    String lastName
    String phoneNumber
    BirthPlace placeOfBirth
    Date dateOfBirth
    String englishProficiency
    String emailAddress
    Address address
    String gender;
    String race;

    static mapping =
    {
        cache true
    }

    static fetchMode = [address:"eager", placeOfBirth:"eager"]

    static List Ethnicities = ['', 'African', 'Asian Pac', 'Latino', 'Native American', 'White'];

    static constraints =
    {
        firstName(blank:false)
        lastName(blank:false)
        englishProficiency(inList:[
            'native',
            'high',
            'medium',
            'low',
            'very low',
            'mono-lingual spanish',
        ])
        gender(inList:["female", "male"])
        phoneNumber(blank:false)
        emailAddress(nullable:true, email:true)
        race(inList: Person.Ethnicities)
        placeOfBirth(nullable:true)
        address(nullable:true)
    }

    static transients = [ "age", "birthDayString", "sortableLastName", "sortableFirstName" ]

    public int compareTo(Person other)
    {
        String[] lastNames = lastName.split("\\s+")
        String[] otherLastNames = other.lastName.split("\\s+")
        int val = lastNames[0].toUpperCase().compareTo(otherLastNames[0].toUpperCase())

        if (val != 0)
            return val

        return firstName.compareTo(other.firstName)
    }

    String getBirthDayString()
    {
        return (new SimpleDateFormat("MMM-dd-yyyy").format(dateOfBirth));
    }

    String getAge()
    {
        if (dateOfBirth == null)
            return "unknown"
        Calendar cal = new GregorianCalendar()
        cal.setTime(dateOfBirth)
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
        return ""+age
    }

    String toString() { return "$lastName, $firstName" }

    String toDebugString()
    {
        return "first: $firstName last: $lastName phone: $phoneNumber birthPlace: $placeOfBirth dob: $dateOfBirth ep: $englishProficiency email: $emailAddress address: $address gender: $gender race: $race"
    }

    private final static List<String> lastNamePrefixes = new ArrayList<String>()
    static
    {
        lastNamePrefixes.add("DE LA ");
        lastNamePrefixes.add("DEL ");
        lastNamePrefixes.add("DE ");
    }

    public String getSortableLastName()
    {
        String sortableLastName = null
        for (String prefix : lastNamePrefixes)
        {
            if (lastName.trim().toUpperCase().startsWith(prefix))
            {
                sortableLastName = prefix+lastName.substring(prefix.length()).split()[0]
                break;
            }
        }

        if (sortableLastName == null)
            sortableLastName = lastName.trim().split()[0]

        //println("sortableLastName: "+sortableLastName)
        return sortableLastName.toUpperCase()
    }

    public String getSortableFirstName()
    {
        return firstName.toUpperCase()
    }
}
