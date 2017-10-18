import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.repositories


buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.0.0-beta7")
        classpath(kotlin("gradle-plugin"))
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

task<Delete>("clean") {
    delete = mutableSetOf<Any>(rootProject.buildDir)
}
