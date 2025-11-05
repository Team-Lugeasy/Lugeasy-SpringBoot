data "aws_vpc" "lugeasy_vpc" {
  filter {
    name   = "tag:Name"
    values = ["lugeasy-vpc"]
  }
}

data "aws_subnet" "lugeasy_public_subnet_a" {
  filter {
    name   = "tag:Name"
    values = ["lugeasy-public-subnet-a"]
  }

  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.lugeasy_vpc.id]
  }
}

data "aws_subnet" "lugeasy_public_subnet_c" {
  filter {
    name   = "tag:Name"
    values = ["lugeasy-public-subnet-c"]
  }

  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.lugeasy_vpc.id]
  }
}

data "aws_subnet" "lugeasy_private_subnet" {
  filter {
    name   = "tag:Name"
    values = ["lugeasy-private-subnet"]
  }

  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.lugeasy_vpc.id]
  }
}

data "aws_key_pair" "lugeasy" {
  key_name = "lugeasy-server-key-pair"
}

