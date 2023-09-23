def call() {
    pipeline {
        agent any
        
        stages {
            
            // stage('Lint Code') {
            //     steps {
            //         sh(libraryResource('lint.sh'))
            //     }
            // }

            // stage('Compile and Build Code') {
            //     steps {
            //         sh(libraryResource('buildAndCompile.sh'))
            //     }
            // }

            // stage('Unit Tests') {
            //     steps {
            //         sh(libraryResource('runUnitTests.sh'))
            //     }
            // }

            // stage('Static Code Analysis') {
            //     steps {
            //         script {
            //             withSonarQubeEnv(installationName: 'SonarQube-on-Docker') {
            //                 // sonarEnvVars = env
            //                 sh(libraryResource('sonarScanner.sh'))
            //             }

            //             timeout(time: 2, unit: 'MINUTES') {
            //                 def qG = waitForQualityGate()
            //                 if (qG.status != 'OK') {
            //                     error "Pipeline aborted due to quality gate failure: ${qG.status}"
            //                 }
            //             }
            //         }
            //     }                  
            // }

            stage('Build Container') {
                steps {
                    script {
                        def GIT_COMMIT_HASH = sh (script: "git log -n 1 --pretty=format:'%H'", returnStdout: true)
                        PYTHON_IMAGE_TAG = "${PYTHON_IMAGE_REPO}:${GIT_COMMIT_HASH}"
                        GO_IMAGE_TAG = "${GO_IMAGE_REPO}:${GIT_COMMIT_HASH}"

                        println("${PYTHON_IMAGE_TAG} :: ${GO_IMAGE_TAG}")
                        
                        withEnv(["PY_IMAGE_TAG=${PYTHON_IMAGE_TAG}", "GOO_IMAGE_TAG=${GO_IMAGE_TAG}"]) {
                            sh(libraryResource('buildContainerImage.sh'))
                        }
                    }
                }
            }
            
        }
    
    }
}