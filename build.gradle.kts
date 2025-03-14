import me.modmuss50.mpp.PublishModTask

plugins {
    id("fabric-loom") version "1.9-SNAPSHOT"
    `maven-publish`
    kotlin("jvm") version "2.1.0"
    kotlin("plugin.serialization") version "2.1.10"
    id("me.modmuss50.mod-publish-plugin") version "0.8.4"
}

var modVersion: String =
    providers.environmentVariable("MOD_VERSION").orNull ?: project.properties["mod_version"]?.toString()
    ?: throw IllegalStateException("mod_version must be set")

val modVersionType = providers.environmentVariable("MOD_VERSION_TYPE").orNull
val modVersionIteration = providers.environmentVariable("MOD_VERSION_ITERATION").orNull

if (modVersionType != null) {
    modVersion = "$modVersion-$modVersionType$modVersionIteration"
}

version = modVersion
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

    if (!mavenUrl.isNullOrEmpty()) {
        repositories.maven {
            url = uri(mavenUrl)

            val mavenUsername = project.providers.environmentVariable("MAVEN_USERNAME").orNull

            if (!mavenUsername.isNullOrEmpty()) {
                credentials(PasswordCredentials::class) {
                    username = mavenUsername
                    password = project.providers.environmentVariable("MAVEN_PASSWORD").toString()
                }
            }
        }
    }
}

tasks.withType<PublishModTask>().configureEach {
    dependsOn(tasks.remapJar)
}

publishMods {
    val modFile = tasks.remapJar.orNull?.archiveFile?.orNull?.asFile
        ?: throw IllegalStateException("Could not retrieve file from task 'remapJar'. ")
    file = modFile
    changelog = providers.environmentVariable("CHANGELOG").getOrElse("No changelog provided")

    if (modVersionType != null) {
        type.set(STABLE)
        if (modVersionType.equals("alpha", true)) {
            type.set(ALPHA)
        }

        if (modVersionType.equals("beta", true)) {
            type.set(BETA)
        }
    }

    modLoaders.add("fabric")

    val curseforgeAccessToken: String? = providers.environmentVariable("CURSEFORGE_API_KEY").orNull

    val modrinthToken = project.providers.environmentVariable("MODRINTH_TOKEN").orNull

    val githubToken: String? = providers.environmentVariable("GITHUB_TOKEN").orNull

    curseforge {
        dryRun = curseforgeAccessToken.isNullOrEmpty()
        accessToken = curseforgeAccessToken
        projectId = "1208878"
        minecraftVersions.add(minecraftVersion)

        clientRequired = true

        requires("fabric-api", "fabric-language-kotlin")
    }

    modrinth {
        dryRun = modrinthToken.isNullOrEmpty()
        accessToken = modrinthToken
        projectId = "h6cMODvx"
        minecraftVersions.add(minecraftVersion)

        requires("fabric-api")

        requires {
            slug = "fabric-language-kotlin"
            version = "476dzMG5"
        }
    }

    github {
        dryRun = githubToken.isNullOrEmpty()
        accessToken = githubToken
        repository = providers.environmentVariable("GITHUB_REPOSITORY").getOrElse("AutoCropHarvester/dryrun")
        commitish = providers.environmentVariable("GITHUB_REF_NAME").getOrElse("dryrun")
    }
}