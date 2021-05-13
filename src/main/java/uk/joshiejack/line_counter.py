import os

count = 0

for root, dirs, files in os.walk("D:\Data\Minecraft Modding\Harvest Festival\src\main\java\joshie"):
    for file in files:
        for str in open(os.path.join(root, file), 'r'):
            if str != "/n":
                count += 1
print(count, "lines of code were found!")