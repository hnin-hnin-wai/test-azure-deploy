#FROM amazoncorretto:21

#ARG JAR_FILE=build/libs/*.jar

#WORKDIR /app

#COPY ${JAR_FILE} app.jar
#RUN chmod 775 app.jar

#Tomcat runs on 8090
#EXPOSE 8090
# which port number your application is listening on (for Documentation purpose )
#ENTRYPOINT ["java","-jar","app.jar "]

FROM amazoncorretto:21
WORKDIR /app
COPY build/libs/heartlink-1.0.1.jar /app
#Tomcat runs on 8090
EXPOSE 8090
# which port number your application is listening on (for Documentation purpose )
ENTRYPOINT ["java","-jar","heartlink-1.0.1.jar"]