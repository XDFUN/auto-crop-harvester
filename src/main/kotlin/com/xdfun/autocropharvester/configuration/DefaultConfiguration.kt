package com.xdfun.autocropharvester.configuration

class DefaultConfiguration(
    override val enableAutoHarvest: Boolean = Configuration.ENABLE_AUTO_HARVEST,
    override val enableSneakAutoHarvest: Boolean = Configuration.ENABLE_SNEAK_AUTO_HARVEST,
    override val enablePrematureAutoHarvest: Boolean = Configuration.ENABLE_PREMATURE_AUTO_HARVEST,
    override val enableAutoPlant: Boolean = Configuration.ENABLE_AUTO_PLANT,
    override val autoHarvestRadius: Double = Configuration.AUTO_HARVEST_RADIUS,
    override val enablePlayerAutoPlant: Boolean = Configuration.ENABLE_PLAYER_AUTO_PLANT,
    override val enablePrematureAutoPlant: Boolean = Configuration.ENABLE_PREMATURE_AUTO_PLANT
) : Configuration