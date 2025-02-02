package com.xdfun.autocropharvester.configuration

class DefaultConfiguration(
    override val enableAutoHarvest: Boolean = Configuration.ENABLE_AUTO_HARVEST,
    override val enableSneakAutoHarvest: Boolean = Configuration.ENABLE_SNEAK_AUTO_HARVEST,
    override val enablePrematureAutoHarvest: Boolean = Configuration.ENABLE_PREMATURE_AUTO_HARVEST,
    override val enableAutoPlant: Boolean = Configuration.ENABLE_AUTO_PLANT,
) : Configuration