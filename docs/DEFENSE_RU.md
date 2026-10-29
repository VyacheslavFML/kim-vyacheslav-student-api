# Краткая подготовка к защите
- Spring Boot поднимает HTTP API; JPA сохраняет Student в PostgreSQL. Gradle собирает и тестирует.
- Docker image — шаблон файлов и настроек; container — запущенный экземпляр. `docker build` создает image.
- Multi-stage build оставляет JDK и Gradle в build stage, в runtime переносит JAR и JRE.
- FROM выбирает базовый образ; WORKDIR рабочую папку; RUN выполняется при build; CMD задает default command,
  ENTRYPOINT основной executable; COPY копирует файлы, ADD дополнительно умеет распаковывать архивы.
  EXPOSE документирует порт, `-p 8080:8080` публикует порт.
- Compose описывает app, db, network и volume. `db` — DNS-имя сервиса; localhost внутри app означает сам app.
- Volume хранит данные вне жизненного цикла контейнера. `down` сохраняет named volume, `down -v` удаляет.
- pg_isready проверяет готовность PostgreSQL принимать соединения; это не проверка CRUD.
- `docker ps`, `docker logs`, `docker inspect`, `docker exec`; stop останавливает, rm удаляет контейнер.
- Git локально хранит версии, GitHub размещает репозиторий. working tree — файлы, index — подготовленные изменения,
  commit — сохраненный снимок. add подготавливает; commit фиксирует; push отправляет; fetch получает ссылки;
  pull получает и объединяет. Перед синхронизацией status, fetch, log/diff; не затирайте чужие изменения force push.
- Branch — перемещаемая ссылка, tag — обозначение версии. merge объединяет историю, rebase переносит коммиты.
  При конфликте исправьте маркеры, согласуйте текст, git add и commit. .gitignore не удаляет уже отслеживаемые секреты.
- Jenkins controller управляет job, agent выполняет стадии. Jenkinsfile хранит pipeline рядом с кодом.
  Declarative использует pipeline/stages, Scripted — Groovy script. Checkout получает исходники.
  Build собирает; Test проверяет; ошибка sh с ненулевым кодом прерывает дальнейшие стадии.
- Credentials хранит секреты вне репозитория. Polling проверяет SCM, webhook сообщает о событии с GitHub.
- Linux — ОС семейства Unix; удобна для серверов и автоматизации. Процесс — выполняющаяся программа с PID.
  ps -ef показывает процессы; kill PID отправляет TERM; kill -9 только в крайнем случае.
- pwd показывает текущий путь, абсолютный путь начинается с /, относительный от текущей директории.
  mkdir создает каталог, mkdir -p создает также родителей; touch создает пустой файл; ls -la показывает скрытые.
  cp копирует, mv перемещает; chmod меняет permissions; 755 = rwxr-xr-x, здесь нужен 744 = rwxr--r--.
- Shebang #!/bin/bash выбирает интерпретатор. chmod +x разрешает запуск; аргументы $1, $2, все — "$@".
- df -h показывает свободное место файловых систем, free -h — RAM/swap, tail -5 последние строки,
  grep 'Ким' file > analysis.txt ищет фамилию. > перезаписывает, >> дописывает.
- При DB failure проверьте app logs, DB health, DB_URL (db, не localhost), username/password и network.
  При Docker Build failure откройте consoleText, найдите первую ошибку, проверьте Docker daemon,
  права агента, Dockerfile, скачивание образа и Gradle. Не маскируйте ошибку `|| true`.
