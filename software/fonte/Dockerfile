########### imgjdk11 INICIO ###########

FROM scratch

ADD oraclelinux-8-slim-amd64-rootfs.tar.xz /

RUN microdnf install java-11-openjdk tar gzip tzdata-java*

RUN microdnf upgrade; \
    microdnf clean all

WORKDIR /tmp

USER 1001

########### imgjdk11 FIM ###########
########### imgjdk11springboot INICIO ###########

ENV TECNOLOGIA="spring-boot-jdk11"

USER root

RUN microdnf install unzip procps vi

ENV SCRIPTINICIO="SPRING_BOOT"

### START.SH ### 

ADD scriptinicializacao.sh /tmp/scriptinicializacao.sh
ADD start.sh /tmp/start.sh

RUN chmod 777 /tmp/start.sh

### HOSTNAME ### 

RUN echo "cat /proc/sys/kernel/hostname" > /usr/bin/hostname && \
    chmod 777 /usr/bin/hostname

### CREATE /home/jboss/source/ ### 

RUN mkdir -p /home/jboss/source/;chmod 777 /home/jboss/source/

########### imgjdk11springboot FIM ###########


########### imgjdk11maven INICIO ###########
ADD apache-maven-3.8.4-bin.tar.gz /tmp/mvnextracted

RUN mv /tmp/mvnextracted/apache* /opt/maven

ENV PATH="/opt/maven/bin:${PATH}"
########### imgjdk11maven FIM ###########
########### icl-api INICIO ###########

# ADD . /tmp/src
# RUN cd /tmp/src/; mvn install -DskipTests

ADD https://github.com/inmjava/icl-api/releases/download/1.0.0/icl-1.0.0.jar /usr/app/app.jar

WORKDIR /usr/app
ENV PORT 8080
EXPOSE $PORT
CMD ["bash", "-c", "while true; do echo 'Looping...'; sleep 1; done"]


