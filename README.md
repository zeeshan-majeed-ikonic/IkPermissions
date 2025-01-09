# IKONIC Permissions Helper

Simple and efficient library to handle android runtime permissions.

---

## **Get Started**

### Step 01:

> Add Maven dependencies:

#### Groovy-DSL

```kotlin 
    repositories {
	mavenCentral()
	maven { url 'https://jitpack.io' }
     }
```
#### Kotlin-DSL

```kotlin 
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
```

### Step 02:

> Add gradle dependencies:

#### IKONIC Permissions Helper
Version: [![](https://jitpack.io/v/zeeshan-majeed-ikonic/IkPermissions.svg)](https://jitpack.io/#zeeshan-majeed-ikonic/IkPermissions)

```kotlin 
    implementation ("com.github.zeeshan-majeed-ikonic:IkPermissions:version")
```

### Step 03:

## Add Required permissions in Manifest

> Like this:

```kotlin 
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
```
### Step 04:

## Ask for Mutli permissions

> If you want to set own message and title on Blocked permissions Rationale dialog options as declaired below. or null if used predefined message

```kotlin 
    val listOfPermissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        findViewById<Button>(R.id.btnMulti).setOnClickListener {
            val ikSettings = IkPermissions.IkPermissionSettings()
                .setSettingsDialogMessage("Here is detailed message for user why to give permission..")
                .setSettingsDialogTitle("Required Permission(s)!")
		.setSettingsButtonBg(R.drawable.btn_bg_custom)
            IkPermissions.checkIkPermissions(
                context = this,
                listOfPermissions,
                options = ikSettings/null,
                ikCallback = object :
                    IkPermissionsCallback() {
                    override fun onGranted() {
                        findViewById<Button>(R.id.btnMulti).text = "All permissions are Granted"
                    }

                    override fun onDenied(context: Context, permissions: List<String>) {
                        super.onDenied(context, permissions)
                        findViewById<Button>(R.id.btnMulti).text = "${permissions.size} Denied"
                    }

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
```

## Ask for Single permission

> If you want to set own message and title on Blocked permissions Rationale dialog options as declaired below. or null if used predefined message

```kotlin 
    findViewById<Button>(R.id.btnSingle).setOnClickListener {
            val ikSettings = IkPermissions.IkPermissionSettings()
                .setSettingsDialogMessage("Here is detailed message for user why to give permission..")
                .setSettingsDialogTitle("Required Permission!")
		.setSettingsButtonBg(R.drawable.btn_bg_custom)
            IkPermissions.checkIkPermission(
                this,
                permission = Manifest.permission.POST_NOTIFICATIONS,
                options = ikSettings/null,
                object : IkPermissionsCallback() {
                    override fun onGranted() {
                        findViewById<Button>(R.id.btnSingle).text = "Permission Granted"
                    }

                    override fun onDenied(context: Context, permissions: List<String>) {
                        super.onDenied(context, permissions)
                        findViewById<Button>(R.id.btnSingle).text = "Permission Denied"
                    }

                    override fun onBlocked(
                        context: Context,
                        permissions: List<String>,
                        deniedPermissions: List<String>
                    ) {
                        super.onBlocked(context, permissions, deniedPermissions)
                        findViewById<Button>(R.id.btnSingle).text = "Permission Blocked"
                    }

                })
        }
```

## Happy coding :)

