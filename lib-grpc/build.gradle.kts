plugins {
    id("java")
    alias(libs.plugins.protobuf)
}

repositories {
    maven { url = uri("https://maven-central.storage-download.googleapis.com/maven2/") }
}

dependencies {
    implementation(libs.grpc.protobuf)
    implementation(libs.grpc.services)
    implementation(libs.grpc.stub)

    implementation("javax.annotation:javax.annotation-api:1.3.2")

    runtimeOnly(libs.grpc.netty.shaded)
}

protobuf {
    protoc { artifact = "com.google.protobuf:protoc:${libs.versions.protobuf.orNull}" }
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