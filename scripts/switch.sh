#!/usr/bin/env bash
if [ $(id -u) -ne 0 ]; then exec sudo bash "$0" "$@"; exit; fi
# switch.sh

# Crawl current connected port of WAS
CURRENT_PORT=$(cat /home/ubuntu/service.inc  | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0

echo "> Nginx currently proxies to ${CURRENT_PORT}."

# Toggle port number
if [ ${CURRENT_PORT} -eq 8081 ]; then
    TARGET_PORT=8082
elif [ ${CURRENT_PORT} -eq 8082 ]; then
    TARGET_PORT=8081
else
    echo "> No WAS is connected to nginx"
    exit 1
fi

# Change proxying port into target port
echo "set \$service_url https://sparta-kdh.kro.kr:${TARGET_PORT};" | tee /home/ubuntu/service.inc

echo "> Now Nginx proxies to ${TARGET_PORT}."

# Reload nginx
sudo service nginx reload

echo "> Nginx reloaded."