package ru.chernakov.layoutbounds.extension

import com.android.ddmlib.IDevice
import com.android.ddmlib.NullOutputReceiver
import com.intellij.notification.NotificationDisplayType
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project
import org.jetbrains.android.sdk.AndroidSdkUtils
import ru.chernakov.layoutbounds.constants.CMD_SERVICE_CALL_ACTIVITY
import ru.chernakov.layoutbounds.constants.CMD_SETPROP_DEBUG_LAYOUT
import ru.chernakov.layoutbounds.constants.PLUGIN_ID
import ru.chernakov.layoutbounds.constants.TITLE_PLUGIN

fun Project.getNotEmptyDevices(): Array<IDevice>? =
    AndroidSdkUtils.getDebugBridge(this)?.devices?.takeIf { it.isNotEmpty() }

fun IDevice.setLayoutBounds(enable: Boolean) {
    executeShellCommand(
        "$CMD_SETPROP_DEBUG_LAYOUT $enable ; $CMD_SERVICE_CALL_ACTIVITY",
        NullOutputReceiver()
    )
}

fun Project.showNotification(message: String) {
    NotificationGroup(PLUGIN_ID, NotificationDisplayType.BALLOON, true)
        .createNotification(
            "$TITLE_PLUGIN:",
            message,
            NotificationType.WARNING,
            null
        ).notify(this)
}