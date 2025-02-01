package com.xdfun.autocropharvester.configuration

import com.xdfun.autocropharvester.utils.ModIdUtils
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import net.fabricmc.loader.api.FabricLoader
import net.minecraft.util.ActionResult
import java.io.File
import java.nio.file.Path

class JsonFileConfiguration private constructor() : ConfigurationChangedCallback {
    companion object {
        private val path: Path = FabricLoader.getInstance().configDir
        private val fileName = ModIdUtils.modId + ".json"
        private val configFile: File = path.resolve(fileName).toFile()
        private val jsonSerializer: Json = Json { prettyPrint = true }

        val INSTANCE: JsonFileConfiguration = JsonFileConfiguration()
    }

    fun load(): Configuration {
        if (configFile.exists().not()) {
            return DefaultConfiguration()
        }

        return Json.decodeFromString<SerializableConfiguration>(configFile.readText())
    }

    override fun interact(configuration: Configuration): ActionResult? {
        configFile.writeText(jsonSerializer.encodeToString(SerializableConfiguration(configuration)))
        return ActionResult.SUCCESS
    }

    @Serializable
    private data class SerializableConfiguration(
        override val enableAutoHarvest: Boolean,
        override val enableAutoPlant: Boolean
    ) : Configuration {
        constructor(configuration: Configuration) : this(configuration.enableAutoHarvest, configuration.enableAutoPlant)
    }
}