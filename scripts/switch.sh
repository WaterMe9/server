#!/bin/bash

IP_ADDRESS=$(cat /home/market/ip_address.inc | cut -c17-26)
CURRENT_PORT=$(cat /home/market/service_url.inc  | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0

echo "> Nginx currently proxies to ${CURRENT_PORT}."

if [ ${CURRENT_PORT} -eq 8081 ]; then
    TARGET_PORT=8082
elif [ ${CURRENT_PORT} -eq 8082 ]; then
    TARGET_PORT=8081
else
    echo "> No WAS is connected to nginx"
    exit 1
fi

echo "set \$service_url http://${IP_ADDRESS}:${TARGET_PORT};" | tee /home/market/service_url.inc

echo "> Now Nginx proxies to ${TARGET_PORT}."

sudo service nginx reload

echo "> Nginx reloaded."