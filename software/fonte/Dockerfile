FROM dsoprd.copel.com:8082/imgjdk11springboot/imgjdk11springboot:latest

COPY target/*.jar /usr/app/app.jar

WORKDIR /usr/app
ENV PORT 8080
EXPOSE $PORT
CMD [ "/tmp/start.sh" ]


