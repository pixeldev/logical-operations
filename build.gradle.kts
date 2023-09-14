plugins {
  java
}

tasks {
  compileJava {
    options.encoding = "UTF-8"
  }

  jar {
    manifest {
      attributes["Main-Class"] = "com.pixeldv.truthtables.Main"
    }
  }
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