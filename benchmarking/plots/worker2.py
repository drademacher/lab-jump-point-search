from os import listdir
from os.path import isfile, join

tollevariable = "rooms_sub"

def compute(data):
    # get median and quartiles
    data = sorted(data)
    q1, med, q3 = data[len(data)//4], data[len(data)//2], data[3*len(data)//4]

    # wisker computation
    w1 = q1 - (q3-q1) * 1.5
    w1 = min(t for t in data if t >= w1)
    # print min(data)
    w2 = q3 + (q3-q1) * 1.5
    w2 = max(t for t in data if t <= w2)

    return str(w1/60000)+"\t", str(q1/60000)+"\t", str(med/60000)+"\t", str(q3/60000)+"\t", str(w2/60000)


file_time   = open("preprocessing_"+tollevariable+".dat", "w")
file_time.write("#n\tlow\tq1\tmedian\tq3\ttop\ttitle\n")

mypath = "../results/"+tollevariable
files = [f for f in listdir(mypath) if isfile(join(mypath, f))]

d = {}

for file in files:
    title, suffix = file[:file.rfind("_")], file[file.rfind("_")+1:]
    if suffix not in d: d[suffix] = []

    f = open(mypath + "/" + file)

    badcounter = 0
    for line in f:
        badcounter += 1
        if badcounter == 4:
            d[suffix].append(int(line[line.rfind(' '):]))
            break

order = ['jpsplus.txt', 'jpsplusbb.txt']
# order = ['astarbb.txt', 'jpsbb.txt', 'jpsplus.txt', 'jpsplusbb.txt']
# order = ['astar.txt', 'astarbb.txt', 'jps.txt', 'jpsbb.txt', 'jpsplus.txt', 'jpsplusbb.txt']

for key in order:
    w1, q1, med, q3, w2 = compute(d[key])
    file_time.write(
        str(order.index(key)) + "\t" + w1 + q1 + med + q3 + w2 + "\t" + "0.3" + "\t" + key[:len(key)-4] + "\n")

file_time.close()
