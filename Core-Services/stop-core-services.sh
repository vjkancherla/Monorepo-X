#!/bin/bash

# Function to verify command success
function verify_command_success {
  if [[ $? -eq 0 ]]; then
    echo "Success: $1"
  else
    echo "Error: $1"
  fi
}

# Step 1: Stop and remove the SonarQube container
docker stop sonarqube
verify_command_success "Stopped SonarQube container."

docker rm sonarqube
verify_command_success "Removed SonarQube container."

# Step 2: Stop and remove the Jenkins container
docker stop jenkins-docker
verify_command_success "Stopped Jenkins container."

docker rm jenkins-docker
verify_command_success "Removed Jenkins container."

# Step 3: Delete K3D cluster
k3d cluster delete mycluster
verify_command_success "Deleted K3D cluster 'mycluster'."

# Step 4: Verify K3D cluster deletion completion
cluster_deleted=false
max_attempts=30
attempt=1

while [[ $attempt -le $max_attempts ]]; do
  k3d cluster list | grep mycluster
  if [[ $? -ne 0 ]]; then
    cluster_deleted=true
    break
  else
    echo "Waiting for K3D cluster 'mycluster' to be deleted... (Attempt $attempt of $max_attempts)"
    sleep 5
    ((attempt++))
  fi
done

if [[ $cluster_deleted == false ]]; then
  echo "Error: K3D cluster 'mycluster' could not be deleted."
  exit 1
fi

# Step 5: Stop Docker Desktop
osascript -e 'quit app "Docker"'

# Step 6: Verify Docker Desktop has stopped
echo "Verifying Docker Desktop..."
if [[ $(pgrep Docker) == "" ]]; then
  echo "Docker Desktop has been successfully stopped."
else
  echo "Error: Docker Desktop is still running."
fi
