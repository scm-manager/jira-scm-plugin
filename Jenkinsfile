pipeline {
    agent { 
        dockerfile true
    }

    stages {
        stage('Clean') {
            steps {
                sh 'atlas-clean'
            }
        }
        stage('Build') {
            steps {
                sh 'atlas-package'
            }
        }
        stage('Unit Tests') {
            steps {
                sh 'atlas-unit-test'
            }
        }
    }

    post {
        always {
            junit 'target/surefire-reports/*.xml'
            archiveArtifacts artifacts: 'target/*.jar', excludes: '**/*-tests.jar', fingerprint: true
        }
    }

}