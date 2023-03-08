from socket import *

HOST = '127.0.0.1'
PORT = 9303

tcpCliSock = socket(AF_INET, SOCK_STREAM)
tcpCliSock.connect((HOST, PORT))

while True:
    data = input('> ')
    if not data:
        break
    tcpCliSock.send(bytes(data, 'utf-8'))
    data = tcpCliSock.recv(1024)
    if not data:
        break
    print('response', data.decode('utf-8'))

tcpCliSock.close()

