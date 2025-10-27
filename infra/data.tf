data "aws_vpc" "lugeasy_vpc" {
    id = "vpc-010ae6b4cbc4493ca"
    cidr_block = "10.0.0.0/24"
}

data "aws_internet_gateway" "lugeasy_internet_gateway" {
    id = "igw-0024cce84865cc19c"
}

data "aws_route_table" "lugeasy_public_route_table" {
    id = "rtb-0fb5627e2f2982d56"
}

data "aws_route_table" "lugeasy_private_route_table" {
    id = "rtb-0edf0de8396b515f7"
}

data "aws_subnet" "lugeasy_public_subnet" {
    id = "subnet-03c09bf8b674e323e"
}

data "aws_subnet" "lugeasy_private_subnet" {
    id = "subnet-0b828c2949ebf9f57"
}