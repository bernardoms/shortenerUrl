FROM openjdk:11

COPY target/*.jar /app/app.jar

CMD java $JAVA_OPTS -jar /app/app.jar
