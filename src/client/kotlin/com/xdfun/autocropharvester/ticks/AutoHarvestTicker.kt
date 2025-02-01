package com.xdfun.autocropharvester.ticks

import com.xdfun.autocropharvester.configuration.Configuration
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.client.MinecraftClient

class AutoHarvestTicker(private val configuration: Configuration) : ClientTickEvents.StartTick {
    override fun onStartTick(client: MinecraftClient?) {
        if(!configuration.enableAutoHarvest) {
            return
        }
    }
}