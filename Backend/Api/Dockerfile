FROM bellsoft/liberica-openjdk-alpine:17 AS build
WORKDIR /workspace/app
COPY mvnw .
COPY .mvn .mvn 
COPY pom.xml .
COPY src src
RUN ./mvnw install -DskipTests

FROM bellsoft/liberica-openjdk-alpine:17
WORKDIR /app
ENV SECRET=kñlkñlkñlkñlkñlkñkñlkkñlkñlkñjlñjlkj
ENV SENDGRID_API_KEY=jlkjlkjlkjlkjlkjklñj
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://pg:5432/bioclick
ENV SPRING_DATASOURCE_USERNAME=Bioclick
ENV SPRING_DATASOURCE_PASSWORD=12345678


COPY --from=build /workspace/app/target/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]