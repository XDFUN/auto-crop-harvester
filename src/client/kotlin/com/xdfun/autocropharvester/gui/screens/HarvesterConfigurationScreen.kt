package com.xdfun.autocropharvester.gui.screens

import com.xdfun.autocropharvester.configuration.manager.HarvesterConfigurationManager
import com.xdfun.autocropharvester.gui.SimpleOptionFactory
import com.xdfun.autocropharvester.utils.TranslationKeys
import com.xdfun.autocropharvester.viewmodels.EnableDisableStatus
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.screen.option.GameOptionsScreen
import net.minecraft.client.gui.tooltip.Tooltip
import net.minecraft.client.option.GameOptions
import net.minecraft.text.Text
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HarvesterConfigurationScreen(parent: Screen, gameOptions: GameOptions) :
    GameOptionsScreen(parent, gameOptions, Title), KoinComponent {
    private val _configuration: HarvesterConfigurationManager by inject()

    companion object {
        val Title: Text = Text.translatable(TranslationKeys.Screens.HARVESTER_CONFIGURATION)
    }

    override fun addOptions() {
        super.body!!.addAll(
            SimpleOptionFactory.getEnableDisableOption(
                TranslationKeys.Options.HarvesterConfiguration.ENABLE_AUTO_HARVEST,
                EnableDisableStatus.Enable,
                EnableDisableStatus.fromBoolean(_configuration.enableAutoHarvest)
            ) { value: EnableDisableStatus ->
                _configuration.enableAutoHarvest = value.toBoolean()
            },
            SimpleOptionFactory.getEnableDisableOption(
                TranslationKeys.Options.HarvesterConfiguration.ENABLE_AUTO_PLANT,
                EnableDisableStatus.Enable,
                EnableDisableStatus.fromBoolean(_configuration.enableAutoPlant)
            ) { value: EnableDisableStatus ->
                _configuration.enableAutoPlant = value.toBoolean()
            },
            SimpleOptionFactory.getEnableDisableOption(
                TranslationKeys.Options.HarvesterConfiguration.ENABLE_PREMATURE_AUTO_PLANT,
                { _ -> Tooltip.of(Text.translatable(TranslationKeys.Options.HarvesterConfiguration.ENABLE_PREMATURE_AUTO_PLANT_TOOLTIP)) },
                EnableDisableStatus.Disable,
                EnableDisableStatus.fromBoolean(_configuration.enablePrematureAutoHarvest)
            ) { value: EnableDisableStatus ->
                _configuration.enablePrematureAutoHarvest = value.toBoolean()
            },
            SimpleOptionFactory.getEnableDisableOption(
                TranslationKeys.Options.HarvesterConfiguration.ENABLE_SNEAK_AUTO_PLANT,
                EnableDisableStatus.Disable,
                EnableDisableStatus.fromBoolean(_configuration.enableSneakAutoHarvest)
            ) { value: EnableDisableStatus ->
                _configuration.enableSneakAutoHarvest = value.toBoolean()
            },
            SimpleOptionFactory.get128RadiusSliderOption(
                TranslationKeys.Options.HarvesterConfiguration.AUTO_HARVEST_RADIUS,
                0.0,
                _configuration.autoHarvestRadius
            ) {
                _configuration.autoHarvestRadius = it
            })
    }
}