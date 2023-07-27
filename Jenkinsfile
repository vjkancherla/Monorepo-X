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
                        'Podinfo-Frontend-App': false,
                        'microservice2': false,
                        'microservice3': false,
                    ]

                    // Check if there's a previous commit
                    def previousCommitExists = sh(
                        script: "git rev-parse ${branchToCompare}^",
                        returnStatus: true
                    ) == 0

                    if (previousCommitExists) {
                      // Get a list of all changed files
                      changedFiles = sh(
                          script: "git diff --name-only ${branchToCompare}^ ${branchToCompare}",
                          returnStdout: true
                      ).trim().split("\n")
                    }
                    else {
                        // If there's no previous commit, consider all files as changed
                        changedFiles = sh(
                            script: "git ls-files",
                            returnStdout: true
                        ).trim().split("\n")
                    }

                    // Detect changes in individual microservices
                    for (file in changedFiles) {
                        if (file.startsWith('Microservices/Podinfo-Frontend-App')) {
                            changes.'Podinfo-Frontend-App' = true
                        } else if (file.startsWith('microservice2')) {
                            changes.'microservice2' = true
                        } else if (file.startsWith('microservice3')) {
                            changes.'microservice3' = true
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
        stage('Podinfo-Frontend-App') {
            when {
                expression {
                    return env.MICROSERVICE1_CHANGED == 'true'
                }
            }
            steps {
                println "Nothing to do, yet."
            }
        }

        stage('Microservice2') {
            when {
                expression {
                    return env.MICROSERVICE2_CHANGED == 'true'
                }
            }
            steps {
                println "Nothing to do, yet."
            }
        }

        stage('Microservice3') {
            when {
                expression {
                    return env.MICROSERVICE3_CHANGED == 'true'
                }
            }
            steps {
                println "Nothing to do, yet."
            }
        }
    }
}
