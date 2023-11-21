dependencyResolutionManagement {
  repositories {
    exclusiveContent {
      forRepository { maven("https://jitpack.io") }
      filter {
        includeGroup("com.github.mcanlas.temporal-diagrams")
      }
    }
    mavenCentral()
  }
}

rootProject.name = "temporal-diagrams"

include(
  "temporal-diagrams-demo",
)
