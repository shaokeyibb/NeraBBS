plugins {
    id("java")
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependency.management)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

configurations {
    compileOnly {
        extendsFrom(annotationProcessor.orNull)
    }
}

dependencies {
    implementation(project(":common"))

    implementation(project(":lib-grpc"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("org.springframework.cloud:spring-cloud-starter-consul-discovery")

    implementation(libs.grpc.spring.boot.starter)

    implementation(libs.minio)

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${libs.versions.springCloud.orNull}")
        mavenBom("org.springframework.data:spring-data-bom:${libs.versions.springData.orNull}")
    }
}