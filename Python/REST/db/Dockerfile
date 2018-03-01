FROM mysql

ENV MYSQL_ROOT_PASSWORD passme
ENV MYSQL_DATABASE weather
ENV MYSQL_ROOT_HOST=%
COPY create.sql /docker-entrypoint-initdb.d
