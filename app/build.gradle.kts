apply {
    plugin("com.android.application")
    plugin("kotlin-android")
    plugin("kotlin-android-extensions")
}

android {
    buildToolsVersion("26.0.2")
    compileSdkVersion(26)
    defaultConfig {
        applicationId = "com.markantoni.linies"
        minSdkVersion(24)
        targetSdkVersion(26)
        versionCode = 2
        versionName = "1.0.1"
        multiDexEnabled = true
    }

    buildTypes {
        getByName("debug") {
            isDebuggable = true
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    compileOnly("com.google.android.wearable:wearable:2.0.5")
    implementation("com.google.android.support:wearable:2.0.5")
    implementation("com.google.android.gms:play-services-wearable:11.4.2")
    implementation("com.android.support:percent:26.1.0")
    implementation("com.android.support:support-v4:26.1.0")
    implementation("com.android.support:palette-v7:26.1.0")
    implementation("com.android.support:wear:26.1.0")
    implementation("io.reactivex.rxjava2:rxandroid:2.0.1")
    implementation("io.reactivex.rxjava2:rxjava:2.1.3")
    implementation("org.greenrobot:eventbus:3.0.0")
}
