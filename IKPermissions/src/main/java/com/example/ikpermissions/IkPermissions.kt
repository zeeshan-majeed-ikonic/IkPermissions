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
 * */

object IkPermissions {
    var canLog: Boolean = true

    /**
     * to disable logs
     */
    fun disableLogs() {
        canLog = false
    }

    /**
     * @param message: this is log to be print
     */
    internal fun logger(message: String) {
        if (canLog) android.util.Log.d("IkPermissions", message)
    }

    fun checkIkPermission(
        context: Context,
        permission: String,
        options: IkPermissionSettings? = null,
        ikCallback: IkPermissionsCallback
    ) {
        checkIkPermissions(context, listOf(permission), options, ikCallback)
    }


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


        fun setNavSettingBtnText(text: String): IkPermissionSettings {
            navSettingBtnText = text
            return this
        }

        fun setCreateNewTask(flag: Boolean): IkPermissionSettings {
            createNewTask = flag
            return this
        }


        fun setSettingsDialogTitle(title: String): IkPermissionSettings {
            settingsDialogTitle = title
            return this
        }

        fun setSettingsDialogMessage(message: String): IkPermissionSettings {
            settingsDialogMessage = message
            return this
        }

        fun sendForSettings(canSend: Boolean): IkPermissionSettings {
            sendToSettings = canSend
            return this
        }

        fun setSettingsButtonBg(resourceId: Int): IkPermissionSettings {
            settingsButtonBg = resourceId
            return this
        }

        fun setHeadingTextSize(size: Int): IkPermissionSettings {
            headingTextSize = size
            return this
        }

        fun setDescTextSize(size: Int): IkPermissionSettings {
            descTextSize = size
            return this
        }

        fun setHeadingColor(colorId: Int): IkPermissionSettings {
            headingColor = colorId
            return this
        }

        fun setDescColor(colorId: Int): IkPermissionSettings {
            descColor = colorId
            return this
        }
    }
}