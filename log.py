import re
params = []
with open("log.txt", 'r') as f:
    for line in f:
        p = re.split(r"\[A: #\d+@\d+\]", line)
        p = [a.strip(" \t\v\n\r\f") for a in p if len(a.strip(" \t\v\n\r\f")) > 0]
        p = ["l 0"] + p
        params.append(p)
    
out = []    
for i in range(1, len(params[0])):
    if params[0][i].rstrip(" 0123456789") in ("Robot turn", "main turn"):
        continue
    out.append([params[0][i].rstrip(" 0123456789"), 0])
    for p in params:
        out[-1][1] += int(p[i].split()[-1]) - int(p[i-1].split()[-1])
    out[-1][1] //= len(params)
for b in out:
    print(*b)