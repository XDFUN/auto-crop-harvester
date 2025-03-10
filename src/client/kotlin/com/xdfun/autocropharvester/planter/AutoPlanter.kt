package com.xdfun.autocropharvester.planter

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

abstract class AutoPlanter protected constructor(protected val logger: Logger) {
    protected open fun plant(
        client: MinecraftClient,
        interactionManager: ClientPlayerInteractionManager,
        player: ClientPlayerEntity,
        seedItem: Item,
        blockPos: BlockPos
    ) {
        val inventory = player.inventory

        if (inventory.mainHandStack.isEmpty.not() && inventory.mainHandStack.item == seedItem) {
            logger.trace("Seed is already selected, no swap")
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

        logger.trace("Trying to acquire seed from inventory")
        plantFromInventory(inventory, interactionManager, player, blockPos, foundSeed)
    }

    private fun tryHotbarSelect(
        inventory: PlayerInventory,
        interactionManager: ClientPlayerInteractionManager,
        player: ClientPlayerEntity,
        blockPos: BlockPos,
        foundSeed: ItemStack
    ): Boolean {
        logger.trace("try find seed in hotbar")
        val originalSeedSlot = inventory.getSlotWithStack(foundSeed)

        if (originalSeedSlot >= PlayerInventory.HOTBAR_SIZE) {
            logger.trace("Could not find seed in hotbar")
            return false
        }

        logger.trace("Found seed in hotbar, changing selection and planting")
        val hotbarSlot = inventory.selectedSlot
        inventory.selectedSlot = originalSeedSlot
        plantMainHand(interactionManager, player, blockPos)
        inventory.selectedSlot = hotbarSlot
        return true
    }

    private fun plantFromInventory(
        inventory: PlayerInventory,
        interactionManager: ClientPlayerInteractionManager,
        player: ClientPlayerEntity,
        blockPos: BlockPos,
        foundSeed: ItemStack
    ) {
        val originalSelectedSlot = inventory.selectedSlot
        val originalSeedSlot = inventory.getSlotWithStack(foundSeed)

        inventory.selectedSlot = inventory.swappableHotbarSlot

        logger.trace("Swapping items hotbar: {} with inv: {}", inventory.main[inventory.selectedSlot], inventory.main[originalSeedSlot])
        swapItems(interactionManager, player, originalSeedSlot, inventory.selectedSlot)

        plantMainHand(interactionManager, player, blockPos)

        logger.trace("Swapping items hotbar: {} with inv: {}", inventory.main[inventory.selectedSlot], inventory.main[originalSeedSlot])
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
        logger.trace("Plant with main hand at pos: {}", centerPos)
        val result = interactionManager.interactBlock(
            player,
            hand,
            BlockHitResult(centerPos, Direction.UP, blockPos, false)
        )
        logger.debug("Plant result: {}", result)
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