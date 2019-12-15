import socket
import time


# with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
try:
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect(('192.168.1.5', 8006))

    s.sendall(b'Hello, world')
    while(True):
        print('sleeping')
        time.sleep(1)
except KeyboardInterrupt:
    s.close()
    exit()