import json


class Config:
    def __init__(self):
        self.localhost = None
        self.kinect_host = None
        self.user1_ip = None
        self.user2_ip = None
        self.broadcast_ip = None

        self.android_port = None
        self.kinect_port = None
        self.unity_port = None
        self.broadcast_port = None

        self.maze_size_width = None
        self.maze_size_height = None

        self.width = None
        self.height = None
        self.offsetX = None
        self.offsetY = None
        self.__read()

    def __read(self):
        with open('config.json', 'r', encoding='utf-8') as fp:
            parsed_json = json.load(fp)
        self.localhost = parsed_json['localhost']
        self.kinect_host = parsed_json['kinect-host']
        self.user1_ip = parsed_json['user1-ip']
        self.user2_ip = parsed_json['user2-ip']
        self.broadcast_ip = parsed_json['broadcast-ip']

        self.android_port = parsed_json['android-port']
        self.kinect_port = parsed_json['kinect-port']
        self.unity_port = parsed_json['unity-port']
        self.broadcast_port = parsed_json['broadcast-port']

        self.maze_size_width = parsed_json['maze-size-width']
        self.maze_size_height = parsed_json['maze-size-height']

        self.width = parsed_json['width']
        self.height = parsed_json['height']
        self.offsetX = parsed_json['offsetX']
        self.offsetY = parsed_json['offsetY']


