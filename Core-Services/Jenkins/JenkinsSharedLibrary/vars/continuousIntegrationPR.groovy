def call() {
    pipeline {
        agent any
        
        stages {
            
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
                            // sonarEnvVars = env
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
            
        }
    
    }
}