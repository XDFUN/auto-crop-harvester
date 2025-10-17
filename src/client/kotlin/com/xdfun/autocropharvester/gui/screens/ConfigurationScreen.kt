package com.xdfun.autocropharvester.gui.screens

import com.xdfun.autocropharvester.utils.TranslationKeys
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.gui.widget.*
import net.minecraft.screen.ScreenTexts
import net.minecraft.text.Text

class ConfigurationScreen(private val parent: Screen?) : Screen(Title) {
    companion object {
        val Title: Text = Text.translatable(TranslationKeys.Screens.CONFIGURATION)
    }

    private val layout = ThreePartsLayoutWidget(this, 61, 33)

    override fun init() {
        val rowLayout =
            this.layout.addHeader<DirectionalLayoutWidget>(DirectionalLayoutWidget.vertical().spacing(8))
        rowLayout.add(
            TextWidget(title, this.textRenderer)
        ) { obj: Positioner? -> obj!!.alignHorizontalCenter() }
        val columnLayout = rowLayout.add(DirectionalLayoutWidget.horizontal()).spacing(8)

        val offsetX = 5

        columnLayout.add(
            ButtonWidget.builder(
                PlayerConfigurationScreen.Title,
            ) {
                client?.setScreen(PlayerConfigurationScreen(this, client!!.options!!))
            }.dimensions(offsetX, this.height / 6 + 12, 150, 20)
                .build()
        )

        columnLayout.add(
            ButtonWidget.builder(
                HarvesterConfigurationScreen.Title,
            ) {
                client?.setScreen(HarvesterConfigurationScreen(this, client!!.options!!))
            }.dimensions(this.width / 2 + offsetX, this.height / 6 + 12, 150, 20)
                .build()
        )

        this.layout.addFooter(
            ButtonWidget.builder(
                ScreenTexts.DONE
            ) { this.close() }.width(200).build()
        )

        this.layout.forEachChild { child: ClickableWidget? ->
            this.addDrawableChild<ClickableWidget?>(child)
        }

        this.refreshWidgetPositions()
    }

    override fun refreshWidgetPositions() {
        this.layout.refreshPositions()
    }

    override fun close() {
        this.client?.setScreen(parent)
    }
}