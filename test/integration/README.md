# Integration Tests for ILCDB Report Functionality

## Overview

The `ClientReportIntegrationTests.groovy` file contains comprehensive integration tests for the report functionality in `ClientController`. These tests verify that the query logic correctly identifies clients based on intake date ranges.

## What These Tests Verify

### 1. **COMBINED_INTAKES_QUERY Logic**
Tests the original three-part OR query that finds clients with intakes that were:
- **Completed** in the date range
- **Opened** in the date range
- **Ongoing** (null completion date OR completion date after the range)

### 2. **Edge Cases**
The tests include 10 different scenarios:

| Scenario | Start Date | Completion Date | Should Include? | Reason |
|----------|------------|-----------------|-----------------|---------|
| overlapsStart | Before range | In range | ✓ | Completed in range |
| overlapsEnd | In range | After range | ✓ | Started in range |
| fullyContained | In range | In range | ✓ | Both in range |
| spansRange | Before range | After range | ✓ | Ongoing logic |
| ongoing | In range | null | ✓ | Ongoing |
| ongoingBeforeRange | Before range | null | ✓ | Ongoing |
| beforeRange | Before range | Before range | ✗ | Outside range |
| afterRange | After range | After range | ✗ | Outside range |
| completedOnBoundary | Before range | On end date | ✓ | Boundary case |
| startedOnBoundary | On start date | In range | ✓ | Boundary case |

### 3. **Query Comparison**
Compares the original COMBINED_INTAKES logic with the alternative "interval overlap" logic to ensure they produce the same results for the test dataset.

### 4. **Service Layer**
Tests the `ClientService.filterStatus()` and `intakeTypeCounts()` methods with various filtering parameters.

## Running the Tests

### Option 1: Using the Helper Script

```bash
export JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-8.jdk/Contents/Home
./scripts/run-report-tests.sh
```

### Option 2: Using Grails Directly

```bash
export JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-8.jdk/Contents/Home
grails test-app integration: ClientReportIntegrationTests
```

### Option 3: Run All Integration Tests

```bash
export JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-8.jdk/Contents/Home
grails test-app integration:
```

## Test Output

The tests will:
1. Create test data with specific date scenarios
2. Run each test case with detailed output
3. Clean up all test data after completion

Example output:
```
================================================================================
Setting up ClientReportIntegrationTests
================================================================================
Creating test data...
  Created client: Overlaps Start (Intake: 2023-12-15 -> 2024-01-15)
  Created client: Overlaps End (Intake: 2024-01-15 -> 2024-02-01)
  ...
Created 10 test clients

--------------------------------------------------------------------------------
TEST: Combined Intakes Query Logic
--------------------------------------------------------------------------------
Found 8 clients
✓ All assertions passed
```

## Test Reports

After running tests, HTML reports are available at:
```
target/test-reports/html/index.html
```

## Understanding Test Failures

If tests fail, it usually indicates:

1. **Query logic changed** - The COMBINED_INTAKES_QUERY may have been modified
2. **Database state** - Unexpected data in the test database
3. **Service logic changed** - Changes to `ClientService.filterStatus()` or `intakeTypeCounts()`

Check the test output for specific assertion failures to understand what's different.

## Adding New Test Cases

To add new edge cases:

1. Add a new client in `setupTestData()`:
```groovy
testClients['myNewCase'] = createTestClient(
    "FirstName", "LastName",
    startDate,
    completionDate
)
```

2. Add assertions in the relevant test method:
```groovy
assertTrue("Should include myNewCase", resultIds.contains(testClients['myNewCase'].id))
```

3. Update the expected count if needed

## Debugging Tests

To see more detailed output:

```bash
grails test-app integration: ClientReportIntegrationTests --verbose
```

To run a single test method:

```bash
grails test-app integration: ClientReportIntegrationTests.testCombinedIntakesQueryLogic
```

## Integration with CI/CD

These tests can be integrated into a continuous integration pipeline:

```bash
#!/bin/bash
# In your CI script
export JAVA_HOME=/path/to/jdk8
grails test-app integration: ClientReportIntegrationTests || exit 1
```

## Known Limitations

1. **Date Precision**: Tests use day-level precision (year-month-day), not time-of-day
2. **Database**: Tests run against HSQLDB in-memory by default (development mode)
3. **Test Data**: Tests create real database records and clean them up after

## Troubleshooting

### "Table not found" errors
The database schema may not be initialized. Try running:
```bash
grails schema-export
```

### "JAVA_HOME not set" errors
Set your Java 8 JDK path:
```bash
export JAVA_HOME=/Library/Java/JavaVirtualMachines/temurin-8.jdk/Contents/Home
```

### Tests pass but production fails
The test database (HSQLDB) may behave differently from production (MySQL). Consider:
- Running tests against a MySQL test database
- Checking for SQL dialect differences
- Verifying date/time handling differences

## Related Files

- **Test File**: `test/integration/ClientReportIntegrationTests.groovy`
- **Controller**: `grails-app/controllers/ClientController.groovy`
- **Service**: `grails-app/services/ClientService.groovy`
- **Test Runner**: `scripts/run-report-tests.sh`
- **Comparison Scripts**: `scripts/CompareReportResults.groovy`, `scripts/QuickCompare.groovy`
