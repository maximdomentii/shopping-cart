FROM openjdk:8
LABEL maintainer="zavod.com"
ARG JAR_FILE=target/zuul-server.jar
ADD ${JAR_FILE} shoppingcart/zuul-server.jar
ENTRYPOINT ["java","-jar","shoppingcart/zuul-server.jar"]
EXPOSE 8762