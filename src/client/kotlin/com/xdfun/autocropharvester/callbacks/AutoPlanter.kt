package com.xdfun.autocropharvester.callbacks

import com.xdfun.autocropharvester.configuration.Configuration
import com.xdfun.autocropharvester.configuration.ConfigurationChangedCallback
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayerEntity
import net.minecraft.client.network.ClientPlayerInteractionManager
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.screen.slot.SlotActionType
import net.minecraft.util.Hand
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import org.slf4j.Logger

class AutoPlanter(configuration: Configuration, logger: Logger) : ConfigurationChangedCallback {

    companion object {
        var Instance: AutoPlanter? = null
            private set
    }

    private val _logger: Logger = logger
    private var _configuration: Configuration = configuration
    private val _requestBlockBreaks: MutableMap<BlockPos, Item> = mutableMapOf()

    init {
        Instance = this
    }

    fun notifyBlockBreakRequest(seedItem: Item, blockPos: BlockPos) {
        if (_configuration.enableAutoPlant.not()) {
            _logger.info("Auto plant is disabled.")
            return
        }

        _logger.trace("Notified break at: {}", blockPos)
        _requestBlockBreaks[blockPos] = seedItem
    }

    fun onBlockUpdate(blockPos: BlockPos, blockState: BlockState) {
        val configuration = _configuration

        if (configuration.enableAutoPlant.not()) {
            _logger.trace("Auto plant is disabled.")
            return
        }

        if(blockState.block != Blocks.AIR) {
            return
        }

        if(!_requestBlockBreaks.containsKey(blockPos)) {
            _logger.trace("Not found break at: {}", blockPos)
            return
        }

        val seedItem = _requestBlockBreaks.remove(blockPos)

        if(seedItem == null) {
            _logger.trace("Could not remove block: {}", blockPos)
            return
        }

        _logger.trace("Block state {}", seedItem)

        val client = MinecraftClient.getInstance()
        val world = client?.world
        val player = client?.player
        val interactionManager = client.interactionManager

        if (world == null || player == null || interactionManager == null) {
            _logger.trace("Could not acquire client instance")
            return
        }

        plant(client, interactionManager, player, configuration, seedItem, blockPos)
    }

    override fun onConfigurationChanged(configuration: Configuration) {
        _configuration = configuration
    }

    private fun plant(
        client: MinecraftClient,
        interactionManager: ClientPlayerInteractionManager,
        player: ClientPlayerEntity,
        configuration: Configuration,
        seedItem: Item,
        blockPos: BlockPos
    ) {
        val inventory = player.inventory

        if (inventory.mainHandStack.isEmpty.not() && inventory.mainHandStack.item == seedItem) {
            _logger.trace("Seed is already selected, no swap")
            plantMainHand(interactionManager, player, blockPos)
            return
        }

        if (inventory.offHand.isEmpty().not()) {
            val offHandStack = inventory.offHand.firstOrNull { it.item == seedItem }

            if (offHandStack != null) {
                plantWithHand(interactionManager, player, blockPos, Hand.OFF_HAND)
                return
            }
        }

        val foundSeed = inventory.main.firstOrNull { it.item == seedItem }

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

        inventory.selectedSlot = inventory.swappableHotbarSlot

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
        plantWithHand(interactionManager, player, blockPos, Hand.MAIN_HAND)
    }

    private fun plantWithHand(
        interactionManager: ClientPlayerInteractionManager,
        player: ClientPlayerEntity,
        blockPos: BlockPos,
        hand: Hand
    ){
        val centerPos = blockPos.toCenterPos()
        _logger.trace("Plant with main hand at pos: {}", centerPos)
        val result = interactionManager.interactBlock(
            player,
            hand,
            BlockHitResult(centerPos, Direction.UP, blockPos, false)
        )
        _logger.debug("Plant result: {}", result)
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