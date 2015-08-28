1) Web-server Tomcat.
В файл context.xml необходимо добавить строчку:
    <Resource name="jdbc/Backend" auth="Container" type="javax.sql.DataSource"
            username="rafael.chumarin"
            password="p@ssw0rd"
            driverClassName="org.postgresql.Driver"
            url="jdbc:postgresql://localhost:5432/rafael.chumarin"
            maxTotal="20"
            maxIdle="10"/>

2) Дамп БД «rafael.chumarin» в СУБД Postgres.
— Создание БД
CREATE DATABASE "rafael.chumarin" WITH ENCODING 'UTF8' LC_COLLATE='ru_RU.UTF-8' LC_CTYPE='ru_RU.UTF-8' TEMPLATE=template0;

— Создание пользователя
CREATE USER  "rafael.chumarin" PASSWORD 'p@ssw0rd';

— Создание таблиц (владельцем таблиц должен быть rafael.chumarin)
ALTER DATABASE "rafael.chumarin" OWNER TO "rafael.chumarin";

CREATE TABLE IF NOT EXISTS users (clientid character varying(35) NOT NULL PRIMARY KEY, fname character varying(30));

CREATE TABLE IF NOT EXISTS messages (id serial NOT NULL PRIMARY KEY, message character varying(120), clientid character varying(35), FOREIGN KEY (clientid) REFERENCES users(clientid));

ALTER TABLE messages owner to "rafael.chumarin";

ALTER TABLE users owner to "rafael.chumarin";

