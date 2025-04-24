plugins {
    id("java")
}

group = "iuh.fit"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}


dependencies {
    implementation(files("libs/SoloSmart-1.0-SNAPSHOT.jar"))

    implementation ("com.formdev:flatlaf:3.2.5")
    implementation ("org.apache.logging.log4j:log4j-core:2.20.0")
    implementation ("org.apache.logging.log4j:log4j-api:2.20.0")
    implementation ("org.apache.poi:poi:5.2.3")
    implementation ("org.apache.poi:poi-ooxml:5.2.3")
    implementation("org.hibernate:hibernate-core:7.0.0.Beta1")
    implementation("org.glassfish.jaxb:jaxb-runtime:4.0.5")
    implementation ("com.toedter:jcalendar:1.4")
    // https://mvnrepository.com/artifact/com.microsoft.sqlserver/mssql-jdbc
    implementation ("com.microsoft.sqlserver:mssql-jdbc:12.3.0.jre20-preview")
    testImplementation ("junit:junit:4.13.1")
    implementation(files("SoloSmart-1.0-SNAPSHOT.jar"))
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    // https://mvnrepository.com/artifact/org.projectlombok/lombok
    compileOnly ("org.projectlombok:lombok:1.18.36")
    annotationProcessor("org.projectlombok:lombok:1.18.36")
    // https://mvnrepository.com/artifact/net.datafaker/datafaker
    implementation ("net.datafaker:datafaker:2.4.2")

    // build.gradle (Gradle)
    testImplementation ("org.mockito:mockito-core:4.6.1")
    implementation ("javax.persistence:javax.persistence-api:2.2")

    // Jakarta Persistence API
    implementation ("jakarta.persistence:jakarta.persistence-api:3.1.0")

    // Hibernate Core
    implementation ("org.hibernate:hibernate-core:6.2.0.Final")

    // H2 Database
    implementation ("com.h2database:h2:2.2.224")

    // https://mvnrepository.com/artifact/com.miglayout/miglayout-swing
    implementation("com.miglayout:miglayout-swing:5.0")

    implementation(files("libs/swing-jnafilechooser.jar"));
    implementation(files("libs/jna-5.5.0.jar"));
}

tasks.test {
    useJUnitPlatform()
}