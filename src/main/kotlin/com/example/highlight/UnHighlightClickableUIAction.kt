package com.example.highlight

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.wm.WindowManager
import com.jetbrains.rd.util.first

class UnHighlightClickableUIAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val frame = WindowManager.getInstance().getFrame(e.project) ?: return
        HighlightClickableUIAction.colorAllElements(frame, HighlightClickableUIAction.originalColors.first().value)
        HighlightClickableUIAction.originalColors.clear()
    }

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabledAndVisible = true
    }
}

