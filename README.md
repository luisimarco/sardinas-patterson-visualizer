# Sardinas-Patterson Visualizer

An interactive console application that checks whether a code is **Uniquely Decipherable (UD)**
using the [Sardinas-Patterson algorithm](https://en.wikipedia.org/wiki/Sardinas%E2%80%93Patterson_algorithm).

Designed as a didactic tool for information theory and source coding courses.
Developed as a personal exercise in Kotlin Multiplatform — including Gradle configuration,
native compilation targets, and GitHub Actions release automation.
---

## Features

- Step-by-step or instant display of the Sardinas-Patterson table
- Built-in example codes (UD and non-UD)
- Kraft-McMillan inequality evaluation
- Interactive console UI

---

## Download

Pre-built executables are available on the [Releases](../../releases) page:

| Platform | File |
|---|---|
| Windows | `sardinas-patterson-visualizer.exe` |
| Linux | `sardinas-patterson-visualizer.kexe` |
| macOS | `sardinas-patterson-visualizer.kexe` |
| Any (JVM) | `sardinas-patterson-visualizer.jar` |

For the JVM version, [Java 21](https://adoptium.net/) or later is required:
```bash
java -jar sardinas-patterson-visualizer.jar
```

---

## Build from Source

**Requirements:** JDK 21, Gradle (included via wrapper)

```bash
git clone https://github.com/luisimarco/sardinas-patterson-visualizer.git
cd sardinas-patterson-visualizer

# JVM
./gradlew jvmJar

# Windows native
./gradlew linkReleaseExecutableMingwX64

# Linux native
./gradlew linkReleaseExecutableLinuxX64

# macOS native
./gradlew linkReleaseExecutableMacosArm64
```

---

## Project Structure