package com.xdfun.autocropharvester

import com.xdfun.autocropharvester.commands.AutoCropHarvesterCommands
import com.xdfun.autocropharvester.configuration.Configuration
import com.xdfun.autocropharvester.configuration.HarvesterConfiguration
import com.xdfun.autocropharvester.configuration.PlayerConfiguration
import com.xdfun.autocropharvester.configuration.events.*
import com.xdfun.autocropharvester.configuration.json.JsonFileConfiguration
import com.xdfun.autocropharvester.configuration.manager.ConfigurationManager
import com.xdfun.autocropharvester.configuration.manager.HarvesterConfigurationManager
import com.xdfun.autocropharvester.configuration.manager.PlayerConfigurationManager
import com.xdfun.autocropharvester.events.BlockUpdateCallback
import com.xdfun.autocropharvester.planter.HarvesterAutoPlanter
import com.xdfun.autocropharvester.planter.PlayerAutoPlanter
import com.xdfun.autocropharvester.gui.screens.ConfigurationScreen
import com.xdfun.autocropharvester.ticks.AutoHarvestTicker
import com.xdfun.autocropharvester.utils.ModIdUtils
import com.xdfun.autocropharvester.utils.TranslationKeys
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents
import net.minecraft.client.MinecraftClient
import net.minecraft.client.option.KeyBinding
import net.minecraft.client.util.InputUtil
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module
import org.lwjgl.glfw.GLFW
import org.slf4j.LoggerFactory

object AutoCropHarvesterClient : ClientModInitializer {
    private val _logger = LoggerFactory.getLogger(ModIdUtils.MOD_ID)

    override fun onInitializeClient() {
        _logger.info("Initializing Auto Crop Harvester")

        val koinApp = registerServices()

        registerEvents(koinApp)

        AutoCropHarvesterCommands.registerCommands()

        createKeybindings()

        koinApp.koin.getAll<ClientTickEvents.StartTick>().forEach {
            ClientTickEvents.START_CLIENT_TICK.register(it)
        }
    }

    private fun registerServices(): KoinApplication {
        return startKoin {
            modules(module {
                single { _logger }

                single { JsonFileConfiguration.Instance } withOptions {
                    bind<ConfigurationChangedCallback>()
                }

                single {
                    val configLoader = get<JsonFileConfiguration>()

                    ConfigurationManager.Instance.initialize(configLoader.load())
                    ConfigurationManager.Instance
                } withOptions {
                    bind<Configuration>()
                }

                single<PlayerConfiguration> { get<Configuration>().playerConfiguration }
                single<HarvesterConfiguration> { get<Configuration>().harvesterConfiguration }

                single<PlayerConfigurationManager> { PlayerConfigurationManager.Instance }
                single<HarvesterConfigurationManager> { HarvesterConfigurationManager.Instance }

                singleOf(::AutoHarvestTicker) withOptions {
                    bind<HarvesterConfigurationChangedCallback>()
                    bind<ClientTickEvents.StartTick>()
                }
                singleOf(::HarvesterAutoPlanter) withOptions {
                    bind<HarvesterConfigurationChangedCallback>()
                    bind<BlockUpdateCallback>()
                }
                singleOf(::PlayerAutoPlanter) withOptions {
                    bind<PlayerConfigurationChangedCallback>()
                    bind<BlockUpdateCallback>()
                }
            })
        }
    }

    private fun registerEvents(koinApp: KoinApplication) {
        koinApp.koin.getAll<ConfigurationChangedCallback>().forEach {
            ConfigurationChangedCallback.Event.register(it)
        }

        koinApp.koin.getAll<HarvesterConfigurationChangedCallback>().forEach {
            HarvesterConfigurationChangedCallback.Event.register(it)
        }

        koinApp.koin.getAll<PlayerConfigurationChangedCallback>().forEach {
            PlayerConfigurationChangedCallback.Event.register(it)
        }

        koinApp.koin.getAll<BlockUpdateCallback>().forEach {
            BlockUpdateCallback.Event.register(it)
        }

        koinApp.koin.getAll<ClientPlayConnectionEvents.Join>().forEach {
            ClientPlayConnectionEvents.JOIN.register(it)
        }

        koinApp.koin.getAll<ClientPlayConnectionEvents.Disconnect>().forEach {
            ClientPlayConnectionEvents.DISCONNECT.register(it)
        }
    }

    private fun createKeybindings() {
        val keyBinding = KeyBindingHelper.registerKeyBinding(
            KeyBinding(
                TranslationKeys.KeyBindings.OPEN_CONFIGURATION,
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                TranslationKeys.KeyBindings.CATEGORY,
            )
        )

        ClientTickEvents.END_CLIENT_TICK.register(ClientTickEvents.EndTick { client: MinecraftClient ->
            while (keyBinding.wasPressed()) {
                client.setScreen(ConfigurationScreen(client.currentScreen))
            }
        })
    }
}