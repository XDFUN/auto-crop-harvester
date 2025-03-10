package com.xdfun.autocropharvester.configuration.initial

import com.xdfun.autocropharvester.configuration.PlayerConfiguration

data class InitialPlayerConfiguration(
    override val enableAutoPlant: Boolean = PlayerConfiguration.ENABLE_AUTO_PLANT,
    override val enablePrematureAutoPlant: Boolean = PlayerConfiguration.ENABLE_PREMATURE_AUTO_PLANT
) : PlayerConfiguration