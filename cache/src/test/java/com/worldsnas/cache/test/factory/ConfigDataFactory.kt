package com.worldsnas.cache.test.factory

import com.worldsnas.cache.model.Config

object ConfigDataFactory {

    fun makeCacheConfig() : Config{
        return Config(DataFactory.randomLong(), DataFactory.randomLong())
    }

}