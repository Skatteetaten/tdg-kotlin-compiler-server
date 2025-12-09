#!/usr/bin/env groovy
def config = [
   scriptVersion              : 'v7',
   iqOrganizationName         : 'Team RST',
   iqEmbedded                 : true,
   iqBreakOnUnstable          : false,
   pipelineScript             : 'https://git.aurora.skead.no/scm/ao/aurora-pipeline-scripts.git',
   iq                         : false,
   checkstyle                 : false,
   javaVersion                : 17,
   compileProperties          : '-U -x test --stacktrace --info --refresh-dependencies',
   sonarQube                  : false,
   disableAllReports          : true,
   jiraSetKomponent           : true,
   jiraFiksetIKomponentversjon: true,
   versionStrategy            : [
           [branch: 'master', versionHint: '0']
       ]
   ]

fileLoader.withGit(config.pipelineScript, config.scriptVersion) {
  jenkinsfile = fileLoader.load('templates/leveransepakke')
}

jenkinsfile.gradle(config.scriptVersion, config)
