
resource "aws_db_subnet_group" "my_db_subnet_group" {
  name       = "my-db-subnet-group"
  subnet_ids = [aws_subnet.private[0].id, aws_subnet.private[1].id, aws_subnet.public[0].id, aws_subnet.public[1].id]

  tags = {
    Name = "My DB Subnet Group"
  }
}

resource "aws_db_instance" "default" {
  allocated_storage   = 10
  storage_type        = "gp2"
  engine              = "postgres"
  engine_version      = "16.1"
  instance_class      = "db.t3.micro"
  identifier          = "mydb"
  username            = var.rds_username
  password            = var.rds_password
  publicly_accessible = true


  vpc_security_group_ids = [aws_security_group.rds_sg.id]
  db_subnet_group_name   = aws_db_subnet_group.my_db_subnet_group.name

  skip_final_snapshot = true
}
