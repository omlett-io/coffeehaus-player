#!/usr/bin/env groovy

pipeline {
  agent {
    kubernetes {
      yaml """
        apiVersion: v1
        kind: Pod
        spec:
          serviceAccountName: jenkins
          containers:
          - name: maven
            image: maven:3.6.2-jdk-11
            tty: true
            volumeMounts:
              - name: dockersock
                mountPath: "/var/run/docker.sock"
          - name: helm
            image: dtzar/helm-kubectl:2.14.3
            tty: true
          volumes:
            - name: dockersock
              hostPath:
                path: /var/run/docker.sock
      """
    }
  }

  parameters {
    string(name: 'MVN_CONFIG_FILE_ID', defaultValue: 'ca19d3a7-1765-4416-8926-58c0c7f0c474',
            description: 'defaultValue: Global Maven settings.xml')

    booleanParam(name: 'FORCE_DOCKER_PUBLISH', defaultValue: false,
            description: 'Force docker to build, push')

    booleanParam(name: 'FORCE_HELM_PUBLISH', defaultValue: false,
            description: 'Force helm to push, install')
  }

  stages {
    stage('Checkout from GitHub') {
      steps {
        checkout(
          [
            $class: 'GitSCM',
            branches: scm.branches,
            extensions: scm.extensions + [[$class: 'LocalBranch', localBranch: '**']],
            userRemoteConfigs: scm.userRemoteConfigs
          ]
        )
      }
    }

    stage('Compile & Test') {
      steps {
        container('maven') {
          sh 'mvn clean test'
        }
      }
    }

    stage('Build & Push New Docker Image') {
      steps {
        container('maven') {
          sh 'mvn clean package'
        }
      }
    }

    stage('Deploy Helm Charts') {
      steps {
        container('helm') {
          withCredentials([string(credentialsId: 'chartmuseum-secret', variable: 'CHARTMUSEUM_CREDENTIALS')]) {
            sh 'helm init --client-only && ' +
              ' helm plugin install https://github.com/chartmuseum/helm-push && ' +
              ' helm repo add omlett https://chartmuseum.omlett.io/ --username=admin --password=$CHARTMUSEUM_CREDENTIALS && ' +
              ' helm repo update && ' +
              ' helm push -f ./charts/coffeehaus-web omlett && ' +
              ' helm repo update && ' +
              ' helm upgrade coffeehaus-web omlett/coffeehaus-web --namespace coffeehaus --recreate-pods'
          }
        }
      }
    }
  }
}
