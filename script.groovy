def fetchcode() {
    echo "Pull Source code from GitHub"
    git branch: 'main', url: 'https://github.com/seunayolu/jenkins-terra-eks.git'
}

def buildcode() {
    echo "Build App with Maven"
    sh 'mvn clean install -DskipTests'
}

def buildimage() {
    echo "Build app with docker"
    dockerImage = docker.build(awsecrregistry + ":$BUILD_NUMBER", "./Docker-files/app/multistage/")
}

def pushimage() {
    echo "Push Docker Image to ECR"
    docker.withRegistry (imageregurl, awsecrcreds) {
        dockerImage.push ("$BUILD_NUMBER")
        dockerImage.push ('latest')
    }
}

def provisionekscluster() {
    withAWS(credentails: 'JenkinsAWSCLI', region: "${awsregion}") {
        dir('terraform') {
            sh 'terraform init'
            sh 'terraform apply --auto-approve'
            EKS_CLUSTER_NAME = sh(
                script: "terraform output cluster_name"
                returnStdout: true
            ).trim()
        }
    }
}

def connecteks() {
    echo "${EKS_CLUSTER_NAME}"
    withAWS(credentails: 'JenkinsAWSCLI', region: "${awsregion}") {
        sh "aws eks update-kubeconfig --name ${EKS_CLUSTER_NAME} --region ${awsregion}"
        sh 'kubectl get nodes'
        sh 'kubectl apply -f app.yaml'
    }


}

