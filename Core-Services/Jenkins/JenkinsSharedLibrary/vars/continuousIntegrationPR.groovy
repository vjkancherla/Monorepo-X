def call() {
    pipeline {
        agent any
        
        stages {
            
            // stage('Lint Code') {
            //     steps {
            //         sh(libraryResource('lint.sh'))
            //     }
            // }

            // stage('Compile and Build Code') {
            //     steps {
            //         sh(libraryResource('buildAndCompile.sh'))
            //     }
            // }

            // stage('Unit Tests') {
            //     steps {
            //         sh(libraryResource('runUnitTests.sh'))
            //     }
            // }

            stage('Static Code Analysis') {
                steps {
                    script {
                        def sonarEnvVars

                        withSonarQubeEnv(installationName: 'SonarQube-on-Docker') {
                            sonarEnvVars = env
                        }

                        // Export the captured environment variables for use in the shell script
                        env.PATH += ":${sonarEnvVars.PATH}"
                        env.SONAR_USER_HOME = sonarEnvVars.SONAR_USER_HOME
                        env.SONAR_SCANNER_OPTS = sonarEnvVars.SONAR_SCANNER_OPTS
                        env.SONARQUBE_SCANNER_PARAMS = sonarEnvVars.SONARQUBE_SCANNER_PARAMS

                        sh(libraryResource('sonarScanner.sh'))
                        
                        timeout(time: 2, unit: 'MINUTES') {
                            def qg = waitForQualityGate()
                            if (qg.status != 'OK') {
                                error "Pipeline aborted due to quality gate failure: ${qg.status}"
                            }
                        }
                    }
                }                  
            }
            
        }
    
    }
}