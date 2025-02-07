package com.xdfun.autocropharvester.callbacks

import com.xdfun.autocropharvester.configuration.Configuration
import com.xdfun.autocropharvester.configuration.ConfigurationChangedCallback
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.block.PlantBlock
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.network.ClientPlayerInteractionManager
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.ItemStack
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket
import net.minecraft.screen.slot.SlotActionType
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import org.slf4j.Logger

class AutoPlanter(configuration: Configuration, logger: Logger) : ConfigurationChangedCallback {

    companion object {
        var INSTANCE: AutoPlanter? = null
            private set
    }

    private val _logger: Logger = logger
    private var _configuration: Configuration = configuration
    private val _requestBlockBreaks: MutableMap<BlockPos, BlockState> = mutableMapOf()

    init {
        INSTANCE = this
    }

    fun notifyBlockBreakRequest(blockState: BlockState, blockPos: BlockPos) {
        if(blockState.block !is PlantBlock) {
            return
        }

        _logger.trace("Notified break at: {}", blockPos)
        _requestBlockBreaks[blockPos] = blockState
    }

    fun onBlockUpdate(packet: BlockUpdateS2CPacket) {
        val blockPos = packet.pos

        if(packet.state.block != Blocks.AIR) {
            return
        }

        if(!_requestBlockBreaks.containsKey(blockPos)) {
            _logger.trace("Not found break at: {}", blockPos)
            return
        }

        val blockState = _requestBlockBreaks.remove(blockPos) ?: return

        val configuration = _configuration

        if (configuration.enableAutoPlant.not()) {
            _logger.trace("Auto plant is disabled.")
            return
        }

        val client = MinecraftClient.getInstance()
        val world = client.world
        val player = client.player
        val interactionManager = client.interactionManager

        if (world == null || player == null || interactionManager == null) {
            _logger.trace("Could not acquire client instance")
            return
        }

        plant(client, interactionManager, player, configuration, blockState.block as PlantBlock, blockPos)
    }

    override fun interact(configuration: Configuration) {
        _configuration = configuration
    }

    private fun plant(
        client: MinecraftClient,
        interactionManager: ClientPlayerInteractionManager,
        player: ClientPlayerEntity,
        configuration: Configuration,
        block: PlantBlock,
        blockPos: BlockPos
    ) {
        val inventory = player.inventory

        if (inventory.mainHandStack.isEmpty.not() && inventory.mainHandStack.item == block.asItem()) {
            _logger.trace("Seed is already selected, no swap")
            plantMainHand(interactionManager, player, blockPos)
        }

        if (inventory.offHand.isEmpty().not()) {
            val offHandStack = inventory.offHand.firstOrNull { it.item == block.asItem() }

            if (offHandStack != null) {
                _logger.trace("Seed is in offhand, no swap")
                val result = interactionManager.interactBlock(
                    player,
                    Hand.OFF_HAND,
                    BlockHitResult(player.pos, Direction.UP, blockPos.down(), false)
                )
                _logger.trace("Off hand plant result: {}", result)

                return
            }
        }

        val foundSeed = inventory.main.firstOrNull { it.item == block.asItem() }

        if (foundSeed == null) {
            return
        }

        if (tryHotbarSelect(inventory, interactionManager, player, blockPos, foundSeed)) {
            return
        }

        _logger.trace("Trying to acquire seed from inventory")
        plantFromInventory(client, inventory, interactionManager, player, blockPos, foundSeed)
    }

    private fun tryHotbarSelect(
        inventory: PlayerInventory,
        interactionManager: ClientPlayerInteractionManager,
        player: ClientPlayerEntity,
        blockPos: BlockPos,
        foundSeed: ItemStack
    ): Boolean {
        _logger.trace("try find seed in hotbar")
        val originalSeedSlot = inventory.getSlotWithStack(foundSeed)

        if (originalSeedSlot >= PlayerInventory.HOTBAR_SIZE) {
            _logger.trace("Could not find seed in hotbar")
            return false
        }

        _logger.trace("Found seed in hotbar, changing selection and planting")
        val hotbarSlot = inventory.selectedSlot
        inventory.selectedSlot = originalSeedSlot
        plantMainHand(interactionManager, player, blockPos)
        inventory.selectedSlot = hotbarSlot
        return true
    }

    private fun plantFromInventory(
        client: MinecraftClient,
        inventory: PlayerInventory,
        interactionManager: ClientPlayerInteractionManager,
        player: ClientPlayerEntity,
        blockPos: BlockPos,
        foundSeed: ItemStack
    ) {
        val originalSelectedSlot = inventory.selectedSlot
        val originalSeedSlot = inventory.getSlotWithStack(foundSeed)

        inventory.selectedSlot = inventory.getSwappableHotbarSlot()

        _logger.trace("Swapping items hotbar: {} with inv: {}", inventory.main[inventory.selectedSlot], inventory.main[originalSeedSlot])
        swapItems(interactionManager, player, originalSeedSlot, inventory.selectedSlot)

        plantMainHand(interactionManager, player, blockPos)

        _logger.trace("Swapping items hotbar: {} with inv: {}", inventory.main[inventory.selectedSlot], inventory.main[originalSeedSlot])
        swapItems(interactionManager, player, originalSeedSlot, inventory.selectedSlot)

        inventory.selectedSlot = originalSelectedSlot
    }

    private fun plantMainHand(
        interactionManager: ClientPlayerInteractionManager,
        player: ClientPlayerEntity,
        blockPos: BlockPos
    ) {
        val underCrop = blockPos
        _logger.trace("Plant with main hand at pos: {}", underCrop)
        val result = interactionManager.interactBlock(
            player,
            Hand.MAIN_HAND,
            BlockHitResult(player.pos, Direction.UP, underCrop, false)
        )
        _logger.trace("Plant result: {}", result)
    }

    private fun swapItems(
        interactionManager: ClientPlayerInteractionManager,
        player: ClientPlayerEntity,
        fromSlot: Int,
        toSlot: Int
    ) {
        interactionManager.clickSlot(player.currentScreenHandler.syncId, fromSlot, toSlot, SlotActionType.SWAP, player)
    }
}