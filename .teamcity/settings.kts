import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs

version = "2021.1"

val dummies = listOf(Dummy("Foo", "foo"), Dummy("Bar", "bar"), Dummy("Baz", "baz"))

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
   }

   triggers {
       vcs {
           branchFilter = "+:releases/${dummy.branchName}-*"
       }
   }
})

class Dummy(val folderName: String, val branchName: String)
