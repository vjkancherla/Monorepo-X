pipeline {
    agent any

    environment {
      PROJECT = "Podinfo-Frontend"
      REGISTRY_USER = "vjkancherla"
      GIT_COMMIT_HASH = sh (script: "git log -n 1 --pretty=format:'%H'", returnStdout: true)
      IMAGE_REPO = "vjkancherla/podinfo_application_jenkins"
      IMAGE_TAG = "${GIT_COMMIT_HASH}"
      DOCKER_CREDENTIALS = credentials('dockerhub-creds')
      KUBECONFIG_CREDENTIALS = credentials('K3d-config-2')
      STORAGE_OPTS = "--root /var/jenkins_home/.local/share/containers/storage"
}

    }

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
                    return env.'PODINFO-FRONTEND-APP_CHANGED'== 'true'
                }
            }
            steps {
              script {
                  def jenkinsfilePath = 'Microservices/Podinfo-Frontend-App/jenkinsfiles/pre-merge/Jenkinsfile.groovy'

                  // Read Jenkinsfile contents
                  def jenkinsfileContents = readFile(jenkinsfilePath)

                  // Evaluate the Jenkinsfile
                 evaluate(jenkinsfileContents)
              }
            }
        }

        stage('Microservice2') {
            when {
                expression {
                    return env.'MICROSERVICE2_CHANGED' == 'true'
                }
            }
            steps {
              script {
                println "Nothing to do, yet."
              }
            }
        }

        stage('Microservice3') {
            when {
                expression {
                    return env.'MICROSERVICE3_CHANGED' == 'true'
                }
            }
            steps {
              script {
                println "Nothing to do, yet."
              }
            }
        }
    }
}
