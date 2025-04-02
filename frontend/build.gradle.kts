import org.siouan.frontendgradleplugin.infrastructure.gradle.InstallFrontendTask
import java.nio.file.Files
import kotlin.io.path.Path

plugins {
    alias(libs.plugins.frontend.gradle)
}

frontend {
    nodeVersion.set("22.12.0")
    assembleScript.set("run build")
    verboseModeEnabled.set(true)

    // disable http proxy
    httpProxyHost.set(null as String?)
    // disable https proxy
    httpsProxyHost.set(null as String?)
}

tasks.named<InstallFrontendTask>("installFrontend") {
    val ciPlatformPresent = providers.environmentVariable("CI").isPresent
    val lockFilePath = "${projectDir}/pnpm-lock.yaml"
    val retainedMetadataFileNames: Set<String>
    if (ciPlatformPresent) {
        retainedMetadataFileNames = setOf(lockFilePath)
    } else {
        retainedMetadataFileNames = mutableSetOf("${projectDir}/package.json")
        if (Files.exists(Path(lockFilePath))) {
            retainedMetadataFileNames.add(lockFilePath)
        }
        outputs.file(lockFilePath).withPropertyName("lockFile")
    }
    inputs.files(retainedMetadataFileNames).withPropertyName("metadataFiles")
    outputs.dir("${projectDir}/node_modules").withPropertyName("nodeModulesDirectory")
}

if (System.getenv("DISABLE_FRONTEND_BUILD") != null) {
    println("Frontend build is disabled by DISABLE_FRONTEND_BUILD environment variable")
    project.tasks.all { this.enabled = false }
}