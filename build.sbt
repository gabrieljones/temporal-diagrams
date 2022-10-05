lazy val root =
  Project("temporal-diagrams", file("."))
    .withCats
    .withTesting
    .aggregate(core, demo)

lazy val core =
  project
    .settings(name := "temporal-diagrams-core")
    .withCats
    .withTesting

lazy val demo =
  project
    .settings(libraryDependencies += "org.typelevel" %% "cats-effect" % "3.3.14")
    .dependsOn(core)
