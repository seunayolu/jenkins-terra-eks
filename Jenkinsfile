def gv

pipeline {
    agent any
    environment {
        awsEcrCreds = 'ecr:us-east-2:JenkinsAWSCLI'
        awsEcrRegistry = "392102158411.dkr.ecr.us-east-2.amazonaws.com/app-image"
        imageRegUrl = "https://392102158411.dkr.ecr.us-east-2.amazonaws.com"
        awsRegion = "us-east-2"
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
        stage ("Fetch Code") {
            steps {
                script {
                    gv.fetchCode()
                }
            }
        }
        stage ('Build') {
            steps {
                script {
                    gv.buildCode()
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
                    gv.buildImage()
                }
            }
        }
        stage ('Push Image to ECR') {
            steps {
                script {
                    gv.pushImage()
                }
            }
        }
        stage ('provision eks cluster') {
            steps {
                script {
                    gv.provisionEksCluster()
                }
            }
        }
        stage ('connect to eks cluster') {
            steps {
                script {
                    gv.connectEks()
                }
            }
        }
    }
}