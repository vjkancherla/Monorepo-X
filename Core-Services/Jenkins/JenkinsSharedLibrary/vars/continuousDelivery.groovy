def call() {
     pipeline {
        agent any

        options {
            // how and when build records or artifacts are discarded for a particular Jenkins job or pipeline
            buildDiscarder(logRotator(numToKeepStr: '5', artifactNumToKeepStr: '5'))

            // prevent multiple concurrent executions of the entire job or a specific stage
            disableConcurrentBuilds()

            // display the timestamp for when that code or step starts and finishes executing
            timestamps()
        }
        
        stages {
            stage('Build Container Image') {
                steps {
                    echo "Build Container Imag"
                }
            }

            stage('Publish Container Image') {
                steps {
                    echo "Publish Container Imag"
                }
            }

            stage('Update Helm charts to use the correct image') {
                steps {
                    echo "Update helm charts to use the correct image"
                }
            }

            stage('Publish Helm charts to JFrog') {
                steps {
                    echo "Publish Helm chanrts to JFrog"
                }
            }

            stage('Create a K8s NS') {
                steps {
                    echo "Create a K8s NS"
                }
            }

            stage('Deploy any database migration K8s Jobs') {
                steps {
                    echo "Deploy any database migration K8s Jobs "
                }
            }

            stage('Deploy Python and Go Apps IN PARALLEL') {
                steps {
                    echo "Deploy Python and Go Apps IN PARALLEL "
                }
            }

            stage('Run Test Suite: Regression tests + Acceptance tests + Smoke tests + E2E tests') {
                steps {
                    echo "Run Test Suite: Regression tests + Acceptance tests + Smoke tests + E2E tests"
                }
            }

            stage('Tear Down K8s NS') {
                steps {
                    echo "Tear Down K8s NS"
                }
            }
        }
     }
}