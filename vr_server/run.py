import time
from _thread import start_new_thread
from multiprocessing import Process, Value, Array
from scan.maze_server import run_maze_server
from unity.ipServer import broadcast_host
from conf.config import Config
from kinect.kinect_server import run_kinect_server
from kinect.kinectData import KinectData
from unity.unity_server import run_unity_server
from assets.alarm import game_start_alarm, upload_maze_alarm


def run(user):
    config = Config()

    # 服务器开启提示
    time.sleep(3)
    start_new_thread(game_start_alarm, ())

    # 语音提示需要上传迷宫
    time.sleep(7)
    start_new_thread(upload_maze_alarm, ())

    # 运行起与android通信
    start_new_thread(run_maze_server, (config,))
    print("android communication start")

    # 运行起广播本地主机的<ip, port>的通信
    start_new_thread(broadcast_host, (config,))
    print("broadcast <ip,port> start")

    # 运行起与Kinect的通信
    # 打开run_kinect.py

    # 运行起与unity端的通信
    start_new_thread(run_unity_server, (config, user, ))
    print("Unity server start")

    while True:
        pass


def run_kinect(user):
    """需要这个类的原因是若以run.py中的线程运行会被阻塞"""
    config = Config()

    # 运行起与Kinect的通信
    run_kinect_server(config, user)
    print("Kinect server start")


def main():
    # user1_x = Value('f', 0.0)
    # user1_y = Value('f', 0.0)
    # user1_up = Value('i', 0)
    #
    # user2_x = Value('f', 0.0)
    # user2_y = Value('f', 0.0)
    # user2_up = Value('i', 0)

    user = Array('f', 6)

    d = Process(target=run, args=(user, ))
    d.start()
    w = Process(target=run_kinect, args=(user, ))
    w.start()

    d.join()
    w.join()


if __name__ == '__main__':
    main()
