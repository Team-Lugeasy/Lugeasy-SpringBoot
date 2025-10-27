resource "aws_vpc" "lugeasy_vpc" {}

resource "aws_internet_gateway" "lugeasy_internet_gateway" {}

resource "aws_route_table" "lugeasy_public_route_table" {}

resource "aws_route_table" "lugeasy_private_route_table" {}

resource "aws_subnet" "lugeasy_public_subnet" {}

resource "aws_subnet" "lugeasy_private_subnet" {}