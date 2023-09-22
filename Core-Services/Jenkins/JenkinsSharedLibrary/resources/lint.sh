#!/bin/bash

# List of directories to lint
directories=("Microservices/Python-App/src" "Microservices/Podinfo-Frontend-App/src")

# Initialize an exit code variable
EXIT_CODE=0

# Iterate over directories and lint the code
for dir in "${directories[@]}"; do
    echo "Linting code in directory: $dir"
    cd "$dir" || exit 1  # Change to the directory; exit on failure
    # flake8 .
    
    # Capture the exit code of Flake8 and update the overall exit code
    dir_exit_code=$?
    if [ "$dir_exit_code" -ne 0 ]; then
        EXIT_CODE=$dir_exit_code
    fi

    cd -  # Return to the previous directory
done

exit $EXIT_CODE
