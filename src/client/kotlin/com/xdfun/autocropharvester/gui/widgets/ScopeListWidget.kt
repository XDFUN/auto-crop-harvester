//package com.xdfun.autocropharvester.gui.widgets
//
//import com.google.common.collect.ImmutableList
//import com.google.common.collect.ImmutableMap
//import net.fabricmc.api.EnvType
//import net.fabricmc.api.Environment
//import net.minecraft.client.MinecraftClient
//import net.minecraft.client.gui.DrawContext
//import net.minecraft.client.gui.Element
//import net.minecraft.client.gui.Selectable
//import net.minecraft.client.gui.screen.Screen
//import net.minecraft.client.gui.screen.option.GameOptionsScreen
//import net.minecraft.client.gui.widget.*
//import net.minecraft.client.option.GameOptions
//import net.minecraft.client.option.SimpleOption
//import net.minecraft.client.option.SimpleOption.OptionSliderWidgetImpl
//import net.minecraft.client.render.RenderLayer
//import net.minecraft.text.Text
//import net.minecraft.util.Identifier
//import java.util.*
//
// TODO: Keeping this as it took some time to create, but I ran into problems with the scopes so it has to be at least commented out
//
//@Environment(EnvType.CLIENT)
//class ScopeListWidget<TScope : ConfigurationScope>(
//    client: MinecraftClient?,
//    width: Int,
//    private val optionsScreen: GameOptionsScreen
//) :
//    ElementListWidget<ScopeListWidget.WidgetEntry>(
//        client,
//        width,
//        optionsScreen.layout.contentHeight,
//        optionsScreen.layout.headerHeight,
//        ITEM_HEIGHT
//    ) {
//
//    init {
//        this.centerListVertically = false
//    }
//
//    val scopes: MutableList<TScope> = mutableListOf()
//
//    fun addScope(scope: TScope, vararg options: SimpleOption<*>?) {
//        scopes.add(scope)
//
//        addEntry(CategoryEntry(scope, optionsScreen, client!!, width, scopes.size <= 1))
//        addEntry(ActivationButtonWidgetEntry(scope, optionsScreen))
//        addAll(*options)
//    }
//
//    protected fun addSingleOptionEntry(option: SimpleOption<*>) {
//        this.addEntry(OptionWidgetEntry.create(this.client.options, option, this.optionsScreen))
//    }
//
//    protected fun addAll(vararg options: SimpleOption<*>?) {
//        var i = 0
//        while (i < options.size) {
//            val simpleOption = if (i < options.size - 1) options[i + 1] else null
//            this.addEntry(
//                OptionWidgetEntry.create(
//                    this.client.options,
//                    options[i]!!,
//                    simpleOption,
//                    this.optionsScreen
//                )
//            )
//            i += 2
//        }
//    }
//
//    protected fun addAll(widgets: MutableList<ClickableWidget>) {
//        var i = 0
//        while (i < widgets.size) {
//            this.addWidgetEntry(widgets[i], if (i < widgets.size - 1) widgets[i + 1] else null)
//            i += 2
//        }
//    }
//
//    protected fun addWidgetEntry(firstWidget: ClickableWidget, secondWidget: ClickableWidget?) {
//        this.addEntry(WidgetEntry.create(firstWidget, secondWidget, this.optionsScreen))
//    }
//
//    override fun getRowWidth(): Int {
//        return WIDTH
//    }
//
//    fun getWidgetFor(option: SimpleOption<*>?): ClickableWidget? {
//        for (widgetEntry in this.children()) {
//            if (widgetEntry is OptionWidgetEntry) {
//                val clickableWidget = widgetEntry.optionWidgets.get(option)
//                if (clickableWidget != null) {
//                    return clickableWidget
//                }
//            }
//        }
//
//        return null
//    }
//
//    fun applyAllPendingValues() {
//        for (widgetEntry in this.children()) {
//            if (widgetEntry is OptionWidgetEntry) {
//                for (clickableWidget in widgetEntry.optionWidgets.values) {
//                    if (clickableWidget is OptionSliderWidgetImpl<*>) {
//                        clickableWidget.applyPendingValue()
//                    }
//                }
//            }
//        }
//    }
//
//    fun getHoveredWidget(mouseX: Double, mouseY: Double): Optional<Element> {
//        for (widgetEntry in this.children()) {
//            for (element in widgetEntry.children()) {
//                if (element.isMouseOver(mouseX, mouseY)) {
//                    return Optional.of<Element>(element)
//                }
//            }
//        }
//
//        return Optional.empty<Element>()
//    }
//
//    @Environment(EnvType.CLIENT)
//    open class WidgetEntry internal constructor(
//        widgets: MutableList<ClickableWidget?>,
//        protected val screen: Screen
//    ) : Entry<WidgetEntry?>() {
//        protected val widgets: MutableList<ClickableWidget> = ImmutableList.copyOf(widgets)
//
//        override fun render(
//            context: DrawContext?,
//            index: Int,
//            y: Int,
//            x: Int,
//            entryWidth: Int,
//            entryHeight: Int,
//            mouseX: Int,
//            mouseY: Int,
//            hovered: Boolean,
//            tickDelta: Float
//        ) {
//            var xOffset = 0
//
//            for (clickableWidget in this.widgets) {
//                alignChild(clickableWidget, xOffset, y)
//                clickableWidget.render(context, mouseX, mouseY, tickDelta)
//                xOffset += WIDGET_X_SPACING
//            }
//        }
//
//        override fun children(): MutableList<out Element> {
//            return this.widgets
//        }
//
//        override fun selectableChildren(): MutableList<out Selectable> {
//            return this.widgets
//        }
//
//        protected fun alignChild(widget: Widget, y: Int) {
//            widget.setPosition(getAlignedX(), y)
//        }
//
//        protected fun alignChild(widget: Widget, xOffset: Int, y: Int) {
//            widget.setPosition(getAlignedX(xOffset), y)
//        }
//
//        protected fun getAlignedX(): Int {
//            return getAlignedX(0)
//        }
//
//        protected fun getAlignedX(xOffset: Int): Int {
//            return (screen.width / 2 - 155) + xOffset
//        }
//
//        companion object {
//            private const val WIDGET_X_SPACING = 160
//
//            fun create(widgets: MutableList<ClickableWidget?>, screen: Screen): WidgetEntry {
//                return WidgetEntry(widgets, screen)
//            }
//
//            fun create(firstWidget: ClickableWidget, secondWidget: ClickableWidget?, screen: Screen): WidgetEntry {
//                return if (secondWidget == null)
//                    WidgetEntry(ImmutableList.of<ClickableWidget?>(firstWidget), screen)
//                else
//                    WidgetEntry(ImmutableList.of<ClickableWidget?>(firstWidget, secondWidget), screen)
//            }
//        }
//    }
//
//    @Environment(EnvType.CLIENT)
//    protected class OptionWidgetEntry private constructor(
//        val optionWidgets: MutableMap<SimpleOption<*>?, ClickableWidget?>,
//        optionsScreen: GameOptionsScreen
//    ) : WidgetEntry(
//        ImmutableList.copyOf<ClickableWidget?>(
//            optionWidgets.values
//        ), optionsScreen
//    ) {
//        companion object {
//            fun create(
//                gameOptions: GameOptions?,
//                option: SimpleOption<*>,
//                optionsScreen: GameOptionsScreen
//            ): OptionWidgetEntry {
//                return OptionWidgetEntry(
//                    ImmutableMap.of<SimpleOption<*>?, ClickableWidget?>(
//                        option,
//                        option.createWidget(gameOptions, 0, 0, WIDTH)
//                    ), optionsScreen
//                )
//            }
//
//            fun create(
//                gameOptions: GameOptions?,
//                firstOption: SimpleOption<*>,
//                secondOption: SimpleOption<*>?,
//                optionsScreen: GameOptionsScreen
//            ): OptionWidgetEntry {
//                val clickableWidget = firstOption.createWidget(gameOptions)
//                return if (secondOption == null)
//                    OptionWidgetEntry(
//                        ImmutableMap.of<SimpleOption<*>?, ClickableWidget?>(firstOption, clickableWidget),
//                        optionsScreen
//                    )
//                else
//                    OptionWidgetEntry(
//                        ImmutableMap.of<SimpleOption<*>?, ClickableWidget?>(
//                            firstOption,
//                            clickableWidget,
//                            secondOption,
//                            secondOption.createWidget(gameOptions)
//                        ), optionsScreen
//                    )
//            }
//        }
//    }
//
//    @Environment(EnvType.CLIENT)
//    protected class ActivationButtonWidgetEntry<TScope : ConfigurationScope>(
//        private val scope: TScope,
//        optionsScreen: GameOptionsScreen
//    ) : WidgetEntry(
//        mutableListOf(), optionsScreen
//    ) {
//        private val activateButton = ButtonWidget.builder(getText()) {
//            scope.toggleActivate()
//        }.build()
//
//        init {
//            activateButton.active = scope.canActivate
//        }
//
//        override fun render(
//            context: DrawContext?,
//            index: Int,
//            y: Int,
//            x: Int,
//            entryWidth: Int,
//            entryHeight: Int,
//            mouseX: Int,
//            mouseY: Int,
//            hovered: Boolean,
//            tickDelta: Float
//        ) {
//            activateButton.message = getText()
//            alignChild(activateButton, y)
//            activateButton.setDimensions(entryWidth, entryHeight)
//            activateButton.render(context, mouseX, mouseY, tickDelta)
//        }
//
//        private fun getText(): Text {
//            return if (scope.isActive) Text.of("Active") else Text.of("Inactive")
//        }
//
//        override fun selectableChildren(): MutableList<Selectable> {
//            return ImmutableList.of(activateButton)
//        }
//
//        override fun children(): MutableList<out Element> {
//            return ImmutableList.of(activateButton)
//        }
//    }
//
//    @Environment(EnvType.CLIENT)
//    protected class CategoryEntry(
//        scope: ConfigurationScope,
//        screen: GameOptionsScreen,
//        private val client: MinecraftClient,
//        width: Int,
//        private val first: Boolean
//    ) : WidgetEntry(mutableListOf(), screen) {
//        private val textWidget = TextWidget(
//            width,
//            ITEM_HEIGHT,
//            Text.translatable(scope::class.simpleName!!),
//            client.textRenderer
//        ).also { it.alignCenter() }
//
//        override fun render(
//            context: DrawContext?,
//            index: Int,
//            y: Int,
//            x: Int,
//            entryWidth: Int,
//            entryHeight: Int,
//            mouseX: Int,
//            mouseY: Int,
//            hovered: Boolean,
//            tickDelta: Float
//        ) {
//            if(first.not())
//            {
//                val headerIdentifier =
//                    if (this.client.world == null) Screen.HEADER_SEPARATOR_TEXTURE else Screen.INWORLD_HEADER_SEPARATOR_TEXTURE
//                context!!.drawTexture(
//                    { texture: Identifier? -> RenderLayer.getGuiTextured(texture) },
//                    headerIdentifier,
//                    getAlignedX(),
//                    y - 2,
//                    0.0f,
//                    0.0f,
//                    entryWidth,
//                    2,
//                    32,
//                    2
//                )
//            }
//
//            alignChild(textWidget, y)
//            textWidget.setDimensions(entryWidth, entryHeight)
//            textWidget.render(context, mouseX, mouseY, tickDelta)
//        }
//
//        override fun selectableChildren(): MutableList<Selectable> {
//            return ImmutableList.of(textWidget)
//        }
//
//        override fun children(): MutableList<out Element> {
//            return ImmutableList.of(textWidget)
//        }
//    }
//
//    companion object {
//        private const val WIDTH = 310
//        private const val ITEM_HEIGHT = 25
//    }
//}