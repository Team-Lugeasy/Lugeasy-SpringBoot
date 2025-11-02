#!/bin/bash
set -euxo pipefail

# 패키지 업데이트 & NGINX 설치
dnf -y update
dnf -y install nginx

# 기본 문서 생성
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

# 권한 정리 (403 방지)
chown -R nginx:nginx /usr/share/nginx/html || true
chmod -R a+rX /usr/share/nginx/html

# 기본 서버블록: 어떤 Host 헤더로 와도 200 응답
# /health 엔드포인트 추가(헬스체크 용)
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
