FROM openjdk:8
LABEL maintainer="zavod.com"
ARG JAR_FILE=target/shopping-cart-client.jar
ADD ${JAR_FILE} shoppingcart/shopping-cart-client.jar
ENTRYPOINT ["java","-jar","shoppingcart/shopping-cart-client.jar"]
EXPOSE 8080