
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

        docker.build('${env.IMAGE_REPO}:${env.IMAGE_TAG}', 'Dockerfile')
    }
  }
}

stage("Push-Image-To-DockerHub") {
  script {
    docker.withRegistry('https://registry.hub.docker.com', 'DOCKER_CREDENTIALS') {
        docker.image('${env.IMAGE_REPO}:${env.IMAGE_TAG}').push()
    }
  }
}
