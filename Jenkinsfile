node {
    def mavenHome = tool name: 'Maven', type: 'maven' // The maven home directory.

    // Checkout the source code from Git.
    stage('Git Checkout') {
        git 'https://github.com/ib-ai/IB.ai/'
    }

    // Packages the source in order to generate a .jar artifact.
    stage('Package') {
        sh "${mavenHome}/bin/mvn clean install" // Execute compile command.
        archiveArtifacts 'target/**/*'
    }

}