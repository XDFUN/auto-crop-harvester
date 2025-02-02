package com.xdfun.autocropharvester.configuration

interface Configuration {
    companion object DEFAULT {
        val ENABLE_AUTO_HARVEST: Boolean = true
        val ENABLE_SNEAK_AUTO_HARVEST: Boolean = false
        val ENABLE_PREMATURE_AUTO_HARVEST: Boolean = false
        val ENABLE_AUTO_PLANT: Boolean = true
    }

    val enableAutoHarvest: Boolean
    val enableSneakAutoHarvest: Boolean
    val enablePrematureAutoHarvest: Boolean
    val enableAutoPlant: Boolean
}