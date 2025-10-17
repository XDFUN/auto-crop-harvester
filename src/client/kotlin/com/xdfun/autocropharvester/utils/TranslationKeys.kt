package com.xdfun.autocropharvester.utils

object TranslationKeys {
    private const val MOD_TRANSLATION_ID = "auto_crop_harvester"
    object Screens {
        private const val SCREENS = "$MOD_TRANSLATION_ID.screens"

        const val CONFIGURATION = "$SCREENS.configuration"
        const val PLAYER_CONFIGURATION = "$SCREENS.player_configuration"
        const val HARVESTER_CONFIGURATION = "$SCREENS.harvester_configuration"
    }
    
    object KeyBindings {
        private const val KEY_BINDINGS = "$MOD_TRANSLATION_ID.keybindings"
        
        const val CATEGORY = "$KEY_BINDINGS.category"
        const val OPEN_CONFIGURATION = "$KEY_BINDINGS.open_config"
    }

    object EnableDisableStatus {
        private const val ENABLE_DISABLE_STATUS = "$MOD_TRANSLATION_ID.enable_disable_status"

        const val ENABLE = "$ENABLE_DISABLE_STATUS.enable"
        const val DISABLE = "$ENABLE_DISABLE_STATUS.disable"
        const val DEFAULT = "$ENABLE_DISABLE_STATUS.default"
    }

    object Options {
        private const val OPTIONS = "$MOD_TRANSLATION_ID.options"

        const val RESET = "$OPTIONS.reset"

        object Scopes {
            private const val SCOPES = "$OPTIONS.scopes"

            const val STATE = "$SCOPES.state"
        }

        object PlayerConfiguration {
            private const val PLAYER_CONFIGURATION = "$OPTIONS.player_configuration"

            const val ENABLE_AUTO_PLANT = "$PLAYER_CONFIGURATION.enable_auto_plant"
            const val ENABLE_PREMATURE_AUTO_PLANT = "$PLAYER_CONFIGURATION.enable_premature_auto_plant"
            const val ENABLE_PREMATURE_AUTO_PLANT_TOOLTIP = "$PLAYER_CONFIGURATION.enable_premature_auto_plant.tooltip"
        }

        object HarvesterConfiguration {
            private const val HARVESTER_CONFIGURATION = "$OPTIONS.harvester_configuration"

            const val ENABLE_AUTO_HARVEST = "$HARVESTER_CONFIGURATION.enable_auto_harvest"
            const val ENABLE_AUTO_PLANT = "$HARVESTER_CONFIGURATION.enable_auto_plant"
            const val ENABLE_PREMATURE_AUTO_PLANT = "$HARVESTER_CONFIGURATION.enable_premature_auto_plant"
            const val ENABLE_PREMATURE_AUTO_PLANT_TOOLTIP = "$HARVESTER_CONFIGURATION.enable_premature_auto_plant.tooltip"
            const val ENABLE_SNEAK_AUTO_PLANT = "$HARVESTER_CONFIGURATION.enable_sneak_auto_plant"
            const val AUTO_HARVEST_RADIUS = "$HARVESTER_CONFIGURATION.auto_harvest_radius"
        }
    }
}