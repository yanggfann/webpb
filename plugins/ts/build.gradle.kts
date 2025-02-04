import utils.signAndPublish

plugins {
    id("java.application")
}

dependencies {
    implementation(project(":libs:commons"))
    implementation(project(":libs:utilities"))
    testImplementation(project(":libs:tests"))
}

val filename = "protoc-webpb-${project.name}"

tasks.bootJar {
    archiveBaseName.set(filename)
    launchScript()
}

signAndPublish(filename) {
    artifact(tasks.bootJar.get()) { classifier = "all" }
    pom {
        description.set("The webpb protoc plugin for TypeScript")
    }
}
