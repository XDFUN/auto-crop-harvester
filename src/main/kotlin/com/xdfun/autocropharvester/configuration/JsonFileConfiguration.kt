package com.xdfun.autocropharvester.configuration

import com.xdfun.autocropharvester.utils.ModIdUtils
import net.fabricmc.loader.api.FabricLoader
import java.nio.file.Path

class JsonFileConfiguration : Configuration {
    companion object {
        private val path: Path = FabricLoader.getInstance().configDir
        private val fileName = ModIdUtils.modId + ".json"

        val INSTANCE: JsonFileConfiguration = JsonFileConfiguration()
    }

    private constructor() {
    }

    override var enableAutoHarvest: Boolean = false
    override var enableAutoPlant: Boolean = false
}