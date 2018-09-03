# IB.ai

The new private build for the /r/IBO Discord server bot.

*To discuss collaboration, contact pants#0001.*

### Dependencies

Run-time:
 - Docker
 - Docker-Compose
 
 Compile-time:
 - Maven
 - Lombok Plugin for IDE
 
Run-time will require a properly formatted local `.toml` config 
file for the bot, a `.conf` file for Redis a valid connection to 
a Redis database via. Docker-Compose and connection
to the Discord API websocket (requires a bot token).