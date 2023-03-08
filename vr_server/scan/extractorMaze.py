import numpy as np
import cv2 as cv
from math import fabs


def edge_detection(image):
    return cv.Canny(image, 30, 250)


# 形态学膨胀
def swell(img):
    kernel = cv.getStructuringElement(cv.MORPH_RECT, (3, 3))
    img_ret = cv.dilate(img, kernel, iterations=2)
    return img_ret


def extractor(origin):
    # 彩色图像采样至(500, 500)
    image = np.zeros((500, 500, 3), dtype=np.uint8)
    image[:, :, 0] = cv.resize(origin[:, :, 0], (500, 500), cv.INTER_CUBIC)
    image[:, :, 1] = cv.resize(origin[:, :, 1], (500, 500), cv.INTER_CUBIC)
    image[:, :, 2] = cv.resize(origin[:, :, 2], (500, 500), cv.INTER_CUBIC)

    # 转化为灰度图
    gray = cv.cvtColor(image, cv.COLOR_BGR2GRAY)
    # 边缘检测
    edge = edge_detection(gray)
    # 形态学膨胀
    img = swell(edge)

    # 划分成若干小块
    # cv.line(img, pt1=(0, 40), pt2=(500, 40), color=255, thickness=1)
    # cv.line(img, pt1=(0, 105), pt2=(500, 105), color=255, thickness=1)
    # cv.line(img, pt1=(0, 145), pt2=(500, 145), color=255, thickness=1)
    # cv.line(img, pt1=(0, 230), pt2=(500, 230), color=255, thickness=1)
    # cv.line(img, pt1=(0, 270), pt2=(500, 270), color=255, thickness=1)
    # cv.line(img, pt1=(0, 355), pt2=(500, 355), color=255, thickness=1)
    # cv.line(img, pt1=(0, 395), pt2=(500, 395), color=255, thickness=1)
    # cv.line(img, pt1=(0, 460), pt2=(500, 460), color=255, thickness=1)

    row = [[0] * 4 for _ in range(5)]
    col = [[0] * 5 for _ in range(4)]

    tolerance = 25

    # 求解出行边
    for i in range(5):
        for j in range(4):
            center_y = 62 + j * 125
            center_x = i * 125
            count = 0
            # cv.rectangle(img, (center_y - tolerance, center_x - tolerance), (center_y+tolerance, center_x+tolerance), 255, 2)
            # cv.circle(img, (center_y, center_x), tolerance, 255, 2)
            for i_x in range(center_x - tolerance, center_x + tolerance):
                for j_x in range(center_y - tolerance, center_y + tolerance):
                    if i_x < 0 or i_x >= 500:
                        continue
                    if j_x < 0 or j_x >= 500:
                        continue
                    if img[i_x, j_x] == 255:
                        count += 1
            if count > 10:
                row[i][j] = 1

    # 求解出列边
    for i in range(4):
        for j in range(5):
            center_x = 62 + i * 125
            center_y = j * 125
            count = 0
            for i_x in range(center_x - tolerance, center_x + tolerance):
                for j_x in range(center_y - tolerance, center_y + tolerance):
                    if i_x < 0 or i_x >= 500:
                        continue
                    if j_x < 0 or j_x >= 500:
                        continue
                    if img[i_x, j_x] == 255:
                        count += 1
            if count > 10:
                col[i][j] = 1

    return row, col


if __name__ == "__main__":
    origin = cv.imread('./demo.png')
    extractor(origin)
