plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    // AQUÍ ESTABA EL ERROR: Le agregamos la versión explícita "2.0.0"
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
    // Plugin para la base de datos (Room)
    id("kotlin-kapt")
}

android {
    namespace = "com.geovanny.appgestorgastos"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.geovanny.appgestorgastos"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
    // IMPORTANTE: Eliminamos el bloque composeOptions antiguo para evitar conflictos con Kotlin 2.0
}

dependencies {
    // Core de Android
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.8.2")

    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.compose.material:material-icons-extended:1.6.3")

    // Compose (Interfaz Gráfica)
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

    // Navegación (Para cambiar entre pantallas)
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation(libs.androidx.foundation.android)

    // Room (Base de Datos) - Necesario para tus archivos en 'data'
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.02.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}