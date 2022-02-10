def timeoutInMin =  '20'


def tag (_imageName, params){​​​​​​​
  openshift.withCluster( ) {​​​​​​​
      openshift.withProject("proxy-dev") {​​​​​​​
        def PARA = params.PARA.split("-")[1]
        echo "Stage Tag - Using project: ${​​​​​​​openshift.project()}​​​​​​​ - image ${​​​​​​​_imageName}​​​​​​​ - Promote ${​​​​​​​params.DE}​​​​​​​ to ${​​​​​​​PARA}​​​​​​​"
        openshift.tag(_imageName+":"+params.DE, _imageName+":"+PARA)
      }​​​​​​​
  }​​​​​​​
}​​​​​​​


def deploy (_namespaceToDeploy, _deploymentConfiguration){​​​​​​​
  def deploy = "DeploymentConfiguration ${​​​​​​​_namespaceToDeploy}​​​​​​​/${​​​​​​​_deploymentConfiguration}​​​​​​​"
  echo deploy
  openshift.withCluster() {​​​​​​​
      openshift.withProject(_namespaceToDeploy) {​​​​​​​
      echo "Using project: ${​​​​​​​openshift.project()}​​​​​​​"
      def rm = openshift.selector("dc", _deploymentConfiguration).rollout().latest()
      timeout(5) {​​​​​​​
          openshift.selector("dc", _deploymentConfiguration).related('pods').untilEach(1) {​​​​​​​
          return (it.object().status.phase == "Running")
          }​​​​​​​
      }​​​​​​​
      }​​​​​​​
  }​​​​​​​
}​​​​​​​




pipeline {​​​​​​​
  agent any


  parameters {​​​​​​​
       //choices['latest', 'cor', 'hml', 'tre', 'bet']
       choice(name: 'DE', choices: ['cor','latest', 'hml', 'bet'], description: 'Qual imagem será utilizada para o Deploy?')


       choice(name: 'PARA', choices: ['proxy-cor', 'proxy-bet', 'proxy-prd'], description: 'Qual ambiente será feito o deploy?')
       
    }​​​​​​​


  options {​​​​​​​
    timeout(time: timeoutInMin, unit: 'MINUTES')
  }​​​​​​​
  
  stages {​​​​​​​
       stage('Tagging Backend') {​​​​​​​
           steps {​​​​​​​
               script {​​​​​​​
                   tag("proxy-dev/proxy-backend", params)
               }​​​​​​​
             }​​​​​​​
         }​​​​​​​


       stage('Deploying Backend') {​​​​​​​
         steps {​​​​​​​
             script {​​​​​​​
                deploy(params.PARA, "prd-backend")
             }​​​​​​​
         }​​​​​​​
       }​​​​​​​

      stage('notifing') {​​​​​​​
        steps {​​​​​​​
            script {​​​​​​​
                def promoteTo = params.PARA
//                if (promoteTo == 'proxy-camel-prd'){​​​​​​​
                 message += props.release.description
                echo message
//                office365ConnectorSend message: message, status:"Completo", webhookUrl:teamsChannelURL
                }​​​​​​​   
            }​​​​​​​
        }​​​​​​​
      }​​​​​​​
  }​​​​​​​
}​​​​​​​
 















