import socket

class LightServer():
    def __init__(self):
        self.s = None
        self.port = 8006
        self.host = '192.168.1.5'
        self.state = 'off'
    
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
                    self.state = 'on'
                    print('setting on')
                else:
                    self.state = 'off'
                    print('setting off')
                
                conn.send(self.state.encode() + b'\n')  # send light status
                print(d.decode('utf-8'))


ls = LightServer()
ls.run()