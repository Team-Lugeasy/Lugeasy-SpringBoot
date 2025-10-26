resource "aws_elasticache_cluster" "lugeasy_elasticache" {
  cluster_id        = "lugeasy-elasticache"
  engine            = "valkey"
  node_type         = "cache.t3.micro"
  num_cache_nodes   = 1
  port              = 6379
  apply_immediately = true
}