package com.xdfun.autocropharvester.ticks

import com.xdfun.autocropharvester.blocks.MaturableBlock
import com.xdfun.autocropharvester.blocks.OffsetBlock
import com.xdfun.autocropharvester.callbacks.AutoPlanter
import com.xdfun.autocropharvester.configuration.Configuration
import com.xdfun.autocropharvester.configuration.ConfigurationChangedCallback
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import org.slf4j.Logger

class AutoHarvestTicker(configuration: Configuration, logger: Logger) : ClientTickEvents.StartTick,
    ConfigurationChangedCallback {
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

        if (client.interactionManager == null) {
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

    private fun harvest(
        client: MinecraftClient,
        player: ClientPlayerEntity,
        world: ClientWorld,
        configuration: Configuration
    ) {
        if (canSneakAutoHarvest(configuration, player).not()) {
            _logger.debug("Auto harvest skipped. Player is sneaking and sneak auto harvest is disabled.")
            return
        }

        val cropPos = player.blockPos
        val cropBlockState = getCropBlockState(cropPos, world)

        if (cropBlockState.block is MaturableBlock) {
            harvestMaturableBlock(client, player, configuration, cropBlockState, cropPos)
        }
    }

    private fun canSneakAutoHarvest(configuration: Configuration, player: ClientPlayerEntity): Boolean {
        return configuration.enableSneakAutoHarvest || player.isSneaking.not()
    }

    private fun getCropBlockState(blockPos: BlockPos, currentWorld: ClientWorld): BlockState {
        return currentWorld.getBlockState(blockPos)
    }

    private fun logHarvesting(blockPos: BlockPos) {
        _logger.debug("Harvesting crop at {}", blockPos)
    }

    private fun canHarvestCropBlock(
        configuration: Configuration,
        cropBlock: MaturableBlock,
        cropBlockState: BlockState,
        blockPos: BlockPos
    ): Boolean {
        return configuration.enablePrematureAutoHarvest || cropBlock.isMature(cropBlockState, blockPos)
    }

    private fun harvestMaturableBlock(
        client: MinecraftClient,
        player: ClientPlayerEntity,
        configuration: Configuration,
        blockState: BlockState,
        blockPos: BlockPos
    ) {
        val cropBlock: MaturableBlock = blockState.block as MaturableBlock

        if (canHarvestCropBlock(configuration, cropBlock, blockState, blockPos)) {
            logHarvesting(blockPos)

            var attackBlockPos = blockPos

            if (blockState.block is OffsetBlock) {
                attackBlockPos = (blockState.block as OffsetBlock).offsetAttack(blockState, blockPos)
            }

            val world = client.world
            var replantBlockState = blockState

            if (world != null) {
                replantBlockState = world.getBlockState(attackBlockPos)
            }

            AutoPlanter.INSTANCE?.notifyBlockBreakRequest(replantBlockState.block.asItem(), attackBlockPos)

            client.interactionManager?.attackBlock(attackBlockPos, Direction.getFacing(player.eyePos))
        }
    }
}