import unittest
import os
from app.app import MyHandler, create_server, run_server
from unittest.mock import patch, Mock
from http.server import HTTPServer
from requests import get
import threading

class TestMyHandler(unittest.TestCase):

    @patch('os.getenv')
    def test_handler(self, mock_getenv):
        mock_getenv.side_effect = ['dev', '1.0', 'test message']

        server_address = ('', 8888)
        httpd = create_server()

        server_thread = threading.Thread(target=httpd.serve_forever)
        server_thread.setDaemon(True)
        server_thread.start()

        response = get('http://localhost:8888')

        httpd.shutdown()
        httpd.server_close()  # Explicitly close the server socket
        server_thread.join()

        self.assertEqual(response.status_code, 200)
        self.assertIn('Environment: dev', response.text)
        self.assertIn('Image_version: 1.0', response.text)
        self.assertIn('Custom_Message: test message', response.text)

    @patch('app.app.run_server')
    def test_run(self, mock_run_server):
        httpd = create_server()
        self.assertIsInstance(httpd, HTTPServer)
        run_server(httpd)
        mock_run_server.assert_called_once()

if __name__ == '__main__':
    unittest.main()
