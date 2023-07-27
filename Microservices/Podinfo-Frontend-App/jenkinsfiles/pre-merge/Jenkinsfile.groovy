
environment {
  PROJECT = "Podinfo-Frontend"
  REGISTRY_USER = "vjkancherla"
  GIT_COMMIT_HASH = sh (script: "git log -n 1 --pretty=format:'%H'", returnStdout: true)
  IMAGE_REPO = "vjkancherla/podinfo_application_jenkins"
  IMAGE_TAG = "${GIT_COMMIT_HASH}"
  DOCKER_CREDENTIALS = credentials('dockerhub-creds')
  KUBECONFIG_CREDENTIALS = credentials('K3d-config-2')
}


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
        docker.build('${IMAGE_REPO}:${IMAGE_TAG}', 'Dockerfile')
    }
  }
}

stage("Push-Image-To-DockerHub") {
  script {
    docker.withRegistry('https://registry.hub.docker.com', 'DOCKER_CREDENTIALS') {
        docker.image('${IMAGE_REPO}:${IMAGE_TAG}').push()
    }
  }
}
