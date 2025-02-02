package com.xdfun.autocropharvester.configuration

import com.xdfun.autocropharvester.utils.ModIdUtils
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import net.fabricmc.loader.api.FabricLoader
import java.io.File
import java.nio.file.Path

class JsonFileConfiguration private constructor() : ConfigurationChangedCallback {
    companion object {
        private val path: Path = FabricLoader.getInstance().configDir
        private val fileName = ModIdUtils.modId + ".json"
        private val configFile: File = path.resolve(fileName).toFile()
        private val module = SerializersModule {
            polymorphic(
                SerializableConfiguration::class,
                SerializableConfigurationV1::class,
                SerializableConfigurationV1.serializer()
            )
        }

        private val jsonSerializer: Json = Json {
            prettyPrint = true
            encodeDefaults = true
            serializersModule = module
        }

        val INSTANCE: JsonFileConfiguration = JsonFileConfiguration()
    }

    fun load(): Configuration {
        if (configFile.exists().not()) {
            return SerializableConfigurationV1()
        }

        val config = jsonSerializer.decodeFromString<SerializableConfiguration>(configFile.readText())

        return config.convertToConfiguration()
    }

    override fun interact(configuration: Configuration) {
        val fileContent =
            jsonSerializer.encodeToString<SerializableConfiguration>(getSerializableConfiguration(configuration))

        configFile.writeText(fileContent)
    }

    private fun getSerializableConfiguration(configuration: Configuration): SerializableConfiguration {
        val config = SerializableConfigurationV1(configuration)

        @Suppress("USELESS_IS_CHECK")
        // Ensure that the config implements Configuration, else it could result in unexpected behavior when saving
        if (config !is Configuration) {
            throw IllegalStateException("The current configuration type must always implement ${Configuration::class.qualifiedName}!")
        }

        return config
    }

    private interface SerializableConfiguration {
        fun convertToConfiguration(): Configuration
    }

    @Serializable
    @SerialName("v1.0.0")
    private data class SerializableConfigurationV1(
        override val enableAutoHarvest: Boolean = Configuration.ENABLE_AUTO_HARVEST,
        override val enableSneakAutoHarvest: Boolean = Configuration.ENABLE_SNEAK_AUTO_HARVEST,
        override val enablePrematureAutoHarvest: Boolean = Configuration.ENABLE_PREMATURE_AUTO_HARVEST,
        override val enableAutoPlant: Boolean = Configuration.ENABLE_AUTO_PLANT,
    ) : SerializableConfiguration, Configuration {
        constructor(configuration: Configuration) : this(
            configuration.enableAutoHarvest,
            configuration.enableSneakAutoHarvest,
            configuration.enablePrematureAutoHarvest,
            configuration.enableAutoPlant
        )

        override fun convertToConfiguration(): Configuration {
            return this
        }
    }
}