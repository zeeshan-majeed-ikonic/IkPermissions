package com.example.ikpermissions

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.Serializable

/**
 * @author: Zeeshan Majeed
 * @since: Dec 2024
 */

class IkPermissionsActivity : AppCompatActivity() {
    companion object {
        const val SETTINGS_CODE = 2023
        const val PERMISSIONS_CODE = 2024

        const val PERMISSIONS_DATA = "PERMISSIONS_DATA"
        const val RATIONALE_DATA = "RATIONALE_DATA"
        const val SETTINGS_DATA = "SETTINGS_DATA"

        var permissionHandler: IkPermissionsCallback? = null

    }

    private lateinit var allPermissions: ArrayList<String>
    private lateinit var deniedPermissions: ArrayList<String>
    private lateinit var noRationalePermissions: ArrayList<String>
    private var ikPermissionsSettings: IkPermissions.IkPermissionSettings? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setFinishOnTouchOutside(false)

        val intent = intent ?: run {
            finish()
            return
        }

        allPermissions = intent.getStringArrayListExtra(PERMISSIONS_DATA) ?: run {
            finish()
            return
        }

        ikPermissionsSettings =
            intent.serializable(SETTINGS_DATA) ?: IkPermissions.IkPermissionSettings()

        deniedPermissions = ArrayList()
        noRationalePermissions = ArrayList()

        val permissionsToRequest = allPermissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (permissionsToRequest.isEmpty()) {
            permissionHandler?.onGranted()
            finish()
        } else {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                PERMISSIONS_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_CODE) {
            deniedPermissions.clear()
            noRationalePermissions.clear()

            permissions.forEachIndexed { index, permission ->
                if (grantResults[index] != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permission)
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                        noRationalePermissions.add(permission)
                    }
                }
            }

            when {
                deniedPermissions.isEmpty() -> {
                    permissionHandler?.onGranted()
                    finish()
                }

                noRationalePermissions.isNotEmpty() -> {
                    showBlockedDialog()
                }

                else -> {
                    showRationaleDialog()
                }
            }
        }
    }

    /**
     * This bottom sheet dialog will appear to user when user clicks on don't allow
     */

    private fun showRationaleDialog() {
        val rationale = intent.getStringExtra(RATIONALE_DATA)
        if (!rationale.isNullOrEmpty()) {
            val bottomSheet = BottomSheetDialog(this, R.style.RoundedBottomSheetTheme)
            bottomSheet.apply {
                val bottomSheetView = LayoutInflater.from(this@IkPermissionsActivity)
                    .inflate(R.layout.ik_bottom_sheet_permissions,null)
                setContentView(bottomSheetView)
                setCancelable(false)
                show()
                bottomSheetView.apply {
                    val heading = findViewById<TextView>(R.id.tv_free_heading)
                    val description = findViewById<TextView>(R.id.tv_description)
                    val btnClose = findViewById<ImageView>(R.id.btn_close)
                    val gotoSettings = findViewById<TextView>(R.id.btn_done)

                    heading.text = ikPermissionsSettings?.settingsDialogTitle
                    description.text = ikPermissionsSettings?.settingsDialogMessage
                    gotoSettings.setBackgroundResource(
                        ikPermissionsSettings?.settingsButtonBg ?: R.drawable.ik_button_bg
                    )

                    heading.textSize = resources.getDimension(
                        ikPermissionsSettings?.headingTextSize ?: R.dimen.headingText
                    )
                    description.textSize = resources.getDimension(
                        (ikPermissionsSettings?.descTextSize ?: R.dimen.descText)
                    )

                    heading.setTextColor(
                        ContextCompat.getColor(
                            this@IkPermissionsActivity,
                            ikPermissionsSettings?.headingColor ?: R.color.black
                        )
                    )
                    description.setTextColor(
                        ContextCompat.getColor(
                            this@IkPermissionsActivity,
                            ikPermissionsSettings?.descColor ?: R.color.gray
                        )
                    )
                    gotoSettings.setTextColor(
                        ContextCompat.getColor(
                            this@IkPermissionsActivity,
                            ikPermissionsSettings?.settingButtonTextColor ?: android.R.color.white
                        )
                    )
                    gotoSettings.textSize = resources.getDimension(
                        ikPermissionsSettings?.settingButtonTextSize ?: R.dimen.buttonSize
                    )
                    btnClose.setImageResource(
                        ikPermissionsSettings?.closeButtonRes ?: R.drawable.ik_ic_close
                    )

                    btnClose.setOnClickListener {
                        permissionHandler?.onDenied(this@IkPermissionsActivity, deniedPermissions)
                        finish()
                    }
                    gotoSettings.text = "Ask Again"
                    gotoSettings.setOnClickListener {
                        ActivityCompat.requestPermissions(
                            this@IkPermissionsActivity,
                            deniedPermissions.toTypedArray(),
                            PERMISSIONS_CODE
                        )
                    }
                }
            }
        } else {
            permissionHandler?.onDenied(this, deniedPermissions)
            finish()
        }
    }

    /**
     * This bottom sheet dialog will appear to user when user clicks on don't allow
     * for multiple time and system blocks rationale dialog
     */
    private fun showBlockedDialog() {
        if (ikPermissionsSettings?.sendToSettings == true) {
            val bottomSheet = BottomSheetDialog(this, R.style.RoundedBottomSheetTheme)
            bottomSheet.apply {
                val bottomSheetView = LayoutInflater.from(this@IkPermissionsActivity)
                    .inflate(R.layout.ik_bottom_sheet_permissions, null)
                setContentView(bottomSheetView)
                setCancelable(false)
                show()
                bottomSheetView.apply {
                    val heading = findViewById<TextView>(R.id.tv_free_heading)
                    val description = findViewById<TextView>(R.id.tv_description)
                    val btnClose = findViewById<ImageView>(R.id.btn_close)
                    val gotoSettings = findViewById<TextView>(R.id.btn_done)

                    gotoSettings.text = ikPermissionsSettings?.navSettingBtnText ?: "Go to Settings"
                    heading.text = ikPermissionsSettings?.settingsDialogTitle
                    description.text = ikPermissionsSettings?.settingsDialogMessage
                    gotoSettings.setBackgroundResource(
                        ikPermissionsSettings?.settingsButtonBg ?: R.drawable.ik_button_bg
                    )
                    heading.textSize = resources.getDimension(
                        ikPermissionsSettings?.headingTextSize ?: R.dimen.headingText
                    )
                    description.textSize = resources.getDimension(
                        (ikPermissionsSettings?.descTextSize ?: R.dimen.descText)
                    )
                    heading.setTextColor(
                        ContextCompat.getColor(
                            this@IkPermissionsActivity,
                            ikPermissionsSettings?.headingColor ?: R.color.black
                        )
                    )
                    description.setTextColor(
                        ContextCompat.getColor(
                            this@IkPermissionsActivity,
                            ikPermissionsSettings?.descColor ?: R.color.gray
                        )
                    )
                    gotoSettings.setTextColor(
                        ContextCompat.getColor(
                            this@IkPermissionsActivity,
                            ikPermissionsSettings?.settingButtonTextColor ?: android.R.color.white
                        )
                    )
                    gotoSettings.textSize = resources.getDimension(
                        ikPermissionsSettings?.settingButtonTextSize ?: R.dimen.buttonSize
                    )
                    btnClose.setImageResource(
                        ikPermissionsSettings?.closeButtonRes ?: R.drawable.ik_ic_close
                    )

                    btnClose.setOnClickListener {
                        permissionHandler?.onBlocked(
                            this@IkPermissionsActivity,
                            noRationalePermissions,
                            deniedPermissions
                        )
                        finish()
                    }
                    gotoSettings.setOnClickListener {
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", packageName, null)
                        }
                        startActivityForResult(intent, SETTINGS_CODE)
                    }
                }
            }
        } else {
            permissionHandler?.onBlocked(
                this,
                noRationalePermissions,
                deniedPermissions
            )
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == SETTINGS_CODE) {
            ActivityCompat.requestPermissions(
                this,
                deniedPermissions.toTypedArray(),
                PERMISSIONS_CODE
            )
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    /*ikPermissionsSettings = intent.getSerializableExtra(SETTINGS_DATA) as? IkPermissions.IkPermissionSettings
            ?: IkPermissions.IkPermissionSettings()*/

    private inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
        else -> @Suppress("DEPRECATION") getSerializable(key) as? T
    }

    private inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(
            key,
            T::class.java
        )

        else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
    }

    override fun onDestroy() {
        permissionHandler = null
        super.onDestroy()
    }
}