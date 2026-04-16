# Vanity Plates

A simple Server-side mod for "vanity" prefixes that users can assign via a friendly and easy to use in-game GUI.

Simply configure your user-assignable prefixes in the configuration file, and assign your users the 
permission node via LuckPerms, and the user can now Set that as a personal prefix.

## Requirements
- [GooeyLibs v3.1.1-1.21.x+](https://modrinth.com/mod/gooeylibs)
- [Luckperms 5.4](https://modrinth.com/mod/luckperms)

## Configuration

> **Note:** Only Prefixes are currently supported.

The configuration file will be located at `./config/vanity_plates/config.json` and by default will contain the following:

```json
{
  "prefixPriority": 1000,
  "availablePlates": [
    {
      "displayItem": "minecraft:paper",
      "label": "Demo",
      "requiredPermission": "demo.plate",
      "prefix": "[Demo]"
    }
  ]
}
```

### LuckPerms setup

In order for your vanity plates to be displayed, you will need a chat mod that adds luckperms prefixes to the chat, as well
as to configure your luckperms to include `highest_own` as an available prefix part in the formatting.

In your `./config/luckperms/luckperms.conf` file in the `meta-formatting` section ensure the following:

```conf
meta-formatting {
  prefix {
    format = [
      "highest_on_track_base",
      "highest_own" # Add this in the prefix section
    ]
  }
}
```

## Usage / Commands

To allow users to use any of the configured available prefixes, all you have to do is assign the user/group the `requiredPermission` you assigned in the config.

- `/vanity` - Opens the Vanity Plates GUI (Available to all users)
- `/plates` - An Alias for `vanity`
- `/vanity reload` - Reloads the Vanity Plates config (Requires OP)
- `/plates reload` - an Alias for `vanity reload` 