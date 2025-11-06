#!/bin/bash
set -euxo pipefail
dnf -y update
sudo -y install ec2-instance-connect
# dnf -y install nginx
dnf -y install docker

# chown -R nginx:nginx /usr/share/nginx/html || true
# chmod -R a+rX /usr/share/nginx/html

# rm -f /etc/nginx/conf.d/default.conf || true
# cat >/etc/nginx/conf.d/lugeasy.conf <<'NGINX'
# server {
#   listen 80 default_server;
#   server_name _;

#   root /usr/share/nginx/html;
#   index index.html;

#   # 헬스체크 엔드포인트
#   location = /health {
#     access_log off;
#     add_header Content-Type text/plain;
#     return 200 'ok';
#   }
# }
# NGINX

# nginx -t
# systemctl enable nginx
# systemctl restart nginx

systemctl enable --now docker

if id -u ec2-user >/dev/null 2>&1; then
  usermod -aG docker ec2-user || true
fi
if [ -n "${SUDO_USER-}" ] && [ "$SUDO_USER" != "root" ]; then
  usermod -aG docker "$SUDO_USER" || true
fi