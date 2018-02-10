FROM adorsys/openjdk-jre-base:8-minideb
MAINTAINER https://git.adorsys.de/adorsys/tppserver
ENV JAVA_OPTS -Xmx1024m
WORKDIR /opt/tppserver
RUN chmod -R 777 /opt/tppserver
COPY /gmo/tppserver/target/tppserver.jar /opt/tppserver/tppserver.jar
EXPOSE 8080
CMD exec $JAVA_HOME/bin/java $JAVA_OPTS -jar /opt/tppserver/tppserver.jar
