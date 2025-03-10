package com.xdfun.autocropharvester.configuration.json

import com.xdfun.autocropharvester.configuration.Configuration
import com.xdfun.autocropharvester.configuration.events.ConfigurationChangedCallback
import com.xdfun.autocropharvester.utils.ModIdUtils
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import net.fabricmc.loader.api.FabricLoader
import org.slf4j.LoggerFactory
import java.io.File
import java.nio.file.Path

class JsonFileConfiguration private constructor() : ConfigurationChangedCallback {
    private val _logger = LoggerFactory.getLogger(ModIdUtils.MOD_ID)

    companion object {
        private val path: Path = FabricLoader.getInstance().configDir
        private const val FILE_NAME = ModIdUtils.MOD_ID + ".json"
        private val configFile: File = path.resolve(FILE_NAME).toFile()
        private val module: SerializersModule = SerializersModule {
            polymorphic(
                SerializableConfiguration::class,
                SerializableConfigurationV1::class,
                SerializableConfigurationV1.serializer()
            )
            polymorphic(
                SerializableConfiguration::class,
                SerializableConfigurationV2::class,
                SerializableConfigurationV2.serializer()
            )
        }

        private val jsonSerializer: Json = Json {
            prettyPrint = true
            encodeDefaults = true
            serializersModule = module
            ignoreUnknownKeys = true
        }

        val Instance: JsonFileConfiguration = JsonFileConfiguration()
    }

    fun load(): Configuration {
        if (configFile.exists().not()) {
            return SerializableConfigurationV2()
        }

        _logger.trace("Deserializing configuration from file")
        val config = jsonSerializer.decodeFromString<SerializableConfiguration>(configFile.readText())
        _logger.debug("Deserialized configuration from file")

        return config.convertToConfiguration()
    }

    override fun onConfigurationChanged(configuration: Configuration) {
        val fileContent =
            jsonSerializer.encodeToString<SerializableConfiguration>(getSerializableConfiguration(configuration))

        _logger.trace("Serializing configuration to file")
        configFile.writeText(fileContent)
    }

    private fun getSerializableConfiguration(configuration: Configuration): SerializableConfiguration {
        val config = SerializableConfigurationV2(configuration)

        @Suppress("USELESS_IS_CHECK")
        // Ensure that the config implements Configuration, else it could result in unexpected behavior when saving
        if (config !is Configuration) {
            throw IllegalStateException("The current configuration type must always implement ${Configuration::class.qualifiedName}!")
        }

        return config
    }
}