# Allay Follow Always

Allays will follow the player that gave them an item always, even through portals.


## Installation

To install this plugin **fabric**, **[fabric API](https://modrinth.com/mod/fabric-api)** is required.

This mod is available on [modrinth](https://modrinth.com/mod/allay-follow-always) with slug `allay-follow-always`.
The project source is available on [github/michiruf](https://github.com/michiruf/MCAllayFollowAlways) with the latest
readme [here](https://github.com/michiruf/MCAllayFollowAlways/blob/master/README.md).


## Usage instructions

Use the commands below to set values for the options of the mod.
The actual value can get queried by not specifying a new value for a specific option.

All available options will be displayed with the command `/allayfollowalways options`.
Commands require a permission level of 4.


### Configuration options

Configure the follow range factor of the allay by multiplying this value with the vanilla follow range of allays.
As of minecraft 1.19 this range is 64.
`rangeFactor` defaults to `1`.
```
/allayfollowalways rangeFactor [double]
```

---
Configure the allays movement speed factor, which gets multiplied by the allays vanilla movement speed.
`movementSpeedFactor` defaults to `1`.
```
/allayfollowalways movementSpeedFactor [float]
```

---
Enable teleportation if further away than `teleportDistance`. This behaviour does not exist in vanilla.
`teleportEnabled` defaults to `true`.
```
/allayfollowalways teleportEnabled [boolean]
```

---
Set the minimum distance for the allay to teleport to the player.
`teleportDistance` defaults to `65`.
```
/allayfollowalways teleportDistance [float]
```

---
If this is set to true, the default minecraft teleportation cooldown for portals will be used also for the teleportation of the allay.
A side effect is, that chunks may have to be loaded a little bit longer because the allay may not follow the player immediately after
switching dimensions. 
Reasons to set this to true might be, that it is possible to push allays through portals. If set to false, they will be teleported back immediately.
`considerEntityTeleportationCooldown` defaults to `false`.
```
/allayfollowalways considerEntityTeleportationCooldown [boolean]
```

---
If set, the allay will not teleport when it is dancing.
`teleportWhenDancing` defaults to `true`.
```
/allayfollowalways teleportWhenDancing [boolean]
```

---
If set, the allay will not teleport to a player that is touching water.
`avoidTeleportingIntoWater` defaults to `true`.
```
/allayfollowalways avoidTeleportingIntoWater [boolean]
```

---
If set, the allay will not teleport to a player that is in lava.
`avoidTeleportingIntoLava` defaults to `true`.
```
/allayfollowalways avoidTeleportingIntoLava [boolean]
```

---
If set, the allay will not teleport to a player that inside a wall.
`avoidTeleportingIntoWalls` defaults to `true`.
```
/allayfollowalways avoidTeleportingIntoWalls [boolean]
```

---
The player leash mode configures how the allay will behave:

* `NONE` - vanilla behaviour
* `FOLLOW` - the allay will follow the player (like dogs do e.g.)
* `DIRECTIONAL_SLOW_DOWN` - the allay will slow down if it is moving away from the player and being further away 
  than `leashSlowDownDistanceStart`

`playerLeashMode` defaults to `NONE`

---
The reason this leash mode got implemented for, is that allays sometimes move out of the range of a leash and so break
that leash. This can be very annoying and so the `generalLeashMode` got introduced. The allay will behave like described
for the player leash mode above.
`generalLeashMode` defaults to `DIRECTIONAL_SLOW_DOWN`

---
The leash slow down distance start is the distance to start applying directional slow down. At exactly 6 distance, no
slow down is applied and then the more distance until `leashSlowDownDistanceEnd` the slowness increases.
`leashSlowDownDistanceStart` defaults to `6`

---
The leash slow down distance end is the distance where the allay will have 0 movement speed if not facing the entity 
that it is leashed to.
`leashSlowDownDistanceEnd` defaults to `8`


## Changelog

Changelog per release cycle can be found [here](https://github.com/michiruf/MCAllayFollowAlways/blob/master/CHANGELOG.md).
This changelog contains information from one release to the next one.


## License

[MIT License](https://github.com/michiruf/MCAllayFollowAlways/blob/master/LICENSE)


## Next steps

Implement a test runner?
https://github.com/Geometrically/fabric-test-runner


## Additional reading (for devs)

* [Fabric wiki](https://fabricmc.net/wiki/start)
* [oωo configuration documentation](https://docs.wispforest.io/owo/config/)
* [List of fabric events](https://docs.wispforest.io/fabric-events/)
* TODO Check this out: https://github.com/jaredlll08/MultiLoader-Template
