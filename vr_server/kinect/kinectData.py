class KinectData:
    def __init__(self):
        self.__x = 0
        self.__y = 0
        self.__up = 0

    def write(self, location):
        with open(location, 'w', encoding='utf-8') as fp:
            fp.write(f"{self.__x},{self.__y},{self.__up}")
            fp.flush()

    def load(self, location):
        with open(location, 'r', encoding='utf-8') as fp:
            data = fp.read()
            data = data.split(',')
            print('data', data)
        self.__x = float(data[0])
        self.__y = float(data[1])
        self.__up = int(data[2])

    @property
    def x(self):
        return self.__x

    @x.setter
    def x(self, x):
        self.__x = x

    @property
    def y(self):
        return self.__y

    @y.setter
    def y(self, y):
        self.__y = y

    @property
    def up(self):
        return self.__up

    @up.setter
    def up(self, up):
        self.__up = up
