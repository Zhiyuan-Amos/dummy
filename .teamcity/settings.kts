import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

version = "2021.1"

val dummies = listOf(Dummy("Foo", "foo"))

project {
   for (dummy in dummies) {
        buildType(HelloWorld(dummy))
   }
}

class HelloWorld(val dummy: Dummy) : BuildType({
   id("${dummy.folderName}")
   name = "Hello World: (${dummy.folderName})"

   vcs {
       root(DslContext.settingsRoot)
   }

   steps {
        script {
            scriptContent = "echo 'Hello world!'"
        }
        script {
            scriptContent = "echo 'Hello again world!'"
        }
        script {
            conditions {
                matches("teamcity.build.branch", "feature/.*")
            }
            scriptContent = "echo 'Hello World (3)'"
        }
   }

   triggers {
       vcs {
           triggerRules = """
               +:parentDirectory/${dummy.folderName}/**
           """.trimIndent()
           branchFilter = """
               +:*
               -:releases/*
               -:hotfix/*
               +:releases/${dummy.branchName}-*
               +:hotfix/${dummy.branchName}-*
           """.trimIndent()
           perCheckinTriggering = true
           groupCheckinsByCommitter = true
           enableQueueOptimization = false
       }
   }
})

class Dummy(val folderName: String, val branchName: String)
