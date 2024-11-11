terraform {
  required_providers {
    aws = {
      source = "hashicorp/aws"
    }
  }

  backend "s3" {
    bucket = "csd-tfstate"
    key    = "state/terraform.tfstate"
    region = "us-east-1"
  }
}
