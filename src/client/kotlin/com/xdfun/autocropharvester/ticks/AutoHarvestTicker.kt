package com.xdfun.autocropharvester.ticks

import com.xdfun.autocropharvester.blocks.MaturableBlock
import com.xdfun.autocropharvester.blocks.OffsetBlock
import com.xdfun.autocropharvester.planter.HarvesterAutoPlanter
import com.xdfun.autocropharvester.configuration.HarvesterConfiguration
import com.xdfun.autocropharvester.configuration.events.HarvesterConfigurationChangedCallback
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.minecraft.block.BlockState
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.world.ClientWorld
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import org.slf4j.Logger
import kotlin.math.abs

class AutoHarvestTicker(configuration: HarvesterConfiguration, logger: Logger) : ClientTickEvents.StartTick,
    HarvesterConfigurationChangedCallback {
    private val _logger = logger
    private var _configuration: HarvesterConfiguration = configuration

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

    override fun onConfigurationChanged(configuration: HarvesterConfiguration) {
        _configuration = configuration
    }

    private fun harvest(
        client: MinecraftClient,
        player: ClientPlayerEntity,
        world: ClientWorld,
        configuration: HarvesterConfiguration
    ) {
        if (canSneakAutoHarvest(configuration, player).not()) {
            _logger.debug("Auto harvest skipped. Player is sneaking and sneak auto harvest is disabled.")
            return
        }

        var absAutoHarvestRadius = abs(configuration.autoHarvestRadius)

        if (player.blockInteractionRange < absAutoHarvestRadius) {
            absAutoHarvestRadius = player.blockInteractionRange
        }

        val centerX = player.blockPos.x
        val centerZ = player.blockPos.z
        val centerY = player.blockPos.y

        val radius = absAutoHarvestRadius.toInt()
        val diameter = absAutoHarvestRadius * absAutoHarvestRadius

        val minX = centerX - radius
        val maxX = centerX + radius
        val minZ = centerZ - radius
        val maxZ = centerZ + radius

        for (x in minX..maxX) {
            for (z in minZ..maxZ) {
                val dx = (x + 0.5) - player.x
                val dz = (z + 0.5) - player.z
                if (dx * dx + dz * dz <= diameter) {
                    val blockPos = BlockPos(x, centerY, z)
                    harvestBlockPos(blockPos, client, player, world, configuration)
                }
            }
        }
    }

    private fun harvestBlockPos(
        blockPos: BlockPos,
        client: MinecraftClient,
        player: ClientPlayerEntity,
        world: ClientWorld,
        configuration: HarvesterConfiguration
    ) {
        val cropBlockState = getCropBlockState(blockPos, world)

        if (cropBlockState.block is MaturableBlock) {
            harvestMaturableBlock(client, player, configuration, cropBlockState, blockPos)
        }
    }

    private fun canSneakAutoHarvest(configuration: HarvesterConfiguration, player: ClientPlayerEntity): Boolean {
        return configuration.enableSneakAutoHarvest || player.isSneaking.not()
    }

    private fun getCropBlockState(blockPos: BlockPos, currentWorld: ClientWorld): BlockState {
        return currentWorld.getBlockState(blockPos)
    }

    private fun logHarvesting(blockPos: BlockPos) {
        _logger.debug("Harvesting crop at {}", blockPos)
    }

    private fun canHarvestCropBlock(
        configuration: HarvesterConfiguration,
        cropBlock: MaturableBlock,
        cropBlockState: BlockState,
        blockPos: BlockPos
    ): Boolean {
        return cropBlock.hasCrop(cropBlockState, blockPos)
                && (configuration.enablePrematureAutoHarvest
                || cropBlock.isMature(cropBlockState, blockPos))
    }

    private fun harvestMaturableBlock(
        client: MinecraftClient,
        player: ClientPlayerEntity,
        configuration: HarvesterConfiguration,
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

            HarvesterAutoPlanter.Instance?.notifyBlockBreakRequest(replantBlockState.block.asItem(), attackBlockPos)
            val direction = getDirectionFromHit(player.eyePos, attackBlockPos)
            _logger.trace("Direction: {}", direction)

            if (client.interactionManager?.attackBlock(attackBlockPos, direction) == true) {
                _logger.trace("Could attack block")
            }
        }
    }

    private fun getDirectionFromHit(eyePos: Vec3d, blockPos: BlockPos): Direction {
        val blockVec3d = Vec3d(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble())

        val direction = blockVec3d.subtract(eyePos).normalize()

        val absX = abs(direction.x)
        val absY = abs(direction.y)
        val absZ = abs(direction.z)

        return when {
            absX >= absY && absX >= absZ -> {
                if (direction.x > 0) Direction.EAST else Direction.WEST
            }

            absY >= absX && absY >= absZ -> {
                if (direction.y > 0) Direction.UP else Direction.DOWN
            }

            else -> {
                if (direction.z > 0) Direction.SOUTH else Direction.NORTH
            }
        }
    }
}