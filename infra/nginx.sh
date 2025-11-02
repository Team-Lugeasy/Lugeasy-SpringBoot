#!/bin/bash
set -euxo pipefail

dnf -y update
dnf -y install nginx

cat >/usr/share/nginx/html/index.html <<'HTML'
<!doctype html>
<html>
<head><meta charset="utf-8"><title>lugeasy-nginx</title></head>
<body style="font-family:sans-serif">
  <h1>NGINX is up 🚀</h1>
  <p>Instance: $(hostname)</p>
  <p>Time: $(date)</p>
</body>
</html>
HTML

chown -R nginx:nginx /usr/share/nginx/html || true
chmod -R a+rX /usr/share/nginx/html

rm -f /etc/nginx/conf.d/default.conf || true
cat >/etc/nginx/conf.d/lugeasy.conf <<'NGINX'
server {
  listen 80 default_server;
  server_name _;

  root /usr/share/nginx/html;
  index index.html;

  # 헬스체크 엔드포인트
  location = /health {
    access_log off;
    add_header Content-Type text/plain;
    return 200 'ok';
  }
}
NGINX

# 설정 적용
nginx -t
systemctl enable nginx
systemctl restart nginx
