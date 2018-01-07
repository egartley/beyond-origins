# Beyond Origins

RPG written in Java, which is not not using a third-party game engine; <b>everything is from scratch*</b>.

The primary objective right now is to have a viable framework, or "scaffolding", that is highly configurable and easy to modify. Once finished, an actual storyline, characers and world(s) will be added. There is currently <u>not</u> a timeline.

<img src="https://github.com/egartley/beyond-origins/blob/indev/resources/images/screenshot.png">

JavaDoc can be found <a href="https://docs.egartley.net/beyond-origins/?via=githubreadme">here</a> (updated periodically)

<h2 style="margin-top:36px">To-do List</h2>

• Allow entities to have multiple boundaries<br>
• Add sector-to-sector connections (i.e. be able to move from one map sector to another)<br>
• Make better sprites for everything (tree, grass, rock, etc.)<br>
• Fix a warning in console output for storing the dummy twice in the entity store

<h2 style="margin-top:36px">Current features</h2>

• Sprite sheets/images are loaded in, cut into rows ("strips"), and seperated<br>
• Entities, both static and animated<br>
• Collision detection<br>
• Maps, which are divided into "sectors"<br>
• Keyboard input for moving the player (WASD) and toggling debug info (F3)
• Game states<br>
• Multi-threading<br>
• 60 frames per second<br>
• Low memory usage

<h2 style="margin-top:36px">Additional Information</h2>

The latest commits will be made to the <a href="https://github.com/egartley/beyond-origins/tree/indev"><b>indev</b></a> branch, while major changes or milestones will be merged into the master branch.

This is developed with <a href="https://eclipse.org">Eclipse</a>, so you will need to get that in order to build locally. Otherwise you can just copy the <i>.java</i> files into your own workflow.

Project page: <a href="https://egartley.net/projects/beyond-origins/?via=githubreadme">https://egartley.net/projects/beyond-origins/</a>

<span style="font-size:11px">* There may be some libraries used for handling file read/write, and for JSON formatting</span>