pipeline {
    agent any
    environment {
        registryCredential = "Jenkins-user"
        AWS_DEFAULT_REGION="us-west-2"
        IMAGE_TAG="latest"
        AWS_ACCOUNT_ID = credentials('aws-account-id')
        CLUSTER_NAME = credentials('ecs-cluster-name')
        SERVICE_NAME = credentials('ecs-service-name')
        IMAGE_REPO_NAME = credentials('ecr-repo-name')
        REPOSITORY_URI = "${env.AWS_ACCOUNT_ID}.dkr.ecr.${env.AWS_DEFAULT_REGION}.amazonaws.com/${env.IMAGE_REPO_NAME}"
    }

    stages {

        stage('application.yml download') {
            steps {
                withCredentials([file(credentialsId: 'application.yml', variable: 'APPLICATION_YML')]) {
                    script {
                        sh 'mkdir -p src/main/resources'
                        sh 'touch src/main/resources/application.yml'
                        sh 'cp $APPLICATION_YML src/main/resources/application.yml'
                    }
                }
            }
        }

        // Building Docker image
        stage('Building image') {
            steps {
                script {
                    dockerImage = docker.build "${IMAGE_REPO_NAME}:${IMAGE_TAG}"
                }
            }
        }

        // Uploading Docker image into AWS ECR
        stage('Releasing') {
            steps {
                script {
                    docker.withRegistry("https://" + REPOSITORY_URI, "ecr:${AWS_DEFAULT_REGION}:" + registryCredential) {
                                dockerImage.push()
                    }
                }
             }
        }

        stage ('ECS Deploy') {
            steps {
                withAWS(credentials: registryCredential, region: "${AWS_DEFAULT_REGION}") {
                    script {
                       sh "aws ecs update-service --cluster ${CLUSTER_NAME} --service ${SERVICE_NAME} --force-new-deployment"
                    }
                }
            }
        }
    }

   // Clear local image registry. Note that all the data that was used to build the image is being cleared.
   // For different use cases, one may not want to clear all this data so it doesn't have to be pulled again for each build.
    post {
        always {
            sh 'docker system prune -a -f'
        }
    }
}