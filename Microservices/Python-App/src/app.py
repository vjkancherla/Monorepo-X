from http.server import BaseHTTPRequestHandler, HTTPServer
import os

environment   = os.getenv('ENVIRONMENT')
image_version = os.getenv('IMAGE_VERSION')
message       = os.getenv('MESSAGE')

class MyHandler(BaseHTTPRequestHandler):
    def do_GET(self):
        self.send_response(200)
        self.send_header('Content-type', 'text/plain')
        self.end_headers()
        self.wfile.write(b'''
          ##         .
    ## ## ##        ==
 ## ## ## ## ##    ===
/"""""""""""""""""\___/ ===
{                       /  ===-
\______ O           __/
 \    \         __/
  \____\_______/


Hello from Docker!
''')
        self.wfile.write(bytes('Environment: '+environment+', Image_version: '+image_version+', Custom_Message: '+message, encoding='utf8'))

def run():
    print('Starting server...')
    server_address = ('', 8080)
    httpd = HTTPServer(server_address, MyHandler)
    print('Server started!')
    httpd.serve_forever()

if __name__ == '__main__':
    run()
