package com.example.permissionshelper

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ikpermissions.IkPermissions
import com.example.ikpermissions.IkPermissionsCallback

class MainActivity : AppCompatActivity() {

//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

       /* val listOfPermissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        findViewById<Button>(R.id.btnMulti).setOnClickListener {
            val ikSettings = IkPermissions.IkPermissionSettings()
                .setSettingsDialogTitle("Required Permission(s)!")
                .setSettingsDialogMessage("Here is detailed message why to give permission(s)..")
                .setSettingsButtonText("Go to Settings")
                .setSettingsButtonBg(R.drawable.btn_bg_custom)
                .setHeadingTextSize(R.dimen.headSize)
                .setDescTextSize(R.dimen.subSize)
                .setSettingsButtonTextSize(R.dimen.btnSize)
                .setHeadingColor(R.color.black)
                .setDescColor(R.color.black)
                .setSettingsButtonTextColor(R.color.white)
                .setCloseButtonResourceId(R.drawable.close_btn)

            IkPermissions.checkIkPermissions(
                context = this,
                listOfPermissions,
                options = ikSettings,
                ikCallback = object :
                    IkPermissionsCallback() {
                    @SuppressLint("SetTextI18n")
                    override fun onGranted() {
                        findViewById<Button>(R.id.btnMulti).text = "All permissions are Granted"
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onDenied(context: Context, permissions: List<String>) {
                        super.onDenied(context, permissions)
                        findViewById<Button>(R.id.btnMulti).text = "${permissions.size} Denied"
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onBlocked(
                        context: Context,
                        permissions: List<String>,
                        deniedPermissions: List<String>
                    ) {
                        super.onBlocked(context, permissions, deniedPermissions)
                        findViewById<Button>(R.id.btnMulti).text = "${permissions.size} Blocked"
                    }
                })
        }

        findViewById<Button>(R.id.btnSingle).setOnClickListener {
            val ikSettings = IkPermissions.IkPermissionSettings()
                .setSettingsDialogMessage("Here is detailed message for user why to give permission..")
                .setSettingsDialogTitle("Required Permission(s)!")
                .setSettingsButtonBg(R.drawable.btn_bg_custom)
                .setSettingsButtonText("Settings")
                .setHeadingTextSize(R.dimen.headSize)
                .setDescTextSize(R.dimen.subSize)
            *//*.setHeadingColor(R.color.yellow)
            .setDescColor(R.color.blue)*//*

            IkPermissions.checkIkPermission(
                this,
                permission = Manifest.permission.POST_NOTIFICATIONS,
                options = ikSettings,
                object : IkPermissionsCallback() {
                    @SuppressLint("SetTextI18n")
                    override fun onGranted() {
                        findViewById<Button>(R.id.btnSingle).text = "Permission Granted"
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onDenied(context: Context, permissions: List<String>) {
                        super.onDenied(context, permissions)
                        findViewById<Button>(R.id.btnSingle).text = "Permission Denied"
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onBlocked(
                        context: Context,
                        permissions: List<String>,
                        deniedPermissions: List<String>
                    ) {
                        super.onBlocked(context, permissions, deniedPermissions)
                        findViewById<Button>(R.id.btnSingle).text = "Permission Blocked"
                    }
                })
        }*/
    }
}