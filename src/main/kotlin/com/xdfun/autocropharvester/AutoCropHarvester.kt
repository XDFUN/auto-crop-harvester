package com.xdfun.autocropharvester

import com.xdfun.autocropharvester.configuration.ConfigurationChangedCallback
import com.xdfun.autocropharvester.configuration.JsonFileConfiguration
import com.xdfun.autocropharvester.utils.ModIdUtils
import net.fabricmc.api.ModInitializer
import org.slf4j.LoggerFactory

object AutoCropHarvester : ModInitializer {
    private val logger = LoggerFactory.getLogger(ModIdUtils.modId)

	override fun onInitialize() {
		ConfigurationChangedCallback.EVENT.register(JsonFileConfiguration.INSTANCE)

		val configuration = JsonFileConfiguration.INSTANCE.load()
		ConfigurationChangedCallback.EVENT.invoker().interact(configuration)
	}
}