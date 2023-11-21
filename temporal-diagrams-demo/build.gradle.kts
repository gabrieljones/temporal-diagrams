plugins {
  scala
}

description = "temporal-diagrams-demo"

object Versions {
  const val temporalDiagrams = "5148c79407"
}

dependencies {
  implementation("org.scala-lang:scala3-library_3:latest.release")
  implementation("org.typelevel:cats-core_3:latest.release")
  implementation("com.github.mcanlas.temporal-diagrams:temporal-diagrams-core_3:${Versions.temporalDiagrams}")
  implementation("com.github.mcanlas.temporal-diagrams:temporal-diagrams-plantuml_3:${Versions.temporalDiagrams}")
  implementation("com.github.mcanlas.temporal-diagrams:temporal-diagrams-mermaid_3:${Versions.temporalDiagrams}")
  implementation("com.github.mcanlas.temporal-diagrams:temporal-diagrams-server_3:${Versions.temporalDiagrams}")

  testImplementation("org.scalatest:scalatest_3:latest.release")
  testRuntimeOnly("org.junit.platform:junit-platform-engine:latest.release")
  testRuntimeOnly("org.junit.platform:junit-platform-launcher:latest.release")
  testRuntimeOnly("co.helmethair:scalatest-junit-runner:latest.release")
}

tasks {
  compileJava {
    options.encoding = "UTF-8"
  }
  test {
    jvmArgs("-Dfile.encoding=UTF-8")
    useJUnitPlatform {
      includeEngines("scalatest")
      testLogging {
        events("passed", "skipped", "failed", "standard_error")
      }
    }
  }
}
