[1] Jenkins on Docker. No Jenkins Agents. Build jobs are run from within the same Docker container where Jenkins is running.

[2] K3D for K8s (on Docker)

[3] Jenkins deploys to K3D

[4] Docker Images are built from within Jenkins container

[4.1] The Jenkins container mounts the MacBooks Docker socket. This is because, its hard to run docker within a docker container.
