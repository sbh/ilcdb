#!/bin/bash

# Report API Comparison Script
# Fetches report data from both branches and compares them

set -e

# Configuration
BASE_URL="${BASE_URL:-https://localhost:8443}"
START_DATE="${START_DATE:-2023-01-01}"
END_DATE="${END_DATE:-2023-12-31}"
USERNAME="${USERNAME:-admin}"
PASSWORD="${PASSWORD:-admin}"

# Output files
SPEEDUP_FILE="/tmp/report_speedup.json"
MAIN_FILE="/tmp/report_main.json"
DIFF_FILE="/tmp/report_diff.txt"

echo "================================================================================"
echo "ILCDB Report API Comparison"
echo "================================================================================"
echo "Base URL: $BASE_URL"
echo "Date Range: $START_DATE to $END_DATE"
echo ""

# Function to fetch report data
fetch_report() {
    local output_file=$1
    local branch_name=$2

    echo ">>> Fetching report data from $branch_name branch..."

    # Use curl with basic auth, ignoring SSL cert (self-signed)
    curl -k -s -u "$USERNAME:$PASSWORD" \
        "$BASE_URL/client/reportApi?startDate=$START_DATE&endDate=$END_DATE" \
        -o "$output_file"

    if [ $? -eq 0 ]; then
        # Pretty print the JSON and show summary
        local total=$(cat "$output_file" | jq -r '.metadata.totalClients // "error"')
        local sa_count=$(cat "$output_file" | jq -r '.metadata.saIntakesCount // 0')
        local sr_count=$(cat "$output_file" | jq -r '.metadata.srIntakesCount // 0')

        if [ "$total" != "error" ]; then
            echo "    ✓ Success: $total clients found"
            echo "      - Staff Advise: $sa_count"
            echo "      - Staff Representation: $sr_count"
            echo "    ✓ Saved to: $output_file"
        else
            echo "    ✗ Error in response:"
            cat "$output_file" | jq '.'
            return 1
        fi
    else
        echo "    ✗ Failed to fetch data"
        return 1
    fi
}

# Function to compare two report files
compare_reports() {
    echo ""
    echo ">>> Comparing results..."

    # Extract client IDs from both files
    cat "$SPEEDUP_FILE" | jq -r '.clients[].id' | sort -n > /tmp/speedup_ids.txt
    cat "$MAIN_FILE" | jq -r '.clients[].id' | sort -n > /tmp/main_ids.txt

    # Get counts
    local speedup_count=$(wc -l < /tmp/speedup_ids.txt | tr -d ' ')
    local main_count=$(wc -l < /tmp/main_ids.txt | tr -d ' ')

    echo ""
    echo "================================================================================"
    echo "COMPARISON RESULTS"
    echo "================================================================================"
    echo "Speedup branch: $speedup_count clients"
    echo "Main branch:    $main_count clients"
    echo "Difference:     $((speedup_count - main_count)) clients"
    echo ""

    # Find differences
    local only_in_speedup=$(comm -23 /tmp/speedup_ids.txt /tmp/main_ids.txt | wc -l | tr -d ' ')
    local only_in_main=$(comm -13 /tmp/speedup_ids.txt /tmp/main_ids.txt | wc -l | tr -d ' ')
    local common=$(comm -12 /tmp/speedup_ids.txt /tmp/main_ids.txt | wc -l | tr -d ' ')

    echo "Common to both:        $common clients"
    echo "Only in speedup:       $only_in_speedup clients"
    echo "Only in main:          $only_in_main clients"
    echo ""

    # Show details of differences
    if [ $only_in_speedup -gt 0 ]; then
        echo "Client IDs ONLY in speedup branch:"
        comm -23 /tmp/speedup_ids.txt /tmp/main_ids.txt | head -20
        if [ $only_in_speedup -gt 20 ]; then
            echo "... and $((only_in_speedup - 20)) more"
        fi
        echo ""
    fi

    if [ $only_in_main -gt 0 ]; then
        echo "Client IDs ONLY in main branch:"
        comm -13 /tmp/speedup_ids.txt /tmp/main_ids.txt | head -20
        if [ $only_in_main -gt 20 ]; then
            echo "... and $((only_in_main - 20)) more"
        fi
        echo ""
    fi

    # Create detailed diff file
    {
        echo "ILCDB Report Comparison - Detailed Diff"
        echo "========================================"
        echo "Date: $(date)"
        echo "Date Range: $START_DATE to $END_DATE"
        echo ""
        echo "Speedup branch: $speedup_count clients"
        echo "Main branch:    $main_count clients"
        echo ""
        echo "ONLY IN SPEEDUP BRANCH:"
        echo "----------------------"
        comm -23 /tmp/speedup_ids.txt /tmp/main_ids.txt
        echo ""
        echo "ONLY IN MAIN BRANCH:"
        echo "-------------------"
        comm -13 /tmp/speedup_ids.txt /tmp/main_ids.txt
    } > "$DIFF_FILE"

    echo ">>> Full diff saved to: $DIFF_FILE"
    echo "================================================================================"
}

# Check if we're doing a single-branch fetch or comparison
if [ "$1" == "speedup" ]; then
    fetch_report "$SPEEDUP_FILE" "speedup"
elif [ "$1" == "main" ]; then
    fetch_report "$MAIN_FILE" "main"
elif [ "$1" == "compare" ]; then
    if [ ! -f "$SPEEDUP_FILE" ] || [ ! -f "$MAIN_FILE" ]; then
        echo "Error: Both report files must exist for comparison"
        echo "Run './compare-report-api.sh speedup' and './compare-report-api.sh main' first"
        exit 1
    fi
    compare_reports
else
    # Interactive mode - guide the user
    echo "This script compares report results between the speedup and main branches."
    echo ""
    echo "USAGE:"
    echo "  1. Make sure the app is running in Docker"
    echo "  2. On ilcdb-speedup branch, run: $0 speedup"
    echo "  3. Switch to main branch and rebuild Docker"
    echo "  4. On main branch, run: $0 main"
    echo "  5. Compare results: $0 compare"
    echo ""
    echo "CURRENT STATUS:"

    if [ -f "$SPEEDUP_FILE" ]; then
        local speedup_count=$(cat "$SPEEDUP_FILE" | jq -r '.metadata.totalClients // "unknown"')
        echo "  ✓ Speedup results exist: $speedup_count clients"
    else
        echo "  ✗ Speedup results missing"
    fi

    if [ -f "$MAIN_FILE" ]; then
        local main_count=$(cat "$MAIN_FILE" | jq -r '.metadata.totalClients // "unknown"')
        echo "  ✓ Main results exist: $main_count clients"
    else
        echo "  ✗ Main results missing"
    fi

    echo ""
    echo "QUICK START (if Docker is running now):"
    echo "  $0 speedup"
    echo ""
fi
