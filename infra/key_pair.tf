resource "aws_key_pair" "lugeasy_key_pair" {
  key_name   = "lugeasy-ec2-key-pair"
  public_key = var.lugeasy_ssh_key
}