# NewDayNotifier

Simple Minecraft plugin for Paper that displays how many in-game days have passed since world creation.  
And also this is my first Java project, so yeah.

## Requirements
- Minecraft Paper 1.21.10
- Java 21
- Gradle (for building)

## Building
To build the plugin, make sure you have Gradle installed and run:

```bash
./gradlew build
````

The compiled `.jar` file will be located in `build/libs/`.

## Installing on server

1. Go to [Releases](https://github.com/kararasenok-gd/NewDayNotifier/releases) tab
2. Download `.jar` from any release

## Supported server versions

| Version | Support |
|---------|---------|
| 1.21.10 | ✅       |

## Configuration

After installing the plugin, a `config.yml` file will be generated in the plugin folder.  
Here’s an example configuration with explanations:

```yaml
# NewDayNotifier config
# by @kararasenok_gd

settings:
  # Where new day should be displayed
  display: "actionbar" # options: actionbar, chat, title

  # Name of the world where the plugin should track days
  world: "world"

  # Play sound when a new day starts
  sound: true

messages:
  # Messages to send when a new day comes. %day% = current day
  actionbar:
    text: "&eDay &6%day%" # Text for action bar display

  chat:
    text: "&eDay &6%day%" # Text for chat display

  title:
    text: "New day!"       # Main title text
    subtext:
      enabled: true         # Show subtext under main title
      text: "Current day: %day%" # Subtext content
````

### Explanation:

* **settings.display** — choose how the day message is shown (`actionbar`, `chat`, or `title`)
* **settings.world** — specify which world to track
* **settings.sound** — enable/disable sound when a new day starts
* **messages** — customize the text for each display type, `%day%` will be replaced with the current in-game day

## Found a bug or want to suggest something?

[Open an issue](https://github.com/kararasenok-gd/NewDayNotifier/issues) and write anything you want to report or suggest to add!