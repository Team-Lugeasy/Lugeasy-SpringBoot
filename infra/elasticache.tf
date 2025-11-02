resource "aws_elasticache_subnet_group" "redis" {
  name        = "redis-subnet-group"
  description = "Private subnet for Redis"
  subnet_ids  = [data.aws_subnet.lugeasy_private_subnet.id]
}

resource "aws_security_group" "lugeasy_redis_sg" {
  name        = "lugeasy-redis-sg"
  description = "Allow Redis 6379"
  vpc_id      = data.aws_vpc.lugeasy_vpc.id
}

resource "aws_security_group_rule" "redis_from_app" {
  type                     = "ingress"
  from_port                = 6379
  to_port                  = 6379
  protocol                 = "tcp"
  security_group_id        = aws_security_group.lugeasy_redis_sg.id
  source_security_group_id = aws_security_group.lugeasy_ec2_security_group.id
  description              = "Allow 6379 from ec2 SG"
}

resource "aws_security_group_rule" "redis_from_vpc" {
  type              = "ingress"
  from_port         = 6379
  to_port           = 6379
  protocol          = "tcp"
  security_group_id = aws_security_group.lugeasy_redis_sg.id
  cidr_blocks       = [data.aws_vpc.lugeasy_vpc.cidr_block]
  description       = "TEMP: Allow 6379 from VPC CIDR"
}

resource "aws_security_group_rule" "redis_egress_all" {
  type              = "egress"
  from_port         = 0
  to_port           = 0
  protocol          = "-1"
  security_group_id = aws_security_group.lugeasy_redis_sg.id
  cidr_blocks       = ["0.0.0.0/0"]
}

resource "aws_elasticache_cluster" "lugeasy_redis" {
  cluster_id        = "lugeasy-redis"
  engine            = "redis"
  node_type         = "cache.t3.micro"
  num_cache_nodes   = 1
  port              = 6379
}