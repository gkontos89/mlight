import socket
from RPi import GPIO

class LightServer():
    def __init__(self):
        self.s = None
        self.port = 8006
        self.host = '192.168.1.4'
        self.state = 'off'
        GPIO.setmode(GPIO.BCM)
        GPIO.setup(17, GPIO.OUT)
        GPIO.setup(27, GPIO.OUT)
        GPIO.setup(17, GPIO.LOW)
        GPIO.setup(27, GPIO.LOW)
    
    def run(self):
        self.s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.s.bind((self.host, self.port))
        self.s.listen(1)
        while(True):
            conn, addr = self.s.accept()
            print('connection received')
            conn.send(self.state.encode() + b'\n')  # send light status
            while(True):
                d = conn.recv(1024)
                if not d:
                    break
                if self.state == 'off':
                    GPIO.output(17, GPIO.HIGH)
                    GPIO.output(27, GPIO.HIGH)
                    self.state = 'on'
                    print('setting on')
                else:
                    GPIO.output(17, GPIO.LOW)
                    GPIO.output(27, GPIO.LOW)
                    self.state = 'off'
                    print('setting off')
                
                conn.send(self.state.encode() + b'\n')  # send light status


ls = LightServer()
ls.run()