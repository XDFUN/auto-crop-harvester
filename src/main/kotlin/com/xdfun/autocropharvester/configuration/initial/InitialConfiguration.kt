package com.xdfun.autocropharvester.configuration.initial

import com.xdfun.autocropharvester.configuration.Configuration
import com.xdfun.autocropharvester.configuration.HarvesterConfiguration
import com.xdfun.autocropharvester.configuration.PlayerConfiguration

data class InitialConfiguration(
    override val harvesterConfiguration: HarvesterConfiguration = InitialHarvesterConfiguration(),
    override val playerConfiguration: PlayerConfiguration = InitialPlayerConfiguration()
) : Configuration