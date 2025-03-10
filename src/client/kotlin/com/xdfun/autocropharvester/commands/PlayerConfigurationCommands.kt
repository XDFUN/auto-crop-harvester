package com.xdfun.autocropharvester.commands

import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.xdfun.autocropharvester.configuration.PlayerConfiguration
import com.xdfun.autocropharvester.configuration.manager.PlayerConfigurationManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object PlayerConfigurationCommands {
    fun registerCommands(rootBuilder: LiteralArgumentBuilder<FabricClientCommandSource>): LiteralArgumentBuilder<FabricClientCommandSource> {
        val playerLiteral = ClientCommandManager.literal("player")
            .then(getPlayerAutoPlantCommands())
            .then(getPrematureAutoPlantCommands())

        return rootBuilder.then(playerLiteral)
    }

    private fun getPlayerAutoPlantCommands(): LiteralArgumentBuilder<FabricClientCommandSource> {
        val setPlayerAutoPlantCommand =
            ClientCommandManager.argument("value", BoolArgumentType.bool()).executes { context ->
                PlayerConfigurationManager.Instance.enableAutoPlant = BoolArgumentType.getBool(context, "value")
                1
            }

        val getPlayerAutoPlant = ClientCommandManager.literal("get").executes { context ->
            context.source.sendFeedback(Text.literal(PlayerConfigurationManager.Instance.enableAutoPlant.toString()))
            1
        }

        val setPlayerAutoPlant = ClientCommandManager.literal("set").then(setPlayerAutoPlantCommand)

        val resetPrematureAutoPlant = ClientCommandManager.literal("reset").executes {
            PlayerConfigurationManager.Instance.enableAutoPlant = PlayerConfiguration.ENABLE_AUTO_PLANT
            1
        }

        return ClientCommandManager.literal("auto_plant").then(setPlayerAutoPlant).then(getPlayerAutoPlant)
            .then(resetPrematureAutoPlant)
    }

    private fun getPrematureAutoPlantCommands(): LiteralArgumentBuilder<FabricClientCommandSource> {
        val setPrematureAutoPlantCommand =
            ClientCommandManager.argument("value", BoolArgumentType.bool()).executes { context ->
                PlayerConfigurationManager.Instance.enablePrematureAutoPlant =
                    BoolArgumentType.getBool(context, "value")
                1
            }

        val getPrematureAutoPlant = ClientCommandManager.literal("get").executes { context ->
            context.source.sendFeedback(Text.literal(PlayerConfigurationManager.Instance.enablePrematureAutoPlant.toString()))
            1
        }

        val setPrematureAutoPlant = ClientCommandManager.literal("set").then(setPrematureAutoPlantCommand)

        val resetPrematureAutoPlant = ClientCommandManager.literal("reset").executes {
            PlayerConfigurationManager.Instance.enablePrematureAutoPlant =
                PlayerConfiguration.ENABLE_PREMATURE_AUTO_PLANT
            1
        }

        return ClientCommandManager.literal("premature_auto_plant").then(setPrematureAutoPlant)
            .then(getPrematureAutoPlant).then(resetPrematureAutoPlant)
    }
}