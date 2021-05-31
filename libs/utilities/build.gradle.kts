import utils.Versions

plugins {
    id("webpb.library")
}

dependencies {
    annotationProcessor("org.projectlombok:lombok:${Versions.lombok}")
    compileOnly("org.projectlombok:lombok:${Versions.lombok}")
    implementation("com.google.protobuf:protobuf-java:${Versions.protobufJava}")
    implementation("org.apache.commons:commons-lang3:${Versions.commonsLang3}")
    implementation(project(":libs:commons"))
    testAnnotationProcessor("org.projectlombok:lombok:${Versions.lombok}")
    testCompileOnly("org.projectlombok:lombok:${Versions.lombok}")
}

tasks.javadoc {
    enabled = false
}
