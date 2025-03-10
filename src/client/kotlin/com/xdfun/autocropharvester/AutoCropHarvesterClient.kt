package com.xdfun.autocropharvester

import com.xdfun.autocropharvester.planter.HarvesterAutoPlanter
import com.xdfun.autocropharvester.commands.AutoCropHarvesterCommands
import com.xdfun.autocropharvester.configuration.events.HarvesterConfigurationChangedCallback
import com.xdfun.autocropharvester.configuration.events.PlayerConfigurationChangedCallback
import com.xdfun.autocropharvester.configuration.manager.ConfigurationManager
import com.xdfun.autocropharvester.configuration.json.JsonFileConfiguration
import com.xdfun.autocropharvester.events.BlockUpdateCallback
import com.xdfun.autocropharvester.planter.PlayerAutoPlanter
import com.xdfun.autocropharvester.ticks.AutoHarvestTicker
import com.xdfun.autocropharvester.utils.ModIdUtils
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import org.slf4j.LoggerFactory

object AutoCropHarvesterClient : ClientModInitializer {
    private val _logger = LoggerFactory.getLogger(ModIdUtils.MOD_ID)

    override fun onInitializeClient() {
        _logger.info("Initializing Auto Crop Harvester")

        val configuration = JsonFileConfiguration.Instance.load()

        val harvestTicker = AutoHarvestTicker(configuration.harvesterConfiguration, _logger)
        val harvesterAutoPlanter = HarvesterAutoPlanter(configuration.harvesterConfiguration, _logger)
        val playerAutoPlanter = PlayerAutoPlanter(configuration.playerConfiguration, _logger)

        HarvesterConfigurationChangedCallback.Event.register(harvestTicker)
        HarvesterConfigurationChangedCallback.Event.register(harvesterAutoPlanter)
        PlayerConfigurationChangedCallback.Event.register(playerAutoPlanter)

        BlockUpdateCallback.Event.register(harvesterAutoPlanter)
        BlockUpdateCallback.Event.register(playerAutoPlanter)

        ConfigurationManager.Instance.initialize(configuration)

        AutoCropHarvesterCommands.registerCommands()

        ClientTickEvents.START_CLIENT_TICK.register(harvestTicker)
    }
}