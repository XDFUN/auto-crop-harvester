package com.xdfun.autocropharvester.commands

import com.mojang.brigadier.CommandDispatcher
import com.xdfun.autocropharvester.utils.ModIdUtils
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess

object AutoCropHarvesterCommands {
    fun registerCommands() {

        var configRoot = ClientCommandManager.literal("config")

        configRoot = HarvesterConfigurationCommands.registerCommands(configRoot)
        configRoot = PlayerConfigurationCommands.registerCommands(configRoot)

        val commandRoot = ClientCommandManager.literal(ModIdUtils.MOD_ID).then(configRoot)

        ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback { dispatcher: CommandDispatcher<FabricClientCommandSource?>, _: CommandRegistryAccess? ->
            dispatcher.register(commandRoot)
        })
    }
}