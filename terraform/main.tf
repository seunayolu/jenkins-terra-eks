provider "aws" {
  region = var.region
}

provider "kubernetes" {
  host = module.eks.cluster_endpoint
  cluster_ca_certificate = base64decode(module.eks.cluster_certificate_authority_data)
}

terraform {
  backend "s3" {
    bucket      = "backend-terra-buk"
    key       = "state/terraform.tfstate"
    region      = "us-east-2"
  }
}

data "aws_availability_zones" "azs" {}