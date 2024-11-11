FROM mysql:8.0.24

ENV MYSQL_DATABASE=softwareProject
ENV MYSQL_ALLOW_EMPTY_PASSWORD=false

COPY ./conf/* /etc/mysql/conf.d
COPY ./docker-entrypoint-initdb/* /docker-entrypoint-initdb.d/  