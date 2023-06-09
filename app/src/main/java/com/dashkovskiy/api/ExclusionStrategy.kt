package com.dashkovskiy.api

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes

annotation class ExcludeField

class ExclusionStrategyImpl : ExclusionStrategy {

    override fun shouldSkipField(f: FieldAttributes?) =
        f?.getAnnotation(ExcludeField::class.java) != null

    override fun shouldSkipClass(clazz: Class<*>?) = false

}