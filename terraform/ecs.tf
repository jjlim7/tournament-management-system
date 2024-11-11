# # ecs.tf
# resource "aws_service_discovery_private_dns_namespace" "my_namespace" {
#   name = "my-namespace"  # Adjust the name as needed (e.g., "internal")
#   vpc  = aws_vpc.main.id # Replace with your actual VPC ID
# }

# # Auth Service Discovery
# resource "aws_service_discovery_service" "auth_service_discovery" {
#   name         = "auth-service"
#   namespace_id = aws_service_discovery_private_dns_namespace.my_namespace.id
#   dns_config {
#     namespace_id = aws_service_discovery_private_dns_namespace.my_namespace.id
#     dns_records {
#       type = "A"
#       ttl  = 60
#     }
#     routing_policy = "MULTIVALUE"
#   }
# }

# # User Service Discovery
# resource "aws_service_discovery_service" "user_service_discovery" {
#   name         = "user-service"
#   namespace_id = aws_service_discovery_private_dns_namespace.my_namespace.id
#   dns_config {
#     namespace_id = aws_service_discovery_private_dns_namespace.my_namespace.id
#     dns_records {
#       type = "A"
#       ttl  = 60
#     }
#     routing_policy = "MULTIVALUE"
#   }
# }

# # Tournament Service Discovery
# resource "aws_service_discovery_service" "tournament_service_discovery" {
#   name         = "tournament-service"
#   namespace_id = aws_service_discovery_private_dns_namespace.my_namespace.id
#   dns_config {
#     namespace_id = aws_service_discovery_private_dns_namespace.my_namespace.id
#     dns_records {
#       type = "A"
#       ttl  = 60
#     }
#     routing_policy = "MULTIVALUE"
#   }
# }

# # Matchmaking Service Discovery
# resource "aws_service_discovery_service" "matchmaking_service_discovery" {
#   name         = "matchmaking-service"
#   namespace_id = aws_service_discovery_private_dns_namespace.my_namespace.id
#   dns_config {
#     namespace_id = aws_service_discovery_private_dns_namespace.my_namespace.id
#     dns_records {
#       type = "A"
#       ttl  = 60
#     }
#     routing_policy = "MULTIVALUE"
#   }
# }

# # Elo-Ranking Service Discovery
# resource "aws_service_discovery_service" "elo_ranking_service_discovery" {
#   name         = "elo-ranking-service"
#   namespace_id = aws_service_discovery_private_dns_namespace.my_namespace.id
#   dns_config {
#     namespace_id = aws_service_discovery_private_dns_namespace.my_namespace.id
#     dns_records {
#       type = "A"
#       ttl  = 60
#     }
#     routing_policy = "MULTIVALUE"
#   }
# }

# # Gateway Service Discovery
# resource "aws_service_discovery_service" "gateway_service_discovery" {
#   name         = "gateway-service"
#   namespace_id = aws_service_discovery_private_dns_namespace.my_namespace.id
#   dns_config {
#     namespace_id = aws_service_discovery_private_dns_namespace.my_namespace.id
#     dns_records {
#       type = "A"
#       ttl  = 60
#     }
#     routing_policy = "MULTIVALUE"
#   }
# }


# resource "aws_ecs_cluster" "main" {
#   name = "cb-cluster"
# }

# ### ECS Task Definition ###
# # Gateway Service
# resource "aws_ecs_task_definition" "gateway_task" {
#   family                   = "gateway-task"
#   network_mode             = "awsvpc"
#   requires_compatibilities = ["FARGATE"]
#   cpu                      = "512"
#   memory                   = "1024"
#   execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn
#   task_role_arn            = aws_iam_role.ecs_task_execution_role.arn

#   container_definitions = <<DEFINITION
# [
#   {
#     "name": "gateway-service",
#     "image": "${aws_ecr_repository.gateway_service.repository_url}:latest",
#     "cpu": 512,
#     "memory": 1024,
#     "portMappings": [
#       {
#         "containerPort": 8080,
#         "hostPort": 8080
#       }
#     ],
#     "essential": true
#   }
# ]
# DEFINITION
# }

# # ECS Task Definition for Matchmaking Service
# resource "aws_ecs_task_definition" "matchmaking_task" {
#   family                   = "matchmaking-task"
#   network_mode             = "awsvpc"
#   requires_compatibilities = ["FARGATE"]
#   cpu                      = "512"
#   memory                   = "1024"
#   execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn
#   task_role_arn            = aws_iam_role.ecs_task_execution_role.arn

#   container_definitions = <<DEFINITION
# [
#   {
#     "name": "matchmaking-service",
#     "image": "${aws_ecr_repository.matchmaking_service.repository_url}:latest",
#     "cpu": 512,
#     "memory": 1024,
#     "portMappings": [
#       {
#         "containerPort": 8080,
#         "hostPort": 8080
#       }
#     ],
#     "essential": true
#   }
# ]
# DEFINITION
# }

# # ECS Task Definition for Elo-Ranking Service
# resource "aws_ecs_task_definition" "elo_ranking_task" {
#   family                   = "elo-ranking-task"
#   network_mode             = "awsvpc"
#   requires_compatibilities = ["FARGATE"]
#   cpu                      = "512"
#   memory                   = "1024"
#   execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn
#   task_role_arn            = aws_iam_role.ecs_task_execution_role.arn

#   container_definitions = <<DEFINITION
# [
#   {
#     "name": "elo-ranking-service",
#     "image": "${aws_ecr_repository.elo_ranking_service.repository_url}:latest",
#     "cpu": 512,
#     "memory": 1024,
#     "portMappings": [
#       {
#         "containerPort": 8080,
#         "hostPort": 8080
#       }
#     ],
#     "essential": true
#   }
# ]
# DEFINITION
# }

# # ECS Task Definition for Tournament Service
# resource "aws_ecs_task_definition" "tournament_task" {
#   family                   = "tournament-task"
#   network_mode             = "awsvpc"
#   requires_compatibilities = ["FARGATE"]
#   cpu                      = "512"
#   memory                   = "1024"
#   execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn
#   task_role_arn            = aws_iam_role.ecs_task_execution_role.arn

#   container_definitions = <<DEFINITION
# [
#   {
#     "name": "tournament-service",
#     "image": "${aws_ecr_repository.tournament_service.repository_url}:latest",
#     "cpu": 512,
#     "memory": 1024,
#     "portMappings": [
#       {
#         "containerPort": 8080,
#         "hostPort": 8080
#       }
#     ],
#     "essential": true
#   }
# ]
# DEFINITION
# }

# # ECS Task Definition for User Service
# resource "aws_ecs_task_definition" "user_task" {
#   family                   = "user-task"
#   network_mode             = "awsvpc"
#   requires_compatibilities = ["FARGATE"]
#   cpu                      = "512"
#   memory                   = "1024"
#   execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn
#   task_role_arn            = aws_iam_role.ecs_task_execution_role.arn

#   container_definitions = <<DEFINITION
# [
#   {
#     "name": "user-service",
#     "image": "${aws_ecr_repository.user_service.repository_url}:latest",
#     "cpu": 512,
#     "memory": 1024,
#     "portMappings": [
#       {
#         "containerPort": 8080,
#         "hostPort": 8080
#       }
#     ],
#     "essential": true
#   }
# ]
# DEFINITION
# }


# ### ECS Services
# # ECS Service for Gateway Service
# resource "aws_ecs_service" "gateway_service" {
#   name            = "gateway-service"
#   cluster         = aws_ecs_cluster.main.id
#   task_definition = aws_ecs_task_definition.gateway_task.arn
#   desired_count   = 1
#   launch_type     = "FARGATE"

#   network_configuration {
#     security_groups  = [aws_security_group.ecs_tasks.id]
#     subnets          = aws_subnet.private[*].id
#     assign_public_ip = true
#   }

#   load_balancer {
#     target_group_arn = aws_alb_target_group.app.id
#     container_name   = "gateway-service"
#     container_port   = 8080
#   }
# }

# # ECS Service for Matchmaking Service
# resource "aws_ecs_service" "matchmaking_service" {
#   name            = "matchmaking-service"
#   cluster         = aws_ecs_cluster.main.id
#   task_definition = aws_ecs_task_definition.matchmaking_task.arn
#   desired_count   = 1
#   launch_type     = "FARGATE"

#   network_configuration {
#     security_groups  = [aws_security_group.ecs_tasks.id]
#     subnets          = aws_subnet.private[*].id
#     assign_public_ip = true
#   }

#   service_registries {
#     registry_arn = aws_service_discovery_service.matchmaking_service_discovery.arn
#   }
# }

# # ECS Service for Elo-Ranking Service
# resource "aws_ecs_service" "elo_ranking_service" {
#   name            = "elo-ranking-service"
#   cluster         = aws_ecs_cluster.main.id
#   task_definition = aws_ecs_task_definition.elo_ranking_task.arn
#   desired_count   = 1
#   launch_type     = "FARGATE"

#   network_configuration {
#     security_groups  = [aws_security_group.ecs_tasks.id]
#     subnets          = aws_subnet.private[*].id
#     assign_public_ip = true
#   }

#   service_registries {
#     registry_arn = aws_service_discovery_service.elo_ranking_service_discovery.arn
#   }
# }

# # ECS Service for Tournament Service
# resource "aws_ecs_service" "tournament_service" {
#   name            = "tournament-service"
#   cluster         = aws_ecs_cluster.main.id
#   task_definition = aws_ecs_task_definition.tournament_task.arn
#   desired_count   = 1
#   launch_type     = "FARGATE"

#   network_configuration {
#     security_groups  = [aws_security_group.ecs_tasks.id]
#     subnets          = aws_subnet.private[*].id
#     assign_public_ip = true
#   }

#   service_registries {
#     registry_arn = aws_service_discovery_service.tournament_service_discovery.arn
#   }
# }

# # ECS Service for User Service
# resource "aws_ecs_service" "user_service" {
#   name            = "user-service"
#   cluster         = aws_ecs_cluster.main.id
#   task_definition = aws_ecs_task_definition.user_task.arn
#   desired_count   = 1
#   launch_type     = "FARGATE"

#   network_configuration {
#     security_groups  = [aws_security_group.ecs_tasks.id]
#     subnets          = aws_subnet.private[*].id
#     assign_public_ip = true
#   }

#   service_registries {
#     registry_arn = aws_service_discovery_service.user_service_discovery.arn
#   }
# }

# # ECS Service for User Service
# resource "aws_ecs_service" "auth_service" {
#   name            = "auth-service"
#   cluster         = aws_ecs_cluster.main.id
#   task_definition = aws_ecs_task_definition.user_task.arn
#   desired_count   = 1
#   launch_type     = "FARGATE"

#   network_configuration {
#     security_groups  = [aws_security_group.ecs_tasks.id]
#     subnets          = aws_subnet.private[*].id
#     assign_public_ip = true
#   }

#   service_registries {
#     registry_arn = aws_service_discovery_service.auth_service_discovery.arn
#   }
# }
