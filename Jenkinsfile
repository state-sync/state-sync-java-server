pipeline {
    agent any
    stages {
        stage ('Build') {
             steps {
                  sh 'mvn clean install'
             }
        }
		stage ('Maven build') {
			rtMaven.deployer.addProperty("status", "in-qa")
			buildInfo = rtMaven.run pom: 'all/pom.xml', goals: 'clean install'
			server.publishBuildInfo buildInfo
		}
/*
        stage ('Test') {
             steps {
                  sh 'mvn test'
             }
        }
        */
    }
}
