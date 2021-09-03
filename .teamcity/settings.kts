import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

version = "2021.1"

project {
   buildType(HelloWorld(Dummy("Foo", "foo")))
}

class HelloWorld(val dummy: Dummy) : BuildType({
   id("${dummy.folderName}")
   name = "Hello World: (${dummy.folderName})"

   vcs {
       root(DslContext.settingsRoot)
   }

   steps {
        script {
            scriptContent = "echo 'Bye world!'"
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
