plugins {
    id("io.spring.dependency-management") version "1.0.11.RELEASE"

    kotlin("multiplatform") version "1.6.10"
    kotlin("plugin.spring") version "1.5.21"
}

apply("versions.gradle.kts")

group = "com.vladkrava"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "16"
        }
        withJava()
        testRuns["test"].executionTask.configure {
            useJUnitPlatform()
        }
    }
    js(BOTH) {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    
    sourceSets {
        val commonMain by getting
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                //	Spring Dependencies
                compileOnly("org.springframework:spring-beans:${extra["spring-framework"]}")
                compileOnly("org.springframework:spring-context:${extra["spring-framework"]}")

                //	Log Dependencies
                compileOnly("org.slf4j:slf4j-api:${extra["slf4j-api"]}")

                //	Other Dependencies
                implementation("org.jetbrains.kotlin:kotlin-reflect:${extra["kotlin-version"]}")
            }
        }
        val jvmTest by getting {
            dependencies {

                //	Spring Dependencies
                implementation("org.springframework.boot:spring-boot-starter:${extra["spring-boot-starter"]}")
                implementation("org.jetbrains.kotlin:kotlin-stdlib:${extra["kotlin-version"]}")

                //	Test Dependencies
                implementation("org.springframework.boot:spring-boot-starter-test:${extra["spring-boot-starter"]}")
            }
        }
        val jsMain by getting
        val jsTest by getting
        val nativeMain by getting
        val nativeTest by getting
    }
}
