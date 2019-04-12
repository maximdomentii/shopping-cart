FROM openjdk:8
LABEL maintainer="zavod.com"
ARG JAR_FILE=target/auth-service.jar
ADD ${JAR_FILE} shoppingcart/auth-service.jar
ENTRYPOINT ["java","-jar","shoppingcart/auth-service.jar"]
EXPOSE 9100