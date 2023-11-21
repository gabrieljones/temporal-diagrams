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
  testImplementation("junit:junit:4.13")
}
