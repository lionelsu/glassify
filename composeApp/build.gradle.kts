import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    jvmToolchain(17)

    jvm("desktop") {
        // Configurações específicas do target
    }
    sourceSets {
        val desktopMain by getting

        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation("net.java.dev.jna:jna:5.17.0")
            implementation("net.java.dev.jna:jna-platform:5.17.0")
            implementation("com.github.kwhat:jnativehook:2.2.2")
            implementation(compose.materialIconsExtended)
        }

        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.lionel.glassify.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Msi)
            packageName = "Glassify"
            packageVersion = "1.0.0"

            // Configuração para incluir JVM
            includeAllModules = true

            // Módulos Java necessários
            modules(
                "java.base",
                "java.desktop",
                "jdk.unsupported"
            )

            // Configurações específicas para Windows
            windows {
                // Caminho absoluto para o ícone (crie esta pasta e arquivo)
                //iconFile.set(project.file("icons/icon.ico").absoluteFile)
                menuGroup = "Acessibilidade"
                upgradeUuid = "5B4C1DD0-77F5-4A6B-8B8D-3D8F1E7A1234"
                perUserInstall = true

                // Configurações para evitar erros comuns
                menu = true
                shortcut = true
            }
        }
    }
}

tasks.register("packageAll") {
    dependsOn("packageMsi")
}