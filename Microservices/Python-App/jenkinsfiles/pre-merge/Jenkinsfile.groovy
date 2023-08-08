stage("SonarQube-Analysis") {
    dir('Microservices/Python-App/src') {
        script {
            withSonarQubeEnv(installationName: 'SonarQube-on-Docker') {
                // Run unit tests with coverage tool
                sh "coverage run -m unittest discover tests"
                // Generate the XML report
                sh "coverage xml"
                // Run the SonarScanner for your project with the stored token
                sh "sonar-scanner"
            }

            timeout(time: 2, unit: 'MINUTES') {
                def qg = waitForQualityGate()
                if (qg.status != 'OK') {
                    error "Pipeline aborted due to quality gate failure: ${qg.status}"
                }
            }
        }
    }
}
