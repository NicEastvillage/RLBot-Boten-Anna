import pyvjoy
import time
import resetDevices

#Pythonic API, item-at-a-time

p1 = pyvjoy.VJoyDevice(1)
p2 = pyvjoy.VJoyDevice(2)
waitTime = 0.5
tiltVerticalWait = 1

def tapA(player):
    player.data.lButtons = 1
    player.update()
    time.sleep(waitTime)
    player.data.lButtons = 0
    player.update()
    time.sleep(waitTime)

def tiltLeft(player):
    player.data.wAxisX = 0
    player.update()
    time.sleep(waitTime)
    player.data.wAxisX = 16383
    player.update()
    time.sleep(waitTime)

def tiltRight(player):
    player.data.wAxisX = 32767
    player.update()
    time.sleep(waitTime)
    player.data.wAxisX = 16383
    player.update()
    time.sleep(waitTime)

def tiltDown(player):
    time.sleep(waitTime)
    player.data.wAxisY = 32767
    player.update()
    time.sleep(waitTime)
    player.data.wAxisY = 16383
    player.update()
    time.sleep(waitTime)

def povDown(player):
    player.set_disc_pov(1, 0)
    time.sleep(waitTime)
    player.reset_povs()
    time.sleep(waitTime)

# Give the user time to tab into the game
time.sleep(3)

# Weird bug fix
tiltRight(p1)

# Press and release the start button
p1.data.lButtons = 128
p1.update()
time.sleep(waitTime)
p1.data.lButtons = 0
p1.update()
time.sleep(waitTime)

# Move down one in the menu options
povDown(p1)
tiltDown(p1)

# Press and release the A button
tapA(p1)

# Tilt left to highlight Yes
tiltLeft(p1)

# Select Yes to restart
tapA(p1)

# Tilt left to highlight "Join Blue"
tiltLeft(p1)

# Join blue
tapA(p1)

# For p2, Auto should already be highlighted. Tap A to auto join.
tapA(p2)
