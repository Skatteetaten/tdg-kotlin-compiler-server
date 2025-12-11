pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()

        maven("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies/")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-ide/")
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()

        maven("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies/")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-ide/")
    }

    versionCatalogs {
        register("libs").configure {
            val kotlinVersion = providers.gradleProperty("kotlin_version")
            if (kotlinVersion.isPresent) {
                version("kotlin", kotlinVersion.get())
            }
        }
    }
}
