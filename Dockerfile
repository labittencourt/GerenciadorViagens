FROM maven:3.6.3-jdk-8

WORKDIR "/usr/src/app"

COPY "$pwd" "/usr/src/app"

RUN mvn clean install -Dmaven.test.skip=true

EXPOSE 8089

CMD [ "mvn", "spring-boot:run" ]