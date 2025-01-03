package com.example.highlight

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.ui.JBColor
import java.awt.Color
import java.awt.Component
import java.awt.Container
import java.awt.Window
import javax.swing.SwingUtilities

class HighlightClickableUIAction : AnAction() {
    companion object {
        val originalColors = HashMap<Component, Color>()
        var isHighlighted = false

        /**
         * recursively colors all containers in the given component
         *
         * @param component the component to iterate through
         * @param color the background color to highlight with
         */
        fun colorAllElements(component: Component, color: Color) {
            if (!component.isVisible) return

            if (component is Container) {
                val children = component.components
                // iterate through all children recursively
                for (child in children) {
                    colorAllElements(child, color)
                }
                // color container, if it has not been colored already
                if (!originalColors.contains(component)) {
                    // store its original color
                    originalColors[component] = component.background
                    component.background = color
                    component.repaint()
                }
            }
        }
    }

    override fun actionPerformed(e: AnActionEvent) {
        val windows = Window.getWindows()
        SwingUtilities.invokeLater {
            // toggle highlighting on or off
            if (isHighlighted) {
                // reset background colors
                originalColors.forEach{ (component, color) ->
                    component.background = color
                }
                originalColors.clear()
            } else {
                // highlight all elements in every window
                for (window in windows) {
                    if (window.isVisible) {
                        colorAllElements(window, JBColor.YELLOW)
                    }
                }
            }
            isHighlighted = !isHighlighted
        }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = true
    }
}