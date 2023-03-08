import socket

from conf.config import Config


def load_maze():
    with open('scan/maze.txt') as fp:
        maze = fp.read()
    return f"A{maze}"


def get_kinect_data(id, user):
    if id == 1:
        response = f"B{user[0] * 17.0 / 3.0},{user[1] * 17.0 / 3.0},{int(user[2])},{user[3] * 17.0 / 3.0},{user[4] * 17.0 / 3.0},{int(user[5])}"
    else:
        response = f"B{user[3] * 17.0 / 3.0},{user[4] * 17.0 / 3.0},{int(user[5])},{user[0] * 17.0 / 3.0},{user[1] * 17.0 / 3.0},{int(user[2])} "
    return response


def run_unity_server(config, user):
    ip1 = config.user1_ip
    ip2 = config.user2_ip

    ip1_maze = True
    ip2_maze = True

    s = socket.socket()
    s.bind((config.localhost, config.unity_port))
    s.listen(5)

    print('unity ask 1')
    sock, addr = s.accept()
    print('unity ask 2')

    while True:
        # 每建立一次连接发送一次数据
        addr = addr[0]
        id = 1 if addr == ip1 else 2
        if id == 1 and not ip1_maze:
            s1 = get_kinect_data(id, user)
            s1 = s1.encode()
            sock.send(s1)
            print(s1)
            print('send kinect data1')
        if id == 2 and not ip2_maze:
            s1 = get_kinect_data(id, user)
            s1 = s1.encode()
            sock.send(s1)
            print(s1)
            print('send kinect data2')
        if id == 1 and ip1_maze:
            s2 = load_maze().encode()
            print('user1 send maze data')
            print(s2)
            sock.send(s2)
            ip1_maze = False
        if id == 2 and ip2_maze:
            s3 = load_maze().encode()
            print('user2 send maze data')
            print(s3)
            sock.send(s3)
            ip2_maze = False
        sock.close()
        sock, addr = s.accept()
