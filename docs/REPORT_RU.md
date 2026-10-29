# Отчет DevOps midterm

Автор: Ким Вячеслав Александрович. Персонализация: Kim_Vyacheslav.
Дата выполнения: 30 октября 2026, Asia/Almaty (UTC+5).
Источник требований: предоставленный DevOps midterm task.docx.
Новый репозиторий: https://github.com/VyacheslavFML/kim-vyacheslav-student-api (private).

## Итог

Реализовано настоящее Gradle/Spring Boot CRUD Student API, PostgreSQL и Jenkins CI.
Работа не использует старое Python-приложение из предыдущей лабораторной.
Все логи в evidence получены реальными командами. H2-тесты отдельно от настоящей
PostgreSQL-проверки. Неудачные запуски сохранены без подмены успешными.

## Соответствие заданию

| Пункт | Реализация | Доказательство в архиве |
|---|---|---|
| 1.1 | Структура создана terminal mkdir -p, скрипт и пустой log перед заполнением | scripts/linux_evidence.sh, evidence/linux.txt |
| 1.2 | ФИО, дата, username, OS/kernel, CPU, RAM, usage/free disk | scripts/Kim_Vyacheslav_system_report.sh; logs/Kim_Vyacheslav_system_report.log |
| 1.3 | chmod 744, backup README с именем, cmp и ls -l | backups/Kim_Vyacheslav_README.md; evidence/script-permissions.txt; backup-verification.txt |
| 1.4 | Процессы, Java, tail -5 и grep фамилии | evidence/linux.txt; java-process.txt; logs/Kim_Vyacheslav_log_analysis.txt |
| 2.1 | main, персональные user.name/email, .gitignore, initial commit | .git; history.bundle; GitHub |
| 2.2 | feature/kim-vyacheslav-crud, PUT, README, два содержательных commits и merge | git log --graph --all; коммиты a774919 и 659019a |
| 2.3 | Изменение одной строки на main и documentation, настоящий конфликт и ручное объединение текста | evidence/git-conflict.txt; merge resolution e10080b |
| 2.4 | GitHub repo, push main и персональных branches, annotated tag v1.0-kim-vyacheslav | итоговые Git metadata и bundle; remote GitHub |
| 3.1 | Multi-stage JDK 21 / Gradle / JRE 21, JAR, порт, personal image/tag | Dockerfile; evidence/docker-compose-build.txt; containers.txt |
| 3.2 | app + PostgreSQL, env, named volume, network, pg_isready, JDBC db | docker-compose.yml; evidence/final-containers.txt |
| 3.3 | ps, logs, inspect, exec, stop/start, rebuild 1.0.1, сохранение записи после down/up | scripts/container_evidence.sh; evidence/containers.txt; persistence-after.json |
| 3.4 | curl create/list/get/update/delete/missing + validation | scripts/api_smoke.sh; evidence/containers.txt |
| 4.1 | Jenkins job Kim_Vyacheslav_Student_API, private GitHub checkout, JDK 21, Docker agent, Credentials | evidence/jenkins-agent.txt; jenkins-build-3.txt |
| 4.2 | Checkout / Build / Test / Docker Build / Verify, имя в console | Jenkinsfile; jenkins-build-3.stages.json |
| 4.3 | Успех, контролируемый сбой в отдельной ветке, исправление | Jenkins #3 SUCCESS, #4 FAILURE, #5 SUCCESS; console и JSON |
| 4.4 | pollSCM H/2, новый commit и автоматический запуск | evidence/polling-trigger-push.txt; итоговые Jenkins JSON с SCMTriggerCause и polling log |

## Реальная проверка Linux

Сначала системный скрипт запущен на macOS. Ограничения sandbox запрещали часть sysctl/ps;
эти попытки сохранены в system-host.txt и system-report-macos.txt. Затем, после запуска
Colima пользователем, тот же скрипт выполнен в настоящем Ubuntu 24.04 container.
Финальный персональный log содержит Linux-данные. Java работает в отдельном app container;
docker top подтвердил java, вывод сохранен в java-process.txt.

Права скрипта -rwxr--r-- соответствуют 744: owner rwx, group r--, others r--.
Git хранит только executable-бит; linux_evidence.sh восстанавливает 744 после clone.
`>` создает или перезаписывает файл; `>>` дописывает. Поиск фамилии перенаправлен через `>`.
Backup совпал с README побайтно по cmp, сохранены файл и результат проверки.

## API и тесты

Java 21, Spring Boot 3.5.7, Gradle 8.14.3 с официальным Wrapper и SHA256 дистрибутива.
Student: id (identity), name, уникальный email, age 1..120. Вход проверяет Bean Validation.
POST возвращает 201 и Location; GET и PUT - 200; DELETE - 204. Отсутствующий ID - 404,
неверный ввод - 400, повторный email - 409. `/` отображает ФИО автора.

9 интеграционных HTTP-тестов на случайном порту проверяют CRUD, сохранение обновления,
валидацию, missing ID, duplicate email и персонализацию. H2 включен только в test profile.
Обычный JAR использует PostgreSQL, без скрытого fallback на H2.
Сохранены локальные success/failure JUnit XML и HTML-report. Контрольный параметр
`-PcontrolledFailure=true` намеренно падает на assertion; это не случайная ошибка приложения.

Для настоящей PostgreSQL curl получил 201/200/200/200/204/404/400. Создана отдельная запись
Persistence. После compose down/up без удаления volume ее JSON полностью совпал с исходным.
Дополнительно psql прочитал строку непосредственно из БД (postgresql-rows.txt).

## Docker

Build-stage: eclipse-temurin:21-jdk-jammy, сборка bootJar через Gradle Wrapper.
Runtime: eclipse-temurin:21-jre-jammy, пользователь student, JAR /app/student-api.jar.
Compose project, app image и tag персонализированы. JDBC использует db, а не localhost.
PostgreSQL 16-alpine, pg_isready, named volume, выделенная сеть. Наружу опубликован только
127.0.0.1:8082 приложения. База не публикует host port.

В containers.txt сохранены image ID, size, created, выборочный inspect без environment,
logs, java -version, stop/start, down/up и rebuild. Версия 1.0.1 передается в Gradle как
appVersion и меняет JAR metadata. Исходный graded image v1.0-kim-vyacheslav сохранен.
Полный docker inspect намеренно не включен: environment содержит пароль базы.

## Jenkins

Локальный учебный контроллер http://localhost:8080/, job Kim_Vyacheslav_Student_API.
Агент имеет label java21-docker, Java 21, Git, Docker CLI и доступ к Docker daemon.
Приватный GitHub checkout использует credential ID kim-vyacheslav-github; сохранение
существующего токена в Jenkins Credentials явно разрешено владельцем. Секрет не выводится
и не входит в архив. Использован существующий учебный controller, а не production host.

Первый запуск выявил синтаксическую проблему компактных блоков Declarative Pipeline.
После форматирования второй запуск дошел до Test, но отсутствовал необязательный junit
plugin. Итоговый Jenkinsfile сохраняет JUnit XML через archiveArtifacts, а тестовый sh
по-прежнему возвращает ненулевой код при провале. Ошибка не замаскирована.

#3 - SUCCESS по всем пяти стадиям.
#4 - FAILURE на feature/kim-vyacheslav-ci-failure, commit 6a64fa3: один из девяти тестов
упал из-за -PcontrolledFailure=true; Docker Build и Verify пропущены.
#5 - SUCCESS после исправления отдельным commit 2048dd4.
#6 - SUCCESS после возврата job на main, базовая проверка перед polling.
Следующий автоматический запуск и его cause находятся в финальной verification.json.

Политика pollSCM проверяет Git раз в две минуты с распределенным H. Новый commit
f7df779 добавил инструкции запуска; он отправлен без ручного запуска последующей сборки.
SCMTriggerCause в build JSON подтверждает автоматизацию. Скриншот истории - настоящий
снимок браузера; consoleText и stages JSON - исходные данные Jenkins.

CI объединяет сборку и тестирование изменений. Continuous Delivery готовит выпуск,
Continuous Deployment выпускает его автоматически. Автодеплой в Jenkins не добавлен:
задание этого не требует. Docker Compose запуск выполнялся как учебная runtime-проверка.

## Секреты и ограничения сдачи

.env, пароль PostgreSQL, Jenkins password, GitHub token, Gradle caches исключены из архива
и Git. .env.example содержит только шаблон. GitHub repo приватный: преподавателю нужно
предоставить доступ либо использовать архив и bundle. Пароли должны передаваться через
локальную .env / Jenkins Credentials, не через Git, поскольку история сохраняет удаленные секреты.

Видео защиты не записывалось; ответы и порядок демонстрации включены в DEFENSE_RU.md и
OPERATIONS_RU.md. Артефакты Jenkins и контейнерные сервисы оставлены локально доступными.
Не требуется выдумывать screenshots или выдавать инструкции за выполненные команды.
