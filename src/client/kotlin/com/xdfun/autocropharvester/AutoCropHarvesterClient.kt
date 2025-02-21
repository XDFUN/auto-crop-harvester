package com.xdfun.autocropharvester

import com.xdfun.autocropharvester.callbacks.AutoPlanter
import com.xdfun.autocropharvester.commands.AutoCropHarvesterCommands
import com.xdfun.autocropharvester.configuration.ConfigurationChangedCallback
import com.xdfun.autocropharvester.configuration.ConfigurationManager
import com.xdfun.autocropharvester.configuration.JsonFileConfiguration
import com.xdfun.autocropharvester.ticks.AutoHarvestTicker
import com.xdfun.autocropharvester.utils.ModIdUtils
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import org.slf4j.LoggerFactory

object AutoCropHarvesterClient : ClientModInitializer {
    private val _logger = LoggerFactory.getLogger(ModIdUtils.MOD_ID)

    override fun onInitializeClient() {
        _logger.info("Initializing Auto Crop Harvester")

        val configuration = JsonFileConfiguration.INSTANCE.load()

        val harvestTicker = AutoHarvestTicker(configuration, _logger)
        val autoPlanter = AutoPlanter(configuration, _logger)

        ConfigurationChangedCallback.EVENT.register(harvestTicker)
        ConfigurationChangedCallback.EVENT.register(autoPlanter)

        ConfigurationManager.Instance.initialize(configuration)

        AutoCropHarvesterCommands.registerCommands()

        ClientTickEvents.START_CLIENT_TICK.register(harvestTicker)
    }
}