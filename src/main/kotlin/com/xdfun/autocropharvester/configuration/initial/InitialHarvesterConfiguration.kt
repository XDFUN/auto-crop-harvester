package com.xdfun.autocropharvester.configuration.initial

import com.xdfun.autocropharvester.configuration.HarvesterConfiguration

data class InitialHarvesterConfiguration(
    override val enableAutoHarvest: Boolean = HarvesterConfiguration.ENABLE_AUTO_HARVEST,
    override val enableSneakAutoHarvest: Boolean = HarvesterConfiguration.ENABLE_SNEAK_AUTO_HARVEST,
    override val enablePrematureAutoHarvest: Boolean = HarvesterConfiguration.ENABLE_PREMATURE_AUTO_HARVEST,
    override val enableAutoPlant: Boolean = HarvesterConfiguration.ENABLE_AUTO_PLANT,
    override val autoHarvestRadius: Double = HarvesterConfiguration.AUTO_HARVEST_RADIUS,
) : HarvesterConfiguration
