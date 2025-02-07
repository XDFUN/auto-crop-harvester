package com.xdfun.autocropharvester.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.xdfun.autocropharvester.configuration.Configuration
import com.xdfun.autocropharvester.configuration.ConfigurationManager
import com.xdfun.autocropharvester.utils.ModIdUtils
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess
import net.minecraft.text.Text

class AutoCropHarvesterCommands {
    companion object {
        fun registerCommands() {

            val configRoot = ClientCommandManager.literal("config")
                .then(getAutoHarvestCommands())
                .then(getSneakAutoHarvestCommands())
                .then(getPrematureAutoHarvestCommands())
                .then(getAutoPlantCommands())

            val commandRoot = ClientCommandManager.literal(ModIdUtils.MOD_ID).then(configRoot)

            ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback { dispatcher: CommandDispatcher<FabricClientCommandSource?>, _: CommandRegistryAccess? ->
                dispatcher.register(commandRoot)
            })
        }

        private fun getAutoHarvestCommands(): LiteralArgumentBuilder<FabricClientCommandSource> {
            val setAutoHarvestCommand =
                ClientCommandManager.argument("value", BoolArgumentType.bool()).executes { context ->
                    ConfigurationManager.INSTANCE.enableAutoHarvest = BoolArgumentType.getBool(context, "value")
                    1
                }

            val getAutoHarvest = ClientCommandManager.literal("get").executes { context ->
                context.source.sendFeedback(Text.literal(ConfigurationManager.INSTANCE.enableAutoHarvest.toString()))
                1
            }

            val setAutoHarvest = ClientCommandManager.literal("set").then(setAutoHarvestCommand)

            val resetAutoHarvest = ClientCommandManager.literal("reset").executes {
                ConfigurationManager.INSTANCE.enableAutoHarvest = Configuration.ENABLE_AUTO_HARVEST
                1
            }

            return ClientCommandManager.literal("auto_harvest").then(setAutoHarvest).then(getAutoHarvest).then(resetAutoHarvest)
        }

        private fun getSneakAutoHarvestCommands(): LiteralArgumentBuilder<FabricClientCommandSource> {
            val setSneakAutoHarvestCommand =
                ClientCommandManager.argument("value", BoolArgumentType.bool()).executes { context ->
                    ConfigurationManager.INSTANCE.enableSneakAutoHarvest = BoolArgumentType.getBool(context, "value")
                    1
                }

            val getSneakAutoHarvest = ClientCommandManager.literal("get").executes { context ->
                context.source.sendFeedback(Text.literal(ConfigurationManager.INSTANCE.enableAutoHarvest.toString()))
                1
            }

            val setSneakAutoHarvest = ClientCommandManager.literal("set").then(setSneakAutoHarvestCommand)

            val resetSneakAutoHarvest = ClientCommandManager.literal("reset").executes {
                ConfigurationManager.INSTANCE.enableSneakAutoHarvest = Configuration.ENABLE_SNEAK_AUTO_HARVEST
                1
            }

            return ClientCommandManager.literal("sneak_auto_harvest").then(setSneakAutoHarvest).then(getSneakAutoHarvest).then(resetSneakAutoHarvest)
        }

        private fun getPrematureAutoHarvestCommands(): LiteralArgumentBuilder<FabricClientCommandSource> {
            val setPrematureAutoHarvestCommand =
                ClientCommandManager.argument("value", BoolArgumentType.bool()).executes { context ->
                    ConfigurationManager.INSTANCE.enablePrematureAutoHarvest = BoolArgumentType.getBool(context, "value")
                    1
                }

            val getPrematureAutoHarvest = ClientCommandManager.literal("get").executes { context ->
                context.source.sendFeedback(Text.literal(ConfigurationManager.INSTANCE.enableAutoHarvest.toString()))
                1
            }

            val setPrematureAutoHarvest = ClientCommandManager.literal("set").then(setPrematureAutoHarvestCommand)

            val resetPrematureAutoHarvest = ClientCommandManager.literal("reset").executes {
                ConfigurationManager.INSTANCE.enablePrematureAutoHarvest = Configuration.ENABLE_PREMATURE_AUTO_HARVEST
                1
            }

            return ClientCommandManager.literal("premature_auto_harvest").then(setPrematureAutoHarvest).then(getPrematureAutoHarvest).then(resetPrematureAutoHarvest)
        }

        private fun getAutoPlantCommands(): LiteralArgumentBuilder<FabricClientCommandSource> {
            val setAutoPlantCommand =
                ClientCommandManager.argument("value", BoolArgumentType.bool()).executes { context ->
                    ConfigurationManager.INSTANCE.enableAutoPlant = BoolArgumentType.getBool(context, "value")
                    1
                }

            val getAutoPlant = ClientCommandManager.literal("get").executes { context ->
                context.source.sendFeedback(Text.literal(ConfigurationManager.INSTANCE.enableAutoHarvest.toString()))
                1
            }

            val setAutoPlant = ClientCommandManager.literal("set").then(setAutoPlantCommand)

            val resetAutoPlant = ClientCommandManager.literal("reset").executes {
                ConfigurationManager.INSTANCE.enablePrematureAutoHarvest = Configuration.ENABLE_PREMATURE_AUTO_HARVEST
                1
            }

            return ClientCommandManager.literal("auto_plant").then(setAutoPlant).then(getAutoPlant).then(resetAutoPlant)
        }
    }
}