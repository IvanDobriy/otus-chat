## Учебный чат

Данный проект состоит из 2х sub-проектов:
- client
- server

Перед запуском сервера необходимо:
- развернуть базу данных:
```
docker run -dp 54321:5432 -e POSTGRES_USER=user -e POSTGRES_DB=chat -e POSTGRES_PASSWORD=123 postgres:14.17
```
- выполнить скрипт с миграциями (см. ./server/migrations.sql)

