package com.xdfun.autocropharvester.configuration.events

import com.xdfun.autocropharvester.configuration.HarvesterConfiguration
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory

interface HarvesterConfigurationChangedCallback {
    companion object {
        var Event: Event<HarvesterConfigurationChangedCallback> = EventFactory.createArrayBacked(
            HarvesterConfigurationChangedCallback::class.java
        ) { listeners ->
            object : HarvesterConfigurationChangedCallback {
                override fun onConfigurationChanged(configuration: HarvesterConfiguration) {
                    for (listener in listeners) {
                        listener.onConfigurationChanged(configuration)
                    }
                }
            }
        }
    }

    fun onConfigurationChanged(configuration: HarvesterConfiguration)
}