package com.xdfun.autocropharvester

import com.xdfun.autocropharvester.callbacks.AutoPlanter
import com.xdfun.autocropharvester.configuration.ConfigurationChangedCallback
import com.xdfun.autocropharvester.configuration.ConfigurationManager
import com.xdfun.autocropharvester.configuration.JsonFileConfiguration
import com.xdfun.autocropharvester.ticks.AutoHarvestTicker
import com.xdfun.autocropharvester.utils.ModIdUtils
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import org.slf4j.LoggerFactory

object AutoCropHarvesterClient : ClientModInitializer {
	private val logger = LoggerFactory.getLogger(ModIdUtils.modId)
	override fun onInitializeClient() {
		logger.error("Client mod initialization")

		val configuration = JsonFileConfiguration.INSTANCE.load()

		val harvestTicker = AutoHarvestTicker(configuration)
		val autoPlanter = AutoPlanter(configuration)

		ConfigurationChangedCallback.EVENT.register(harvestTicker)
		ConfigurationChangedCallback.EVENT.register(autoPlanter)

		ConfigurationManager.INSTANCE.initialize(configuration)

		ClientTickEvents.START_CLIENT_TICK.register(harvestTicker)
		PlayerBlockBreakEvents.AFTER.register(autoPlanter)
	}
}