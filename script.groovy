def fetchCode() {
    echo "Pull Source code from GitHub"
    git branch: 'main', url: 'https://github.com/seunayolu/jenkins-terra-eks.git'
}

def buildCode() {
    echo "Build App with Maven"
    sh 'mvn clean install -DskipTests'
}

def buildImage() {
    echo "Build app with docker"
    dockerImage = docker.build(awsEcrRegistry + ":$BUILD_NUMBER", "./Docker-files/app/multistage/")
}

def pushImage() {
    echo "Push Docker Image to ECR"
    docker.withRegistry (imageRegUrl, awsEcrCreds) {
        dockerImage.push ("$BUILD_NUMBER")
        dockerImage.push ('latest')
    }
}

def provisionEksCluster() {
    withAWS(credentialsId: 'JenkinsAWSCLI', region: 'us-east-2') {
        dir('terraform') {
            sh 'terraform init'
            sh 'terraform apply --auto-approve'
            EKS_CLUSTER_NAME = sh(
                script: "terraform output cluster_name",
                returnStdout: true
            ).trim()
        }
    }
}

def connectEks() {
    echo "${EKS_CLUSTER_NAME}"
    withAWS(credentialsId: 'JenkinsAWSCLI', region: 'us-east-2') {
        sh "aws eks update-kubeconfig --name ${EKS_CLUSTER_NAME} --region ${awsRegion}"
        sh 'kubectl get nodes'
        sh 'kubectl apply -f app.yaml'
    }


}

return this 

