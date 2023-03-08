from conf.config import Config


def translate(config, x, z):
    """改变原点"""
    return x + config.offsetX, z - config.offsetY


def map_location_to_vr(x, y):
    """转换到VR中"""

    return x, y


def analyze(config, data):
    data = data.split('#')
    # print(data)

    # 返回给unity端的消息
    msg = ""

    # 获得用户编号
    userID = int(data[1])

    # 获得用户的位置
    locationBase1 = eval(data[5])
    locationBase2 = eval(data[6])
    locationBase3 = eval(data[7])
    location = [0, 0]
    location[0] = (locationBase1[0] + locationBase2[0] + locationBase3[0]) / 3.0
    location[1] = (locationBase1[2] + locationBase2[2] + locationBase3[2]) / 3.0
    location[0], location[1] = translate(config, location[0], location[1])
    location[0] = location[0] / config.maze_size_width * config.maze_size_width
    location[1] = location[1] / config.maze_size_height * config.maze_size_height

    location[0] = 0 if location[0] < 0 else location[0]
    location[1] = 0 if location[1] < 0 else location[1]

    location[0] = 4 if location[0] > 4 else location[0]
    location[1] = 4 if location[1] > 4 else location[1]

    # 转换到VR中
    location[0], location[1] = map_location_to_vr(location[0], location[1])

    # 解析手部是否举起
    handsUpBase = eval(data[2])
    leftHand = eval(data[3])
    rightHand = eval(data[4])
    if leftHand[1] > handsUpBase[1] or rightHand[1] > handsUpBase[1]:
        up = 1
    else:
        up = 0
    return userID, location[0], location[1], up


if __name__ == '__main__':
    config = Config()
    data = 'K#0#(0.23, 0.93, 1.02)#(0.29, 0.71, 0.91)#(0.66, 0.69, 1.07)#(-0.03, 0.59, 0.95)#(0.06, 0.52, 0.99)#(0.01, ' \
           '0.54, 1.00)#+ '
    msg = analyze(config, data)
    print(msg)
