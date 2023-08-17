FROM openjdk:17-jdk-alpine3.14

ARG JAR_FILE

RUN mkdir -p /apps
COPY ./target/${JAR_FILE} /apps/app.jar
COPY ./entrypoint.sh /apps/entrypoint.sh
RUN chmod +x /apps/entrypoint.sh

EXPOSE 8080
CMD ["/apps/entrypoint.sh"]
