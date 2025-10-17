package com.xdfun.autocropharvester.gui.screens

import com.xdfun.autocropharvester.configuration.manager.PlayerConfigurationManager
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

class PlayerConfigurationScreen(parent: Screen, gameOptions: GameOptions) :
    GameOptionsScreen(parent, gameOptions, Title), KoinComponent {
    private val _configuration: PlayerConfigurationManager by inject()

    companion object {
        val Title: Text = Text.translatable(TranslationKeys.Screens.PLAYER_CONFIGURATION)
    }

    override fun addOptions() {
        super.body!!.addAll(
            SimpleOptionFactory.getEnableDisableOption(
                TranslationKeys.Options.PlayerConfiguration.ENABLE_AUTO_PLANT,
                EnableDisableStatus.Enable,
                EnableDisableStatus.fromBoolean(_configuration.enableAutoPlant)
            ) { value: EnableDisableStatus ->
                _configuration.enableAutoPlant = value.toBoolean()
            },
            SimpleOptionFactory.getEnableDisableOption(
                TranslationKeys.Options.PlayerConfiguration.ENABLE_PREMATURE_AUTO_PLANT,
                { _ -> Tooltip.of(Text.translatable(TranslationKeys.Options.PlayerConfiguration.ENABLE_PREMATURE_AUTO_PLANT_TOOLTIP)) },
                EnableDisableStatus.Disable,
                EnableDisableStatus.fromBoolean(_configuration.enablePrematureAutoPlant)
            ) { value: EnableDisableStatus ->
                _configuration.enablePrematureAutoPlant = value.toBoolean()
            })
    }
}