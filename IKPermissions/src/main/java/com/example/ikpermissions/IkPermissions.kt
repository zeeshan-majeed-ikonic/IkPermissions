package com.example.ikpermissions

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import java.io.Serializable

object IkPermissions {
    var canLog: Boolean = true


    fun disableLogs() {
        canLog = false
    }

    internal fun logger(message: String) {
        if (canLog) android.util.Log.d("IkPermissions", message)
    }

    fun checkIkPermission(
        context: Context,
        permission: String,
        options: IkPermissionSettings?=null,
        ikCallback: IkPermissionsCallback
    ) {
        checkIkPermissions(context, listOf(permission), options, ikCallback)
    }


    fun checkIkPermissions(
        context: Context,
        permissions: List<String>,
        options: IkPermissionSettings?=null,
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
        var navSettingBtnText: String = "Settings"
        var rationaleDialogTitle: String = "Permission(s) Required"
        var settingsDialogTitle: String = "Permission(s) Required"
        var settingsDialogMessage: String =
            "App need permission(s) to use this feature. Please goto settings and allow required permission. This will allow you to use this App seamlessly."
        var sendToSettings: Boolean = true
        var createNewTask: Boolean = false

        fun setNavSettingBtnText(text: String): IkPermissionSettings {
            navSettingBtnText = text
            return this
        }

        fun setCreateNewTask(flag: Boolean): IkPermissionSettings {
            createNewTask = flag
            return this
        }

        fun setRationaleDialogTitle(title: String): IkPermissionSettings {
            rationaleDialogTitle = title
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
    }
}