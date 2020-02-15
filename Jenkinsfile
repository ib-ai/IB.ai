node {

    // Checkout the source code from Git.
    stage('Git Checkout') {
        git 'https://github.com/ib-ai/IB.ai/'
    }

    // Packages the source in order to generate a .jar artifact.
    stage('Package') {
        def mavenHome = tool name: 'maven-3', type: 'maven' // The maven home directory.
        sh "${mavenHome}/bin/mvn clean package" // Execute compile command.
    }

}