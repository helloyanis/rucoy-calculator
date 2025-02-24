plugins {
    alias(libs.plugins.com.android.application)
    alias(libs.plugins.org.jetbrains.kotlin.android)
}

android {
    namespace = "com.helloyanis.rucoycalculator"
    compileSdk = 35
    defaultConfig {
        applicationId = "com.helloyanis.rucoycalculator"
        minSdk = 24
        targetSdk = 35
        versionCode = 87
        versionName = "8.7"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.0"
    }
    androidResources {
        generateLocaleConfig = true
    }
    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.androidx.datastore.preferences.v100)
    implementation(libs.androidx.datastore.preferences.rxjava2.v100)
    implementation(libs.androidx.datastore.preferences.rxjava3.v100)
    implementation(libs.androidx.runtime)
    implementation(libs.datastore.preferences)
    implementation(libs.datastore.preferences.rxjava2)
    implementation(libs.datastore.preferences.rxjava3)
    implementation(libs.material.v1120)

}