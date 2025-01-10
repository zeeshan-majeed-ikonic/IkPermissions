package com.example.ikpermissions

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import java.io.Serializable

/**
 * @author: Zeeshan Majeed
 * @since: Dec 2024
 */

object IkPermissions {
    var canLog: Boolean = true

    /**
     * to disable logs
     */
    fun disableLogs() {
        canLog = false
    }

    /**
     * @param message: to be print in logcat
     */
    internal fun logger(message: String) {
        if (canLog) android.util.Log.d("IkPermissions", message)
    }

    /**
     * Call this function to check and ask only single permission
     * @param context: Context of current activity or fragment
     * @param permission: Single runtime permission, which you have declared in Manifest
     * and want to check and ask to grant.
     * @param options: (Optional), can customise rationale dialog
     * @param ikCallback: In this register listeners like on grant, on denied or on blocked
     */

    fun checkIkPermission(
        context: Context,
        permission: String,
        options: IkPermissionSettings? = null,
        ikCallback: IkPermissionsCallback
    ) {
        checkIkPermissions(context, listOf(permission), options, ikCallback)
    }

    /**
     * Call this function to check and ask list of multiple permissions
     * @param context: Context of current activity or fragment
     * @param permissions: this is list of runtime permission, which you have declared in Manifest
     * and want to check and ask to grant.
     * @param options: (Optional), can customise rationale dialog
     * @param ikCallback: In this register listeners like on grant, on denied or on blocked
     */
    fun checkIkPermissions(
        context: Context,
        permissions: List<String>,
        options: IkPermissionSettings? = null,
        ikCallback: IkPermissionsCallback
    ) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            ikCallback.onGranted()
            logger("Version is below Android 6")
        } else {
            val permissionsSet = permissions.toSet()
            val allPermissionsGranted = permissionsSet.all {
                ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
            }

            if (allPermissionsGranted) {
                ikCallback.onGranted()
                logger("Permissions ${if (IkPermissionsActivity.permissionHandler == null) "Permissions already granted." else "Granted from settings."}")
                IkPermissionsActivity.permissionHandler = null
            } else {
                IkPermissionsActivity.permissionHandler = ikCallback
                val permissionsList = ArrayList(permissionsSet)

                val intent = Intent(context, IkPermissionsActivity::class.java).apply {
                    putStringArrayListExtra(IkPermissionsActivity.PERMISSIONS_DATA, permissionsList)
                    putExtra(IkPermissionsActivity.RATIONALE_DATA, "show rationale")
                    putExtra(IkPermissionsActivity.SETTINGS_DATA, options)
                    if (options?.createNewTask == true) {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                }
                context.startActivity(intent)
            }
        }
    }

    /**
     * Settings for complete rationale dialog
     * Customize according to your need
     * Can set heading, desc, settings button text size and color
     * Can set button background
     */

    class IkPermissionSettings : Serializable {
        var navSettingBtnText: String = "Go to Settings"
        var settingsDialogTitle: String = "Permission(s) Required"
        var settingsDialogMessage: String =
            "App need permission(s) to use this feature. Please goto settings and allow required permission. This will allow you to use this App seamlessly."
        var sendToSettings: Boolean = true
        var createNewTask: Boolean = false
        var settingsButtonBg: Int = R.drawable.ik_button_bg
        var headingTextSize: Int = R.dimen.headingText
        var descTextSize: Int = R.dimen.descText
        var headingColor: Int = R.color.black
        var descColor: Int = R.color.gray
        var settingButtonTextSize: Int = R.dimen.buttonSize
        var settingButtonTextColor: Int = android.R.color.white
        var closeButtonRes: Int = R.drawable.ik_ic_close

        /**
         * @param text: set button text on rational dialog
         */
        fun setSettingsButtonText(text: String): IkPermissionSettings {
            navSettingBtnText = text
            return this
        }

        fun setCreateNewTask(flag: Boolean): IkPermissionSettings {
            createNewTask = flag
            return this
        }

        /**
         * @param title: set heading text on rational dialog
         */
        fun setSettingsDialogTitle(title: String): IkPermissionSettings {
            settingsDialogTitle = title
            return this
        }

        /**
         * @param message: set description text on rational dialog
         */
        fun setSettingsDialogMessage(message: String): IkPermissionSettings {
            settingsDialogMessage = message
            return this
        }

        fun sendForSettings(canSend: Boolean): IkPermissionSettings {
            sendToSettings = canSend
            return this
        }

        /**
         * @param resourceId: set resource drawable for settings button background
         */
        fun setSettingsButtonBg(resourceId: Int): IkPermissionSettings {
            settingsButtonBg = resourceId
            return this
        }

        /**
         * @param dimen: set resource dimen for dialog heading text size
         */
        fun setHeadingTextSize(dimen: Int): IkPermissionSettings {
            headingTextSize = dimen
            return this
        }

        /**
         * @param dimen: set resource dimen for dialog desc text size
         */
        fun setDescTextSize(dimen: Int): IkPermissionSettings {
            descTextSize = dimen
            return this
        }

        /**
         * @param colorId: set resource color for dialog heading text color
         */
        fun setHeadingColor(colorId: Int): IkPermissionSettings {
            headingColor = colorId
            return this
        }

        /**
         * @param colorId: set resource color for dialog desc text color
         */
        fun setDescColor(colorId: Int): IkPermissionSettings {
            descColor = colorId
            return this
        }

        /**
         * @param dimen: set resource dimen for button text size
         */
        fun setSettingsButtonTextSize(dimen: Int): IkPermissionSettings {
            settingButtonTextSize = dimen
            return this
        }

        /**
         * @param colorId: set resource color for button text color
         */
        fun setSettingsButtonTextColor(colorId: Int): IkPermissionSettings {
            settingButtonTextColor = colorId
            return this
        }

        /**
         * @param resourceId: set drawable resource for close button
         */
        fun setCloseButtonResourceId(resourceId: Int): IkPermissionSettings {
            closeButtonRes = resourceId
            return this
        }
    }
}