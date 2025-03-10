package com.xdfun.autocropharvester.configuration.manager

import com.xdfun.autocropharvester.configuration.PlayerConfiguration
import com.xdfun.autocropharvester.configuration.events.PlayerConfigurationChangedCallback
import com.xdfun.autocropharvester.configuration.initial.InitialPlayerConfiguration

class PlayerConfigurationManager private constructor() :
    PlayerConfiguration {
    companion object {
        var Instance = PlayerConfigurationManager()
    }

    private var _configuration: MutableConfiguration = MutableConfiguration(InitialPlayerConfiguration())

    fun initialize(configuration: PlayerConfiguration) {
        _configuration = MutableConfiguration(configuration)

        invokeConfigurationChangedCallback()
    }

    override var enableAutoPlant: Boolean
        get() = _configuration.enableAutoPlant
        set(value) {
            _configuration.enableAutoPlant = value
            invokeConfigurationChangedCallback()
        }

    override var enablePrematureAutoPlant: Boolean
        get() = _configuration.enablePrematureAutoPlant
        set(value) {
            _configuration.enablePrematureAutoPlant = value
            invokeConfigurationChangedCallback()
        }

    private fun invokeConfigurationChangedCallback() {
        PlayerConfigurationChangedCallback.Event.invoker().onConfigurationChanged(_configuration)
    }

    private class MutableConfiguration(configuration: PlayerConfiguration) : PlayerConfiguration {
        override var enableAutoPlant: Boolean = configuration.enableAutoPlant
        override var enablePrematureAutoPlant: Boolean = configuration.enablePrematureAutoPlant
    }
}