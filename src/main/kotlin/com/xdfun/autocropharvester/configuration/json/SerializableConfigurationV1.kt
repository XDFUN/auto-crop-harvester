package com.xdfun.autocropharvester.configuration.json

import com.xdfun.autocropharvester.configuration.Configuration
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("v1.0.0")
data class SerializableConfigurationV1(
    val enableAutoHarvest: Boolean = true,
    val enableSneakAutoHarvest: Boolean = false,
    val enablePrematureAutoHarvest: Boolean = false,
    val enableAutoPlant: Boolean = true,
    val autoHarvestRadius: Double = 1.0,
    val enablePlayerAutoPlant: Boolean = true,
    val enablePrematureAutoPlant: Boolean = false
) : SerializableConfiguration {
    override fun convertToConfiguration(): Configuration {
        return SerializableConfigurationV2(
            SerializableConfigurationV2.SerializableHarvesterConfigurationV1(
                enableAutoHarvest = enableAutoHarvest,
                enableSneakAutoHarvest = enableSneakAutoHarvest,
                enablePrematureAutoHarvest = enablePrematureAutoHarvest,
                enableAutoPlant = enableAutoPlant,
                autoHarvestRadius = autoHarvestRadius,
            ),
            SerializableConfigurationV2.SerializablePlayerConfigurationV1(
                enableAutoPlant = enablePlayerAutoPlant,
                enablePrematureAutoPlant = enablePrematureAutoPlant,
            )
        )
    }
}