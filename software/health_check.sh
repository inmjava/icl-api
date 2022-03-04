#!/bin/bash
exit 0
URL_TESTE=http://$(hostname):8080/gap/paginas/inicio.jsf
if [ 200 -eq $(curl --write-out %{http_code} --silent --output /dev/null ${URL_TESTE}) ]; then
        exit 0
fi
SEGUNDOS_UPTIME_JAVA=$(ps h -eo etime,cmd | grep java | grep -v grep | awk '{print $1}' | tr '-' ':' | awk -F: '{ total=0; m=1; } { for (i=0; i < NF; i++) {total += $(NF-i)*m; m *= i >= 2 ? 24 : 60 }} {print total}')
if [ $SEGUNDOS_UPTIME_JAVA -gt 600 ]; then
        exit 1
fi