# temporal-diagrams-demo via Gradle and Ji
Compile temporal-diagrams-demo with gradle and jitpack dependencies

- https://github.com/mcanlas/temporal-diagrams
- https://jitpack.io/#mcanlas/temporal-diagrams

# Why

At work we have a jitpack mirror in our artifactory and we use Gradle for Scala.

Its easier for us to consume the lib from jitpack than from github through the firewall.


# Lockfile

`gradle.lockfile` takes a snapshot of the dynamically versioned `:latest.release` dependencies allowing this build on this commit to be reproducible.

See: https://docs.gradle.org/current/userguide/dependency_locking.html

## Updating versions in the lockfile

```shell
./gradlew :temporal-diagrams-demo:dependencies --write-locks
```
