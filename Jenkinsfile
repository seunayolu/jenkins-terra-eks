def gv

pipeline {
    agent any
    environment {
        awsecrcreds = 'ecr:us-east-2:JenkinsAWSCLI'
        awsecrregistry = "392102158411.dkr.ecr.us-east-2.amazonaws.com/app-image"
        imageregurl = "https://392102158411.dkr.ecr.us-east-2.amazonaws.com"
        awsregion = "us-east-2"
    }
    tools {
        maven "MAVEN3"
        jdk "OracleJDK8"
    }
    stages {
        stage ("init") {
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }
        stage ('Fetch Code') {
            steps {
                script {
                    gv.fetchcode()
                }
            }
        }
        stage ('Build') {
            steps {
                script {
                    gv.buildcode()
                }
            }
            post {
                success {
                    archiveArtifacts artifacts: '**/*.war'
                }
            }
        }
        stage ('Build Image') {
            steps {
                script {
                    gv.buildimage()
                }
            }
        }
        stage ('Push Image to ECR') {
            steps {
                script {
                    gv.pushimage()
                }
            }
        }
        stage ('provision eks cluster') {
            steps {
                script {
                    gv.provisionekscluster()
                }
            }
        }
        stage ('connect to eks cluster') {
            steps {
                script {
                    gv.connecteks()
                }
            }
        }
    }
}