allprojects {
    group = "io.hikarilan"
    version = "1.0-SNAPSHOT"

    repositories {
        maven("https://maven.aliyun.com/repository/public")
        maven("https://maven.aliyun.com/repository/central")
        maven("https://maven.aliyun.com/repository/apache-snapshots")
        mavenCentral()
    }
}