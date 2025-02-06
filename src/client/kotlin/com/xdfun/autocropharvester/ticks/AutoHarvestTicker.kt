package com.xdfun.autocropharvester.ticks

import com.xdfun.autocropharvester.callbacks.AutoPlanter
import com.xdfun.autocropharvester.configuration.Configuration
import com.xdfun.autocropharvester.configuration.ConfigurationChangedCallback
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.block.BlockState
import net.minecraft.block.CropBlock
import net.minecraft.block.NetherWartBlock
import net.minecraft.block.SugarCaneBlock
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.world.ClientWorld
import net.minecraft.state.property.IntProperty
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import org.slf4j.Logger

class AutoHarvestTicker(configuration: Configuration, logger: Logger) : ClientTickEvents.StartTick, ConfigurationChangedCallback {
    private val _logger = logger
    private var _configuration: Configuration = configuration

    override fun onStartTick(client: MinecraftClient?) {
        val configuration = _configuration
        if (configuration.enableAutoHarvest.not()) {
            return
        }

        if (client == null) {
            return
        }

        if(client.interactionManager == null) {
            return
        }

        val player = client.player
        val world = client.world

        if (player != null && world != null) {
            harvest(client, player, world, configuration)
        }
    }

    override fun interact(configuration: Configuration) {
        _configuration = configuration
    }

    private fun harvest(client: MinecraftClient, player: ClientPlayerEntity, world: ClientWorld, configuration: Configuration) {
        if (canSneakAutoHarvest(configuration, player).not()) {
            _logger.debug("Auto harvest skipped. Player is sneaking and sneak auto harvest is disabled.")
            return
        }

        val cropPos = getCropPosition(player.blockPos)

        val cropBlockState = getCropBlockState(cropPos, world)

        when (cropBlockState.block) {
            is CropBlock -> harvestCropBlock(client, player, configuration, cropBlockState, cropPos)
            is NetherWartBlock -> harvestNetherWart(client, player, configuration, cropBlockState, cropPos)
            is SugarCaneBlock -> harvestSugarCane(client, player, configuration, cropBlockState, cropPos)
        }
    }

    private fun canSneakAutoHarvest(configuration: Configuration, player: ClientPlayerEntity): Boolean {
        return configuration.enableSneakAutoHarvest || player.isSneaking.not()
    }

    private fun getCropBlockState(blockPos: BlockPos, currentWorld: ClientWorld): BlockState {
        return currentWorld.getBlockState(blockPos)
    }

    private fun getCropPosition(blockPos: BlockPos): BlockPos {
        // when standing on farmland, the block below the player is dirt, the block at the position is the farmland and above the crop.
        return blockPos.up()
    }

    private fun logHarvesting(blockPos: BlockPos) {
        _logger.debug("Harvesting crop at {}", blockPos)
    }

    private fun canHarvestCropBlock(configuration: Configuration, cropBlock: CropBlock, cropBlockState: BlockState): Boolean {
        return configuration.enablePrematureAutoHarvest || cropBlock.isMature(cropBlockState)
    }

    private fun canHarvestAgingBlock(configuration: Configuration, cropBlockState: BlockState, ageProperty: IntProperty, maxAge: Int): Boolean {
        return configuration.enablePrematureAutoHarvest || (cropBlockState[ageProperty] == maxAge)
    }

    private fun harvestCropBlock(client: MinecraftClient, player: ClientPlayerEntity, configuration: Configuration, blockState: BlockState, blockPos: BlockPos) {
        val cropBlock: CropBlock = blockState.block as CropBlock

        if (canHarvestCropBlock(configuration, cropBlock, blockState)) {
            logHarvesting(blockPos)
            attackBlock(client, player, blockState, blockPos)
        }
    }

    private fun harvestNetherWart(client: MinecraftClient, player: ClientPlayerEntity, configuration: Configuration, blockState: BlockState, blockPos: BlockPos) {
        if (canHarvestAgingBlock(configuration, blockState, NetherWartBlock.AGE, NetherWartBlock.MAX_AGE)) {
            logHarvesting(blockPos)
            attackBlock(client, player, blockState, blockPos)
        }
    }

    private fun harvestSugarCane(client: MinecraftClient, player: ClientPlayerEntity, configuration: Configuration, blockState: BlockState, blockPos: BlockPos) {
        logHarvesting(blockPos)
        attackBlock(client, player, blockState, blockPos)
    }

    private fun attackBlock(client: MinecraftClient, player: ClientPlayerEntity, blockState: BlockState, blockPos: BlockPos) {
        AutoPlanter.INSTANCE?.notifyBlockBreakRequest(blockState, blockPos)

        client.interactionManager?.attackBlock(blockPos, Direction.getFacing(player.eyePos))
    }
}