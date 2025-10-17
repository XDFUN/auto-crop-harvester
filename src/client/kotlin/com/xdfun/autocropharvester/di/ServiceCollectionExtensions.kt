package com.xdfun.autocropharvester.di

inline fun <reified T : Any> ServiceCollection.addSingleton(noinline factory: (ServiceProvider) -> T): ServiceCollection {
    addSingleton(T::class, factory)

    return this
}