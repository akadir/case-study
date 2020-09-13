FROM openjdk:8-jre-alpine3.9
LABEL repository="https://github.com/akadir/case-study" maintainer="https://github.com/akadir"
WORKDIR /case-study
COPY ["./target", "./"]
ENTRYPOINT ["java", "-jar", "/case-study/case-study.jar"]