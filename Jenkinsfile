pipeline {
    agent any

    environment {
        IMAGE_NAME = 'priya123456/restapi'
        CONTAINER_NAME = 'restapi-container'
        PORT_MAPPING = '8081:7000'
    }

    triggers {
        githubPush()
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/ZishaanAsif/java-rest-demo.git'
            }
        }

        stage('Build and Test') {
            agent {
                docker {
                    image 'maven:3.9.6-eclipse-temurin-17'
                }
            }
            steps {
                sh 'mvn clean compile'
                sh 'mvn test'
                sh 'mvn package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                    docker build -t $IMAGE_NAME .
                '''
            }
        }

        stage('Push Docker Image') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-token',
                                                  usernameVariable: 'MY_DOCKER_USER',
                                                  passwordVariable: 'MY_DOCKER_PASS')]) {
                    sh '''
                        echo "$MY_DOCKER_PASS" | docker login -u "$MY_DOCKER_USER" --password-stdin
                        docker tag $IMAGE_NAME $MY_DOCKER_USER/restapi
                        docker push $MY_DOCKER_USER/restapi
                    '''
                }
            }
        }

        stage('Run Container') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-token',
                                                  usernameVariable: 'MY_DOCKER_USER',
                                                  passwordVariable: 'MY_DOCKER_PASS')]) {
                    sh '''
                        docker pull $MY_DOCKER_USER/restapi
                        docker rm -f $CONTAINER_NAME || true
                        docker run -d --name $CONTAINER_NAME -p $PORT_MAPPING $MY_DOCKER_USER/restapi
                        sleep 5
                        docker ps | grep $CONTAINER_NAME
                    '''
                }
            }
        }
    }

    post {
        success {
            echo "Pipeline completed successfully"
        }
        failure {
            echo "Pipeline failed"
        }
    }
}
