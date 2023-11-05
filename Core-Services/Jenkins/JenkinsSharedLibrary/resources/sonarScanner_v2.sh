#!/bin/bash

# Initialize an exit code variable
EXIT_CODE=0

# Loop through all environment variables and print them
for var in $(env); do
    echo "$var"
done

# Function to run SonarQube analysis for Python code
run_python_sonarqube_analysis() {
    local python_dir="Microservices/Python-App/src"
    echo "Running SonarQube analysis for Python code in directory: $python_dir"
    cd "$python_dir" || exit 1  # Change to the Python directory; exit on failure
    
    # Run unit tests with coverage tool
    coverage run -m unittest discover tests
                
    # Generate the XML report
    coverage xml

    # Run the SonarScanner for your project with the stored token
    sonar-scanner

    # Capture the exit code of the SonarScanner command and update the overall exit code
    dir_exit_code=$?
    if [ "$dir_exit_code" -ne 0 ]; then
        EXIT_CODE=$dir_exit_code
    fi
}

# Function to run SonarQube analysis for Go code
run_go_sonarqube_analysis() {
    local go_dir="Microservices/Podinfo-Frontend-App/src"
    echo "Running SonarQube analysis for Go code in directory: $go_dir"
    cd "$go_dir" || exit 1  # Change to the Go directory; exit on failure
    
    # Run the SonarScanner for your project with the stored token
    sonar-scanner

    # Capture the exit code of the SonarScanner command and update the overall exit code
    dir_exit_code=$?
    if [ "$dir_exit_code" -ne 0 ]; then
        EXIT_CODE=$dir_exit_code
    fi
}

# Check the PROJECT environment variable and call the appropriate function
if [ "$PROJECT" == "python" ]; then
    run_python_sonarqube_analysis
elif [ "$PROJECT" == "go" ]; then
    run_go_sonarqube_analysis
else
    echo "Environment variable PROJECT is not set correctly."
    echo "Not running any SonarQube analysis."
    # Set a non-zero exit code to indicate failure
    EXIT_CODE=1
fi

exit $EXIT_CODE
