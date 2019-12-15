from subprocess import Popen, PIPE
import requests
import time
import sys

curl = 'C:\\Projects\\curl-7.67.0_5-win64-mingw\\curl-7.67.0-win64-mingw\\bin\\curl.exe'
get_url = 'https://us-central1-mlight-fd890.cloudfunctions.net/getLightCommand'
set_url = 'https://us-central1-mlight-fd890.cloudfunctions.net/lightCommand'
off = [curl, '-X', 'POST', '-H', 'Content-Type:application/json', set_url + '?cmd=off']
on = [curl, '-X', 'POST', '-H', 'Content-Type:application/json', set_url + '?cmd=on']

def main():
    while(True):
        print('commanding light to off')       
        p = Popen(off, stdout=PIPE, stderr=PIPE)
        p.communicate()
        time.sleep(3)
        # print('retrieving light status')        
        # r = requests.get(get_url)
        # print(r.text)

        print('commanding light to on')       
        p = Popen(on, stdout=PIPE, stderr=PIPE)
        p.communicate()
        time.sleep(3)
        # print('retrieving light status')        
        # r = requests.get(get_url)
        # print(r.text)



if __name__ == '__main__':
    main()