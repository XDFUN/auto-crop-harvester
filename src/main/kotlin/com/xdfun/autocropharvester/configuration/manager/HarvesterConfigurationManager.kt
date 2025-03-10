package com.xdfun.autocropharvester.configuration.manager

import com.xdfun.autocropharvester.configuration.HarvesterConfiguration
import com.xdfun.autocropharvester.configuration.events.HarvesterConfigurationChangedCallback
import com.xdfun.autocropharvester.configuration.initial.InitialHarvesterConfiguration

class HarvesterConfigurationManager private constructor() :
    HarvesterConfiguration {
    companion object {
        var Instance = HarvesterConfigurationManager()
    }

    private var _configuration: MutableConfiguration = MutableConfiguration(InitialHarvesterConfiguration())

    fun initialize(configuration: HarvesterConfiguration) {
        _configuration = MutableConfiguration(configuration)

        invokeConfigurationChangedCallback()
    }

    override var enableAutoHarvest: Boolean
        get() = _configuration.enableAutoHarvest
        set(value) {
            _configuration.enableAutoHarvest = value
            invokeConfigurationChangedCallback()
        }

    override var enableSneakAutoHarvest: Boolean
        get() = _configuration.enableSneakAutoHarvest
        set(value) {
            _configuration.enableSneakAutoHarvest = value
            invokeConfigurationChangedCallback()
        }

    override var enablePrematureAutoHarvest: Boolean
        get() = _configuration.enablePrematureAutoHarvest
        set(value) {
            _configuration.enablePrematureAutoHarvest = value
            invokeConfigurationChangedCallback()
        }

    override var enableAutoPlant: Boolean
        get() = _configuration.enableAutoPlant
        set(value) {
            _configuration.enableAutoPlant = value
            invokeConfigurationChangedCallback()
        }

    override var autoHarvestRadius: Double
        get() = _configuration.autoHarvestRadius
        set(value) {
            _configuration.autoHarvestRadius = value
            invokeConfigurationChangedCallback()
        }

    private fun invokeConfigurationChangedCallback() {
        HarvesterConfigurationChangedCallback.Event.invoker().onConfigurationChanged(_configuration)
    }

    private class MutableConfiguration(configuration: HarvesterConfiguration) : HarvesterConfiguration {
        override var enableAutoHarvest: Boolean = configuration.enableAutoHarvest
        override var enableSneakAutoHarvest: Boolean = configuration.enableSneakAutoHarvest
        override var enablePrematureAutoHarvest: Boolean = configuration.enablePrematureAutoHarvest
        override var enableAutoPlant: Boolean = configuration.enableAutoPlant
        override var autoHarvestRadius: Double = configuration.autoHarvestRadius
    }
}