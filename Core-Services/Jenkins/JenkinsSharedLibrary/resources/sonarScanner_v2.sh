#!/bin/bash

# Initialize an exit code variable
EXIT_CODE=0

# Loop through all environment variables and print them
for var in $(env); do
    echo "$var"
done

sonarqube_analysis() {
    cd Microservices

    # Run the SonarScanner for your project with the stored token
    sonar-scanner

    #Capture the exit code of the SonarScanner command and update the overall exit code
    dir_exit_code=$?
    if [ "$dir_exit_code" -ne 0 ]; then
        EXIT_CODE=$dir_exit_code
    fi
}

sonarqube_analysis

exit $EXIT_CODE
