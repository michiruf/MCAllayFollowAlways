# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

Fabric mod that makes Allays persistently follow the player who gave them an item, including across dimensions via teleportation. Published on Modrinth as `allay-follow-always`.

## Build System

Uses **Gradle + Fabric Loom + Stonecutter** for multi-version support across 29 Minecraft versions (1.19 through 26.3).

- **Stonecutter** manages version branching — each MC version gets a Gradle subproject under `versions/{version}/`
- Active version set in `stonecutter.gradle` (currently `1.21.11`), VCS version in `settings.gradle`
- Version definitions (Java version, mappings, Fabric versions) live in `build.versions.gradle`
- Java 17 for MC 1.19–1.20.4, Java 21 for MC 1.20.5–1.21.11, Java 25 for MC 26.1+
- Gradle itself must run on Java 25 (required by Loom); `gradle/gradle-daemon-jvm.properties` makes Gradle provision it automatically
- MC 26.1+ is unobfuscated: `build.gradle` applies `net.fabricmc.fabric-loom` (no mappings, plain `implementation`) for 26.1+ and `net.fabricmc.fabric-loom-remap` (Mojang mappings, `modImplementation`) for older versions
- `releaseJar` task builds the distributable jar for either variant (`remapJar` before 26.1, `jar` since)
- Fabric API mod id: `fabric` for <26.1, `fabric-api` for 26.1+ (substituted into `fabric.mod.json` via `fabricApiModId`)

### Common Commands

```bash
# Build the active version
./gradlew :1.21.11:build

# Run unit tests for a specific version
./gradlew :1.21.11:test

# Run game tests (in-game integration tests)
./gradlew :1.21.11:runGameTest

# Generate IDE run configurations
./gradlew ideaSyncTask

# Switch active Stonecutter version
./gradlew "Set active version to 1.21.11"
```

## Version-Specific Code (Stonecutter Syntax)

Code uses Stonecutter conditional comments for version differences:

```java
//? if <1.19.1 {
/*oldVersionCode();
*///? } else {
newVersionCode();
//? }
```

The `src/main/java/.../versioned/` package contains helper classes that abstract API differences across MC versions (method renames, constructor changes, etc.).

## Architecture

**Mixin-based** — 4 server-side mixins hook into Allay/Entity behavior:
- `AllayBrainMixin` — multiplies follow range by config factor
- `AllayEntityMixin` — adds teleport logic on tick, overrides leash behavior
- `EntityMixin` — applies leash directional slow-down to velocity
- `LivingEntityMixin` — multiplies movement speed by config factor

**Core logic** in `allay/` package:
- `AllayTeleportBehaviour` — decision logic (distance check, safety checks for water/lava/walls, dancing state, portal cooldown)
- `AllayTeleport` — executes teleportation, looks up liked player across all worlds
- `AllayLeashBehaviour` — directional slow-down calculations

**Config** uses JSON5 format, persisted to Fabric config dir. All options are runtime-configurable via `/allayfollowalways` command (permission level 4).

## Package Layout

```
src/main/java/de/michiruf/allayfollowalways/
├── AllayFollowAlwaysMod.java    # Entry point (ModInitializer)
├── allay/                       # Core teleport + leash logic
├── command/                     # In-game command interface
├── config/                      # Config model + JSON5 persistence
├── helper/                      # Math, debug, world comparison utils
├── mixin/                       # Server-side bytecode hooks
└── versioned/                   # Version-abstracted MC API wrappers
```

## Testing

- **Unit tests**: JUnit Jupiter, under `src/test/`
- **Game tests**: Fabric game test framework under `src/gametest/`, requires FakePlayer API (MC 1.19.4+)
- CI runs all 29 versions in parallel via GitHub Actions

## Adding a New Minecraft Version

1. Add version entry to `build.versions.gradle` with java, mappings, fabric, fabricApi values
2. Update versioned helper classes in `versioned/` if MC APIs changed
3. Add version to CI matrices in `.github/workflows/build.yml` and `test.yml`
4. Add version to the version lists in `.claude/skills/*/SKILL.md`
