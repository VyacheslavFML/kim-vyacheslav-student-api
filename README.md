# Kim Vyacheslav Student API
Documentation status: initial version

Автор: Ким Вячеслав Александрович (Kim_Vyacheslav).
Gradle, Java 21, Spring Boot 3.5.7, PostgreSQL 16. API: `/api/students`.

## Запуск
```bash
cp .env.example .env
# Измените POSTGRES_PASSWORD в .env
./gradlew clean test bootJar
docker compose up -d --build
curl http://localhost:8082/
./scripts/api_smoke.sh http://localhost:8082
```
Тесты используют только H2 в профиле test. Основное приложение требует PostgreSQL.
Пароли и .env не коммитятся: они раскрывают доступ, а удаление файла не удаляет секрет из истории Git.
Для Linux задан режим скрипта 744 (rwxr--r--); 755 разрешал бы исполнение также группе и остальным.
`>` перезаписывает файл, `>>` дописывает в конец.

Ветка feature/kim-vyacheslav-crud: Kim_Vyacheslav добавил PUT /api/students/{id}. Полное обновление проверяет name, email и age; отсутствующий ID возвращает 404.
