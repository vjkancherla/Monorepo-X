stage("SonarQube-Analysis") {
  dir('Microservices/Podinfo-Frontend-App/src') {
      script {
           echo "HELLO!"
          }
      }
  }
}
