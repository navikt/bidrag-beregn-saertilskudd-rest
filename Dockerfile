FROM navikt/java:15
LABEL maintainer="Team Bidrag" \
      email="bidrag@nav.no"

COPY ./target/bidrag-beregn-saertilskudd-rest-*.jar app.jar

EXPOSE 8080
