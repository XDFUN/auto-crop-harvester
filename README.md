<div align="center">
  <h1>Auto Crop Harvester</h1>
  <img src="resources/icon.png" width="20%" height="20%" alt="The icon mod. Showing minecraft wheat plant on soil, overlaid with an icon of hands holding a gear with a checkmark.">
</div>

A fabric client-side mod to automatically harvest and replant crops in player proximity.

## Installation

### Modrinth

Id: h6cMODvx\
Slug: auto-crop-harvester\
URL: https://modrinth.com/mod/auto-crop-harvester

### CurseForge

Id: 1208878\
Slug: auto-crop-harvester\
URL: https://www.curseforge.com/minecraft/mc-mods/auto-crop-harvester

### Github

1. Download the `.jar` file from the release.
2. Place the `.jar` file into the `.mincraft/mods` folder
   - Windows: `%APPDATA%\.minecraft\mods`
   - Linux: `/home/user/.minecraft/mods`
3. Check that the mods `Fabric Api` and `Fabric Language Kotlin` are installed.
4. Done

For more information on how to install fabric see: https://wiki.fabricmc.net/install

## How to use

To auto harvest just walk over the crop.

To auto plant either break the crop with the harvester or manually while the `auto_plant` option for either is enabled.\
The seed of the harvested crop must be somewhere in the inventory for it to work.\
The seeds are searched in the following order: Main Hand -> OffHand -> Hotbar -> Inventory

## Supported Crops

- Wheat
- Potato
- Carrot
- Beetroot
- Sugarcane
- Netherwart

## Commands

### Configuration

Commands to configure the mod always follow this structure `/auto-crop-harvester config <section> <option> <get | set | reset> <set:value>`.

#### Methods

- `get`: Returns the current configured value
- `set`: Sets the value
- `reset`: Resets the value to it's default

#### Sections

##### Harvester

| Name                   | Description                                            | Default Value | Allowed Values     |
|------------------------|--------------------------------------------------------|---------------|--------------------|
| auto_harvest           | Enables the auto harvester                             | `true`        | `true` and `false` |
| sneak_auto_harvest     | Enables the auto harvester while sneaking              | `false`       | `true` and `false` |
| premature_auto_harvest | Enables the auto harvester for premature crops         | `false`       | `true` and `false` |
| auto_plant             | Configures if auto harvested crops should be replanted | `true`        | `true` and `false` |
| auto_harvest_radius    | The radius around the player which gets harvested      | `1`           | `0` up to `4.5`    |

##### Player

| Name                 | Description                                                  | Default Value | Allowed Values     |
|----------------------|--------------------------------------------------------------|---------------|--------------------|
| auto_plant           | Enables if player broken crops should be replanted           | `true`        | `true` and `false` |
| premature_auto_plant | Enables if player broken premature crops should be replanted | `false`       | `true` and `false` |