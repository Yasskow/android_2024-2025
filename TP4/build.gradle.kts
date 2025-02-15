// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "2.0.21"
    alias(libs.plugins.compose.compiler) apply true
}