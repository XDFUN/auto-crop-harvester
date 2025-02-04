package com.xdfun.autocropharvester.callbacks

import com.xdfun.autocropharvester.configuration.Configuration
import com.xdfun.autocropharvester.configuration.ConfigurationChangedCallback
import com.xdfun.autocropharvester.utils.ModIdUtils
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents
import net.minecraft.block.BlockState
import net.minecraft.block.PlantBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.network.ClientPlayerInteractionManager
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import org.slf4j.LoggerFactory

class AutoPlanter(configuration: Configuration) : PlayerBlockBreakEvents.After, ConfigurationChangedCallback {
    private val _logger = LoggerFactory.getLogger(ModIdUtils.modId)
    private var _configuration: Configuration = configuration

    override fun afterBlockBreak(
        world: World?,
        player: PlayerEntity?,
        blockPos: BlockPos?,
        blockState: BlockState?,
        blockEntity: BlockEntity?
    ) {
        if (blockPos == null || blockState == null || blockState.block !is PlantBlock) {
            return
        }
        val configuration = _configuration

        if (configuration.enableAutoPlant.not()) {
            return
        }

        val client = MinecraftClient.getInstance()

        val clientWorld = client.world
        val clientPlayer = client.player
        val interactionManager = client.interactionManager

        if (clientWorld == null || clientPlayer == null || interactionManager == null) {
            return
        }

        plant(interactionManager, clientPlayer, configuration, blockState.block as PlantBlock, blockPos)
    }

    override fun interact(configuration: Configuration) {
        _configuration = configuration
    }

    private fun plant(
        interactionManager: ClientPlayerInteractionManager,
        player: ClientPlayerEntity,
        configuration: Configuration,
        block: PlantBlock,
        blockPos: BlockPos
    ) {
        val inventory = player.inventory

        if (inventory.mainHandStack.isEmpty.not() && inventory.mainHandStack.item == block.asItem()) {
            plantMainHand(interactionManager, player, blockPos)
        }

        if (inventory.offHand.isEmpty().not()) {
            val offHandStack = inventory.offHand.firstOrNull { it.item == block.asItem() }

            if (offHandStack != null) {
                interactionManager.interactBlock(
                    player,
                    Hand.OFF_HAND,
                    BlockHitResult(player.pos, Direction.DOWN, blockPos, false)
                )
            }
        }

        val foundSeed = inventory.main.firstOrNull { it.item == block.asItem() }

        if(foundSeed == null) {
            return
        }

        if (tryHotbarSelect(inventory, interactionManager, player, blockPos, foundSeed)) {
            return
        }
    }

    private fun tryHotbarSelect(
        inventory: PlayerInventory,
        interactionManager: ClientPlayerInteractionManager,
        player: ClientPlayerEntity,
        blockPos: BlockPos?,
        foundSeed: ItemStack
    ): Boolean {
        val originalSeedSlot = inventory.getSlotWithStack(foundSeed)

        if (originalSeedSlot > PlayerInventory.HOTBAR_SIZE) {
            return false
        }

        val hotbarSlot = inventory.selectedSlot
        inventory.selectedSlot = originalSeedSlot
        plantMainHand(interactionManager, player, blockPos)
        inventory.selectedSlot = hotbarSlot
        return true
    }

    private fun plantMainHand(
        interactionManager: ClientPlayerInteractionManager,
        player: ClientPlayerEntity,
        blockPos: BlockPos?
    ) {
        interactionManager.interactBlock(
            player,
            Hand.MAIN_HAND,
            BlockHitResult(player.pos, Direction.DOWN, blockPos, false)
        )
    }
}