plugins {
    id("io.spring.dependency-management") version "1.0.11.RELEASE"

    kotlin("multiplatform") version "1.5.10"
    kotlin("plugin.spring") version "1.5.21"
}

group = "com.vladkrava"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
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
                compileOnly("org.springframework:spring-beans:5.3.13")
                compileOnly("org.springframework:spring-context:5.3.13")

                compileOnly("org.slf4j:slf4j-api:1.7.32")

                implementation("org.jetbrains.kotlin:kotlin-reflect:1.5.10")
            }
        }
        val jvmTest by getting {
            dependencies {

                //	Spring Dependencies
                implementation("org.springframework.boot:spring-boot-starter:2.6.1")
                implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

                //	Test Dependencies
                implementation("org.springframework.boot:spring-boot-starter-test:2.6.1")
                implementation("org.springframework.kafka:spring-kafka-test:2.7.6")
                implementation("org.assertj:assertj-core:3.21.0")
            }
        }
        val jsMain by getting
        val jsTest by getting
        val nativeMain by getting
        val nativeTest by getting
    }
}
