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
          - name: docker
            image: docker:latest
            tty: true
            volumeMounts:
              - name: dockersock
                mountPath: "/var/run/docker.sock"
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

    stage('Build and Test') {
      steps {
        container('docker') {
          sh 'echo TODO: Figure this shit out'
        }
      }
    }
  }
}
