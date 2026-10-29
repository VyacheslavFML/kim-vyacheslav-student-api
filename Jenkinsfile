pipeline {
 agent { label 'java21-docker' }
 options { timestamps(); disableConcurrentBuilds(); timeout(time: 30, unit: 'MINUTES'); skipDefaultCheckout(true) }
 triggers { pollSCM('H/2 * * * *') }
 environment { IMAGE = 'kim-vyacheslav-student-api:v1.0-kim-vyacheslav' }
 stages {
  stage('Checkout') { steps { checkout scm; echo 'Ким Вячеслав Александрович | Kim_Vyacheslav' } }
  stage('Build') { steps { sh './gradlew --no-daemon clean bootJar' } }
  stage('Test') { steps { sh './gradlew --no-daemon test' } post { always { junit 'build/test-results/test/*.xml' } } }
  stage('Docker Build') { steps { sh 'docker build -t "$IMAGE" .' } }
  stage('Verify') { steps { sh 'docker image inspect "$IMAGE" --format "Image={{.Id}} Size={{.Size}} Created={{.Created}}"' } }
 }
 post { success { archiveArtifacts artifacts: 'build/libs/student-api.jar', fingerprint: true } }
}
