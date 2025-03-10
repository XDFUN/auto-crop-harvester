package com.xdfun.autocropharvester.configuration.events

import com.xdfun.autocropharvester.configuration.PlayerConfiguration
import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory

interface PlayerConfigurationChangedCallback {
    companion object {
        var Event: Event<PlayerConfigurationChangedCallback> = EventFactory.createArrayBacked(
            PlayerConfigurationChangedCallback::class.java
        ) { listeners ->
            object : PlayerConfigurationChangedCallback {
                override fun onConfigurationChanged(configuration: PlayerConfiguration) {
                    for (listener in listeners) {
                        listener.onConfigurationChanged(configuration)
                    }
                }
            }
        }
    }

    fun onConfigurationChanged(configuration: PlayerConfiguration)
}