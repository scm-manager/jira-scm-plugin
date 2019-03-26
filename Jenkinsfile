agent { 
    dockerfile true
    stages {
        stage('Clean') {
            steps {
                sh 'atlas-package'
            }
        }
        stage('Build') {
            steps {
                sh 'atlas-package'
            }
        }
    }
    post {
        always {
            junit 'target/surefire-reports/*.xml'
            junit 'target/failsafe-reports/*.xml'
            archiveArtifacts artifacts: '/target/**', excludes: '**/*-test.jar', fingerprint: true
        }
    }
}