# Jenkins для Kim_Vyacheslav
Создайте Pipeline job `Kim_Vyacheslav_Student_API`. Definition: Pipeline script from SCM, SCM Git,
URL `https://github.com/VyacheslavFML/kim-vyacheslav-student-api.git`, branch `*/main`, script path `Jenkinsfile`.
Для private repository выберите credential ID из Jenkins Credentials (GitHub PAT в Username with password).
Токен не записывайте в Jenkinsfile. У агента с меткой `java21-docker` должны быть Git, JDK 21,
POSIX shell, доступ к Docker daemon и интернет для Gradle. Wrapper уже включен.
Доступ к Docker socket дает высокие привилегии — используйте собственный выделенный учебный агент.

1. Запустите Build Now, сохраните номер, consoleText и stage view. Ожидаются Checkout, Build, Test, Docker Build, Verify.
2. Контролируемая ошибка сохранена в commit `6a64fa3` ветки `feature/kim-vyacheslav-ci-failure`.
   Для повторения используйте этот commit в отдельной job. Test запускается с `-PcontrolledFailure=true`, поэтому assertion падает.
   Docker Build и Verify должны быть пропущены. Сохраните реальный FAILED consoleText.
3. Исправление уже сохранено commit `2048dd4`; финальная версия ветки проходит.
   Для собственной повторной демонстрации исправьте команду на `./gradlew --no-daemon test`, сделайте commit и push.
   Сохраните следующий SUCCESS consoleText. В main намеренную ошибку не переносите.
4. Основная pipeline содержит pollSCM раз в 2 минуты с распределением H. После первого ручного запуска
   создайте и отправьте осмысленный commit. Дождитесь запуска от SCM polling, сохраните build cause,
   build history и consoleText. Ручной запуск не доказывает автоматический trigger.

CI автоматически собирает и тестирует изменения. Continuous Delivery готовит проверенный выпуск,
но публикация требует решения; Continuous Deployment выпускает автоматически. Этот Jenkinsfile
только проверяет и архивирует JAR, автоматического deployment нет.

Фактический статус исполнения смотрите в REPORT_RU.md: инструкция не является доказательством запуска.
