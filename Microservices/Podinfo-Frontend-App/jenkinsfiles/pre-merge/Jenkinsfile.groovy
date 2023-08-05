

stage('Test App in K3D Dev') {
  script {
      withCredentials([file(credentialsId: 'k3d-config', variable: 'KUBECONFIG')]) {
          sh script: '''
              export KUBECONFIG=${KUBECONFIG}

              i=1
              while [ "$i" -le 30 ]; do
                  if kubectl get pods -n dev | grep -q Running; then
                      echo "Application is running!"
                      break
                  elif [ "$i" -eq 30 ]; then
                      echo "Application failed to start within the expected time."
                      currentBuild.result = 'FAILURE'
                      exit 1
                  else
                      echo "Waiting for application to start..."
                      sleep 1
                      i=$((i+1))
                  fi
              done

              service_name=$(kubectl get service -n dev -o jsonpath='{.items[*].metadata.name}')
              kubectl run -n dev curl --image=curlimages/curl -i --rm --restart=Never -- curl ${service_name}:9898
          ''',
          shell: 'bash'
      }
  }
}
