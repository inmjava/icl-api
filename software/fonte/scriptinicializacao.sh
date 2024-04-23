#!/bin/bash

echo "=============TECNOLOGIA $SCRIPTINICIO============="

if [ "$SCRIPTINICIO" == "EAP_STD" ]
then
   /opt/eap/bin/standalone.sh -c standalone-cicd.xml -bmanagement 0.0.0.0 -b 0.0.0.0 ${JAVA_PROXY_OPTIONS} ${JBOSS_HA_ARGS} ${JBOSS_MESSAGING_ARGS}
fi

if [ "$SCRIPTINICIO" == "SPRING_BOOT" ]
then
   ls -la /usr/app/
   java  ${JAVA_PROXY_OPTIONS} ${JAVA_OPTS}  -Dserver.port=8080 -jar /usr/app/app.jar
fi


