package com.xdfun.autocropharvester.viewmodels

enum class EnableDisableStatus {
    Enable,
    Disable;

    companion object {
        fun fromBoolean(value: Boolean?): EnableDisableStatus {
            return if (value == true) Enable else Disable
        }
    }

    fun toBoolean(): Boolean {
        return when (this) {
            Enable -> true
            Disable -> false
        }
    }
}