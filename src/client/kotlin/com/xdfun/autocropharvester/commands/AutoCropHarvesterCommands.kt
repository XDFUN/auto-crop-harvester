package com.xdfun.autocropharvester.commands

import com.mojang.brigadier.CommandDispatcher
import com.xdfun.autocropharvester.gui.screens.ConfigurationScreen
import com.xdfun.autocropharvester.utils.ModIdUtils
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.command.CommandRegistryAccess

object AutoCropHarvesterCommands {
    fun registerCommands() {
        val configRoot = ClientCommandManager.literal("config").then(ClientCommandManager.literal("open").executes {
            val client = it.getSource().client
            client.execute { client.setScreen(ConfigurationScreen(null)) }
            1
        })

        val commandRoot = ClientCommandManager.literal(ModIdUtils.MOD_ID).then(configRoot)

        ClientCommandRegistrationCallback.EVENT.register(ClientCommandRegistrationCallback { dispatcher: CommandDispatcher<FabricClientCommandSource?>, _: CommandRegistryAccess? ->
            dispatcher.register(commandRoot)
        })
    }
}