#!/bin/bash

# Define cluster and region
CLUSTER_NAME="cb-cluster"
REGION="ap-southeast-1"

# List of services to update
SERVICES=("auth-service" "user-service" "tournament-service" "matchmaking-service" "elo-ranking-service" "gateway-service")

# Loop through each service and update
for SERVICE in "${SERVICES[@]}"; do
    echo "Updating service: $SERVICE in cluster: $CLUSTER_NAME"
    
    # Run update command
    aws ecs update-service --cluster "$CLUSTER_NAME" --service "$SERVICE" --force-new-deployment --region "$REGION"
    
    # Check if the command was successful
    if [ $? -eq 0 ]; then
        echo "Successfully triggered deployment for $SERVICE"
    else
        echo "Failed to update service $SERVICE" >&2
    fi
done
