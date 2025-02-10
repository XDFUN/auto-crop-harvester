plugins {
    id("fabric-loom") version "1.9-SNAPSHOT"
    `maven-publish`
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.10"
    id("me.modmuss50.mod-publish-plugin") version "0.4.5"
}

version = providers.environmentVariable("mod_version").orNull ?: project.properties["mod_version"]
        ?: throw IllegalStateException("mod_version must be set")
project.version = version
group = project.properties["maven_group"] ?: throw IllegalStateException("maven_group must be set")

val minecraftVersion: String =
    project.properties["minecraft_version"]?.toString() ?: throw IllegalStateException("minecraft_version must be set")

base {
    archivesName = project.properties["archives_base_name"]?.toString()
        ?: throw IllegalStateException("archives_base_name must be set")
}

repositories {
    // Add repositories to retrieve artifacts from in here.
    // You should only use this when depending on other mods because
    // Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
    // See https://docs.gradle.org/current/userguide/declaring_repositories.html
    // for more information about repositories.
    mavenCentral()
}

loom {
    splitEnvironmentSourceSets()

    mods {
        create(base.archivesName.get()) {
            sourceSets {
                sourceSets.main
                sourceSets["client"]
            }
        }
    }
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    // To change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:${minecraftVersion}")
    mappings("net.fabricmc:yarn:${project.properties["yarn_mappings"]}:v2")
    modImplementation("net.fabricmc:fabric-loader:${project.properties["loader_version"]}")

    // Fabric API. This is technically optional, but you probably want it anyway.
    modImplementation("net.fabricmc.fabric-api:fabric-api:${project.properties["fabric_version"]}")
    modImplementation("net.fabricmc:fabric-language-kotlin:${project.properties["fabric_kotlin_version"]}")
}

tasks.processResources {
    inputs.property("version", project.version)

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

tasks.withType(JavaCompile::class.java).configureEach {
    options.release = 21
}

kotlin {
    jvmToolchain(21)
}

java {
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
    from("LICENSE") {
        rename {
            "${it}_${base.archivesName}"
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            artifact(base.archivesName)
            from(components["java"])
        }
    }

    val mavenUrl = project.providers.environmentVariable("MAVEN_URL").orNull

    if (mavenUrl != null) {
        repositories.maven {
            url = uri(mavenUrl)

            val mavenUsername = project.providers.environmentVariable("MAVEN_USERNAME").orNull

            if (mavenUsername != null) {
                credentials(PasswordCredentials::class) {
                    username = mavenUsername
                    password = project.providers.environmentVariable("MAVEN_PASSWORD").toString()
                }
            }
        }
    }
}

publishMods {
    file = tasks.remapJar.get().archiveFile.get().asFile
    changelog = providers.environmentVariable("CHANGELOG").getOrElse("No changelog provided")
    type.set(if (project.properties["prerelease"] == "true") BETA else STABLE)
    modLoaders.add("fabric")

    val curseforgeAccessToken: String? = null
    val curseforgeMinecraftVersion = providers.environmentVariable("curseforge_minecraft_version").get()

    val modrinthToken = project.providers.environmentVariable("MODRINTH_TOKEN").orNull

    val githubToken: String? = providers.environmentVariable("GITHUB_TOKEN").orNull

    // Currently not setup
//    curseforge {
//        dryRun = curseforgeAccessToken == null
//        accessToken = providers.environmentVariable("CURSEFORGE_API_KEY")
//        projectId = "306612"
//        minecraftVersions.add(curseforgeMinecraftVersion)
//    }

    modrinth {
        dryRun = modrinthToken == null
        accessToken = modrinthToken
        projectId = "h6cMODvx"
        minecraftVersions.add(minecraftVersion)
    }

    github {
        dryRun = githubToken == null
        accessToken = githubToken
        repository = providers.environmentVariable("GITHUB_REPOSITORY").getOrElse("AutoCropHarvester/dryrun")
        commitish = providers.environmentVariable("GITHUB_REF_NAME").getOrElse("dryrun")
    }
}