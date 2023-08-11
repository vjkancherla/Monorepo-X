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

stage("Package-Image") {
  dir('Microservices/Python-App/src')
  {
    IMAGE_REPO = "${env.REGISTRY_USER}/python_app_jenkins"
    script {
        sh "sudo docker build -t ${IMAGE_REPO}:${env.IMAGE_TAG} -f Dockerfile ."
    }
  }
}

stage("Push-Image-To-DockerHub") {
  IMAGE_REPO = "${env.REGISTRY_USER}/python_app_jenkins"
  script {
    withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
        sh '''
            echo ${DOCKER_PASSWORD} | sudo docker login -u ${DOCKER_USERNAME} --password-stdin
            echo docker push ${IMAGE_REPO}:${env.IMAGE_TAG}
            sudo docker push ${IMAGE_REPO}:${env.IMAGE_TAG}
        '''
    }
  }
}
