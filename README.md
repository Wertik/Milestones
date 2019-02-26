# Milestones
KMR - Future milestone system prototype

GOAL:
  Making it possible to create your own statistics, in here so called "Milestones".
  
CONDITIONS:
  - toolTypes == tools in the main hand.
  - inInventory == item type in players inventory
  - targetType == block/mob type of the target
  - biomeType == biome type list
  - regionNames == list of region names

 PLANNED:
  - block under the player.. idk..

OPTIONS:
  - log-only-once == count the milestone only once, doesn't work on global milestones currently
  - global-milestone == boolean, the milestone counts for all of the players together

REWARDS:
  - inform-message == string, the message only the player gets
  - broadcast-message == string, the message all of the players get
  - consoleCommands == list<string>, list of consoleCommands that run after completing the milestone
  - items == list of items from data storage the player gets

 STAGED REWARDS:
  - "cumulative" rewards, after a certain amount of points, set a reward.
  
LOGGED EVENTS:
  - entityDeath
  - blockBreak
  - blockPlace
  - playerJoin
  - playerQuit
  - playerChat: messages (targetType)

PLANNED:
  - bedEvent
  - changeWorld: world-names (targetType)
  - pickupEvent
  - shearEvent: color of the sheep?
  - fishEvent
  - voteEvent (votifier hook up)

PLACEHOLDER(S):
  - %milestones_(name)% == fill the name of the milestone, displays the score per player, global scores not working currently

PLANS:
  - First of all, establishing the core system, then adding up stuff.
  - adding lang.yml to achieve the top level customisation

ABOUT:
  I'm still in the early process of learning Java. My structures are simple and made out of basic consoleCommands and syntaxes, easy to understand.
  If you find anything that i could improve, just pm. :^^
