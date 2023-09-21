def call(){
    pipeline {
        agent any
            stage('Lint Code') {
                steps {
                    dir('Microservices/Podinfo-Frontend-App') {
                        echo "Lint Go Code"
                    }

                    dir('Microservices/Python-App') {
                        echo "Lint Python Code"
                    }
                }
            }

            stage('Compile and Build Code') {
                steps {
                    dir('Microservices/Podinfo-Frontend-App') {
                        echo "Compile and Build Go Code"
                    }

                    dir('Microservices/Python-App') {
                        echo "Compile and Build Python Code"
                    }
                }
            }

            stage('Unit Tests') {
                steps {
                    dir('Microservices/Podinfo-Frontend-App') {
                        echo "Run Unit Tests for Go"
                    }

                    dir('Microservices/Python-App') {
                        echo "Run Unit Tests for Python"
                    }
                }
            }

            stage('Static Code Analysis') {
                steps {
                    dir('Microservices/Podinfo-Frontend-App') {
                        echo "Run Static Code Analysis"
                    }

                    dir('Microservices/Python-App') {
                        echo "Run Static Code Analysis"
                    }
                }
            }


            stage('Build Container') {
                steps {
                    dir('Microservices/Podinfo-Frontend-App') {
                        echo "Build Container for Go"
                    }

                    dir('Microservices/Python-App') {
                        echo "Build Container for Python"
                    }
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
