# Report Comparison Guide

This guide explains how to use the comparison script to identify differences in report results between branches.

## Quick Start

### Option 1: Run from Grails Console (Recommended)

1. **Start Grails console:**
   ```bash
   grails console
   ```

2. **Load and run the script:**
   ```groovy
   evaluate(new File("scripts/CompareReportResults.groovy"))
   ```

3. **Switch to other branch and repeat:**
   ```bash
   # In terminal (not console)
   git stash  # if you have changes
   git checkout main
   grails console
   ```

   ```groovy
   # In console
   evaluate(new File("scripts/CompareReportResults.groovy"))
   ```

4. **Compare results:**
   ```groovy
   def script = evaluate(new File("scripts/CompareReportResults.groovy"))
   script.compareFiles('/tmp/report_current_branch.csv', '/tmp/report_other_branch.csv')
   ```

### Option 2: Run as Script

```bash
# On ilcdb-speedup branch
grails run-script scripts/CompareReportResults.groovy

# Switch branches
git checkout main

# On main branch
grails run-script scripts/CompareReportResults.groovy
```

Then compare the generated files manually or use the `compareFiles()` function.

## Customizing Test Parameters

Edit the configuration section at the top of `CompareReportResults.groovy`:

```groovy
def TEST_START_DATE = new Date(2023 - 1900, 0, 1)  // Jan 1, 2023
def TEST_END_DATE = new Date(2023 - 1900, 11, 31)  // Dec 31, 2023
def MUNICIPALITY = "Boulder"  // or null for any
def MUN_TYPE = "City"  // "Any", "City", "County", "State"
def ATTORNEY = "Any"  // or specific attorney name
def INTAKE_STATE = "any"
def INTAKE_TYPE = "any"
def STATUS_ACHIEVED = "any"
def HOME_COUNTRY = null  // or Country ID
```

## Understanding the Output

### CSV Output Format
```
ClientID,ClientName,IntakeID,IntakeType,StartDate,CompletionDate,Attorney,City,State,HomeCountry
1234,John Doe,5678,STAFF_REPRESENTATION,2023-03-15,2023-06-20,Jane Smith,Boulder,Colorado,Mexico
```

### Comparison Output

The `compareFiles()` function shows:
- **Total counts** in each file
- **Common records** (records in both files)
- **Only in File 1** (records missing from File 2)
- **Only in File 2** (records missing from File 1)

A full diff is written to `/tmp/report_diff.txt`.

## Example Workflow

```groovy
// In Grails console on ilcdb-speedup branch
def script = evaluate(new File("scripts/CompareReportResults.groovy"))

// This generates /tmp/report_current_branch.csv automatically
// Now switch branches...

// After switching to main branch, in Grails console:
def script = evaluate(new File("scripts/CompareReportResults.groovy"))

// This generates /tmp/report_current_branch.csv (will overwrite)
// So let's be more explicit:

// On ilcdb-speedup:
script.generateReport('/tmp/report_speedup.csv')

// On main:
script.generateReport('/tmp/report_main.csv')

// Then compare (can run from either branch):
script.compareFiles('/tmp/report_main.csv', '/tmp/report_speedup.csv')
```

## Troubleshooting

### "Script not found"
Make sure you're running from the project root directory where `scripts/` exists.

### "No such property: Client"
The script needs access to domain classes. Make sure you're running in `grails console`, not a standalone Groovy environment.

### Different result counts but hard to analyze
- Check `/tmp/report_diff.txt` for the full list
- Import the CSV files into Excel/Google Sheets for visual comparison
- Look at the intake dates to understand why certain records are included/excluded

## What to Look For

When comparing results, focus on:

1. **Boundary cases** - Intakes that started/ended right at the date range boundaries
2. **Ongoing intakes** - Intakes with null completion dates
3. **Attorney/municipality filters** - Ensure filtering logic is consistent
4. **Duplicate clients** - Same client appearing multiple times due to multiple intakes

## Next Steps

Once you identify the differences:
1. Examine specific client/intake records in the database
2. Review the query logic in `ClientController.groovy`
3. Create integration tests for edge cases you discover
4. Document expected behavior for boundary conditions
