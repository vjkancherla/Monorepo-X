
stage("Package-Image") {
  dir('Microservices/podinfo-application-frontend')
  {
    script {

        echo "Environment variables:"
        echo "PROJECT: ${env.PROJECT}"
        echo "REGISTRY_USER: ${env.REGISTRY_USER}"
        echo "GIT_COMMIT_HASH: ${env.GIT_COMMIT_HASH}"
        echo "IMAGE_REPO: ${env.IMAGE_REPO}"
        echo "IMAGE_TAG: ${env.IMAGE_TAG}"

        sh "buildah --storage-driver vfs bud -t ${env.IMAGE_REPO}:${env.IMAGE_TAG} -f Dockerfile ."
    }
  }
}

stage("Push-Image-To-DockerHub") {
  script {
    withCredentials([usernamePassword(credentialsId: "${env.DOCKER_CREDENTIALS}", usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
        sh '''
            echo ${DOCKER_PASSWORD} | buildah login -u ${DOCKER_USERNAME} --password-stdin docker.io
            buildah push ${IMAGE_REPO}:${IMAGE_TAG} docker://${IMAGE_REPO}:${IMAGE_TAG}
        '''
    }
  }
}
