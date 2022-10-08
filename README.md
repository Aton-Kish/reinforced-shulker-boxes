![Reinforced Shulker Boxes](./images/header.png)

[![Mod Loader: Fabric](https://img.shields.io/static/v1?label=modloader&message=fabric&color=brightgreen)](https://www.curseforge.com/minecraft/mc-mods/fabric-api)
![Mod Environment](https://img.shields.io/static/v1?label=environment&message=client%2Fserver&color=yellow)
[![Downloads](https://raw.githubusercontent.com/Aton-Kish/mcmod-stats/main/reinforced-shulker-boxes/downloads.svg)](https://www.curseforge.com/minecraft/mc-mods/reinforced-shulker-boxes)
[![MIT License](https://img.shields.io/static/v1?label=licence&message=MIT&color=blue)](./LICENSE)
[![build](https://github.com/Aton-Kish/reinforced-shulker-boxes/actions/workflows/build.yaml/badge.svg?branch=1.17)](https://github.com/Aton-Kish/reinforced-shulker-boxes/actions/workflows/build.yaml?query=branch:1.17)

# Reinforced Shulker Boxes

The Reinforced Shulker Boxes mod adds reinforced shulker boxes.

[<img alt="Requires Fabric API" src="https://i.imgur.com/Ol1Tcf8.png" width="128"/>](https://www.curseforge.com/minecraft/mc-mods/fabric-api)

## Reinforced Storage Mod Series

- [Reinforced Chests](https://github.com/Aton-Kish/reinforced-chests)
- [Reinforced Barrels](https://github.com/Aton-Kish/reinforced-barrels)

## Recipe

| Name                  | Type            | Ingredients                           | Recipe                                                                                                           | Description                                                                                 |
| --------------------- | --------------- | ------------------------------------- | ---------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------- |
| Copper Shulker Box    | Shaped Crafting | Shulker Box + Copper Ingot            | <img alt="Copper Shulker Box Recipe" src="./images/recipes/copper_shulker_box.png" width="256" />                | A copper shulker box has 45 inventory slots.                                                |
| Iron Shulker Box      | Shaped Crafting | Copper Shulker Box + Iron Ingot       | <img alt="Iron Shulker Box Recipe" src="./images/recipes/iron_shulker_box.png" width="256" />                    | An iron shulker box has 54 inventory slots.                                                 |
| Gold Shulker Box      | Shaped Crafting | Iron Shulker Box + Gold Ingot         | <img alt="Gold Shulker Box Recipe" src="./images/recipes/gold_shulker_box.png" width="256" />                    | A gold shulker box has 81 inventory slots.                                                  |
| Diamond Shulker Box   | Shaped Crafting | Gold Shulker Box + Diamond            | <img alt="Diamond Shulker Box Recipe" src="./images/recipes/diamond_shulker_box.png" width="256" />              | A diamond shulker box has 108 inventory slots.                                              |
| Netherite Shulker Box | Smithing        | Diamond Shulker Box + Netherite Ingot | <img alt="Netherite Shulker Box Recipe" src="./images/recipes/netherite_shulker_box_smithing.png" width="256" /> | A netherite shulker box has 108 inventory slots. This is resistant to blast, fire and lava. |

### Crafting from [Reinforced Chests](https://github.com/Aton-Kish/reinforced-chests)

| Name                  | Ingredients                     | Recipe                                                                                                                       |
| --------------------- | ------------------------------- | ---------------------------------------------------------------------------------------------------------------------------- |
| Copper Shulker Box    | Copper Chest + Shulker Shell    | <img alt="Copper Shulker Box Recipe" src="./images/recipes/copper_shulker_box_from_copper_chest.png" width="256" />          |
| Iron Shulker Box      | Iron Chest + Shulker Shell      | <img alt="Iron Shulker Box Recipe" src="./images/recipes/iron_shulker_box_from_iron_chest.png" width="256" />                |
| Gold Shulker Box      | Gold Chest + Shulker Shell      | <img alt="Gold Shulker Box Recipe" src="./images/recipes/gold_shulker_box_from_gold_chest.png" width="256" />                |
| Diamond Shulker Box   | Diamond Chest + Shulker Shell   | <img alt="Diamond Shulker Box Recipe" src="./images/recipes/diamond_shulker_box_from_diamond_chest.png" width="256" />       |
| Netherite Shulker Box | Netherite Chest + Shulker Shell | <img alt="Netherite Shulker Box Recipe" src="./images/recipes/netherite_shulker_box_from_netherite_chest.png" width="256" /> |

### Dyeing

| Name                       | Ingredients                               |
| -------------------------- | ----------------------------------------- |
| Any Reinforced Shulker Box | Any Reinforced Shulker Box + Matching Dye |

Dyed reinforced shulker boxes can be undyed using a cauldron.

## Configure

[The Reinforced Core lib](https://github.com/Aton-Kish/reinforced-core) has been integrated with [Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu) since version 3.0.0.

![Mod Menu](./images/modmenu/modmenu.png)

### Screen Type

_Available in Reinforced Shulker Boxes mod version 2.1.0+._

Screen type is `SINGLE` or `SCROLL`. (default: `SINGLE`)

| `SINGLE` screen                               | `SCROLL` screen                                |
| --------------------------------------------- | ---------------------------------------------- |
| ![Single Screen](./images/modmenu/single.png) | ![Scroll Screen](./images/modmenu/scroll6.png) |

### Scroll Screen

#### Rows

_Available in Reinforced Shulker Boxes mod version 2.1.0+._

Rows is an integer in the range from `6` to `9`. (default: `6`)

| Rows: `6`                                              | Rows: `9`                                              |
| ------------------------------------------------------ | ------------------------------------------------------ |
| ![Scroll Screen: 6 rows](./images/modmenu/scroll6.png) | ![Scroll Screen: 9 rows](./images/modmenu/scroll9.png) |

## License

The Reinforced Shulker Boxes mod is licensed under the MIT License, see [LICENSE](./LICENSE).
