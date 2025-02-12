


FROM  ayman909/customjdk:v1  AS BUILD_IMAGE

WORKDIR /app

COPY  target/*.jar app.jar

CMD ["java", "-jar", "app.jar"]