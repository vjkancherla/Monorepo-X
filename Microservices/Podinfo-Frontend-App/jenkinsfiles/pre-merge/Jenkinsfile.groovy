#!/usr/bin/groovy

pipeline {
  agent any

  options {
      disableConcurrentBuilds()
  }

  environment {
    PROJECT = "Podinfo-Frontend"
    REGISTRY_USER = "vjkancherla"
    GIT_COMMIT_HASH = sh (script: "git log -n 1 --pretty=format:'%H'", returnStdout: true)
    IMAGE_REPO = "vjkancherla/podinfo_application_jenkins"
    IMAGE_TAG = "${GIT_COMMIT_HASH}"
    DOCKER_CREDENTIALS = credentials('dockerhub-creds')
    KUBECONFIG_CREDENTIALS = credentials('K3d-config-2')
  }

  stages  {
    stage("Build") {
      steps {
        package_image()
        push_image()
      }
    }

    /*
    stage("Deploy-To-Dev") {
      when {
        // skip this stage unless branch is NOT master
        not {
          branch "master"
        }
      }
      steps {
        deploy_to_dev()
      }
    }

    stage("Verify-Deployment-To-Dev") {
      when {
        // skip this stage unless branch is NOT master
        not {
          branch "master"
        }
      }
      steps {
        verify_deployment("dev")
      }
    }
    */

  }
}

def package_and_push_image() {
  dir('`pwd`/podinfo-application-frontend')
  {
    script {
        docker.build('${IMAGE_REPO}:${IMAGE_TAG}', 'Dockerfile')
    }
  }
}

def push_image() {
  script {
    docker.withRegistry('https://registry.hub.docker.com', 'DOCKER_CREDENTIALS') {
        docker.image('${IMAGE_REPO}:${IMAGE_TAG}').push()
    }
  }
}

/*
def deploy_to_dev() {
  container("k8s") {
    sh "helm upgrade --install -n dev --create-namespace --values helm-chart-podinfo-frontend/namespaces/dev/values.yaml --set image.repository=${IMAGE_REPO} --set image.tag=${IMAGE_TAG} helm-pi-fe-dev helm-chart-podinfo-frontend/"
  }
}

def verify_deployment(env) {
  sleep(30)
  container("k8s") {
    sh "kubectl run curl --image=curlimages/curl -i --rm --restart=Never -- curl frontend-podinfo-${env}.${env}.svc.cluster.local:9898"
  }
}
*/
