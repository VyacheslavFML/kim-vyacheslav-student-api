# Эксплуатация учебного проекта

## На этом Mac
Docker работает через Colima. Если он остановлен, откройте Terminal:
```bash
export PATH="/Users/papkimka/.codex/.chatgpt-projects/g-p-6ab19f9ac76081918375ee95ecf3392a/tmp/devops-tools/bin:$PATH"
colima start
```
Перейдите в каталог проекта. Используйте Docker CLI с Compose plugin. Приложение доступно
по http://localhost:8082/, Jenkins по http://localhost:8080/job/Kim_Vyacheslav_Student_API/.
Jenkins username: student. Пароль уже хранится локально в
`tmp/devops-infra/jenkins_password` в корне проекта University; он не включается в сдаваемые файлы.
Не переносите существующий пароль в GitHub или отчет.

## На другом компьютере
1. Установите JDK 21 и Docker с Compose v2. Распакуйте архив или клонируйте GitHub.
2. Скопируйте `.env.example` в `.env`, замените пароль. При необходимости измените APP_PORT.
3. Выполните `./gradlew clean test bootJar`, затем `docker compose up -d --build`.
4. Выполните `./scripts/api_smoke.sh http://localhost:8082`.
5. Системное задание выполняйте в Linux: `./scripts/linux_evidence.sh`.
6. `./scripts/container_evidence.sh` запускает учебную проверку stop/start и down/up.
   Сервисы временно недоступны во время этой проверки, записи сохраняются в named volume.

## Учетные данные
`POSTGRES_PASSWORD` задается только локально. Compose передает его через environment,
поэтому полный `docker inspect` содержит секрет. Для отчета выводятся только безопасные поля
Name, Image, User, Networks. Credentials Jenkins зашифрованы в Jenkins home, а GitHub credential
выбирается по ID `kim-vyacheslav-github`.

## История Git
В архиве есть каталог `.git` и отдельный bundle со всеми refs. Для восстановления без GitHub:
```bash
git clone Kim_Vyacheslav_history.bundle kim-vyacheslav-student-api
cd kim-vyacheslav-student-api
git branch -a
git tag
git log --graph --decorate --oneline --all
```
Git хранит только флаг executable, а не точный режим 744. После clone задайте
`chmod 744 scripts/Kim_Vyacheslav_system_report.sh`, как делает linux_evidence.sh.

## Данные и завершение
`docker compose down` сохраняет PostgreSQL volume. Не используйте `down -v`, если нужны данные.
Приложение учебное, без авторизации. Порт опубликован на 127.0.0.1. Jenkins оставлен на
существующем локальном учебном контроллере с выделенной меткой java21-docker.
Видео защиты следует записать самостоятельно по реальным сервисам и материалу DEFENSE_RU.md.
