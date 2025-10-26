provider "aws" {
  region = "ap-northeast-3"
}

terraform {
  backend "s3" {
    bucket         = "lugeasy-provider-bucket"
    key            = "terraform.tfstate"
    region         = "ap-northeast-3"
    encrypt        = true
  }
}