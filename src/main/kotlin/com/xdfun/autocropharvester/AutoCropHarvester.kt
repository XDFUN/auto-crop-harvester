package com.xdfun.autocropharvester

import com.xdfun.autocropharvester.configuration.events.ConfigurationChangedCallback
import com.xdfun.autocropharvester.configuration.json.JsonFileConfiguration
import net.fabricmc.api.ModInitializer

object AutoCropHarvester : ModInitializer {
	override fun onInitialize() {
		ConfigurationChangedCallback.Event.register(JsonFileConfiguration.Instance)
	}
}