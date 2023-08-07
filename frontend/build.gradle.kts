import org.siouan.frontendgradleplugin.infrastructure.gradle.InstallFrontendTask
import java.nio.file.Files
import kotlin.io.path.Path

plugins {
    alias(libs.plugins.frontend.gradle)
}

frontend {
    nodeVersion.set("18.17.0")
    assembleScript.set("run build")
//    cleanScript.set("run clean")
//    checkScript.set("run check")
    verboseModeEnabled.set(true)

    // disable http proxy
    httpProxyHost.set(null as String?)
    // disable https proxy
    httpsProxyHost.set(null as String?)
}

tasks.named<InstallFrontendTask>("installFrontend") {
    val ciPlatformPresent = providers.environmentVariable("CI").isPresent()
    val lockFilePath = "${projectDir}/yarn.lock"
    val retainedMetadataFileNames: Set<String>
    if (ciPlatformPresent) {
        retainedMetadataFileNames = setOf(lockFilePath)
    } else {
        // The naive configuration below allows to skip the task if the last successful execution did not change neither
        // the package.json file, nor the lock file, nor the node_modules directory. Any other scenario where for
        // example the lock file is regenerated will lead to another execution before the task is "up-to-date" because
        // the lock file is both an input and an output of the task.
        retainedMetadataFileNames = mutableSetOf("${projectDir}/package.json")
        if (Files.exists(Path(lockFilePath))) {
            retainedMetadataFileNames.add(lockFilePath)
        }
        outputs.file(lockFilePath).withPropertyName("lockFile")
    }
    inputs.files(retainedMetadataFileNames).withPropertyName("metadataFiles")
    outputs.dir("${projectDir}/node_modules").withPropertyName("nodeModulesDirectory")
}