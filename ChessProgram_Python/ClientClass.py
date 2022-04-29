import socket

class Client():

    s = socket
    # The server's hostname or IP address and the port used by the server
    _HOST = '127.0.0.1'
    _PORT = 30003
    
    def setSendString(self, msg):   
        self.s.send((msg + "\r\n").encode())

    def start(self):
        self.s= socket.socket(socket.AF_INET, socket.SOCK_STREAM)
        self.s.connect((self._HOST, self._PORT))

    def setHost(self, address):
        self._HOST = address

    def setPort(self, port):
        self._PORT = port

    def stop(self):
        self.s.close()

    receivedString = []

    def setReceivedString (self, recStringList):
        self.receivedString.extend(recStringList)

    def getReceivedString(self):   
        recString = str(Client.receivedString[0])
        self.receivedString.pop(0)
        return recString

    def hasReceivedString (self):
        self.receiveString()
        if(len(Client.receivedString) > 0):
            return True
        else:
            return False

    def receiveString(self):
        try:  
            data = self.s.recv(1024)
            dataStr = str(data)
            dataStr = str(dataStr[2:])
            dataStr = str(dataStr[:-1])
            dataStr = dataStr.split("\\r\\n")
            dataStr = filter(None,dataStr)
            self.setReceivedString(dataStr)
        except TimeoutError:  
            pass
        
        
        