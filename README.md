[1] Jenkins on Docker. No Jenkins Agents. Build jobs are run from within the same Docker container where Jenkins is running.

[1.1] We use our own custom Jenkins image, which has Helm, kubectl, docker-client, sonar-scanner packages installed.

[2] K3D for K8s cluster (on Docker).

[3] Application/Microservices Docker Images are built from within Jenkins container, using Docker.

[3.1] The Jenkins container mounts the MacBooks Docker socket. This is because, its hard to run Docker within a docker container.

[4] A Jenkins build does the following:
a. Checksout the Monorepo from GitHub
b. Detects which Microservices code has changed
c. Triggers the changed Microservices' jenkinsfile
d. The Microservices' jenkinsfile:
  i. does static code analysis using SonarScanner and SonarQube
  ii. creates a docker image
  iii. pushes the docker image to DockerHub
  iv. deploys the image to K3D using helm charts
  v. tests/verifies that the helm release has worked
  vi. [optional/manual] deletes the helm release
