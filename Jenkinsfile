pipeline {
    agent any
    environment {
        IMAGE_NAME = 'ahaive-backend'

        ECR_PATH = '407401394535.dkr.ecr.ap-northeast-2.amazonaws.com/dnd/backend'
        REGION = 'ap-northeast-2'

        SERVICE_EC2_IP = '10.0.6.196'
        SERVICE_EC2_USER = 'ubuntu'
    }
    stages {
        stage('Checkout Github') {
            steps {
                checkout scmGit(
                    branches: [[name: '*/main']],
                    extensions: [],
                    userRemoteConfigs: [[credentialsId: 'github-credential', url: 'https://github.com/dnd-side-project/dnd-14th-7-backend.git']]
                )
            }
            post {
                success { echo 'success checkout' }
                failure { error 'fail checkout' }
            }
        }

//         stage('test') {
//             steps {
//                 echo 'test start'
//                 sh './gradlew test'
//             }
//             post {
//                 success { echo 'success test' }
//                 failure { error 'fail test' }
//             }
//         }

        stage('docker image build') {
            steps {
                echo 'docker image build start'
                sh 'docker build -t ${ECR_PATH}/${IMAGE_NAME}:${BUILD_NUMBER} .'
                sh 'docker tag ${ECR_PATH}/${IMAGE_NAME}:${BUILD_NUMBER} ${ECR_PATH}/${IMAGE_NAME}:latest'

            }
            post {
                success { echo 'success docker image build' }
                failure { error 'fail docker image build' }
            }
        }

        stage('docker image push') {
            steps {
                script {
                    sh 'aws ecr get-login-password --region ${REGION} | docker login --username AWS --password-stdin ${ECR_PATH}'
                    sh 'docker push ${ECR_PATH}/${IMAGE_NAME}:${BUILD_NUMBER}'
                    sh 'docker push ${ECR_PATH}/${IMAGE_NAME}:latest'
                }
            }
            post {
                success { echo 'success image push' }
                failure { error 'fail image push' }
            }
        }

        stage('deploy') {
            steps {
                echo 'deploy the application...'
                sshagent(credentials: ['service_ec2_ssh']) {
                            sh """
                                ssh -v -o StrictHostKeyChecking=no ${SERVICE_EC2_USER}@${SERVICE_EC2_IP} '
                                    # ECR 로그인
                                    aws ecr get-login-password --region ap-northeast-2 | \
                                    docker login --username AWS --password-stdin ${ECR_PATH}

                                    # 새 이미지 pull
                                    docker pull ${ECR_PATH}/${IMAGE_NAME}:${BUILD_NUMBER}

                                    # docker-compose 이미지 태그 업데이트 후 재시작
                                    cd /home/ubuntu/app
                                    docker compose -f docker-compose-prod.yml up -d db
                                    IMAGE_TAG=${BUILD_NUMBER} docker compose -f docker-compose-prod.yml up -d --no-deps app

                                    # 이전 이미지 정리
                                    docker image prune -f
                                '
                            """
                        }
            }
            post {
                success { echo 'success deploy' }
                failure { error 'fail deploy' }
            }
        }
    }
}