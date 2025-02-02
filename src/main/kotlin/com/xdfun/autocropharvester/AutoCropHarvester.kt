package com.xdfun.autocropharvester

import com.xdfun.autocropharvester.configuration.ConfigurationChangedCallback
import com.xdfun.autocropharvester.configuration.JsonFileConfiguration
import net.fabricmc.api.ModInitializer

object AutoCropHarvester : ModInitializer {
	override fun onInitialize() {
		ConfigurationChangedCallback.EVENT.register(JsonFileConfiguration.INSTANCE)
	}
}