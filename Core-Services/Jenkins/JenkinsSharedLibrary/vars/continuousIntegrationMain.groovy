def call() {
    pipeline {
        agent any

        environment {
            PYTHON_IMAGE_TAG = "${REGISTRY_USER}/python_app_jenkins:${GIT_COMMIT_HASH}"
            GO_IMAGE_TAG = "${REGISTRY_USER}/go_app_jenkins:${GIT_COMMIT_HASH}"
        }

        stages{

            stage('Lint Code') {
                steps {
                    sh(libraryResource('lint.sh'))
                }
            }

            stage('Compile and Build Code') {
                steps {
                    sh(libraryResource('buildAndCompile.sh'))
                }
            }

            stage('Unit Tests') {
                steps {
                    sh(libraryResource('runUnitTests.sh'))
                }
            }

            stage('Static Code Analysis') {
                steps {
                    script {
                        withSonarQubeEnv(installationName: 'SonarQube-on-Docker') {
                            sh(libraryResource('sonarScanner.sh'))
                        }

                        timeout(time: 2, unit: 'MINUTES') {
                            def qG = waitForQualityGate()
                            if (qG.status != 'OK') {
                                error "Pipeline aborted due to quality gate failure: ${qG.status}"
                            }
                        }
                    }
                }                  
            }

            stage('Build Container') {
                steps {
                    // script {
                    //     def python_image_tag = REGISTRY_USER+"/python_app_jenkins:"+GIT_COMMIT_HASH
                    //     def go_image_tag = REGISTRY_USER+"/go_app_jenkins:"+GIT_COMMIT_HASH

                    //     sh(libraryResource('buildContainerImage ${python_image_tag} ${go_image_tag}'))
                    // }
                    sh(libraryResource('buildContainerImage ${python_image_tag} ${go_image_tag}'))
                }
            }

            stage('Push Container to Docker Hub') {
                steps {
                    dir('Microservices/Podinfo-Frontend-App') {
                        echo "Push Container for Go"
                    }

                    dir('Microservices/Python-App') {
                        echo "Push Container for Python"
                    }
                }
            }
            
        }

    }
}