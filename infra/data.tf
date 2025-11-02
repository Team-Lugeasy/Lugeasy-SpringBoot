data "aws_vpc" "lugeasy_vpc" {
    id = "vpc-0e2383015022f184e"
    cidr_block = "10.0.0.0/24"
}

data "aws_subnet" "lugeasy_public_subnet" {
    id = "subnet-0f273e6148db9b268"
}

data "aws_subnet" "lugeasy_private_subnet" {
    id = "subnet-0f9496ed64852d32f"
}