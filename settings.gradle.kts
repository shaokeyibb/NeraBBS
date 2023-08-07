rootProject.name = "NeraBBS"

include("common")

include("lib-grpc")

include("gateway")

include("service-auth")
include("service-user")

include("frontend")

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        maven("https://maven.aliyun.com/repository/spring-plugin")
    }
}
