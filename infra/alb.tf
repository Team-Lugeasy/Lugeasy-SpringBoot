variable "alb_certificate_arn" {
  type        = string
  description = "ALB HTTPS(443)에 사용할 ACM 인증서 ARN (ap-northeast-3)"
  default     = "arn:aws:acm:ap-northeast-3:636335650359:certificate/794ec315-3370-4e7c-889a-ea9b5ce741c9"
}

resource "aws_security_group" "lugeasy_alb_sg" {
  name        = "lugeasy-alb-sg"
  description = "ALB SG for HTTP/HTTPS"
  vpc_id      = data.aws_vpc.lugeasy_vpc.id
  tags        = { Name = "lugeasy-alb-sg" }
}

resource "aws_vpc_security_group_ingress_rule" "alb_http_80" {
  security_group_id = aws_security_group.lugeasy_alb_sg.id
  ip_protocol       = "tcp"
  from_port         = 80
  to_port           = 80
  cidr_ipv4         = "0.0.0.0/0"
  description       = "Allow HTTP 80 from anywhere"
}

resource "aws_vpc_security_group_ingress_rule" "alb_https_443" {
  security_group_id = aws_security_group.lugeasy_alb_sg.id
  ip_protocol       = "tcp"
  from_port         = 443
  to_port           = 443
  cidr_ipv4         = "0.0.0.0/0"
  description       = "Allow HTTPS 443 from anywhere"
}

resource "aws_vpc_security_group_egress_rule" "alb_all_egress" {
  security_group_id = aws_security_group.lugeasy_alb_sg.id
  ip_protocol       = "-1"
  cidr_ipv4         = "0.0.0.0/0"
  description       = "Allow all egress"
}

resource "aws_lb" "lugeasy_alb" {
  name               = "lugeasy-alb"
  load_balancer_type = "application"
  security_groups    = [aws_security_group.lugeasy_alb_sg.id]
  subnets = [
    data.aws_subnet.lugeasy_public_subnet_a.id,
    data.aws_subnet.lugeasy_public_subnet_c.id,
  ]
  idle_timeout = 60
  tags        = { Name = "lugeasy-alb" }
}

resource "aws_lb_target_group" "lugeasy_tg" {
  name        = "lugeasy-tg"
  port        = 8080               
  protocol    = "HTTP"
  target_type = "instance"
  vpc_id      = data.aws_vpc.lugeasy_vpc.id

  health_check {
    enabled             = true
    protocol            = "HTTP"
    path                = "/"     
    matcher             = "200-399"
    interval            = 30
    timeout             = 5
    healthy_threshold   = 2
    unhealthy_threshold = 2
  }

  tags = { Name = "lugeasy-tg" }
}


resource "aws_lb_target_group_attachment" "lugeasy_tg_attach_ec2" {
  target_group_arn = aws_lb_target_group.lugeasy_tg.arn
  target_id        = aws_instance.lugeasy_ec2.id
  port             = 8080
}

resource "aws_lb_listener" "http_80_redirect_to_https" {
  load_balancer_arn = aws_lb.lugeasy_alb.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type = "redirect"
    redirect {
      protocol    = "HTTPS"
      port        = "443"
      status_code = "HTTP_301"
    }
  }
}

resource "aws_lb_listener" "https_443" {
  load_balancer_arn = aws_lb.lugeasy_alb.arn
  port              = 443
  protocol          = "HTTPS"
  ssl_policy        = "ELBSecurityPolicy-TLS13-1-2-2021-06"
  certificate_arn   = var.alb_certificate_arn

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.lugeasy_tg.arn
  }
}

# resource "aws_vpc_security_group_ingress_rule" "ec2_allow_80_from_alb" {
#   security_group_id            = aws_security_group.lugeasy_ec2_security_group.id
#   ip_protocol                  = "tcp"
#   from_port                    = 80
#   to_port                      = 80
#   referenced_security_group_id = aws_security_group.lugeasy_alb_sg.id
#   description                  = "Allow HTTP 80 only from ALB SG"
# }

output "alb_dns_name" {
  value = aws_lb.lugeasy_alb.dns_name
}

output "alb_arn" {
  value = aws_lb.lugeasy_alb.arn
}

output "alb_https_listener_arn" {
  value = aws_lb_listener.https_443.arn
}
