stage("Package-Image") {
  dir('Microservices/Podinfo-Frontend-App/src')
  {
    script {

        echo "Environment variables:"
        echo "PROJECT: ${env.PROJECT}"
        echo "REGISTRY_USER: ${env.REGISTRY_USER}"
        echo "GIT_COMMIT_HASH: ${env.GIT_COMMIT_HASH}"
        echo "IMAGE_REPO: ${env.IMAGE_REPO}"
        echo "IMAGE_TAG: ${env.IMAGE_TAG}"

        sh "sudo docker build -t ${env.IMAGE_REPO}:${env.IMAGE_TAG} -f Dockerfile ."
    }
  }
}

stage("Push-Image-To-DockerHub") {
  script {
    withCredentials([usernamePassword(credentialsId: "${env.DOCKER_CREDENTIALS}", usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
        sh '''
            echo ${DOCKER_PASSWORD} | sudo docker login -u ${DOCKER_USERNAME} --password-stdin
            sudo docker push ${IMAGE_REPO}:${IMAGE_TAG}
        '''
    }
  }
}
