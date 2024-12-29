plugins {
    id("java-library")
    alias(libs.plugins.protobuf)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.grpc.protobuf)
    implementation(libs.grpc.stub)

    implementation(libs.jakarta.annotation)
}

protobuf {
    protoc { artifact = "com.google.protobuf:protoc:${libs.versions.protobuf.orNull}" }
    tasks.named("clean") {
        delete(generatedFilesBaseDir)
    }
    plugins {
        register("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${libs.versions.grpc.orNull}"
        }
    }
    generateProtoTasks {
        this.all().forEach {
            it.plugins {
                register("grpc")
            }
        }
    }
}

sourceSets {
    main {
        java {
            srcDirs("build/generated/source/proto/main/grpc")
            srcDirs("build/generated/source/proto/main/java")
        }
    }
}