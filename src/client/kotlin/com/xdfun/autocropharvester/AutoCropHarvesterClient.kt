package com.xdfun.autocropharvester

import com.xdfun.autocropharvester.configuration.ConfigurationChangedCallback
import com.xdfun.autocropharvester.configuration.ConfigurationManager
import com.xdfun.autocropharvester.configuration.JsonFileConfiguration
import com.xdfun.autocropharvester.ticks.AutoHarvestTicker
import com.xdfun.autocropharvester.utils.ModIdUtils
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import org.slf4j.LoggerFactory

object AutoCropHarvesterClient : ClientModInitializer {
	private val logger = LoggerFactory.getLogger(ModIdUtils.modId)
	override fun onInitializeClient() {
		logger.error("Client mod initialization")

		val configuration = JsonFileConfiguration.INSTANCE.load()

		val harvestTicker = AutoHarvestTicker(configuration)

		ConfigurationChangedCallback.EVENT.register(harvestTicker)

		ConfigurationManager.INSTANCE.initialize(configuration)

		ClientTickEvents.START_CLIENT_TICK.register(harvestTicker)
	}
}