import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script

version = "2021.1"

val dummies = listOf("Foo", "Bar", "Baz")

project {
   for (dummy in dummies) {
        buildType(HelloWorld(dummy))
   }
}

class HelloWorld(val dummy: String) : BuildType({
   id("${dummy}")
   name = "Hello World: ($dummy)"

   vcs {
       root(DslContext.settingsRoot)
   }

   steps {
        script {
            scriptContent = "echo 'Hello world!'"
        }
   }
})