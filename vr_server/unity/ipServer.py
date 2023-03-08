import socket
import time


def broadcast_host(config):
    """每隔2s广播一次当前主机的ip和端口"""
    ip = config.localhost
    port = config.unity_port

    client = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    client.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
    client.bind((config.broadcast_ip, 12345))

    while True:
        msg = f"{ip};{port}"
        client.sendto(msg.encode('utf-8'), ('<broadcast>', config.broadcast_port))
        time.sleep(2)
