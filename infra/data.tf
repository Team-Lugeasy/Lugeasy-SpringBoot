data "aws_vpc" "lugeasy_vpc" {
    id = "vpc-010ae6b4cbc4493ca"
    cidr_block = "10.0.0.0/24"
}

data "aws_subnet" "lugeasy_public_subnet" {
    id = "subnet-03c09bf8b674e323e"
}

data "aws_subnet" "lugeasy_private_subnet" {
    id = "subnet-0b828c2949ebf9f57"
}