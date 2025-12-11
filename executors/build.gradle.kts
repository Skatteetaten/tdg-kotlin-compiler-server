repositories {
  mavenLocal()
  mavenCentral()

  maven("https://packages.jetbrains.team/maven/p/ij/intellij-dependencies/")
  maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-ide/")
}

plugins {
  id("base-kotlin-jvm-conventions")
}

dependencies {
  implementation(libs.junit)
}

tasks.withType<Jar>().getByName("jar") {
  destinationDirectory.set(libJVMFolder)
}