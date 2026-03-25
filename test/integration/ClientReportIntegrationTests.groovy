import grails.test.*
import groovy.util.GroovyTestCase
import org.joda.time.Interval

/**
 * Integration tests for ClientController report functionality
 *
 * These tests verify the COMBINED_INTAKES_QUERY logic with edge cases
 * to ensure reports correctly include clients based on intake dates.
 */
class ClientReportIntegrationTests extends GroovyTestCase {

    def clientService
    def clientController

    // Test date range: January 2024
    static final Date TEST_START_DATE = new Date(2024 - 1900, 0, 1)  // Jan 1, 2024
    static final Date TEST_END_DATE = new Date(2024 - 1900, 0, 31)   // Jan 31, 2024

    // Dates for edge cases
    static final Date BEFORE_RANGE_START = new Date(2023 - 1900, 11, 15)  // Dec 15, 2023
    static final Date BEFORE_RANGE_END = new Date(2023 - 1900, 11, 31)    // Dec 31, 2023
    static final Date IN_RANGE_START = new Date(2024 - 1900, 0, 15)       // Jan 15, 2024
    static final Date IN_RANGE_END = new Date(2024 - 1900, 0, 20)         // Jan 20, 2024
    static final Date AFTER_RANGE_START = new Date(2024 - 1900, 1, 1)     // Feb 1, 2024
    static final Date AFTER_RANGE_END = new Date(2024 - 1900, 1, 28)      // Feb 28, 2024

    def testClients = [:]
    def testCountry

    protected void setUp() {
        super.setUp()
        println "\n" + "=" * 80
        println "Setting up ClientReportIntegrationTests"
        println "=" * 80

        // Create test data
        setupTestData()
    }

    protected void tearDown() {
        super.tearDown()
        // Grails integration tests are transactional - data is automatically rolled back
        testClients.clear()
    }

    void setupTestData() {
        println "Creating test data..."

        // Create a test country for birth places
        testCountry = Country.findByName("TestCountry") ?: new Country(name: "TestCountry").save(failOnError: true)

        // Create default AMI if it doesn't exist
        def defaultAMI = AMI.findByLabel("UNSPECIFIED") ?: new AMI(label: "UNSPECIFIED", level: 0).save(failOnError: true)

        // Create test clients with various intake scenarios
        testClients['overlapsStart'] = createTestClient(
            "Overlaps", "Start",
            BEFORE_RANGE_START,    // Started before range
            IN_RANGE_START          // Completed in range
        )

        testClients['overlapsEnd'] = createTestClient(
            "Overlaps", "End",
            IN_RANGE_START,         // Started in range
            AFTER_RANGE_START       // Completed after range
        )

        testClients['fullyContained'] = createTestClient(
            "Fully", "Contained",
            IN_RANGE_START,         // Started in range
            IN_RANGE_END            // Completed in range
        )

        testClients['spansRange'] = createTestClient(
            "Spans", "Range",
            BEFORE_RANGE_START,     // Started before range
            AFTER_RANGE_END         // Completed after range
        )

        testClients['ongoing'] = createTestClient(
            "Ongoing", "Case",
            IN_RANGE_START,         // Started in range
            null                    // Not completed (ongoing)
        )

        testClients['ongoingBeforeRange'] = createTestClient(
            "Ongoing", "Before",
            BEFORE_RANGE_START,     // Started before range
            null                    // Not completed (ongoing)
        )

        testClients['beforeRange'] = createTestClient(
            "Before", "Range",
            BEFORE_RANGE_START,     // Started before range
            BEFORE_RANGE_END        // Completed before range
        )

        testClients['afterRange'] = createTestClient(
            "After", "Range",
            AFTER_RANGE_START,      // Started after range
            AFTER_RANGE_END         // Completed after range
        )

        testClients['completedOnBoundary'] = createTestClient(
            "Boundary", "Complete",
            BEFORE_RANGE_START,     // Started before
            TEST_END_DATE           // Completed exactly on end date
        )

        testClients['startedOnBoundary'] = createTestClient(
            "Boundary", "Start",
            TEST_START_DATE,        // Started exactly on start date
            IN_RANGE_END            // Completed in range
        )

        println "Created ${testClients.size()} test clients"
    }

    Client createTestClient(String firstName, String lastName, Date intakeStart, Date intakeComplete) {
        // Create birth place
        def birthPlace = new BirthPlace(
            city: "TestBirthCity",
            state: "TestBirthState",
            country: testCountry
        ).save(failOnError: true)

        // Create person first (without address)
        def person = new Person(
            firstName: firstName,
            lastName: lastName,
            phoneNumber: "555-0000",
            dateOfBirth: new Date(1990 - 1900, 0, 1),
            race: "Latino",
            gender: "female",
            englishProficiency: "medium",
            emailAddress: "${firstName}.${lastName}@test.com",
            placeOfBirth: birthPlace
        ).save(failOnError: true)

        // Create address with person reference
        def address = new Address(
            street: "123 Test St",
            city: "TestCity",
            county: "TestCounty",
            state: "TestState",
            country: testCountry,
            postalCode: "12345",
            person: person
        ).save(failOnError: true)

        // Update person with address
        person.address = address
        person.save(failOnError: true)

        // Create client (use saved AMI from database)
        def ami = AMI.findByLabel("UNSPECIFIED")
        def client = new Client(
            client: person,
            ami: ami,
            householdIncomeLevel: 50,
            numberInHousehold: 1,
            firstVisit: intakeStart,
            fileLocation: "TEST"
        ).save(failOnError: true)

        // Create intake (case)  - use valid attorney and add caseType
        def attorney = Attorney.findByFirstName("Maria") ?: new Attorney(
            firstName: "Maria",
            lastName: "Test",
            email: "maria@test.com"
        ).save(failOnError: true)

        def caseType = CaseType.find{} ?: new CaseType(
            formName: "I-485",
            description: "Test case type",
            type: "Application",
            associatedStatus: "LPR"
        ).save(failOnError: true)
        def clientCase = new ClientCase(
            client: client,
            startDate: intakeStart,
            completionDate: intakeComplete,
            intakeType: ClientCase.STAFF_REPRESENTATION,
            attorney: attorney,
            caseType: caseType
        ).save(failOnError: true)

        client.addToCases(clientCase)
        client.save(failOnError: true)

        println "  Created client: ${firstName} ${lastName} (Intake: ${intakeStart?.format('yyyy-MM-dd')} -> ${intakeComplete?.format('yyyy-MM-dd') ?: 'ongoing'})"

        return client
    }

    /**
     * Test the original COMBINED_INTAKES_QUERY logic:
     * - Completed in range
     * - Opened in range
     * - Ongoing (null completion OR completion >= endDate)
     */
    void testCombinedIntakesQueryLogic() {
        println "\n" + "-" * 80
        println "TEST: Combined Intakes Query Logic"
        println "-" * 80

        def query = """
            SELECT DISTINCT client FROM Client client
            INNER JOIN FETCH client.client person
            INNER JOIN FETCH person.address address
            INNER JOIN FETCH person.placeOfBirth placeOfBirth
            INNER JOIN FETCH client.cases intake
            WHERE (
                (intake.completionDate >= :startDate AND intake.completionDate <= :endDate)
                OR (intake.startDate >= :startDate AND intake.startDate <= :endDate)
                OR (intake.startDate <= :endDate AND (intake.completionDate IS NULL OR intake.completionDate >= :endDate))
            )
        """

        def results = Client.executeQuery(query, [
            startDate: TEST_START_DATE,
            endDate: TEST_END_DATE
        ])

        println "Found ${results.size()} clients"

        // Expected results based on the query logic:
        // 1. overlapsStart: completionDate in range ✓
        // 2. overlapsEnd: startDate in range ✓
        // 3. fullyContained: both in range ✓
        // 4. spansRange: completionDate >= endDate (ongoing logic) ✓
        // 5. ongoing: completionDate IS NULL ✓
        // 6. ongoingBeforeRange: completionDate IS NULL ✓
        // 7. beforeRange: ✗ (completed before range)
        // 8. afterRange: ✗ (started after range, completed after)
        // 9. completedOnBoundary: completionDate in range (on boundary) ✓
        // 10. startedOnBoundary: startDate in range (on boundary) ✓

        def expectedCount = 8
        assertEquals("Should find ${expectedCount} clients", expectedCount, results.size())

        // Verify specific clients are included
        def resultIds = results*.id as Set
        assertTrue("Should include overlapsStart", resultIds.contains(testClients['overlapsStart'].id))
        assertTrue("Should include overlapsEnd", resultIds.contains(testClients['overlapsEnd'].id))
        assertTrue("Should include fullyContained", resultIds.contains(testClients['fullyContained'].id))
        assertTrue("Should include spansRange", resultIds.contains(testClients['spansRange'].id))
        assertTrue("Should include ongoing", resultIds.contains(testClients['ongoing'].id))
        assertTrue("Should include ongoingBeforeRange", resultIds.contains(testClients['ongoingBeforeRange'].id))
        assertTrue("Should include completedOnBoundary", resultIds.contains(testClients['completedOnBoundary'].id))
        assertTrue("Should include startedOnBoundary", resultIds.contains(testClients['startedOnBoundary'].id))

        // Verify exclusions
        assertFalse("Should NOT include beforeRange", resultIds.contains(testClients['beforeRange'].id))
        assertFalse("Should NOT include afterRange", resultIds.contains(testClients['afterRange'].id))

        println "✓ All assertions passed"
    }

    /**
     * Test the interval overlap logic (alternative implementation):
     * An intake is active if startDate <= endDate AND (completionDate IS NULL OR completionDate >= startDate)
     */
    void testIntervalOverlapQueryLogic() {
        println "\n" + "-" * 80
        println "TEST: Interval Overlap Query Logic"
        println "-" * 80

        def query = """
            SELECT DISTINCT client FROM Client client
            INNER JOIN FETCH client.client person
            INNER JOIN FETCH person.address address
            INNER JOIN FETCH person.placeOfBirth placeOfBirth
            INNER JOIN FETCH client.cases intake
            WHERE (
                intake.startDate <= :endDate
                AND (intake.completionDate IS NULL OR intake.completionDate >= :startDate)
            )
        """

        def results = Client.executeQuery(query, [
            startDate: TEST_START_DATE,
            endDate: TEST_END_DATE
        ])

        println "Found ${results.size()} clients"

        // Expected results with interval overlap:
        // 1. overlapsStart: starts before, ends in range ✓
        // 2. overlapsEnd: starts in range, ends after ✓
        // 3. fullyContained: both in range ✓
        // 4. spansRange: starts before, ends after ✓
        // 5. ongoing: starts in range, no end ✓
        // 6. ongoingBeforeRange: starts before, no end ✓
        // 7. beforeRange: ✗ (completionDate < startDate)
        // 8. afterRange: ✗ (startDate > endDate)
        // 9. completedOnBoundary: ✓
        // 10. startedOnBoundary: ✓

        def expectedCount = 8
        assertEquals("Should find ${expectedCount} clients with interval overlap", expectedCount, results.size())

        println "✓ Interval overlap logic matches combined logic for this dataset"
    }

    /**
     * Test the ClientService.filterStatus method
     */
    void testClientServiceFilterStatus() {
        println "\n" + "-" * 80
        println "TEST: ClientService filterStatus"
        println "-" * 80

        def interval = new Interval(TEST_START_DATE.getTime(), TEST_END_DATE.getTime())
        def allTestClients = testClients.values() as List

        // Test with no filtering (statusAchieved = "any")
        def filtered = clientService.filterStatus(allTestClients, "any", "any", "any", interval)

        println "Filtered ${filtered.size()} clients from ${allTestClients.size()} total"

        // All clients should pass through with "any" filter
        assertEquals("Should return all clients with 'any' filter", allTestClients.size(), filtered.size())

        println "✓ Filter logic working correctly"
    }

    /**
     * Test intakeTypeCounts method
     */
    void testIntakeTypeCounts() {
        println "\n" + "-" * 80
        println "TEST: Intake Type Counts"
        println "-" * 80

        def interval = new Interval(TEST_START_DATE.getTime(), TEST_END_DATE.getTime())
        def allTestClients = testClients.values() as List

        def counts = clientService.intakeTypeCounts(allTestClients, interval)

        println "Staff Advise: ${counts[0]}, Staff Representation: ${counts[1]}"

        // All our test clients have STAFF_REPRESENTATION intakes
        assertEquals("Should have 0 Staff Advise intakes", 0, counts[0])
        assertTrue("Should have Staff Representation intakes", counts[1] > 0)

        println "✓ Intake type counting working correctly"
    }

    /**
     * Test edge case: Intake completed exactly on range start date
     */
    void testIntakeCompletedOnRangeStart() {
        println "\n" + "-" * 80
        println "TEST: Edge Case - Completed on Range Start"
        println "-" * 80

        def client = createTestClient(
            "Edge", "StartComplete",
            BEFORE_RANGE_START,
            TEST_START_DATE  // Completed exactly on start date
        )

        def query = """
            SELECT DISTINCT client FROM Client client
            INNER JOIN FETCH client.client person
            INNER JOIN FETCH person.address address
            INNER JOIN FETCH person.placeOfBirth placeOfBirth
            INNER JOIN FETCH client.cases intake
            WHERE (
                (intake.completionDate >= :startDate AND intake.completionDate <= :endDate)
                OR (intake.startDate >= :startDate AND intake.startDate <= :endDate)
                OR (intake.completionDate IS NULL OR intake.completionDate >= :endDate)
            )
            AND client.id = :clientId
        """

        def results = Client.executeQuery(query, [
            startDate: TEST_START_DATE,
            endDate: TEST_END_DATE,
            clientId: client.id
        ])

        assertEquals("Should include intake completed on range start", 1, results.size())

        // Cleanup
        client.cases*.delete()
        client.delete()

        println "✓ Boundary case handled correctly"
    }

    /**
     * Comprehensive report test comparing both query approaches
     */
    void testComprehensiveReportComparison() {
        println "\n" + "-" * 80
        println "TEST: Comprehensive Report Comparison"
        println "-" * 80

        // Query 1: Original COMBINED_INTAKES logic
        def query1 = """
            SELECT DISTINCT client FROM Client client
            INNER JOIN FETCH client.client person
            INNER JOIN FETCH person.address address
            INNER JOIN FETCH person.placeOfBirth placeOfBirth
            INNER JOIN FETCH client.cases intake
            WHERE (
                (intake.completionDate >= :startDate AND intake.completionDate <= :endDate)
                OR (intake.startDate >= :startDate AND intake.startDate <= :endDate)
                OR (intake.startDate <= :endDate AND (intake.completionDate IS NULL OR intake.completionDate >= :endDate))
            )
        """

        // Query 2: Interval overlap logic
        def query2 = """
            SELECT DISTINCT client FROM Client client
            INNER JOIN FETCH client.client person
            INNER JOIN FETCH person.address address
            INNER JOIN FETCH person.placeOfBirth placeOfBirth
            INNER JOIN FETCH client.cases intake
            WHERE (
                intake.startDate <= :endDate
                AND (intake.completionDate IS NULL OR intake.completionDate >= :startDate)
            )
        """

        def results1 = Client.executeQuery(query1, [startDate: TEST_START_DATE, endDate: TEST_END_DATE])
        def results2 = Client.executeQuery(query2, [startDate: TEST_START_DATE, endDate: TEST_END_DATE])

        println "Original logic: ${results1.size()} clients"
        println "Interval overlap: ${results2.size()} clients"

        def ids1 = results1*.id as Set
        def ids2 = results2*.id as Set

        def onlyIn1 = ids1 - ids2
        def onlyIn2 = ids2 - ids1

        if (onlyIn1) {
            println "Only in original: ${onlyIn1.collect { testClients.find { it.value.id == it }?.key }}"
        }
        if (onlyIn2) {
            println "Only in interval overlap: ${onlyIn2.collect { testClients.find { it.value.id == it }?.key }}"
        }

        // For this test dataset, both should return the same results
        assertEquals("Both queries should return same count", results1.size(), results2.size())
        assertEquals("Both queries should return same clients", ids1, ids2)

        println "✓ Both query approaches produce identical results"
    }
}
