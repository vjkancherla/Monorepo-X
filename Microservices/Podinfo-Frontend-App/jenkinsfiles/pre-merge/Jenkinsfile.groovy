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
    withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
        sh '''
            echo ${DOCKER_PASSWORD} | sudo docker login -u ${DOCKER_USERNAME} --password-stdin
            sudo docker push ${IMAGE_REPO}:${IMAGE_TAG}
        '''
    }
  }
}

stage('Deploy to K3D Dev') {
  dir('Microservices/Podinfo-Frontend-App/helm-chart') {
      script {
        withCredentials([file(credentialsId: 'k3d-config', variable: 'KUBECONFIG')]) {
          sh """
            export KUBECONFIG=${KUBECONFIG}
            helm upgrade --install helm-pi-fe-dev -n dev --create-namespace \
            --values namespaces/dev/values.yaml \
            --set image.repository=${env.IMAGE_REPO} \
            --set image.tag=${env.IMAGE_TAG} \
            .
          """
        }
      }
  }
}

stage('Test K3D Dev') {
    sleep(30)
    script {
        sh "kubectl run curl --image=curlimages/curl -i --rm --restart=Never -- curl frontend-podinfo-dev.dev.svc.cluster.local:9898"
    }
}

stage('Delete K3D Dev Helm Release') {
  dir('Microservices/Podinfo-Frontend-App/helm-chart') {
    script {
      input message: 'Do you want to delete the helm release?', ok: 'Yes'
      withCredentials([file(credentialsId: 'k3d-config', variable: 'KUBECONFIG')]) {
        sh """
          export KUBECONFIG=${KUBECONFIG}
          helm delete helm-pi-fe-dev -n dev
        """
      }
    }
  }
}
