package com.xdfun.autocropharvester.configuration

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.util.ActionResult


interface ConfigurationChangedCallback {
    companion object {
        var EVENT: Event<ConfigurationChangedCallback> = EventFactory.createArrayBacked(
            ConfigurationChangedCallback::class.java
        ) { listeners ->
            object : ConfigurationChangedCallback {
                override fun interact(configuration: Configuration): ActionResult? {
                    for (listener in listeners) {
                        val result: ActionResult? = listener.interact(configuration)

                        if (result !== ActionResult.PASS) {
                            return result
                        }
                    }
                    return ActionResult.PASS
                }
            }
        }
    }

    fun interact(configuration: Configuration): ActionResult?
}