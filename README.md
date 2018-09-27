# Milestones
KMR - Future milestone system prototype

GOAL:
  Making it possible to create your own statistics, in here so called "Milestones".
  
CONDITIONS:
  - toolTypes == tools in the main hand.
  - inInventory == item type in players inventory
  - targetType == block/mob type of the target
  - biomeType == biome type list
 PLANNED:
  - regions, block under the player..

OPTIONS:
  - inform-player == boolean
  - inform-message == string, the message only the player gets
  - broadcast == boolean
  - broadcast-message == string, the message all of the players get
  - log-only-once == count the milestone only once, doesn't work on global milestones curently
  - global-milestone == boolean, the milestone counts for all of the players together
  - commands == list<string>, list of commands that run after completing the milestone
  
LOGGED EVENTS:
  - entityDeath
  - blockBreak
  - blockPlace
 PLANNED:
  - playerChat
  - bedEvents
  - changeWorld
  - joinEvent
  - quitEvent
  - pickupEvent
  - shearEvent
  - fishEvent

PLANS:
  - adding lang.yml to achieve the top level customisation
  - item data support (planned from the start, just wanter to setup the system first)

ABOUT:
  I'm still in the early process of learning Java. My structures are simple and made out of basic commands and syntaxes, easy to understand.
  If you find anything that i could improve, just pm. :^^
