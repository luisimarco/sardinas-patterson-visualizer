plugins {
    id("org.jetbrains.kotlin.multiplatform") version "2.1.21"
}

group = "io.github.luisimarco"
version = "1.0.0"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(21)

    jvm()

    mingwX64 {
        binaries {
            executable { entryPoint = "main" }
        }
    }

    linuxX64 {
        binaries {
            executable { entryPoint = "main" }
        }
    }

    macosArm64 {
        binaries {
            executable { entryPoint = "main" }
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

// Produces a fat JAR including the Kotlin stdlib,
// runnable with: java -jar sardinas-patterson-visualizer.jar
tasks.named<Jar>("jvmJar") {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations["jvmRuntimeClasspath"].map {
        if (it.isDirectory) it else zipTree(it)
    })

}

