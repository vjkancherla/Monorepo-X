@NonCPS
def getQualityGateStatus() {
    return waitForQualityGate().status
}

stage("SonarQube-Analysis") {
  dir('Microservices/Podinfo-Frontend-App/src') {
      script {
           withSonarQubeEnv(installationName: 'SonarQube-on-Docker') {
              // Run the SonarScanner for your project with the stored token
              sh "sonar-scanner"
          }

          timeout(time: 2, unit: 'MINUTES') {
           def qgStatus = getQualityGateStatus()
           if (qgStatus != 'OK') {
             error "Pipeline aborted due to quality gate failure: ${qgStatus}"
           }
         }
      }
  }
}
