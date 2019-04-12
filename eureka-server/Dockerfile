FROM openjdk:8
LABEL maintainer="zavod.com"
ARG JAR_FILE=target/eureka-server.jar
ADD ${JAR_FILE} shoppingcart/eureka-server.jar
ENTRYPOINT ["java","-jar","shoppingcart/eureka-server.jar"]
EXPOSE 8761