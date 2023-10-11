plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.cthacademy.android"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cthacademy.android"
        minSdk = 26
        targetSdk = 34
        versionCode = 2
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), file("proguard-rules.pro"))
        }
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), file("proguard-rules.pro"))
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("android.arch.lifecycle:extensions:1.1.1")
    //noinspection GradleCompatible
    implementation("androidx.compose.foundation:foundation:1.5.3")
    implementation("androidx.compose.material:material:1.5.3")
    implementation("androidx.compose.ui:ui-tooling:1.5.3")
    implementation("androidx.compose.ui:ui-tooling-preview-android:1.5.3")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.annotation:annotation:1.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0-alpha13")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.google.android.material:material:1.10.0")
    implementation("com.google.firebase:firebase-bom:32.3.1")
    implementation("com.google.firebase:firebase-core:21.1.1")
    implementation("com.google.firebase:firebase-auth-ktx:22.1.2")
    implementation("com.google.firebase:firebase-database-ktx:20.2.2")
    implementation("com.google.firebase:firebase-firestore-ktx:24.8.1")
    implementation("com.google.firebase:firebase-appcheck-playintegrity:17.0.1")
    implementation("com.google.firebase:firebase-appcheck-ktx:17.0.1")
    implementation("com.google.firebase:firebase-appcheck-debug:17.0.1")
    implementation("com.google.android.exoplayer:exoplayer:2.19.1")
    implementation("com.google.firebase:firebase-storage-ktx:20.2.1")
    implementation("com.google.android.gms:play-services-tagmanager-v4-impl:18.0.3")
    implementation("com.google.android.gms:play-services-ads:22.4.0")
    implementation("com.google.android.exoplayer:exoplayer-core:2.19.1")
    implementation("com.google.android.exoplayer:exoplayer-ui:2.19.1")
    implementation("com.makeramen:roundedimageview:2.3.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.2")
    implementation("com.cheekiat:slideview:1.6")
    implementation("com.android.billingclient:billing:6.0.1")
    implementation("com.github.mmin18:realtimeblurview:1.2.1")
    implementation("com.github.dhaval2404:imagepicker:2.1")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation("com.kizitonwose.calendar:view:2.4.0")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("nl.joery.animatedbottombar:library:1.1.0")
    implementation("jp.wasabeef:blurry:4.0.1")
    implementation("jp.wasabeef:glide-transformations:4.3.0")
    implementation("com.google.firebase:firebase-firestore-ktx:24.8.1")
    implementation("androidx.test:monitor:1.6.1")
    implementation("com.github.javiersantos:AppUpdater:2.7")
}