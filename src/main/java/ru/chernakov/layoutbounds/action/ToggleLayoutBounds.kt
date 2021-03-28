package ru.chernakov.layoutbounds.action

import com.android.ddmlib.IDevice
import com.android.ddmlib.MultiLineReceiver
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import ru.chernakov.layoutbounds.constants.CMD_GETPROP_DEBUG_LAYOUT
import ru.chernakov.layoutbounds.constants.MSG_NO_CONNECTED_DEVICES
import ru.chernakov.layoutbounds.extension.getNotEmptyDevices
import ru.chernakov.layoutbounds.extension.setLayoutBounds
import ru.chernakov.layoutbounds.extension.showNotification


class ToggleLayoutBounds : AnAction() {

    override fun actionPerformed(event: AnActionEvent) {
        event.project?.getNotEmptyDevices()?.forEach { device ->
            device.toggleLayoutBounds()
        } ?: run {
            event.project?.showNotification(MSG_NO_CONNECTED_DEVICES)
        }
    }

    private fun IDevice.toggleLayoutBounds() {
        executeShellCommand(CMD_GETPROP_DEBUG_LAYOUT, SingleLineReceiver {
            setLayoutBounds(it.toBoolean().not())
        })
    }

    class SingleLineReceiver(
        private val processFirstLine: (response: String) -> Unit
    ) : MultiLineReceiver() {

        private var isClosed = false

        override fun processNewLines(lines: Array<out String>?) {
            lines?.getOrNull(0)?.let { firstLine ->
                processFirstLine(firstLine)
                isClosed = true
            }
        }

        override fun isCancelled() = isClosed
    }
}