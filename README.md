# Kim Vyacheslav Student API
Documentation status: Kim Vyacheslav main instructions and feature documentation

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

## API
| Method | Path | Result |
|---|---|---|
| GET | / | Автор и приложение |
| POST | /api/students | 201 и Location |
| GET | /api/students | 200 список |
| GET | /api/students/{id} | 200 или 404 |
| PUT | /api/students/{id} | 200 или 404 |
| DELETE | /api/students/{id} | 204 или 404 |

Пример JSON: `{"name":"Ким Вячеслав Александрович","email":"kim@example.com","age":21}`.
Имя обязательно, email должен быть корректным и уникальным, age от 1 до 120. Ошибки: 400/409.

## Проверки и доказательства
- `./gradlew test`: интеграционные HTTP-тесты на случайном порту и H2. Это не доказательство PostgreSQL.
- `./scripts/linux_evidence.sh`: системный отчет, права 744, backup и анализ.
- `./scripts/container_evidence.sh`: Compose, CRUD, управление, перезапуск с сохранением записей, rebuild 1.0.1.
- Фактические результаты и ограничения: `docs/REPORT_RU.md`.
- Jenkins: `docs/JENKINS_SETUP_RU.md`. Защита: `docs/DEFENSE_RU.md`.

## Воспроизведение
Требуются JDK 21, Docker Engine с Compose v2, Bash, curl и Python 3 для smoke script.
```bash
./gradlew clean test bootJar
java -jar build/libs/student-api.jar # нужны DB_URL, DB_USERNAME, DB_PASSWORD
# Или PostgreSQL + app:
docker compose up -d --build
./scripts/api_smoke.sh http://localhost:8082
docker compose down # данные остаются
```
Порт приложения опубликован только на loopback; PostgreSQL наружу не публикуется.
Для внешнего доступа используйте защищенный reverse proxy и аутентификацию.
`ddl-auto=update` подходит для учебной работы; для production нужны versioned migrations.
