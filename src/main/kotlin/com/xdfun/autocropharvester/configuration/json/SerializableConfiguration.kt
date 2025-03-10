package com.xdfun.autocropharvester.configuration.json

import com.xdfun.autocropharvester.configuration.Configuration

interface SerializableConfiguration {
    fun convertToConfiguration(): Configuration
}