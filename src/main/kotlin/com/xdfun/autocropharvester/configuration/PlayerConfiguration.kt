package com.xdfun.autocropharvester.configuration

interface PlayerConfiguration {
    companion object DEFAULT {
        val ENABLE_AUTO_PLANT: Boolean = true
        val ENABLE_PREMATURE_AUTO_PLANT = true
    }

    val enableAutoPlant: Boolean
    val enablePrematureAutoPlant: Boolean
}