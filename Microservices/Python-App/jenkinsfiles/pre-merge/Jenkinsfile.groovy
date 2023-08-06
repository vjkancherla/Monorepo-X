stage("SonarQube-Analysis") {
  dir('Microservices/Python-App/src') {
      script {
           withSonarQubeEnv(installationName: 'SonarQube-on-Docker') {
              // Run the SonarScanner for your project with the stored token
              sh "sonar-scanner"
          }

          timeout(time: 2, unit: 'MINUTES') {
           def qgStatus = waitForQualityGate().status
           if (qgStatus != 'OK') {
             error "Pipeline aborted due to quality gate failure: ${qgStatus}"
           }
         }
      }
  }
}
