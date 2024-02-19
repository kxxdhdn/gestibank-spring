pipeline {

	environment {
	    // Credential de DockerHub sauvegardé dans Jenkins
	    DOCKERHUB_CREDENTIALS=credentials('docker')
        DOCKER_REPO = "kxxdhdn"
        DOCKER_IMAGE = "gesticat"
        VERSION = "1.0.0" // initial
    }
    agent any 

    stages {
        stage('Début du pipeline ...') { 
            steps {
                echo 'Chargement' 
            }
        }
        
        stage('Obtenir la version la plus récente ...') {
            steps {
                script {
                    try {
                        // tags existants
                        def tags = sh(script: "docker pull ${DOCKER_REPO}/${DOCKER_IMAGE}:${VERSION} && docker image ls --format='{{.Tag}}' ${DOCKER_REPO}/${DOCKER_IMAGE}", returnStdout: true).trim()
                        echo "tags : ${tags}"
                        // tag list
                        //def tagList = tags.tokenize("\n")
                        //echo "tagList : ${tagList}"
                        // tag le plus récent
                        def latestTag = tags.tokenize("\n")[-1]
                        echo "latestTag : ${latestTag}"
                        // incrément version
                        if (latestTag != "") {
                            VERSION = incrementVersion(latestTag)
                            echo "Version ${VERSION}"
                        }
                    } catch (Exception e) {
                        echo "Version initiale."
                    }
                }
            }
        }

        stage('Création image Docker ...') { 
            steps {
                sh "docker build -t ${DOCKER_REPO}/${DOCKER_IMAGE}:${VERSION} ."
            }
        }
        
        stage('Tag et push image ...') {
            steps {
                echo "Push image version ${VERSION} ..."

                sh "docker tag ${DOCKER_REPO}/${DOCKER_IMAGE}:${VERSION} ${DOCKER_REPO}/${DOCKER_IMAGE}:${VERSION}"
                
                //sh "docker login -u $DOCKERHUB_CREDENTIALS_USR -p $DOCKERHUB_CREDENTIALS_PSW"
                withCredentials([usernamePassword(credentialsId: 'docker', passwordVariable: 'DOCKERHUB_PSW', usernameVariable: 'DOCKERHUB_USR')]) {
                    
                    script {
                        sh "docker login -u ${DOCKERHUB_USR} -p ${DOCKERHUB_PSW}"
                    }
                }
                
                sh "docker push ${DOCKER_REPO}/${DOCKER_IMAGE}:${VERSION}"
                    
                sh "docker logout"
            }

            post{

                success{
                    echo "====++++success++++===="
                }

                failure{
                    echo "====++++failed++++===="
                }
            }
        }

        stage('Lancement Stack Docker-Compose ...') { 
            steps {
                sh 'docker compose -f Docker-compose.yml up -d'
            }
        }
    }
}

def incrementVersion(version) {
    // numéro de version majeure, mineure et de révision
    def parts = version.tokenize('.')
    def major = parts[0] as Integer
    def minor = parts[1] as Integer
    def patch = parts[2] as Integer
    // incrément version majeure
    //major++
    //patch = 0
    // incrément version mineure
    //minor++
    //patch = 0
    // incrément version révision
    patch++
    
    return "${major}.${minor}.${patch}"
}