//package com.xdfun.autocropharvester.gui.screens
//
//import com.xdfun.autocropharvester.configuration.scopes.ConfigurationScope
//import com.xdfun.autocropharvester.gui.widgets.ScopeListWidget
//import net.minecraft.client.gui.screen.Screen
//import net.minecraft.client.gui.screen.option.GameOptionsScreen
//import net.minecraft.client.gui.widget.ButtonWidget
//import net.minecraft.client.option.GameOptions
//import net.minecraft.client.option.SimpleOption
//import net.minecraft.text.Text
//
// TODO: Keeping this when I come around trying my luck the scopes again
//
//abstract class ConfigurationScopeScreen<TConfig : ConfigurationScope>(
//    title: Text,
//    parent: Screen,
//    gameOptions: GameOptions,
//    protected val scopes: Iterable<TConfig>
//) : GameOptionsScreen(parent, gameOptions, title) {
//    private var resetAllButton: ButtonWidget? = null
//    private var scopeWidgets: ScopeListWidget<TConfig>? = null
//
//    override fun initBody() {
//        addOptions()
//    }
//
//    override fun refreshWidgetPositions() {
//        super.refreshWidgetPositions()
//        scopeWidgets?.position(this.width, this.layout)
//    }
//
//    override fun close() {
//        scopeWidgets?.applyAllPendingValues()
//        super.close()
//    }
//
//    override fun addOptions() {
//        for (scope in scopes) {
//            scopeWidgets = scopeWidgets ?: layout.addBody<ScopeListWidget<TConfig>>(
//                ScopeListWidget(
//                    client!!,
//                    layout.width,
//                    this,
//                )
//            )
//
//            scopeWidgets?.addScope(scope, *getOptions(scope))
//        }
//    }
//
//    abstract fun getOptions(scope: TConfig): Array<SimpleOption<*>>
//}