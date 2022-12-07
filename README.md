# Allay Follow Always

Allays will follow the player that gave them an item always, even through portals.


## Behaviour

The allay will teleport when:
* TODO

Chunks will be kept loaded when:
* TODO


## Installation

To install this plugin **fabric**, **[fabric API](https://modrinth.com/mod/fabric-api)** and **[owo-lib](https://modrinth.com/mod/owo-lib)** is required.

This mod is available on [modrinth](https://modrinth.com/mod/allay-follow-always) with slug `allay-follow-always`.
The project source is available on [github/michiruf](https://github.com/michiruf/MCAllayFollowAlways) with the latest
readme [here](https://github.com/michiruf/MCAllayFollowAlways/blob/master/README.md).


## Usage instructions

Use the commands below to set values for the options of the mod.
The actual value can get queried by not specifying a new value for a specific option.

All available options will be displayed with the command `/allayfollowalways options`.
Commands require a permission level of 4.


### Configuration options

Configure the follow range factor of the allay (default 1):
```
/allayfollowalways rangeFactor [double]
```

---
Configure the allays movement speed factor (default 1):
```
/allayfollowalways movementSpeedFactor [float]
```

---
Enable teleportation if further away than `teleportDistance` (default: true, vanilla nonexistent):
```
/allayfollowalways teleportEnabled [boolean]
```

---
Set the minimum distance for the allay to teleport to the player (default: 65): 
```
/allayfollowalways teleportDistance [float]
```

---
If this is set to true, the default minecraft teleportation cooldown for portals will be used also for the teleportation of the allay.
A side effect is, that chunks may have to be loaded a little bit longer because the allay may not follow the player immediately after
switching dimensions. 
Reasons to set this to true might be, that it is possible to push allays through portals. If set to false, they will be teleported back immediately. (default: false)
```
/allayfollowalways considerEntityTeleportationCooldown [boolean]
```

---
If set, the allay will not teleport when it is dancing (default: true):
```
/allayfollowalways teleportWhenDancing [boolean]
```

---
If set, the allay will not teleport to a player that is touching water (default: true):
```
/allayfollowalways avoidTeleportingIntoWater [boolean]
```

---
If set, the allay will not teleport to a player that is in lava (default: true):
```
/allayfollowalways avoidTeleportingIntoLava [boolean]
```

---
If set, the allay will not teleport to a player that inside a wall (default: true):
```
/allayfollowalways avoidTeleportingIntoWalls [boolean]
```

---
In 1.19 there are several reports that are about leash breakings for allays. For example:
* https://bugs.mojang.com/browse/MC-252866
* https://bugs.mojang.com/browse/MCPE-158955

If that happens, enable this flag. Since this should always happen and some people might
upgrade without reading the docs, this flag is initially set to true.

This flag will make allays follow a leash and by that, not try to follow the player,
because this caused them to move to far from the post they were bound to and so broke
the leash.
```
/allayfollowalways fixLeashBreakingIn_1_19_followLeash [boolean]
```


## License

[MIT License](https://github.com/michiruf/MCAllayFollowAlways/blob/master/LICENSE)


## Additional reading (for devs)

* [Fabric wiki](https://fabricmc.net/wiki/start)
* [oωo configuration documentation](https://docs.wispforest.io/owo/config/)
* [List of fabric events](https://docs.wispforest.io/fabric-events/)
