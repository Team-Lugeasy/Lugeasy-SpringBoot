data "aws_ami" "al2023" {
  most_recent = true
  owners      = ["137112412989"]

  filter {
    name   = "name"
    values = ["al2023-ami-*-x86_64"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }
}

resource "aws_instance" "lugeasy_ec2" {
  ami                    = data.aws_ami.al2023.id
  instance_type          = "t2.micro"
  subnet_id              = "subnet-08970d55b3403bf2b"
  vpc_security_group_ids = [aws_security_group.lugeasy_security_group.id]
  key_name               = aws_key_pair.lugeasy_key_pair.name

  associate_public_ip_address = true

  tags = {
    Name = "lugeasy"
  }
}