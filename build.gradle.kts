import toni.blahaj.*
import toni.blahaj.api.*

val templateSettings = object : BlahajSettings {
	// -------------------- Dependencies ---------------------- //
	override val depsHandler: BlahajDependencyHandler get() = object : BlahajDependencyHandler {
		override fun addGlobal(mod : ModData, deps: DependencyHandler) {

		}

		override fun addFabric(mod : ModData, deps: DependencyHandler) {
			when (mod.version) {
				"1.20.1" -> {
					deps.modImplementation(modrinth("better-compatibility-checker", "gatP97QZ"))
				}
				"1.21.1" -> {
					deps.modImplementation(modrinth("better-compatibility-checker", "i3Buju9g"))

				}
				"1.21.4" -> {

				}
			}
		}

		override fun addForge(mod : ModData, deps: DependencyHandler) {
			deps.modImplementation(modrinth("better-compatibility-checker", "build.58+mc1.20"))
			deps.include(deps.modApi("dev.su5ed.sinytra.fabric-api:fabric-lifecycle-events-v1:+")!!)
		}

		override fun addNeo(mod : ModData, deps: DependencyHandler) {
			when (mod.version) {
				"1.21.1" -> {
					deps.modImplementation(modrinth("better-compatibility-checker", "cfm6GZlI"))
					deps.include(deps.modApi("org.sinytra.forgified-fabric-api:fabric-lifecycle-events-v1:2.3.12+")!!)
				}
				"1.21.4" -> {
					deps.include(deps.modApi("org.sinytra.forgified-fabric-api:fabric-lifecycle-events-v1:2.4.0+")!!)
				}
			}

		}
	}

	// ---------- Curseforge/Modrinth Configuration ----------- //
	// For configuring the dependecies that will show up on your mod page.
	override val publishHandler: BlahajPublishDependencyHandler get() = object : BlahajPublishDependencyHandler {
		override fun addShared(mod : ModData, deps: DependencyContainer) {
			if (mod.isFabric) {
				deps.requires("fabric-api")
			}
		}

		override fun addCurseForge(mod : ModData, deps: DependencyContainer) {

		}

		override fun addModrinth(mod : ModData, deps: DependencyContainer) {

		}
	}
}

plugins {
	`maven-publish`
	application
	id("toni.blahaj") version "1.0.69"
	kotlin("jvm")
	kotlin("plugin.serialization")
	id("dev.kikugie.j52j") version "1.0"
	id("dev.architectury.loom")
	id("me.modmuss50.mod-publish-plugin")
	id("systems.manifold.manifold-gradle-plugin")
}

blahaj {
	sc = stonecutter
	settings = templateSettings
	init()
}

// Dependencies
repositories {
	maven("https://www.cursemaven.com")
	maven("https://api.modrinth.com/maven")
	maven("https://thedarkcolour.github.io/KotlinForForge/")
	maven("https://maven.kikugie.dev/releases")
	maven("https://maven.txni.dev/releases")
	maven("https://jitpack.io")
	maven("https://maven.neoforged.net/releases/")
	maven("https://maven.terraformersmc.com/releases/")
	maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/")
	maven("https://maven.parchmentmc.org")
	maven("https://maven.su5ed.dev/releases")
	maven("https://maven.su5ed.dev/releases")
	maven("https://maven.fabricmc.net")
	maven("https://maven.shedaniel.me/")
}
