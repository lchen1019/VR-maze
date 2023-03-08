import base64
import socket

import numpy as np
import cv2 as cv

from time import time, sleep
from socket import *
from conf.config import Config
from .extractorMaze import extractor
from assets.alarm import upload_maze_suc_alarm, tips1_alarm
from _thread import start_new_thread


def extract(tcpCliSock):
    """Android上传图片，然后提取迷宫"""
    img = b""
    while True:
        data = tcpCliSock.recv(1024)
        img += data
        if not data:
            break
    img = base64.b64decode(img)
    img = np.frombuffer(img, np.uint8)
    img = cv.imdecode(img, cv.COLOR_RGB2BGR)

    print(f'images/{time()}.png')
    cv.imwrite(f'images/{time()}.png', img)

    row, col = extractor(img)
    msg = ""
    for i in row:
        for j in i:
            msg += str(j)
    msg += ';'
    for i in col:
        for j in i:
            msg += str(j)

    print(msg)
    tcpCliSock.send(msg.encode())
    tcpCliSock.close()


def send_maze(tcpCliSock):
    """收到迷宫矩阵，并转发给unity"""
    maze = b""
    while True:
        data = tcpCliSock.recv(1024)
        maze += data
        if not data:
            break

    # 写入文件，等到与unity连接建立完成的时候再进行存储
    with open('scan/maze.txt', 'w', encoding='utf-8') as fp:
        fp.write(maze.decode())

    # 返回给Android上传成功
    tcpCliSock.send("suc".encode())
    tcpCliSock.close()

    # 语音提示上传成功
    start_new_thread(upload_maze_suc_alarm, ())

    # 提示请第一个玩家进入
    sleep(3)
    start_new_thread(tips1_alarm, ())


def run_maze_server(config):
    # 建立连接
    tcpSerSock = socket(AF_INET, SOCK_STREAM)
    tcpSerSock.bind((config.localhost, config.android_port))
    tcpSerSock.listen(5)

    # 等待客户端的请求
    while True:
        tcpCliSock, addr = tcpSerSock.accept()
        print(addr)
        print("连接建立")
        command = tcpCliSock.recv(1)
        if command == b'0':
            extract(tcpCliSock)
        elif command == b'1':
            send_maze(tcpCliSock)


if __name__ == '__main__':
    config = Config()
    run_maze_server(config)
