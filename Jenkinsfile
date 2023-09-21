def gv

pipeline {
    agent any
    environment {
        awsecrcreds = 'ecr:us-east-2:JenkinsAWSCLI'
        awsecrregistry = "392102158411.dkr.ecr.us-east-2.amazonaws.com/app-image"
        imageregurl = "https://392102158411.dkr.ecr.us-east-2.amazonaws.com"
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
                    echo "Fetching code from GitHub"
                    gv.fetchcode()
                }
            }
        }
    }
}