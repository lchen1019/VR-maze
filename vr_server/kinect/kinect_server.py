import socket
import json

from .analyze import analyze
from conf.config import Config
from .kinectData import KinectData
from assets.alarm import tips2_alarm, begin
from _thread import start_new_thread


def run_kinect_server(config, user_info):
    user1First = True
    user2First = True

    kinectSocket = socket.socket()
    kinectSocket.connect((config.kinect_host, config.kinect_port))
    msg = ''
    user = [0, 1]

    # 新增的ID，需要确定的ID
    needID = 2

    count = 0
    while True:
        msg += kinectSocket.recv(2048).decode()
        data = msg.split('@')
        # print(data)
        for i in range(len(data) - 1):
            count += 1
            if count != 100:
                continue
            count = 0
            if data[i] == '':
                continue
            # print(data[i])
            id, x, y, up = analyze(config, data[i])
            # print("kinect", (id, x, y, up))
            # 解决用户遮挡时候的编号问题
            if id == user[0]:
                if needID != -1:
                    user[1] = needID
                    needID = -1
                user_info[0] = x
                user_info[1] = y
                user_info[2] = up
                if user1First:
                    start_new_thread(tips2_alarm, ())
                    user1First = False
            elif id == user[1]:
                if needID != -1:
                    user[0] = needID
                    needID = -1
                user_info[3] = x
                user_info[4] = y
                user_info[5] = up
                if user2First:
                    start_new_thread(begin, ())
                    user2First = False
            else:
                needID = id
                continue

            # print(id, x, y, up)

        msg = data[-1]


# if __name__ == '__main__':
#     config = Config()
#     run_kinect_server(config)
