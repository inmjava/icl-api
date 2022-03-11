FROM dsoprd.copel.com:8082/imgjdk11springboot/imgjdk11springboot:latest

COPY target/*.jar /usr/app/app.jar

WORKDIR /usr/app
ENV PORT 8080
EXPOSE $PORT
#CMD [ "sh", "-c", "mvn -Dserver.port=${PORT} spring-boot:run" ]
#CMD [ "sh", "-c", "java -Dserver.port=${PORT} -jar /usr/app/app.jar" ]
CMD [ "/tmp/start.sh" ]
#CMD while sleep 2; do echo 'Edit Dockerfile!'; done

