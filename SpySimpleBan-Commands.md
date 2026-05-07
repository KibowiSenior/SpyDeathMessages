# SpySimpleBan Plugin - Complete Command Documentation

## Plugin Information
- **Name:** SpySimpleBan
- **Version:** 1.0.0
- **Description:** Simple ban plugin with IP and UUID banning
- **Author:** spy
- **API Version:** 1.21

## Available Commands

### Ban Commands

#### `/ban <player> <reason>`
- **Description:** Permanently ban a player by name, IP, and UUID
- **Permission:** `spy.admin`
- **Usage Example:** `/ban PlayerName Griefing the spawn area`
- **Features:**
  - Bans by name, IP address, and UUID simultaneously
  - Works on both online and offline players
  - For online players: Immediately kicks with ban message
  - For offline players: Creates offline ban that updates IP/UUID on next join attempt
  - Broadcasts ban message to all players
  - Permanent ban (no expiration)

#### `/tempban <player> <time> <reason>`
- **Description:** Temporarily ban a player by name, IP, and UUID
- **Permission:** `spy.tempban`
- **Usage Example:** `/tempban PlayerName 1h Spamming in chat`
- **Time Format:**
  - `s` = seconds (e.g., `30s`)
  - `m` = minutes (e.g., `15m`)
  - `h` = hours (e.g., `2h`)
  - `d` = days (e.g., `7d`)
  - Can combine: `1d12h30m` for 1 day, 12 hours, 30 minutes
- **Features:**
  - Same IP/UUID banning as permanent ban
  - Automatically expires after specified time
  - Shows duration in kick message
  - Auto-cleanup removes expired bans

#### `/unban <player>`
- **Description:** Remove a ban from a player
- **Permission:** `spy.unban`
- **Usage Example:** `/unban PlayerName`
- **Features:**
  - Removes all ban data (name, IP, UUID)
  - Works for both permanent and temporary bans
  - Broadcasts unban message to all players
  - Returns error if player is not banned

#### `/banlist`
- **Description:** Display all currently active banned players
- **Permission:** `spy.banlist`
- **Usage Example:** `/banlist`
- **Features:**
  - Shows only active (non-expired) bans
  - Displays total ban count
  - For each ban shows:
    - Player name
    - Ban reason
    - Who banned them
    - Ban date and time
    - Ban type (Permanent/Temporary)
    - For temporary bans: expiration date and time remaining
  - Formatted with colors for easy reading

### Mute Commands

#### `/mute <player> <reason>`
- **Description:** Permanently mute a player
- **Permission:** `spy.mute`
- **Usage Example:** `/mute PlayerName Using inappropriate language`
- **Features:**
  - Prevents player from sending chat messages
  - Works on both online and offline players
  - For online players: Immediately notifies them of mute
  - For offline players: Creates offline mute that updates UUID on next join
  - Broadcasts mute message to all players
  - Permanent mute (no expiration)

#### `/tempmute <player> <time> <reason>`
- **Description:** Temporarily mute a player
- **Permission:** `spy.tempmute`
- **Usage Example:** `/tempmute PlayerName 30m Spamming in chat`
- **Time Format:**
  - `s` = seconds (e.g., `30s`)
  - `m` = minutes (e.g., `15m`)
  - `h` = hours (e.g., `2h`)
  - `d` = days (e.g., `7d`)
  - Can combine: `1h30m` for 1 hour, 30 minutes
- **Features:**
  - Same mute functionality as permanent mute
  - Automatically expires after specified time
  - Shows duration in mute message
  - Auto-cleanup removes expired mutes every second
  - Works on both online and offline players
  - For online players: Immediately notifies them with duration
  - Broadcasts mute message with duration to all players

#### `/unmute <player>`
- **Description:** Remove a mute from a player
- **Permission:** `spy.mute`
- **Usage Example:** `/unmute PlayerName`
- **Features:**
  - Removes all mute data
  - Broadcasts unmute message to all players
  - Returns error if player is not muted
  - Player can immediately chat again

#### `/mutelist`
- **Description:** Display all currently muted players
- **Permission:** `spy.mutelist`
- **Usage Example:** `/mutelist`
- **Features:**
  - Shows all active mutes
  - Displays total mute count
  - For each mute shows:
    - Player name
    - Mute reason
    - Who muted them
    - Mute date and time
  - Formatted with colors for easy reading

### Warning Commands

#### `/warn <player> <reason>`
- **Description:** Issue a warning to a player
- **Permission:** `spy.warn`
- **Usage Example:** `/warn PlayerName Breaking server rules`
- **Features:**
  - Records warning in player's history
  - Works on both online and offline players
  - For online players: Shows formatted warning message with total count
  - Notifies all online staff members
  - Warnings accumulate (no automatic removal)
  - Displays warning count to warned player

#### `/unwarn <player>`
- **Description:** Remove all warnings from a player
- **Permission:** `spy.unwarn`
- **Usage Example:** `/unwarn PlayerName`
- **Features:**
  - Removes ALL warnings from the specified player
  - Shows how many warnings were removed
  - Notifies target player if online
  - Staff-only notifications (no public broadcast)
  - Returns error if player has no warnings

#### `/warnlist <player>`
- **Description:** Display all warnings for a specific player
- **Permission:** `spy.warnlist`
- **Usage Example:** `/warnlist PlayerName`
- **Features:**
  - Shows all warnings for the specified player
  - Displays total warning count
  - For each warning shows:
    - Warning reason
    - Who issued the warning
    - Warning date and time
  - Works for both online and offline players
  - Returns message if player has no warnings

## Permission System

### Main Permissions
- `spy.admin` - Master permission (grants access to all commands)
- `spy.tempban` - Allows /tempban command
- `spy.unban` - Allows /unban command
- `spy.mute` - Allows /mute and /unmute commands
- `spy.tempmute` - Allows /tempmute command
- `spy.warn` - Allows /warn command
- `spy.banlist` - Allows /banlist command
- `spy.unwarn` - Allows /unwarn command
- `spy.warnlist` - Allows /warnlist command
- `spy.mutelist` - Allows /mutelist command

### Permission Details
- All permissions default to `op` level
- `spy.admin` includes all other permissions as children
- `/ban` command requires `spy.admin` permission directly
- Staff members need appropriate permissions to use moderation commands

## Data Storage

### File Locations
- **Bans:** `plugins/SpySimpleBan/bans.json`
- **Mutes:** `plugins/SpySimpleBan/mutes.json`
- **Warnings:** `plugins/SpySimpleBan/warns.json`

### Data Features
- JSON format for easy reading and backup
- Automatic data saving on all changes
- Thread-safe operations
- Automatic expired ban and mute cleanup every second
- UUID enrichment for offline players when they join

## Special Features

### IP and UUID Banning
- Bans apply to player name, IP address, and UUID
- Prevents ban evasion through alt accounts on same IP
- UUID banning prevents name changes from bypassing bans

### Offline Player Support
- All commands work on offline players
- Data is enriched with IP/UUID when player joins
- Accurate targeting using stored player data

### Auto-Expiration
- Temporary bans automatically expire
- Temporary mutes automatically expire
- Expired bans and mutes are cleaned up every second
- No manual intervention needed for timed punishments

### Staff Notifications
- Warn command notifies all online staff
- Unwarn command sends staff-only notifications
- Broadcasting keeps staff informed of moderation actions

### Chat Integration
- Muted players cannot send chat messages
- Chat listener prevents muted player messages
- Real-time mute checking during chat events

### Error Handling
- Clear error messages for invalid usage
- Permission checks with user-friendly messages
- Validation for all command arguments
- Graceful handling of offline player operations