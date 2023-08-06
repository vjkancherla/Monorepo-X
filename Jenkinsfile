node {
    // Environment Variables
    env.PROJECT = "MonoRepo-Microservices"
    env.REGISTRY_USER = "vjkancherla"
    env.GIT_COMMIT_HASH = sh (script: "git log -n 1 --pretty=format:'%H'", returnStdout: true).trim()
    env.IMAGE_REPO = "vjkancherla/podinfo_application_jenkins"
    env.IMAGE_TAG = "${GIT_COMMIT_HASH}"

    stage('Detect Changes') {
        // Change this to be whatever branches you need to compare
        def branchToCompare = 'origin/main'

        // Initialize a changes map
        def changes = [
            'Podinfo-Frontend-App': false,
            'Python-App': false,
            'microservice3': false,
        ]

        // Check if there's a previous commit
        def previousCommitExists = sh(
            script: "git rev-parse ${branchToCompare}^",
            returnStatus: true
        ) == 0

        def changedFiles

        if (previousCommitExists) {
          // Get a list of all changed files
          changedFiles = sh(
              script: "git diff --name-only ${branchToCompare}^ ${branchToCompare}",
              returnStdout: true
          ).trim().split("\n")
        }
        else {
            // If there's no previous commit, consider all files as changed
            changedFiles = sh(
                script: "git ls-files",
                returnStdout: true
            ).trim().split("\n")
        }

        // Detect changes in individual microservices
        for (file in changedFiles) {
            if (file.startsWith('Microservices/Podinfo-Frontend-App')) {
                changes.'Podinfo-Frontend-App' = true
            } else if (file.startsWith('Microservices/Python-App')) {
                changes.'Python-App' = true
            } else if (file.startsWith('microservice3')) {
                changes.'microservice3' = true
            }
        }

        // Print changes
        for (entry in changes) {
            println "${entry.key} has changes: ${entry.value}"
        }

        // Set changes in environment variables for later stages
        for (entry in changes) {
            env."${entry.key.toUpperCase()}_CHANGED" = "${entry.value}"
        }
    }

    stage('Run Changed Microservices') {
        def microservices = [
            'Podinfo-Frontend-App': 'Microservices/Podinfo-Frontend-App/jenkinsfiles/pre-merge/Jenkinsfile.groovy',
            'Python-App': 'Microservices/Python-App/jenkinsfiles/pre-merge/Jenkinsfile.groovy',
            'microservice3': 'microservice3/jenkinsfiles/pre-merge/Jenkinsfile.groovy',
        ]

        def buildStages = [:]
        for (entry in microservices) {
            def microservice = entry.key
            def jenkinsfilePath = entry.value

            def changed = env."${microservice.toUpperCase()}_CHANGED"

            if (changed == 'true') {
                buildStages[microservice] = {
                    node {
                        stage("${microservice}") {
                            load("${jenkinsfilePath}")
                        }
                    }
                }
            }
        }

        if (!buildStages.isEmpty()) {
            parallel buildStages
        }
    }
}
