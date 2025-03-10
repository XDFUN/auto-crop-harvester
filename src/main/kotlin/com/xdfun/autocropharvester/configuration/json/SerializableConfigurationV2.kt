package com.xdfun.autocropharvester.configuration.json

import com.xdfun.autocropharvester.configuration.Configuration
import com.xdfun.autocropharvester.configuration.HarvesterConfiguration
import com.xdfun.autocropharvester.configuration.PlayerConfiguration
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("v2.0.0")
data class SerializableConfigurationV2(
    override val harvesterConfiguration: SerializableHarvesterConfigurationV1 = SerializableHarvesterConfigurationV1(),
    override val playerConfiguration: SerializablePlayerConfigurationV1 = SerializablePlayerConfigurationV1(),
) : SerializableConfiguration, Configuration {

    constructor(configuration: Configuration) : this(
        SerializableHarvesterConfigurationV1(configuration.harvesterConfiguration),
        SerializablePlayerConfigurationV1(configuration.playerConfiguration)
    )

    @Serializable
    data class SerializableHarvesterConfigurationV1(
        override val enableAutoHarvest: Boolean = HarvesterConfiguration.ENABLE_AUTO_HARVEST,
        override val enableSneakAutoHarvest: Boolean = HarvesterConfiguration.ENABLE_SNEAK_AUTO_HARVEST,
        override val enablePrematureAutoHarvest: Boolean = HarvesterConfiguration.ENABLE_PREMATURE_AUTO_HARVEST,
        override val enableAutoPlant: Boolean = HarvesterConfiguration.ENABLE_AUTO_PLANT,
        override val autoHarvestRadius: Double = HarvesterConfiguration.AUTO_HARVEST_RADIUS,
    ) : HarvesterConfiguration {
        constructor(harvesterConfiguration: HarvesterConfiguration) : this(
            harvesterConfiguration.enableAutoHarvest,
            harvesterConfiguration.enableSneakAutoHarvest,
            harvesterConfiguration.enablePrematureAutoHarvest,
            harvesterConfiguration.enableAutoPlant,
            harvesterConfiguration.autoHarvestRadius,
        )
    }

    @Serializable
    data class SerializablePlayerConfigurationV1(
        override val enableAutoPlant: Boolean = PlayerConfiguration.ENABLE_AUTO_PLANT,
        override val enablePrematureAutoPlant: Boolean = PlayerConfiguration.ENABLE_PREMATURE_AUTO_PLANT
    ) : PlayerConfiguration {
        constructor(playerConfiguration: PlayerConfiguration) : this(
            playerConfiguration.enableAutoPlant,
            playerConfiguration.enablePrematureAutoPlant
        )
    }

    override fun convertToConfiguration(): Configuration {
        return this
    }
}
