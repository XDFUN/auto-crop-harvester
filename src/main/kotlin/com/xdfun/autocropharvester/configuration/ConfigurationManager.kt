package com.xdfun.autocropharvester.configuration

class ConfigurationManager private constructor() : Configuration {
    companion object {
        val Instance = ConfigurationManager()
    }

    private var _configuration: MutableConfiguration = MutableConfiguration(DefaultConfiguration())

    fun initialize(configuration: Configuration) {
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

    override var enablePlayerAutoPlant: Boolean
        get() = _configuration.enablePlayerAutoPlant
        set(value) {
            _configuration.enablePlayerAutoPlant = value
            invokeConfigurationChangedCallback()
        }

    override var enablePrematureAutoPlant: Boolean
        get() = _configuration.enablePrematureAutoPlant
        set(value) {
            _configuration.enablePrematureAutoPlant = value
            invokeConfigurationChangedCallback()
        }

    private fun invokeConfigurationChangedCallback() {
        ConfigurationChangedCallback.EVENT.invoker().onConfigurationChanged(_configuration)
    }

    private class MutableConfiguration(configuration: Configuration) : Configuration {
        override var enableAutoHarvest: Boolean = configuration.enableAutoHarvest
        override var enableSneakAutoHarvest: Boolean = configuration.enableSneakAutoHarvest
        override var enablePrematureAutoHarvest: Boolean = configuration.enablePrematureAutoHarvest
        override var enableAutoPlant: Boolean = configuration.enableAutoPlant
        override var autoHarvestRadius: Double = configuration.autoHarvestRadius
        override var enablePlayerAutoPlant: Boolean = configuration.enablePlayerAutoPlant
        override var enablePrematureAutoPlant: Boolean = configuration.enablePrematureAutoPlant
    }
}