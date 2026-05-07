# SpyDeathMessages

A fully-featured, advanced Minecraft death messages plugin built with the Spigot API (1.21).  
All old SpySimpleBan code has been removed and replaced entirely with this plugin.

## Features

- **16+ death causes** with individual message configs:  
  `killed`, `killed_by_mob`, `fall`, `drowned`, `burned`, `lava`, `starved`, `suffocated`,  
  `void`, `magic`, `poison`, `wither`, `explosion`, `cactus`, `lightning`, `freeze`, `projectile`, `generic`
- **4 delivery channels per cause**: chat, actionbar, title, subtitle (each independently enable/disable)
- **Random messages** — provide multiple lines in any list and one is chosen at random
- **Full colour support**: hex colours (`#ff1313`) and legacy `&` colour codes side-by-side
- **Customisable broadcast**: `global`, `world`, or `radius` mode
- **Configurable title animation**: fade-in, stay, fade-out in ticks
- **Toggle** death messages on/off at runtime (persists until next restart)
- **Placeholders**: `%victim%`, `%attacker%`, `%weapon%`, `%world%`
- **Cancel vanilla death message** option
- **Tab-complete** on all sub-commands

## Commands

| Command | Permission | Description |
|---|---|---|
| `/dm reload` | `spydm.reload` | Reload config.yml |
| `/dm toggle` | `spydm.toggle` | Toggle messages on/off |
| `/dm help` | — | Show help |

Alias: `/deathmessages`

## Project Structure

```
src/main/java/com/spy/spydeathmessages/
  SpyDeathMessages.java                  ← Main plugin class
  commands/DeathMessagesCommand.java     ← /dm command
  listeners/DeathListener.java           ← PlayerDeathEvent handler
  util/ColorUtil.java                    ← Hex + & colour translator
  util/MessageSender.java               ← Broadcasts chat/actionbar/title/subtitle

src/main/resources/
  plugin.yml                            ← Plugin metadata & commands
  config.yml                            ← Full customisable config
```

## Building

```bash
mvn package
```

Output: `target/SpyDeathMessages.jar`

## Stack

- Java 8
- Spigot API 1.21-R0.1-SNAPSHOT
- Maven (compile + shade)
