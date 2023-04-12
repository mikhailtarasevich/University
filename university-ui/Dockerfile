FROM tomcat:9

COPY ./src/main/webapp/META-INF/context-docker.xml /usr/local/tomcat/conf/context.xml
COPY ./target/university-ui-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]