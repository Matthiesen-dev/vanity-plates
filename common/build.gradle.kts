plugins {
    id("dev.architectury.loom")
    id("architectury-plugin")
}

architectury {
    common("neoforge", "fabric")
}

loom {
    silentMojangMappingsLicense()
}

dependencies {
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings(loom.officialMojangMappings())

    compileOnly("net.luckperms:api:${property("luckperms_version")}")
    modImplementation("ca.landonjw.gooeylibs:api:${property("gooeylibs_version")}")
}

tasks {
    test {
        useJUnitPlatform()
    }

    remapSourcesJar {
        archiveBaseName.set("${rootProject.property("archives_base_name")}-${project.name}")
        archiveVersion.set("${project.version}")
        archiveClassifier.set("sources")
    }
}
