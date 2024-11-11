# outputs.tf

output "alb_hostname" {
  value = "${aws_alb.main.dns_name}:3000"
}

# # Output the necessary values
# output "ecs_cluster_name" {
#   value = aws_ecs_cluster.main.name
# }
