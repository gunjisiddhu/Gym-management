pipeline {
    agent any

    environment {
        SONAR_TOKEN = credentials('Gym-App-Sonar-Secret-Key')
         TOMCAT_CREDENTIALS = credentials('tomcat-credentials') // Use the ID of the Jenkins credential containing Tomcat credentials

    }

    stages {
        stage('Build, Test, and Generate Coverage') {
            steps {
                bat "mvn clean verify"
                bat "mvn jacoco:prepare-agent"
            }
        }
        stage('SonarQube Analysis') {
            steps {
                bat "mvn sonar:sonar -Dsonar.projectKey=Gym-Management -Dsonar.projectName='Gym-Management' -Dsonar.host.url=http://localhost:9000 -Dsonar.login=%SONAR_TOKEN% -Dsonar.java.coveragePlugin=jacoco"
            }
            post{
            success{
            archiveArtifacts artifacts:'**/target/*.war'
            }
            }

        }

                 stage('Deploy to Tomcat') {
                             steps {
                                 // Deploy the war file to Tomcat
                                deploy adapters: [tomcat9(credentialsId: 'TOMCAT_CREDENTIALS', path: '', url: 'http://localhost:9005')], contextPath: 'assessment', war: '**/*.war'
                             }
                         }
    }
}