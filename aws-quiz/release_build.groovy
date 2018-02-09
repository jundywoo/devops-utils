node('devops.kennie-ng.cc') {
    def BRANCH = 'master'
    def PROJECT_URL = 'https://github.com/jundywoo/AWS_Projects.git/'

    try {
        stage('Github SCM') {
          git branch: BRANCH, url: PROJECT_URL, credentialsId: 'a1db2c2a-ccf4-4366-83de-1d47aa2fe9fd'
        
            def pom = readMavenPom file: 'aws-quiz/pom.xml'
           def version = pom.version
        
          currentBuild.displayName="#${currentBuild.number} - $version"
       }
    
      stage('Maven Build') {
          sh 'mvn clean install -DskipDocker -f aws-quiz/pom.xml'
      }
    
      stage('Nexus Deploy') {
           sh "mvn deploy -Dmaven.test.skip=true -f aws-quiz/pom.xml"
       }
       
       currentBuild.result = 'SUCCESS'
    } catch (Exception e) {
       currentBuild.result = 'FAILURE'
       currentBuild.description = "Failure: " + e.getMessage();
    }
}
