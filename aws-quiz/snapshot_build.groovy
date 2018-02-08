node('devops.kennie-ng.cc') {
    def BRANCH='develop'
    def GITHUB_URL='https://github.com/jundywoo/AWS_Projects.git/'
    
    try {
        stage('Github SCM') {
          git([url: GITHUB_URL, branch: BRANCH])
        
            def pom = readMavenPom file: 'aws-quiz/pom.xml'
           def version = pom.version
        
          currentBuild.displayName="#${currentBuild.number} - $version"
       }
    
      stage('Maven Build') {
          sh 'mvn clean install -DskipDocker -f aws-quiz/pom.xml'
      }
    
      stage('Nexus Deploy') {
           sh "mvn deploy -Dmaven.test.skip=true -DskipDockerPush -f aws-quiz/pom.xml"
       }
       
       currentBuild.result = 'SUCCESS'
    } catch (Exception e) {
       currentBuild.result = 'FAILURE'
       currentBuild.description = "Failure: " + e.getMessage();
    }
}
