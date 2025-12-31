FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

COPY target/rental-store.jar app.jar

EXPOSE 8080

# Install curl for healthcheck and set JVM options as environment variable
RUN apk add --no-cache curl

ENV JAVA_OPTS="-Duser.language=en -Duser.country=US"

# Use shell form to expand environment variables
ENTRYPOINT sh -c "java $JAVA_OPTS -jar app.jar"

HEALTHCHECK --interval=5s --timeout=3s --start-period=10s --retries=5 \
  CMD curl -f http://localhost:8080/health || exit 1
