# Beyond Origins

2D game written in Java. The intention is for this to be an RPG, with some puzzle elements. Right now, the focus is on creating mechanics and a framework for being able to properly implement a story and gameplay sometime in the future. See the "Status" section for an outline of what is currently in the game and working. Below is a video showcasing some of these, as well as a link to the JavaDoc.

Demo video: [https://youtu.be/9yA8Gh5eoog](https://youtu.be/9yA8Gh5eoog)

Documentation: [https://egartley.net/docs/beyond-origins](https://egartley.net/docs/beyond-origins)

![screenshot](https://raw.githubusercontent.com/egartley/media/master/screenshots/beyond-origins.png)

More screenshots are available at [egartley.net](https://egartley.net/projects/beyond-origins/?via=gh).

Earlier iterations of the game from 2013 through 2015 can be found [here](https://github.com/egartley/archive/tree/master/Java/Beyond%20Origins/Source) as well.

## Status
These are the features or game mechanics that have been implemented so far as of commit [4303e75](https://github.com/egartley/beyond-origins/commit/4303e753c032fe5f990ac8aa04fda08eee88d607). A more technical outline may be added in the future, but the one below is meant for anyone to be able to understand.
- Core
  - Entities, either animated or static (non-animated)
    - Includes the player, other characters and objects
  - Collision detection between entities
    - The "side" in which the collision occured is calculated
  - Maps, which are like levels or worlds
    - These maps have different areas called sectors
  - Management of keyboard and mouse input
  - Debug mode, which displays technical information
  - Ability to "wait" an amount of time before doing something
  - Characters/NPCs can talk to the player
    - Dialogue is shown in a box at the bottom of the screen
    - The full text of the dialogue is automatically broken into lines
    - If there is more than can be displayed, press space to advance
  - User interface elements, such as the inventory panel or buttons
    - Can be clicked and enabled/disabled
  - Notification banners that show for a short period of time
    - Slide in/out at the top of the screen
    - Can have an icon, or small image, and text
  - Basic saving and loading
- Player
  - Controlled with WASD keys (up, left, down, right)
  - Can move between different maps and sectors (described above)
  - Ability to attack other entities that are damageable
  - Gain experience and level up
- Inventory
  - Accessed by pressing E
  - Can hold different items
  - These items can be re-arranged in slots
  - They can also be dropped by dragging them out
  - Dropped items can be picked up again
  - Tooltips are shown upon hovering over the item
- Quests
  - Accessed by pressing E and clicking the quests tab
  - Given to the player by talking to NPCs or other means
  - A list of objectives, or tasks, to complete
  - User interface to view active quests and their objectives

## Additional Information

Tools:
[IntelliJ IDEA](https://www.jetbrains.com/idea/)
[Paint.NET](https://www.getpaint.net/)

Libraries:
[Slick2D](http://slick.ninjacave.com/)
[JSON-java](https://github.com/stleary/JSON-java/)
