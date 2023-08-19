pipeline {
    agent any

    environment {
        SONAR_TOKEN = credentials('Gym-App-Sonar-Secret-Key')
    }

    stages {
        stage('Build, Test') {
            steps {
                bat "mvn clean verify"
                bat "mvn jacoco:prepare-agent"
            }
        }
        stage('SonarQube Analysis') {
            steps {
                bat "mvn sonar:sonar -Dsonar.projectKey=Gym-Management -Dsonar.projectName='Gym-Management' -Dsonar.host.url=http://localhost:9000 -Dsonar.login=%SONAR_TOKEN% -Dsonar.java.coveragePlugin=jacoco"
            }
        }

        stage("Build Modules & Build Docker Images") {
                steps {
                    script {
                        def modules = ['eurekaserverforgymapp', 'gatewayforgymapp','gymauthenticationservice', 'gymnotificationservice','gymmanagementapplication', 'gymreportmanagement']
                        for (def module in modules) {
                            dir("${module}") {
                                echo "         - - - -  - >  Building ${module}...  < - - -  - - "
                                bat "mvn clean install"
                            }
                        }
                    }
                }
            }
    }
}


