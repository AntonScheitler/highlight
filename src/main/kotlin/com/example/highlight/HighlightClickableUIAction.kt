package com.example.highlight

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.ui.JBColor
import java.awt.Color
import java.awt.Component
import java.awt.Container
import java.awt.Window
import javax.swing.JComboBox
import javax.swing.JList
import javax.swing.JMenuItem
import javax.swing.JTextField
import javax.swing.SwingUtilities

class HighlightClickableUIAction : AnAction() {
    companion object {
        val originalColors = HashMap<Component, Color>()
        var isHighlighted = false

        fun colorAllElements(component: Component, color: Color) {
            if (!component.isVisible) return

            if (component is Container) {
                val children = component.components
                for (child in children) {
                    colorAllElements(child, color)
                }
                if (!originalColors.contains(component)) {
                    if (children.isEmpty() || component is JTextField || component is JComboBox<*> ||
                        component is JList<*> || component is JMenuItem
                    ) {
                        originalColors[component] = component.background
                        component.background = color
                        component.repaint()
                    }
                }
            }
        }
    }

    override fun actionPerformed(e: AnActionEvent) {
        val windows = Window.getWindows()
        SwingUtilities.invokeLater {
            if (isHighlighted) {
                originalColors.forEach{ (component, color) ->
                    component.background = color
                }
                originalColors.clear()
            } else {
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