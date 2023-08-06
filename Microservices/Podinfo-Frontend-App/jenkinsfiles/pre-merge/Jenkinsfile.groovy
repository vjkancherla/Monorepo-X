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
      }
  }
}
