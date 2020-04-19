FROM openjdk:11

COPY target/*.jar /app/app.jar

RUN curl -O "http://download.newrelic.com/newrelic/java-agent/newrelic-agent/current/newrelic-java.zip"

RUN unzip newrelic-java.zip -d /app

CMD java -javaagent:/app/newrelic/newrelic.jar  $JAVA_OPTS -jar /app/app.jar
