package com.xdfun.autocropharvester.configuration

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory

interface ConfigurationChangedCallback {
    companion object {
        var EVENT: Event<ConfigurationChangedCallback> = EventFactory.createArrayBacked(
            ConfigurationChangedCallback::class.java
        ) { listeners ->
            object : ConfigurationChangedCallback {
                override fun onConfigurationChanged(configuration: Configuration) {
                    for (listener in listeners) {
                        listener.onConfigurationChanged(configuration)
                    }
                }
            }
        }
    }

    fun onConfigurationChanged(configuration: Configuration)
}