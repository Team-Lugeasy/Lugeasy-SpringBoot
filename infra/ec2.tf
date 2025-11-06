data "aws_ami" "al2023" {
  most_recent = true
  owners      = ["amazon"]

  filter {
    name   = "name"
    values = ["al2023-ami-*-kernel-6.1-*"]
  }

  filter {
    name   = "architecture"
    values = ["x86_64"]
  }
}

resource "aws_instance" "lugeasy_ec2" {
  ami                         = data.aws_ami.al2023.id
  instance_type               = "t3.micro"
  subnet_id                   = data.aws_subnet.lugeasy_public_subnet_a.id
  vpc_security_group_ids      = [aws_security_group.lugeasy_ec2_security_group.id]
  associate_public_ip_address = true

  user_data                  = file("${path.module}/nginx.sh")
  user_data_replace_on_change = true

  key_name = data.aws_key_pair.lugeasy.key_name

  tags = {
    Name = "lugeasy-ec2"
  }
}

resource "aws_eip" "lugeasy_ec2_eip" {
  domain = "vpc"               
  tags = {
    Name = "lugeasy-ec2-eip"
  }
}

resource "aws_eip_association" "lugeasy_ec2_assoc" {
  allocation_id = aws_eip.lugeasy_ec2_eip.id
  instance_id   = aws_instance.lugeasy_ec2.id
}


resource "aws_security_group" "lugeasy_ec2_security_group" {
  name        = "lugeasy-ec2-security-group"
  description = "lugeasy ec2 security group"
  vpc_id      =  data.aws_vpc.lugeasy_vpc.id

  tags = {
    Name = "lugeasy-ec2-sg"
  }
}

# ingress rule
resource "aws_vpc_security_group_ingress_rule" "lugeasy_ec2_22_ingress_rule" {
  security_group_id = aws_security_group.lugeasy_ec2_security_group.id
  cidr_ipv4         = "0.0.0.0/0"
  from_port         = 22
  ip_protocol       = "tcp"
  to_port           = 22
}

resource "aws_vpc_security_group_ingress_rule" "ec2_allow_8080_from_alb" {
  security_group_id            = aws_security_group.lugeasy_ec2_security_group.id
  ip_protocol                  = "tcp"
  from_port                    = 8080
  to_port                      = 8080
  referenced_security_group_id = aws_security_group.lugeasy_alb_sg.id
  description                  = "Allow HTTP 8080 only from ALB SG"
}

# egress rule
resource "aws_security_group_rule" "lugeasy_ec2_any_open_egress_rule" {
  type              = "egress"
  security_group_id = aws_security_group.lugeasy_ec2_security_group.id
  from_port         = 0
  to_port           = 0
  protocol          = "-1"
  cidr_blocks       = ["0.0.0.0/0"]
}