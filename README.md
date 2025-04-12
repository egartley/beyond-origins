# New!
The existing Java code is being converted to Python with pygame.

See the progress on the [pygame-rewrite](https://github.com/egartley/beyond-origins/tree/pygame-rewrite) branch.

# Beyond Origins

Website: [https://egartley.net/projects/beyond-origins](https://egartley.net/projects/beyond-origins/?via=gh)

Demo video: [https://youtu.be/9yA8Gh5eoog](https://youtu.be/9yA8Gh5eoog)

![screenshot](https://raw.githubusercontent.com/egartley/media/master/screenshots/beyond-origins.png)

## Status
These are the features or game mechanics that have been implemented as of commit [fd39801](https://github.com/egartley/beyond-origins/commit/fd398016d82eeaf00bb2de16c7fcfeb0ec4a6ff4)
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
