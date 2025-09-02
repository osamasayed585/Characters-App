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
    }
}

rootProject.name = "Rick Morty Characters"
include(":app")
include(":core:network")
include(":core:common")
include(":core:dataStore")
include(":core:data")
include(":core:domain")
include(":core:navigation")
include(":core:design")
include(":core:model")
include(":feature:home")
include(":feature:details")
