pipeline {
    properties([
        parameters([
            choice(
                name: 'MICROSERVICE',
                choices: ['payments-ui', 'workbench-ui'],
                description: 'Microservice that was deployed'
            ),
            choice(
                name: 'ENVIRONMENT',
                choices: ['QA', 'UAT', 'DEMO'],
                description: 'Environment where microservice was deployed'
            )
        ])
    ])

    agent any

    tools {
        maven 'Maven-3.9'
        jdk 'JDK-11'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Install Dependencies') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Install Playwright Browsers') {
            steps {
                sh 'mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install --with-deps chromium"'
            }
        }

        stage('Run Tests') {
            steps {
                script {
                    // Determine BASE_URL based on Environment and Microservice
                    def baseUrl
                    switch(params.ENVIRONMENT) {
                        case 'QA':
                            baseUrl = "https://unified-qa.digit.org/${params.MICROSERVICE}/employee/user/login"
                            break
                        case 'UAT':
                            baseUrl = "https://unified-uat.digit.org/${params.MICROSERVICE}/employee/user/login"
                            break
                        case 'DEMO':
                            baseUrl = "https://health-demo.digit.org/${params.MICROSERVICE}/employee/user/login"
                            break
                        default:
                            error("Invalid environment: ${params.ENVIRONMENT}")
                    }

                    // Determine test groups based on Microservice
                    // "common" group runs for both; specific group runs only for that microservice
                    def groups = "common,${params.MICROSERVICE}"

                    echo "Running ${params.MICROSERVICE} tests on ${params.ENVIRONMENT} environment"
                    echo "Using test groups: ${groups}"
                }

                // Use withCredentials to securely access credentials
                withCredentials([
                    string(credentialsId: "${params.ENVIRONMENT}-digit-username", variable: 'USERNAME'),
                    string(credentialsId: "${params.ENVIRONMENT}-digit-password", variable: 'PASSWORD')
                ]) {
                    script {
                        // Create .env file with environment-specific configuration
                        writeFile file: '.env', text: """
BASE_URL=${baseUrl}
USERNAME=${USERNAME}
PASSWORD=${PASSWORD}
BROWSER=chromium
HEADLESS=true
COUNTRY=Nigeria
STATE=Bouenza
LGA=Loudima
WARD=Loudima gare
VILLAGE=Malela
AREA=Village12
"""
                        // Run tests with group filtering
                        sh "mvn test -Dgroups=${groups} -DsuiteXmlFile=testng.xml"
                    }
                }
            }
        }

        stage('Generate Reports') {
            steps {
                publishHTML([
                    reportDir: 'test-output',
                    reportFiles: 'index.html',
                    reportName: 'TestNG Report',
                    keepAll: true
                ])
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'screenshots/**/*.png', allowEmptyArchive: true
            junit 'target/surefire-reports/junitreports/*.xml'
        }
        success {
            echo 'Tests executed successfully!'
        }
        failure {
            echo 'Tests failed! Check reports and screenshots.'
        }
    }
}
