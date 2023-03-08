import time

from playsound import playsound


def game_start_alarm():
    playsound('assets/game_start.mp3')


def tips1_alarm():
    playsound('assets/tips1.mp3')


def tips2_alarm():
    playsound('assets/tips2.mp3')


def upload_maze_alarm():
    playsound('assets/upload_maze.mp3')


def upload_maze_suc_alarm():
    playsound('assets/upload_maze_suc.mp3')


def begin():
    time.sleep(3)
    playsound('assets/begin.mp3')
