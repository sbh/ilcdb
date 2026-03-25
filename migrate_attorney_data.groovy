#!/usr/bin/env groovy
/**
 * Migration script to populate Attorney table and update ClientCase attorney references
 *
 * This script should be run via Grails console or as a script after switching to the
 * Attorney domain model.
 *
 * Usage:
 *   1. Via Grails console: grails console
 *      Then paste this script's contents
 *
 *   2. Via MySQL direct update (see SQL commands below)
 */

// First, ensure the Attorney table has the attorneys from the old static list
def attorneyNames = ["Belen", "Laurel", "Maria"]

println "=== Attorney Data Migration Script ==="
println "Step 1: Creating Attorney records for the static list..."

attorneyNames.each { name ->
    def existing = Attorney.findByFirstName(name)
    if (!existing) {
        def attorney = new Attorney(
            firstName: name,
            lastName: "Staff",  // Update with real last names if known
            email: "${name.toLowerCase()}@example.com"  // Update with real emails if known
        )
        if (attorney.save(flush: true)) {
            println "  ✓ Created Attorney: ${name}"
        } else {
            println "  ✗ Failed to create Attorney: ${name}"
            attorney.errors.each {
                println "    Error: ${it}"
            }
        }
    } else {
        println "  - Attorney already exists: ${name}"
    }
}

println "\nStep 2: Checking ClientCase records with invalid attorney references..."

// Get all ClientCase records
def allCases = ClientCase.list()
def invalidCount = 0
def fixedCount = 0

allCases.each { clientCase ->
    try {
        // Try to access the attorney - this will throw ObjectNotFoundException if invalid
        def attorney = clientCase.attorney
        if (attorney == null) {
            println "  Warning: ClientCase #${clientCase.id} has null attorney"
            invalidCount++
        }
    } catch (org.hibernate.ObjectNotFoundException e) {
        println "  Error: ClientCase #${clientCase.id} has invalid attorney reference"
        invalidCount++

        // Set to null to allow the system to work
        // User will need to manually assign attorneys through the UI
        clientCase.attorney = null
        if (clientCase.save(flush: true)) {
            fixedCount++
        }
    }
}

println "\nMigration Summary:"
println "  Total ClientCase records: ${allCases.size()}"
println "  Invalid attorney references: ${invalidCount}"
println "  Fixed by setting to null: ${fixedCount}"
println "\nNote: Cases with null attorneys will need to be updated through the UI"
println "      to assign the correct attorney from the dropdown."


println "\n=== SQL Alternative ==="
println "If you prefer to run SQL directly, use these commands:\n"
println """
-- Step 1: Populate Attorney table (if not already done)
INSERT INTO attorney (version, first_name, last_name, email)
VALUES (0, 'Belen', 'Staff', 'belen@example.com');

INSERT INTO attorney (version, first_name, last_name, email)
VALUES (0, 'Laurel', 'Staff', 'laurel@example.com');

INSERT INTO attorney (version, first_name, last_name, email)
VALUES (0, 'Maria', 'Staff', 'maria@example.com');

-- Step 2: Get attorney IDs
SELECT id, first_name FROM attorney;

-- Step 3: Update ClientCase records with correct attorney_id
-- Replace <belen_id>, <laurel_id>, <maria_id> with actual IDs from Step 2
-- This example assumes you have a way to know which cases belong to which attorney
-- You may need to do this manually or based on other data

-- Set all invalid references to NULL first (allows app to work)
UPDATE client_case SET attorney_id = NULL WHERE attorney_id NOT IN (SELECT id FROM attorney);

-- If you have data about which cases should be assigned to which attorney,
-- update them here. Example:
-- UPDATE client_case SET attorney_id = <belen_id> WHERE <some_condition>;
"""
