DEFAULT_MAPS = [
"DefaultSmall",
"DefaultMedium",
"DefaultLarge",
"DefaultHuge",
"AceOfSpaces",
"Alien",
"Ambush",
"Battlecode24",
"BigDucksBigPond",
"Canals",
"CH3353C4K3F4CT0RY",
"Duck",
"Fountain",
"Hockey",
"MazeRunner",
"Rivers",
"Snake",
"Soccer",
"SteamboatMickey",
"Yinyang"
]
MAPS_TO_RUN = DEFAULT_MAPS + [
#"example_disabled_map",
"anthole",
"asteroids",
"attackmicro",
"bananas",
"castle",
"checkerb",
"clout",
"cookie jar",
"crosshatch",
"flappy bird",
"funnySpawns1",
"lanes",
"lol",
#"maze1",
"maze2",
"meanders",
"ninjastar",
"pathfind1",
"pie slice",
"pokemon battle",
"rails",
"secret passage",
"tetris",
"water wall",
"wtf3"
]

for m in MAPS_TO_RUN:
    ct = 0
    with open(f"waxthebuilder_waxthebuilder_{m}.txt") as f:
        for line in f:
            if "Bytecode" in line:
                ct += 1
    try:
       pass
    except:
        print("error")
                
    print(f"{m} {ct}")