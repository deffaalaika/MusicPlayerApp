import java.io.FileInputStream
import java.util.Properties

// Load the local.properties file
val localProperties = Properties()
val localPropertiesFile = file("local.properties")

if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}

val localUsername = localProperties["username"]?.toString()
val localPassword = localProperties["password"]?.toString()


pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url = uri("https://maven.pkg.github.com/deffaalaika/MusicPlayerSearch")
            credentials {
                username = localUsername
                password = localPassword
            }
        }
    }
}

rootProject.name = "MusicPlayerApp"
include(":app")
