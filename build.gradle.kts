plugins {
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("java")
}

val pluginName = "DailyFeatures"
val packageName = "ru.elementcraft.dailyfeatures"

repositories {
    mavenCentral()
    maven {
        name = "spigotmc-repo"
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven {
        name = "panda"
        url = uri("https://repo.panda-lang.org/releases/")
    }
    maven {
        name = "jitpack"
        url = uri("https://www.jitpack.io")
    }


}

dependencies {
    // Processor
    compileOnly("org.projectlombok:lombok:1.18.34")
    annotationProcessor("org.projectlombok:lombok:1.18.34")

    // Database
    implementation("com.h2database:h2:2.1.214")
    implementation("org.hibernate:hibernate-core:6.5.2.Final")

    // Minecraft
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    implementation("dev.rollczi:litecommands-bukkit:3.6.0")
    implementation("com.github.BoomEaro:MenuInv:1.5.10")


}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

tasks.shadowJar {
    archiveFileName.set("$pluginName.jar")

    exclude(
            "org/intellij/lang/annotations/**",
            "org/jetbrains/annotations/**"
    )


    listOf(
            "dev.rollczi.litecommands"/*,
            "org.glassfish.jaxb",
            "jakarta.xml.bind",
            "org.hibernate"*/
    ).forEach { relocate(it, "$packageName.libs.$it") }
}


tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
    options.release = 17
    options.encoding = "UTF-8"
}

