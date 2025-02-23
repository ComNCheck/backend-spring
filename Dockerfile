FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} prod.jar

COPY src/main/resources/gcp/ancient-pipe-447417-i4-755ce59fbf03.json /app/ancient-pipe-447417-i4-755ce59fbf03.json
ENV GOOGLE_APPLICATION_CREDENTIALS="app/ancient-pipe-447417-i4-755ce59fbf03.json"

COPY src/main/resources/firebase/comncheck-firebase-adminsdk-fbsvc-1255c6f2ed.json /app/comncheck-firebase-adminsdk-fbsvc-1255c6f2ed.json

EXPOSE 8080
CMD ["java", "-Dtest.customName=${CUSTOM_NAME}", "-jar", "prod.jar"]

