FROM openjdk:8
LABEL maintainer="zavod.com"
ARG JAR_FILE=target/shopping-cart-service.jar
ADD ${JAR_FILE} shoppingcart/shopping-cart-service.jar
ENTRYPOINT ["java","-jar","shoppingcart/shopping-cart-service.jar"]
EXPOSE 9100