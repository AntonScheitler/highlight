package com.example.highlight

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.JBColor
import java.awt.Color
import java.awt.Component
import java.awt.Container
import javax.swing.SwingUtilities

class HighlightClickableUIAction : AnAction() {
    companion object {
        val originalColors = HashMap<Component, Color>()
        var isHighlighted = false

        fun colorAllElements(component: Component, color: Color) {
            if (!component.isVisible) return

            if (component is Container) {
                for (child in component.components) {
                    colorAllElements(child, color)
                }
                if (component.components.isEmpty() && !originalColors.contains(component)) {
                    originalColors[component] = component.background
                    component.background = color
                    component.repaint()
                }
            }
        }
    }

    override fun actionPerformed(e: AnActionEvent) {
        val dataContext = e.dataContext
        val component = dataContext.getData(PlatformDataKeys.CONTEXT_COMPONENT)
        val window = SwingUtilities.getWindowAncestor(component)
        SwingUtilities.invokeLater {
            if (isHighlighted) {
                originalColors.forEach{ (component, color) ->
                    component.background = color
                }
                originalColors.clear()
            } else {
                colorAllElements(window, JBColor.YELLOW)
            }
            isHighlighted = !isHighlighted
        }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = true
    }
}