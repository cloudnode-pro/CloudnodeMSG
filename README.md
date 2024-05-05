# CloudnodeMSG

A Minecraft Java plugin for private messages.

Supports message channels (so you don't have to use a command for every DM) and integrates with vanish plugins.

[See the default config](https://github.com/cloudnode-pro/CloudnodeMSG/blob/main/src/main/resources/config.yml)

## Commands

The following are all commands from this plugin.

***

### `/msg <player> <message>`

Send a private message to another player.

<dl>
    <dt>Aliases:</dt> <dd><code>/message</code>, <code>/tell</code>, <code>/t</code>, <code>/whisper</code>, <code>/dm</code>, <code>/m</code>, <code>/pm</code></dd>
    <dt>Permission:</dt> <dd><code>cloudnodemsg.use</code></dd>
</dl>

***

### `/msg <player>`

Create a private message channel to a player;
i.e. all of your chat messages will be sent as private messages to this player.
Run the command again to disable the message channel.

<dl>
    <dt>Aliases:</dt> <dd><code>/message</code>, <code>/tell</code>, <code>/t</code>, <code>/whisper</code>, <code>/dm</code>, <code>/m</code>, <code>/pm</code></dd>
    <dt>Permission:</dt> <dd><code>cloudnodemsg.use</code></dd>
</dl>

***

### `/reply <message>`

Send a private message to the last player that messaged you.

<dl>
    <dt>Aliases:</dt> <dd><code>/r</code>, <code>/re</code></dd>
    <dt>Permission:</dt> <dd><code>cloudnodemsg.use</code></dd>
</dl>

***

### `/teammsg <message>`

Send a private message to your [scoreboard team](https://minecraft.fandom.com/wiki/Scoreboard#Teams).

<dl>
    <dt>Aliases:</dt> <dd><code>/tm</code>, <code>/tmsg</code>, <code>/teamchat</code></dd>
    <dt>Permission:</dt> <dd><code>cloudnodemsg.use.team</code></dd>
</dl>

***

### `/teammsg`

Create a private message channel to your [scoreboard team](https://minecraft.fandom.com/wiki/Scoreboard#Teams);
i.e. all of your chat messages will be sent as private team messages.
Run the command again to disable the message channel.

<dl>
    <dt>Aliases:</dt> <dd><code>/tm</code>, <code>/tmsg</code>, <code>/teamchat</code></dd>
    <dt>Permission:</dt> <dd><code>cloudnodemsg.use.team</code></dd>
</dl>

***

### `/ignore <player>`

Ignore a player—you will stop seeing all of their messages (including public chat messages).

<dl>
    <dt>Aliases:</dt> <dd><code>/block</code></dd>
    <dt>Permission:</dt> <dd><code>cloudnodemsg.ignore</code></dd>
</dl>

***

### `/unignore <player>`

You will stop ignoring the player and will be able to see their messages again.

<dl>
    <dt>Aliases:</dt> <dd><code>/unblock</code></dd>
    <dt>Permission:</dt> <dd><code>cloudnodemsg.ignore</code></dd>
</dl>

***

### `/togglemsg`

Enable or disable receiving private messages.
When your private messages are disabled, nobody can message you, but you can still send messages.

<dl>
    <dt>Aliases:</dt> <dd><code>/toggledms</code>, <code>/togglepms</code></dd>
    <dt>Permission:</dt> <dd><code>cloudnodemsg.toggle</code></dd>
</dl>

***

### `/togglemsg <player>`

Enable or disable receiving private messages of another player.

<dl>
    <dt>Aliases:</dt> <dd><code>/toggledms</code>, <code>/togglepms</code></dd>
    <dt>Permission:</dt> <dd><code>cloudnodemsg.toggle.other</code></dd>
</dl>



***

### `/cloudnodemsg reload`

Reload the plugin configuration.

<dl>
    <dt>Aliases:</dt> <dd><code>/toggledms</code>, <code>/togglepms</code></dd>
    <dt>Permission:</dt> <dd><code>cloudnodemsg.reload</code></dd>
</dl>

## Permissions

Here is a list of the permissions used by this plugin.

| Permission                   | Description                                                             | Recommended Group |
|------------------------------|-------------------------------------------------------------------------|-------------------|
| `cloudnodemsg.use`           | Allows using the `/msg` and `/r` commands                               | default           |
| `cloudnodemsg.team`          | Allows using the `/teammsg` command                                     | default           |
| `cloudnodemsg.send.vanished` | Allows sending messages to vanished players                             | admin             |
| `cloudnodemsg.ignore`        | Allows using the `/ignore` and `/unignore` commands                     | default           |
| `cloudnodemsg.ignore.bypass` | Makes your private messages visible, even if the recipient ignored you  | admin             |
| `cloudnodemsg.toggle`        | Allows you to use the `/togglemsg` command                              | default           |
| `cloudnodemsg.toggle.other`  | Allows you to toggle msg for another player using `/togglemsg <player>` | admin             |
| `cloudnodemsg.toggle.bypass` | Allows you to send messages to players with disabled DMs                | admin             |
| `cloudnodemsg.spy`           | Players with this permission see ALL private messages and team messages | admin             |

## Release Cycle

CloudnodeMSG follows a weekly **time-based release schedule**,
with new features or changes typically released every **Tuesday**.

When we merge critical bug fixes, we may publish out-of-band releases on any day of the week.

## Report Issues

Please ensure
that you are using the [latest version](https://modrinth.com/plugin/5Ce4fxJB/version/latest) of CloudnodeMSG.
The newest bug fixes are only available in the most recent version,
and support is provided exclusively for this version.

If you encounter any problems with the plugin,
please first check
the [list of known issues](https://github.com/cloudnode-pro/CloudnodeMSG/issues?q=is%3Aopen+is%3Aissue+label%3Abug) on
our GitHub repository.
If you don’t find a similar fault listed there,
we encourage you to [submit a new issue](https://github.com/cloudnode-pro/CloudnodeMSG/issues/new?labels=bug).
Resolving bugs is the highest priority for this project.

To help us resolve your issue as quickly as possible, please provide as much relevant information as possible,
including error logs, screenshots, and detailed steps to reproduce the problem.

## Feature Requests

To suggest a new feature, please [create a new issue](https://github.com/cloudnode-pro/CloudnodeMSG/issues/new),
providing a detailed description of your idea.

## Contributing

CloudnodeMSG is licensed under the [GPL-3.0 licence](https://github.com/cloudnode-pro/CloudnodeMSG/blob/main/LICENSE).
The source code is available on [GitHub](https://github.com/cloudnode-pro/CloudnodeMSG).

New contributors are most welcome to the project. If you're interested in contributing, follow these steps:

1. [Fork the repository](https://github.com/cloudnode-pro/CloudnodeMSG/fork)
2. Create a new branch for your contributions.
3. Make your changes and ensure they align with the project’s goals.
4. Commit your changes with clear and descriptive messages.
5. Push your changes to your fork.
6. Submit a pull request.
