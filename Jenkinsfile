pipeline {
    agent any

    stages {
        stage('Detect Changes') {
            steps {
                script {
                    // Change this to be whatever branches you need to compare
                    def branchToCompare = 'origin/main'

                    // Initialize a changes map
                    def changes = [
                        microservice1: false,
                        microservice2: false,
                        microservice3: false,
                    ]

                    // Get a list of all changed files
                    def changedFiles = sh(
                        script: "git diff --name-only ${branchToCompare}",
                        returnStdout: true
                    ).trim().split("\n")

                    // Detect changes in individual microservices
                    for (file in changedFiles) {
                        if (file.startsWith('microservice1')) {
                            changes.microservice1 = true
                        } else if (file.startsWith('microservice2')) {
                            changes.microservice2 = true
                        } else if (file.startsWith('microservice3')) {
                            changes.microservice3 = true
                        }
                    }

                    // Print changes
                    for (entry in changes) {
                        println "${entry.key} has changes: ${entry.value}"
                    }

                    // Set changes in environment variables for later stages
                    for (entry in changes) {
                        env."${entry.key.toUpperCase()}_CHANGED" = "${entry.value}"
                    }
                }
            }
        }

        // Use changes in your build/test stages
        stage('Microservice1') {
            when {
                expression {
                    return env.MICROSERVICE1_CHANGED == 'true'
                }
            }
            steps {
                // Your build/test steps for microservice1
            }
        }

        stage('Microservice2') {
            when {
                expression {
                    return env.MICROSERVICE2_CHANGED == 'true'
                }
            }
            steps {
                // Your build/test steps for microservice2
            }
        }

        stage('Microservice3') {
            when {
                expression {
                    return env.MICROSERVICE3_CHANGED == 'true'
                }
            }
            steps {
                // Your build/test steps for microservice3
            }
        }
    }
}
