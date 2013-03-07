import grails.test.*
import java.util.Date;
import java.util.Random;
import java.text.SimpleDateFormat;
import groovy.util.GroovyTestCase;


class ClientTests extends GroovyTestCase {
	protected void setUp() {
		super.setUp();
		println "ClientTests setup"
	}

	protected void tearDown() {
		super.tearDown();
	}

	private String generateRandomString(int length )
	{
		return (1..length).inject(""){a,b -> a+= ('a'..'z')[ new Random().nextFloat() * 26 as int ] }.capitalize();
	}

	void testClientSave( )
	{
		boolean transactional = false;
		try{
			println "Running client save test";
			def random = new Random();
			
		
			def firstName = generateRandomString( random.nextFloat() * 9 + 1 as int);
			def lastName = generateRandomString( random.nextFloat() * 15 + 1 as int);
			def gender = (random.nextFloat() > 0.5) ? "male" : "female";
			
			def dateOfBirth = new Date(1, 1, 1900);
			
			def placeOfBirth = new BirthPlace( ["city":"Oaxaca", "state":"Oaxaca", "country":"MEXICO" ]);
			
			def address = new Address(["street":"1234 Fake Street", "city":"Boulder", "county":"Boulder", "state":"Colorado", "country":"UNITED STATES", "postalCode":"80306" ]);
			
			def person = new Person(["firstName":firstName, "lastName":lastName, "phoneNumber":"555-555-5555",
				 "dateOfBirth":dateOfBirth, "race":"Latino", "gender":gender, "englishProficiency":"native",
				 "emailAddress":"123@123.com", "":"" ])
			def client = new Client( ["householdIncomeLevel":50, "numberInHousehold":50, "firstVisit":new Date(), "fileLocation":"none"] );
			
			
			if( address.hasErrors() || address.validate() )
			{
				address.errors.allErrors.each{println "Error Validating Address: " +  it + "\n";};	
				assertTrue false;
			}
			else
			{
				person.address = address;	
				address.person = person;
			}
			
			if( placeOfBirth.hasErrors() || !placeOfBirth.validate() )
			{
				placeOfBirth.errors.allErrors.each{println "Error Validating PoB: " + it + "\n";};
				assertTrue false;
			}
			else
			{
				person.placeOfBirth = placeOfBirth;	
			}
			
			if(person.hasErrors() || !person.validate())
			{
				person.errors.allErrors.each{println "Error Validating Person: " + it + "\n"};	
				assertTrue false;
			}
			else
			{
				client.client = person;
			}
			if(client.hasErrors() || !client.validate())
			{
				client.errors.allErrors.each{println "Error Validating Client: " + it + "\n"};
				assertTrue false;
			}
			println "\n**************SAVING******************\n";
			
			def clientCase = new ClientCase()
			Date now = new Date()
			clientCase.startDate = now
			clientCase.completionDate = now
			clientCase.intakeType = ClientCase.STAFF_ADVISE
			client.addToCases(clientCase)
		
			
			println "bp: " + placeOfBirth
			println "Person: " + person.toDebugString();
			println "Client: " + client.toDebugString();
			
			if(!address.save(flush:true))
			{
				address.errors.each{println "Error Saving Address: " + it + "\n"};	
				assertTrue false;
			}
			if(!placeOfBirth.save(flush:true))
			{
				placeOfBirth.errors.each{println "Error Saving PoB: " + it + "\n"};	
				assertTrue false;
			}
			if(!person.save(flush:true))
			{
				person.errors.each{println "Error Saving Person: " + it + "\n"};
				assertTrue false;
			}
			if(!client.save(flush:true))
			{
				client.errors.each{println "Error Saving Client: " + it + "\n"};
				assertTrue false;
			}
			
		}
		catch (Exception e)
		{
			println "test failed. Exception:" + e;
			assertTrue false; 
		}

		assertTrue true;
	}
}
