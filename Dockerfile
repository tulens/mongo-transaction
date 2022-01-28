FROM maven:3-eclipse-temurin-17-alpine as builder

WORKDIR /opt/mongo-transaction

COPY . .

RUN addgroup -S myuser \
    && adduser -S myuser -G myuser \
    && chown -R myuser:myuser /opt/mongo-transaction

USER myuser

RUN mvn -B -fae clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine

COPY --from=builder /opt/mongo-transaction/target/*.jar /opt/mongo-transaction/api.jar
COPY entrypoint.sh /

ENV JAVA_OPTS="-XX:MaxRAMPercentage=90" \
 SPRING_PROFILES_ACTIVE="successful"

RUN addgroup -S myuser \
     && adduser -S myuser -G myuser \
     && chown -R myuser:myuser /opt/mongo-transaction

USER myuser

ENTRYPOINT ["/entrypoint.sh"]
