import os
from time import time_ns, asctime
import concurrent.futures

###
# Map Batch Runner
# Place in battlecode24 directory and create trial_logs folder
# Execute by running trial.py, will summarize at the end
# Change teams, with_reverse, and maps below
# To regenerate new MAPS_TO_RUN go to the bottom and comment out runmaps() and uncomment mapnames()
###

TEAM1 = "bobthebuilder"
TEAM2 = "bobnoheal"
WITH_REVERSE = True
SAVE_LOCATION = "./trial_logs/"
COMMAND = "gradlew"
#COMMAND = "./gradlew"
MAX_WORKERS = 4

# when run mapnames() replace between example and closing bracket
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
"bananas",
"castle",
"checkerb",
#"cookie jar",
"cout for clout",
"crosshatch",
#"flappy bird",
"lanes",
"lol",
#"maze1",
"maze2",
"meanders",
"ninjastar",
"pieslice",
"pokemon battle",
"rails",
"secret passage",
"tetris",
"water wall",
#"wtf3"
]

def runmaps():
    timestamp = time_ns()
    shortname = f"{TEAM1}_{TEAM2}_{timestamp}"
    subfolder = f"{SAVE_LOCATION}{shortname}"
    os.system(f"mkdir \"{subfolder}\"")
    
    summary = runmatch(TEAM1, TEAM2, subfolder) + "\n"
    if WITH_REVERSE:
        summary += runmatch(TEAM2, TEAM1, subfolder)
    with open(f"{SAVE_LOCATION}{shortname}.txt", "w") as file:
        file.write(asctime()+"\n")
        file.write(summary)
    
def runmatch(team1, team2, subfolder):
    t1wins = 0
    t2wins = 0
    header = f"-------------------- {team1} vs. {team2} --------------------\n"
    body = ""
    queue = []
    
    with concurrent.futures.ProcessPoolExecutor(max_workers=MAX_WORKERS) as ex:
        ct = len(MAPS_TO_RUN)
        results = [ex.submit(runsingle, (team1, team2, map_name, subfolder)) for map_name in MAPS_TO_RUN]
        
        for future in concurrent.futures.as_completed(results):
            t1, t2, b = future.result()
            t1wins += t1 
            t2wins += t2
            body += b
            ct -= 1
            if ct:
                print(f"{ct} maps remain")
            else:
                print("all complete")

    header += f"result: {team1} ({t1wins} - {t2wins}) {team2}\n"
    return header + body
    
def runsingle(arg):
    team1, team2, map_name, subfolder = arg
    print(f"START {team1} vs. {team2} on {map_name}")
    arg = f"gradlew run -Pmaps=\"{map_name}\" -PteamA=\"{team1}\" -PteamB=\"{team2}\""
    
    try:
        output = os.popen(arg).read()
    except:
        pass
    
    body = ""
    t1, t2 = 0, 0
    lines = output.split("\n")
    k = 0
    for i, line in enumerate(lines):
        if "(A) wins" in line:
            t1 += 1    
            k = i
            break
        elif "(B) wins" in line:
            t2 += 1
            k = i
            break
    body += f"{map_name}\n"
    s1 = lines[k].strip(" \n\t\r").removeprefix("[server]").strip(" \n\t\r")
    s2 = lines[k+1].strip(" \n\t\r").removeprefix("[server]").strip(" \n\t\r")
    body += f"\t\t{s1}\n\t\t{s2}\n"
    
    with open(f"{subfolder}/{team1}_{team2}_{map_name}.txt", "w") as file:
        file.write(output)

    print(f"END   {team1} vs. {team2} on {map_name}")
    return (t1, t2, body)
    
# print list of maps for MAPS_TO_RUN for when new maps made
def mapnames():
    list = []
    for file in os.listdir("./maps"):
        list.append(file.split(".")[0])
    out = ""
    for file in list:
        out += f"\t\"{file}\",\n"
    print(out.strip(" \n,"))

if __name__ == "__main__":
    runmaps()
    #mapnames()