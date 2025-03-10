package com.xdfun.autocropharvester.commands

import com.mojang.brigadier.arguments.BoolArgumentType
import com.mojang.brigadier.arguments.DoubleArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.xdfun.autocropharvester.configuration.HarvesterConfiguration
import com.xdfun.autocropharvester.configuration.manager.HarvesterConfigurationManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.text.Text

object HarvesterConfigurationCommands {
    fun registerCommands(rootBuilder: LiteralArgumentBuilder<FabricClientCommandSource>): LiteralArgumentBuilder<FabricClientCommandSource> {
        val harvesterLiteral = ClientCommandManager.literal("harvester")
            .then(getAutoHarvestCommands())
            .then(getSneakAutoHarvestCommands())
            .then(getPrematureAutoHarvestCommands())
            .then(getAutoPlantCommands())
            .then(getAutoHarvestRadiusCommands())

        return rootBuilder.then(harvesterLiteral)
    }

    private fun getAutoHarvestCommands(): LiteralArgumentBuilder<FabricClientCommandSource> {
        val setAutoHarvestCommand =
            ClientCommandManager.argument("value", BoolArgumentType.bool()).executes { context ->
                HarvesterConfigurationManager.Instance.enableAutoHarvest = BoolArgumentType.getBool(context, "value")
                1
            }

        val getAutoHarvest = ClientCommandManager.literal("get").executes { context ->
            context.source.sendFeedback(Text.literal(HarvesterConfigurationManager.Instance.enableAutoHarvest.toString()))
            1
        }

        val setAutoHarvest = ClientCommandManager.literal("set").then(setAutoHarvestCommand)

        val resetAutoHarvest = ClientCommandManager.literal("reset").executes {
            HarvesterConfigurationManager.Instance.enableAutoHarvest = HarvesterConfiguration.ENABLE_AUTO_HARVEST
            1
        }

        return ClientCommandManager.literal("auto_harvest").then(setAutoHarvest).then(getAutoHarvest)
            .then(resetAutoHarvest)
    }

    private fun getSneakAutoHarvestCommands(): LiteralArgumentBuilder<FabricClientCommandSource> {
        val setSneakAutoHarvestCommand =
            ClientCommandManager.argument("value", BoolArgumentType.bool()).executes { context ->
                HarvesterConfigurationManager.Instance.enableSneakAutoHarvest =
                    BoolArgumentType.getBool(context, "value")
                1
            }

        val getSneakAutoHarvest = ClientCommandManager.literal("get").executes { context ->
            context.source.sendFeedback(Text.literal(HarvesterConfigurationManager.Instance.enableSneakAutoHarvest.toString()))
            1
        }

        val setSneakAutoHarvest = ClientCommandManager.literal("set").then(setSneakAutoHarvestCommand)

        val resetSneakAutoHarvest = ClientCommandManager.literal("reset").executes {
            HarvesterConfigurationManager.Instance.enableSneakAutoHarvest =
                HarvesterConfiguration.ENABLE_SNEAK_AUTO_HARVEST
            1
        }

        return ClientCommandManager.literal("sneak_auto_harvest").then(setSneakAutoHarvest).then(getSneakAutoHarvest)
            .then(resetSneakAutoHarvest)
    }

    private fun getPrematureAutoHarvestCommands(): LiteralArgumentBuilder<FabricClientCommandSource> {
        val setPrematureAutoHarvestCommand =
            ClientCommandManager.argument("value", BoolArgumentType.bool()).executes { context ->
                HarvesterConfigurationManager.Instance.enablePrematureAutoHarvest =
                    BoolArgumentType.getBool(context, "value")
                1
            }

        val getPrematureAutoHarvest = ClientCommandManager.literal("get").executes { context ->
            context.source.sendFeedback(Text.literal(HarvesterConfigurationManager.Instance.enablePrematureAutoHarvest.toString()))
            1
        }

        val setPrematureAutoHarvest = ClientCommandManager.literal("set").then(setPrematureAutoHarvestCommand)

        val resetPrematureAutoHarvest = ClientCommandManager.literal("reset").executes {
            HarvesterConfigurationManager.Instance.enablePrematureAutoHarvest =
                HarvesterConfiguration.ENABLE_PREMATURE_AUTO_HARVEST
            1
        }

        return ClientCommandManager.literal("premature_auto_harvest").then(setPrematureAutoHarvest)
            .then(getPrematureAutoHarvest).then(resetPrematureAutoHarvest)
    }

    private fun getAutoPlantCommands(): LiteralArgumentBuilder<FabricClientCommandSource> {
        val setAutoPlantCommand =
            ClientCommandManager.argument("value", BoolArgumentType.bool()).executes { context ->
                HarvesterConfigurationManager.Instance.enableAutoPlant = BoolArgumentType.getBool(context, "value")
                1
            }

        val getAutoPlant = ClientCommandManager.literal("get").executes { context ->
            context.source.sendFeedback(Text.literal(HarvesterConfigurationManager.Instance.enableAutoPlant.toString()))
            1
        }

        val setAutoPlant = ClientCommandManager.literal("set").then(setAutoPlantCommand)

        val resetAutoPlant = ClientCommandManager.literal("reset").executes {
            HarvesterConfigurationManager.Instance.enableAutoPlant = HarvesterConfiguration.ENABLE_AUTO_PLANT
            1
        }

        return ClientCommandManager.literal("auto_plant").then(setAutoPlant).then(getAutoPlant).then(resetAutoPlant)
    }

    private fun getAutoHarvestRadiusCommands(): LiteralArgumentBuilder<FabricClientCommandSource> {
        val setAutoHarvestRadiusCommand =
            ClientCommandManager.argument("value", DoubleArgumentType.doubleArg(0.0, 4.5)).executes { context ->
                HarvesterConfigurationManager.Instance.autoHarvestRadius =
                    DoubleArgumentType.getDouble(context, "value")
                1
            }

        val getAutoHarvestRadius = ClientCommandManager.literal("get").executes { context ->
            context.source.sendFeedback(Text.literal(HarvesterConfigurationManager.Instance.autoHarvestRadius.toString()))
            1
        }

        val setAutoHarvestRadius = ClientCommandManager.literal("set").then(setAutoHarvestRadiusCommand)

        val resetAutoHarvestRadius = ClientCommandManager.literal("reset").executes {
            HarvesterConfigurationManager.Instance.autoHarvestRadius = HarvesterConfiguration.AUTO_HARVEST_RADIUS
            1
        }

        return ClientCommandManager.literal("auto_harvest_radius").then(setAutoHarvestRadius).then(getAutoHarvestRadius)
            .then(resetAutoHarvestRadius)
    }
}