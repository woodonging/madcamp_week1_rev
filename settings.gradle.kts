pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        maven(url = uri("https://www.jitpack.io"))
        google()
        mavenCentral()
    }
}

rootProject.name = "madcamp_week1_rev"
include(":app")
