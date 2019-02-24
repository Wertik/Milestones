# Milestones
KMR - Future milestone system prototype

GOAL:
  Making it possible to create your own statistics, in here so called "Milestones".
  
CONDITIONS:
  - tools == tools in the main hand.
  - in-inventory == item type in players inventory
  - targets == block/mob type of the target
  - biomes == biome type list
  - regions == list of region names
  - worlds == list of worlds names the milestone is counted in

 PLANNED:
  - block under the player.. idk..
  - time spent in specific conditions

OPTIONS:
  - log-only-once == count the milestone only once
  - global-milestone == boolean, the milestone counts for all of the players together

REWARDS:
  - inform-message == string, the message only the player gets
  - broadcast-message == string, the message all of the players get
  - commands == list<string>, list of commands that run after completing the milestone
  - items == list of items the player gets

 STAGED REWARDS:
  - "cumulative" rewards, after a certain amount of points, set a reward.
  
LOGGED EVENTS:
  - entityDeath
  - blockBreak: block type (target)
  - blockPlace: block type (tool)
  - playerJoin
  - playerQuit
  - playerChat: message (target)

PLANNED:
  - bedEvent
  - changeWorld: worlds (target)
  - pickupEvent: block type (target)
  - shearEvent: color of the sheep as a target?
  - fishEvent
  - voteEvent (votifier hook up)

PLACEHOLDER(S) FOR PLACEHOLDER API:
  - %milestones_(name)% == fill the name of the milestone, displays the score

PLANS:
  - First of all, establishing the core system, then adding up stuff.
  - adding lang.yml to achieve the top level of customisation

ABOUT:
  I'm still in the early process of learning Java. My structures are simple and made out of basic syntaxes, easy to understand.
  If you find anything that i could improve, just pm. :^^
