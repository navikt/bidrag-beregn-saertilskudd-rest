FROM navikt/java:17
LABEL maintainer="Team Bidrag" \
      email="bidrag@nav.no"

COPY ./target/bidrag-beregn-saertilskudd-rest-*.jar app.jar

ENV SPRING_PROFILES_ACTIVE=nais

EXPOSE 8080
