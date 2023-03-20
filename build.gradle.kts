plugins {
  java
}

java {
  toolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
  }
}

repositories {
  mavenCentral()
}

dependencies {
  compileOnly("org.jetbrains:annotations:24.0.1")
}