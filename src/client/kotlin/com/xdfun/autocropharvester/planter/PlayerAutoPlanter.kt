package com.xdfun.autocropharvester.planter

import com.xdfun.autocropharvester.configuration.PlayerConfiguration
import com.xdfun.autocropharvester.configuration.events.PlayerConfigurationChangedCallback
import com.xdfun.autocropharvester.events.BlockUpdateCallback
import net.minecraft.block.BlockState
import net.minecraft.block.Blocks
import net.minecraft.client.MinecraftClient
import net.minecraft.item.Item
import net.minecraft.util.math.BlockPos
import org.slf4j.Logger

class PlayerAutoPlanter(configuration: PlayerConfiguration, logger: Logger) : AutoPlanter(logger),
    PlayerConfigurationChangedCallback, BlockUpdateCallback {

    companion object {
        var Instance: PlayerAutoPlanter? = null
            private set
    }

    private val _logger: Logger = logger
    private var _configuration: PlayerConfiguration = configuration
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

    override fun onBlockUpdate(blockPos: BlockPos, blockState: BlockState) {
        val configuration = _configuration

        if (configuration.enableAutoPlant.not()) {
            _logger.trace("Auto plant is disabled.")
            return
        }

        if (blockState.block != Blocks.AIR) {
            return
        }

        if (!_requestBlockBreaks.containsKey(blockPos)) {
            _logger.trace("Not found break at: {}", blockPos)
            return
        }

        val seedItem = _requestBlockBreaks.remove(blockPos)

        if (seedItem == null) {
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

        plant(client, interactionManager, player, seedItem, blockPos)
    }

    override fun onConfigurationChanged(configuration: PlayerConfiguration) {
        _configuration = configuration
    }
}