package com.example.highlight

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.JBColor
import java.awt.Color
import java.awt.Component
import java.awt.Container
import javax.swing.SwingUtilities

class HighlightClickableUIAction : AnAction() {
    companion object {
        val originalColors = HashMap<Component, Color>()

        fun colorAllElements(component: Component, color: Color) {
            if (!component.isVisible) return

            if (component is Container) {
                for (child in component.components) {
                    colorAllElements(child, color)
                }
                if (component.background != color) {
                    originalColors[component] = component.background
                    component.background = color
                    component.repaint()
                }
            }
        }
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project
        val frame = WindowManager.getInstance().getFrame(project) ?: return
        SwingUtilities.invokeLater {
            colorAllElements(frame, JBColor.YELLOW)
        }
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = true
    }
}