FROM maven:3.6.3-jdk-11 as build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package


FROM openjdk:11.0.15-jre-slim
ENV APP_USER java
RUN adduser -D -g '' $APP_USER

COPY --from=build /usr/src/app/target/payment-services.jar app.jar
RUN sh -c 'touch /app.jar'
RUN chown -R $APP_USER /app.jar

USER $APP_USER
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
