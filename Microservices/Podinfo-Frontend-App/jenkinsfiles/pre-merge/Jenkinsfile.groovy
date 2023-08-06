
pipeline {
    agent any

    stages {
        stage('SonarQube-Analysis') {
            steps {
                dir('Microservices/Podinfo-Frontend-App/src') {
                    script {
                        withSonarQubeEnv('SonarQube-on-Docker') {
                            // Run the SonarScanner for your project with the stored token
                            sh 'sonar-scanner'
                        }

                        timeout(time: 2, unit: 'MINUTES') {
                            script {
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
}
