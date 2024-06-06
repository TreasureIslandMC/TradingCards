rootProject.name = "TradingCards"

include(":tradingcards-extras")
include(":tradingcards-api")
include(":tradingcards-plugin")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            plugin("shadow", "com.github.johnrengelman.shadow").version("8.1.1")
            plugin("plugin-yml-bukkit", "net.minecrell.plugin-yml.bukkit").version("0.6.0")
            plugin("jooq", "nu.studer.jooq").version("9.0")
            plugin("messages", "com.github.sarhatabaot.messages").version("1.0.6")


            version("jooq", "3.19.9")
            library("jooq", "org.jooq","jooq").versionRef("jooq")
            library("jooq-codegen", "org.jooq", "jooq-codegen").versionRef("jooq")
            library("jooq-meta", "org.jooq", "jooq-meta").versionRef("jooq")
            library("jooq-meta-extensions","org.jooq","jooq-meta-extensions").versionRef("jooq")
            library("jooq-mysql-connector", "com.mysql:mysql-connector-j:8.4.0")
            library("annotations", "org.jetbrains:annotations:24.1.0")
            library("spigot-api", "org.spigotmc:spigot-api:1.19.2-R0.1-SNAPSHOT")
            library("nbt-api", "de.tr7zw:item-nbt-api:2.12.4")
            library("kraken-core", "com.github.sarhatabaot:KrakenCore:1.7.3")
            library("vault-api", "com.github.MilkBowl:VaultAPI:1.7.1")
            library("treasury-api", "me.lokka30:treasury-api:1.2.1")
            
            version("configurate", "4.1.2")
            library("configurate-core", "org.spongepowered", "configurate-core").versionRef("configurate")
            library("configurate-yaml", "org.spongepowered", "configurate-yaml").versionRef("configurate")
            library("adventure-api", "net.kyori:adventure-api:4.16.0")
            library("adventure-bukkit","net.kyori:adventure-platform-bukkit:4.3.2")
            
            library("bstats", "org.bstats:bstats-bukkit:3.0.2")
            library("acf", "co.aikar:acf-paper:0.5.1-SNAPSHOT")
            library("placeholder-api","me.clip:placeholderapi:2.11.6")

            library("hikaricp", "com.zaxxer:HikariCP:5.1.0")
            library("zip4j", "net.lingala.zip4j:zip4j:2.11.5")
            library("flyway", "org.flywaydb:flyway-mysql:10.14.0")
            
            version("rng", "1.5")
            library("rng-api", "org.apache.commons", "commons-rng-client-api").versionRef("rng")
            library("rng-core", "org.apache.commons","commons-rng-core").versionRef("rng")
            library("rng-simple", "org.apache.commons", "commons-rng-simple").versionRef("rng")
            library("rng-sampling", "org.apache.commons", "commons-rng-sampling").versionRef("rng")
            
            library("mockito", "org.mockito:mockito-core:5.11.0")
            library("mockbukkit", "com.github.seeseemelk:MockBukkit-v1.18:2.85.2")
            library("junit-platform","org.junit:junit-bom:5.10.2")
            library("junit-jupiter", "org.junit.jupiter","junit-jupiter").withoutVersion()

            version("caffeine", "3.1.8")
            library("caffeine","com.github.ben-manes.caffeine", "caffeine").versionRef("caffeine")
        }
    }
}

pluginManagement {
    resolutionStrategy {
        eachPlugin {
            requested.apply {
                if ("$id".startsWith("com.github.sarhatabaot")) {
                    useModule("com.github.sarhatabaot.messages-gradle-plugin:com.github.sarhatabaot.messages.gradle.plugin:$version")
                }
            }
        }
    }
    repositories {
        gradlePluginPortal()
        maven (
            url = uri("https://jitpack.io")
        )
    }
}