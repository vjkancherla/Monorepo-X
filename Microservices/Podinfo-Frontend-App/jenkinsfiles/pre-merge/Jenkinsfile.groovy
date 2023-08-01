stage("SonarQube-Analysis") {
  dir('Microservices/Podinfo-Frontend-App/src') {
      script {
           withSonarQubeEnv(installationName: 'SonarQube-on-Docker', credentialsId: 'sonarqube-token') {
              // Run the SonarScanner for your project with the stored token
              sh "sonar-scanner"
          }
      }
  }
}

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

stage('Deploy App to K3D Dev') {
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

stage('Test App in K3D Dev') {
    script {
      withCredentials([file(credentialsId: 'k3d-config', variable: 'KUBECONFIG')]) {
        sh '''
          export KUBECONFIG=${KUBECONFIG}
          for i in {1..30}; do
            if kubectl get pods -n dev | grep Running; then
              echo "Application is running!"
              break
            elif [ "$i" -eq "30" ]; then
              echo "Application failed to start within the expected time."
              exit 1
            else
              echo "Waiting for application to start..."
              sleep 1
            fi
          done
          service_name=$(kubectl get service -n dev -o jsonpath='{.items[*].metadata.name}')
          kubectl run -n dev curl --image=curlimages/curl -i --rm --restart=Never -- curl ${service_name}:9898
        '''
      }
    }
}

stage('Delete K3D Dev Helm Release') {
  script {
    try {
      timeout(time: 30, unit: 'SECONDS') {
        input message: 'Do you want to delete the helm release?', ok: 'Yes'
      }
      withCredentials([file(credentialsId: 'k3d-config', variable: 'KUBECONFIG')]) {
        sh """
          export KUBECONFIG=${KUBECONFIG}
          helm delete helm-pi-fe-dev -n dev
        """
      }
    } catch (Exception e) {
      echo 'User did not respond within 30 seconds. Proceeding with default behavior.'
    }
  }
}
