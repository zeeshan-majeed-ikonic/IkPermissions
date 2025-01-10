package com.example.ikpermissions

import android.content.Context

/**
 * @author: Zeeshan Majeed
 * @since: Dec 2024
 */

abstract class IkPermissionsCallback {

    /**
     * Trigger when user clicks on allow on asked permission
     */
    abstract fun onGranted()

    /**
     * Trigger when user clicks on don't allow on asked permission(s)
     * @param permissions: list of denied permissions
     */
    open fun onDenied(context: Context, permissions: List<String>) {
        if (IkPermissions.canLog) {
            val deniedList = permissions.joinToString(separator = " ") { it }
            IkPermissions.logger("Denied: $deniedList")
        }
    }

    /**
     * Trigger when system block to ask again after 2 to 3 attempts
     * @param permissions: list of blocked permissions
     * @param deniedPermissions: list of denied permissions
     */
    open fun onBlocked(context: Context, permissions: List<String>, deniedPermissions: List<String>) {
        if (IkPermissions.canLog) {
            val blockedPermissions = permissions.joinToString(separator = " ") { it }
            IkPermissions.logger("Not to ask again: $blockedPermissions")
        }
        onDenied(context, deniedPermissions)
    }
}