#!/bin/bash

# Start time
START_TIME=$(date +%s)

# Run Docker build command and append output to docker_run_log.txt
docker build --progress=plain -f sap-abap/$1.Dockerfile -t $1 --no-cache . >> $1.txt 2>&1

# End time
END_TIME=$(date +%s)

# Calculate duration
DURATION=$((END_TIME - START_TIME))

# Echo duration to both terminal and append to the log file
echo "Docker build duration: $DURATION seconds"

# Flag to track when to start capturing lines
start_capture=0

# Read the file line by line
while IFS= read -r line; do
    # Check if the line contains the start pattern and toggle the flag
    if [[ $line == *"[internal] load build context"* ]]; then
        start_capture=1
    fi

    # If the flag is set, print the line
    if [[ $start_capture -eq 1 ]]; then
        echo "$line"
        # If the line is empty, stop capturing and exit the loop
        if [[ -z $line ]]; then
            break
        fi
    fi
done < $1.txt

rm $1.txt