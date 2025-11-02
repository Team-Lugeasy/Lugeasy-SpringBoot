resource "aws_elasticache_subnet_group" "valkey" {
  name        = "valkey-subnet-group"
  description = "Private subnet(s) for Valkey"
  subnet_ids  = [
    data.aws_subnet.lugeasy_private_subnet.id
  ]
}

resource "aws_security_group" "lugeasy_valkey_sg" {
  name        = "lugeasy-valkey-sg"
  description = "Allow Valkey 6379"
  vpc_id      = data.aws_vpc.lugeasy_vpc.id
  tags = { Name = "lugeasy-valkey-sg" }
}

resource "aws_security_group_rule" "valkey_from_ec2_sg" {
  type                     = "ingress"
  from_port                = 6379
  to_port                  = 6379
  protocol                 = "tcp"
  security_group_id        = aws_security_group.lugeasy_valkey_sg.id
  source_security_group_id = aws_security_group.lugeasy_ec2_security_group.id
  description              = "Allow 6379 from lugeasy EC2 SG"
}


resource "aws_security_group_rule" "valkey_egress_all" {
  type              = "egress"
  from_port         = 0
  to_port           = 0
  protocol          = "-1"
  security_group_id = aws_security_group.lugeasy_valkey_sg.id
  cidr_blocks       = ["0.0.0.0/0"]
}

resource "aws_elasticache_replication_group" "lugeasy_valkey" {
  replication_group_id = "lugeasy-valkey"
  description          = "Valkey cache for lugeasy"   

  engine         = "valkey"
  engine_version = "7.2"
  node_type      = "cache.t3.micro"
  port           = 6379

  num_node_groups         = 1                        
  replicas_per_node_group = 0
  automatic_failover_enabled = false

  subnet_group_name  = aws_elasticache_subnet_group.valkey.name
  security_group_ids = [aws_security_group.lugeasy_valkey_sg.id]

  at_rest_encryption_enabled = true
  transit_encryption_enabled = true

  tags = { Name = "lugeasy-valkey" }
}
