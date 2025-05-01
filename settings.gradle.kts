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
                username = "deffaalaika"
                password = "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"
            }
        }
    }
}

rootProject.name = "MusicPlayerApp"
include(":app")
