package com.example.ikpermissions

import android.content.Context

abstract class IkPermissionsCallback {

    abstract fun onGranted()

    open fun onDenied(context: Context, permissions: List<String>) {
        if (IkPermissions.canLog) {
            val deniedList = permissions.joinToString(separator = " ") { it }
            IkPermissions.logger("Denied: $deniedList")
        }
    }

    open fun onBlocked(context: Context, permissions: List<String>, deniedPermissions: List<String>) {
        if (IkPermissions.canLog) {
            val blockedPermissions = permissions.joinToString(separator = " ") { it }
            IkPermissions.logger("Just set not to ask again: $blockedPermissions")
        }
        onDenied(context, deniedPermissions)
    }
}