resource "aws_security_group" "lugeasy_ec2_security_group" {
  name        = "lugeasy-ec2-security-group"
  description = "lugeasy ec2 security group"
  vpc_id      =  data.aws_vpc.lugeasy_vpc.id
}

# ingress rule
resource "aws_vpc_security_group_ingress_rule" "lugeasy_ec2_22_ingress_rule" {
  security_group_id = aws_security_group.lugeasy_ec2_security_group.id
  cidr_ipv4         = "0.0.0.0/0"
  from_port         = 22
  ip_protocol       = "tcp"
  to_port           = 22
}

resource "aws_vpc_security_group_ingress_rule" "lugeasy_ec2_80_ingress_rule" {
  security_group_id = aws_security_group.lugeasy_ec2_security_group.id
  cidr_ipv4         =  "0.0.0.0/0"
  from_port         = 80
  ip_protocol       = "tcp"
  to_port           = 80
}

resource "aws_vpc_security_group_ingress_rule" "lugeasy_ec2_443_ingress_rule" {
  security_group_id = aws_security_group.lugeasy_ec2_security_group.id
  cidr_ipv4         = "0.0.0.0/0"
  from_port         = 443
  ip_protocol       = "tcp"
  to_port           = 443
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