package com.example.highlight

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.JBColor
import java.awt.Color
import java.awt.Component
import java.awt.Container
import javax.swing.JMenu
import javax.swing.SwingUtilities

class HighlightClickableUIAction : AnAction() {
    companion object {
        val originalColors = HashMap<Component, Color>()

        fun colorAllElements(component: Component, color: Color) {
            if (!component.isVisible) return

            if (component.isVisible) {
                originalColors[component] = component.background
                component.background = color
                component.repaint()
            }

            if (component is Container) {
                for (child in component.components) {
                    colorAllElements(child, color)
                }
            }

            if (component is JMenu) {
                for (child in component.popupMenu.components) {
                    colorAllElements(child, color)
                }
            }
        }
    }

    override fun actionPerformed(e: AnActionEvent) {
        originalColors.clear()
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