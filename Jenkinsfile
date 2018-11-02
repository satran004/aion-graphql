pipeline {
    agent any

    tools {
        jdk 'jdk10'
    }

    stages {
        stage('Cleanup Existing code') {
            steps {
                deleteDir()
            }
        }
        stage('Checkout') {
            steps {
                git 'http://github.com/satran004/aion-graphql'
            }
        }

        stage('Build') {
            steps {
                sh './gradlew clean build -x integrationTest'
            }
        }

//        stage('Integration Test') {
//            steps {
//                sh './gradlew integrationTest'
//            }
//        }

        stage('Test') {
            steps {
                sh './gradlew check'
            }
        }
    }
    post {
        always {
            archiveArtifacts artifacts: 'build/distributions/*.*', fingerprint: true
            junit 'build/test-results/**/*.xml'
        }
    }

}