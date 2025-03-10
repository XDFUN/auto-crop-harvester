package com.xdfun.autocropharvester.configuration.manager

import com.xdfun.autocropharvester.configuration.Configuration
import com.xdfun.autocropharvester.configuration.HarvesterConfiguration
import com.xdfun.autocropharvester.configuration.PlayerConfiguration
import com.xdfun.autocropharvester.configuration.events.ConfigurationChangedCallback
import com.xdfun.autocropharvester.configuration.events.HarvesterConfigurationChangedCallback
import com.xdfun.autocropharvester.configuration.events.PlayerConfigurationChangedCallback
import com.xdfun.autocropharvester.configuration.initial.InitialConfiguration

class ConfigurationManager private constructor() : Configuration, PlayerConfigurationChangedCallback,
    HarvesterConfigurationChangedCallback {
    companion object {
        val Instance = ConfigurationManager()
    }

    private var _configuration: MutableConfiguration = MutableConfiguration(InitialConfiguration())
    private var _harvesterConfiguration: HarvesterConfigurationManager = HarvesterConfigurationManager.Instance
    private var _playerConfiguration: PlayerConfigurationManager = PlayerConfigurationManager.Instance

    init {
        HarvesterConfigurationChangedCallback.Event.register(this)
        PlayerConfigurationChangedCallback.Event.register(this)
    }

    override var harvesterConfiguration: HarvesterConfiguration
        get() = _harvesterConfiguration
        set(value) {
            // The configuration changed callback will be called by the harvester configuration changed callback
            _harvesterConfiguration.initialize(value)
        }

    override var playerConfiguration: PlayerConfiguration
        get() = _playerConfiguration
        set(value) {
            // The configuration changed callback will be called by the player configuration changed callback
            _playerConfiguration.initialize(value)
        }

    fun initialize(configuration: Configuration) {
        _configuration = MutableConfiguration(configuration)

        // TODO: Both initialize calls will trigger the configuration changed callback.
        //  This should preferably be changed to a single configuration changed callback,
        //  but since it is only during the init I will ignore it for now.
        _harvesterConfiguration.initialize(_configuration.harvesterConfiguration)
        _playerConfiguration.initialize(_configuration.playerConfiguration)
    }

    override fun onConfigurationChanged(configuration: HarvesterConfiguration) {
        _configuration.harvesterConfiguration = configuration
        invokeConfigurationChangedCallback()
    }

    override fun onConfigurationChanged(configuration: PlayerConfiguration) {
        _configuration.playerConfiguration = configuration
        invokeConfigurationChangedCallback()
    }

    private fun invokeConfigurationChangedCallback() {
        ConfigurationChangedCallback.Event.invoker().onConfigurationChanged(_configuration)
    }

    private class MutableConfiguration(configuration: Configuration) : Configuration {
        override var harvesterConfiguration: HarvesterConfiguration = configuration.harvesterConfiguration
        override var playerConfiguration: PlayerConfiguration = configuration.playerConfiguration
    }
}