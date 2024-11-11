resource "aws_instance" "web_server" {
  ami                    = "ami-057318957b68ae793" # Amazon Linux 2 AMI (replace with a current AMI ID if needed)
  instance_type          = "t4g.medium"
  subnet_id              = aws_subnet.public[0].id
  vpc_security_group_ids = [aws_security_group.public_sg.id]

  # User data script to install Docker, pull the Spring Boot image, and run it
  user_data = <<-EOF
              #!/bin/bash
              # Install Docker
              # Update and install prerequisite packages
              sudo apt-get update -y
              sudo apt-get install -y apt-transport-https ca-certificates curl software-properties-common

              # Install Docker
              curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg
              echo "deb [arch=arm64 signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
              sudo apt-get update -y
              sudo apt-get install -y docker-ce docker-ce-cli containerd.io

              # Add ubuntu user to the docker group
              sudo usermod -aG docker ubuntu

              # Enable and start Docker service
              sudo systemctl enable docker
              sudo systemctl start docker

              # Install Docker Compose
              sudo curl -L "https://github.com/docker/compose/releases/download/1.28.5/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
              sudo chmod +x /usr/local/bin/docker-compose

              git clone https://github.com/jjlim7/cs203-csd
              

              EOF

  tags = {
    Name = "SpringBootWebServer"
  }
}
