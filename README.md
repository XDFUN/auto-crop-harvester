<div align="center">
  <h1>Auto Crop Harvester</h1>
  <img src="resources/icon.png" width="20%" height="20%">
</div>

A fabric client-side mod to automatically harvest and replant crops in player proximity.

## Installation

### Modrinth

Id: h6cMODvx\
Slug: auto-crop-harvester\
URL: https://modrinth.com/mod/auto-crop-harvester

### Github

1. Download the `.jar` from the release.
2. Place the jar into the `.mincraft/mods` folder
3. Check that the mods `Fabric Api` and `Fabric Language Kotlin` are installed.
4. Done

For more information on how to install fabric see: https://wiki.fabricmc.net/install

## How to use

To auto harvest just walk over the crop.

To auto plant you need to have the seed of the harvested crop somewhere in the inventory.\
The seeds are searched in the following order: Main Hand -> Off Hand -> Hotbar -> Inventory
> Currently only auto harvested crops are replanted, player broken crops are ignored.

## Supported Crops

- Wheat
- Potato
- Carrot
- Beetroot
- Sugarcane
- Netherward

## Commands

- `/auto-crop-harvester config auto_harvest set [true|false]`
  : Enables or disables auto harvesting

- `/auto-crop-harvester config auto_harvest get`
  : Returns if auto harvesting is enabled or disabled.

- `/auto-crop-harvester config auto_harvest reset`
  : Resets auto harvesting to it's default value (`true`)

- `/auto-crop-harvester config sneak_auto_harvest set [true|false]`
  : Enables or disables auto harvesting when sneaking

- `/auto-crop-harvester config sneak_auto_harvest get`
  : Returns if auto harvesting when sneaking is enabled or disabled

- `/auto-crop-harvester config sneak_auto_harvest reset`
  : Resets auto harvesting when sneaking to it's default value (`false`)

- `/auto-crop-harvester config premature_auto_harvest set [true|false]`
  : Enables or disables auto harvesting of premature crops

- `/auto-crop-harvester config premature_auto_harvest get`
  : Returns if auto harvesting of premature crops is enabled or disabled

- `/auto-crop-harvester config premature_auto_harvest reset`
  : Resets auto harvesting of premature crops to it's default value (`false`)

- `/auto-crop-harvester config auto_plant set [true|false]`
  : Enables or disables auto planting of auto harvested crops

- `/auto-crop-harvester config auto_plant get`
  : Returns if auto planting is enabled or disabled

- `/auto-crop-harvester config auto_plant reset`
  : Resets auto planting to it's default value (`true`)