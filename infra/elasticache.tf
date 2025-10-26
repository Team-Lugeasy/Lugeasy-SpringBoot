locals {
  vpc_id = "vpc-0f4129e666e1fe560"
}

data "aws_vpc" "this" {
  id = local.vpc_id
}

resource "aws_elasticache_subnet_group" "redis" {
  name        = "redis-subnet-group"
  description = "Subnets for Redis"
  subnet_ids  = ["subnet-077252355d1d43013"]
}

resource "aws_security_group" "lugeasy_redis_sg" {
  name        = "lugeasy-redis-sg"
  description = "Allow Redis 6379"
  vpc_id      = data.aws_vpc.this.id
  tags        = { Name = "sg-redis" }
}

resource "aws_security_group_rule" "redis_from_app" {
  count                    = 1
  type                     = "ingress"
  from_port                = 6379
  to_port                  = 6379
  protocol                 = "tcp"
  security_group_id        = aws_security_group.lugeasy_redis_sg.id
  source_security_group_id = "sg-0eccd8531caa34401"
  description              = "Allow 6379 from app SG"
}

resource "aws_security_group_rule" "redis_from_vpc" {
  count             = 1
  type              = "ingress"
  from_port         = 6379
  to_port           = 6379
  protocol          = "tcp"
  security_group_id = aws_security_group.lugeasy_redis_sg.id
  cidr_blocks       = [data.aws_vpc.this.cidr_block]
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
  apply_immediately = true

  subnet_group_name  = aws_elasticache_subnet_group.redis.name
  security_group_ids = [aws_security_group.lugeasy_redis_sg.id]
}

output "redis_endpoint" {
  value = aws_elasticache_cluster.lugeasy_redis.cache_nodes[0].address
}
