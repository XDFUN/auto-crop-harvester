package com.xdfun.autocropharvester.di

interface ServiceDescriptor {
    fun get(serviceProvider: ServiceProvider): Any
}