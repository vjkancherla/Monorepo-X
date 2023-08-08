import unittest
import os
from src.app.app import create_server, run_server
from unittest.mock import patch, Mock
from http.server import HTTPServer
from requests import get
import threading

class TestMyHandler(unittest.TestCase):

    def setUp(self):
        self.server_address = ('', 8888)
        self.httpd = create_server()
        self.server_thread = threading.Thread(target=self.httpd.serve_forever)
        self.server_thread.setDaemon(True)
        self.server_thread.start()

    def tearDown(self):
        self.httpd.shutdown()
        self.server_thread.join()

    @patch('os.getenv')
    def test_handler(self, mock_getenv):
        mock_getenv.side_effect = ['dev', '1.0', 'test message']

        response = get('http://localhost:8888')

        self.assertEqual(response.status_code, 200)
        self.assertIn('Environment: dev', response.text)
        self.assertIn('Image_version: 1.0', response.text)
        self.assertIn('Custom_Message: test message', response.text)

class TestAppFunctions(unittest.TestCase):

    @patch('app.run_server')  # Mock the blocking call
    def test_run_function(self, mock_run_server):
        app.run()  # This should now only run create_server() and print "Setting up server..."
        mock_run_server.assert_called_once()  # Ensure run_server() was called

    def test_create_server(self):
        httpd = app.create_server()
        self.assertIsInstance(httpd, HTTPServer)  # Basic check to ensure the server is set up correctly

if __name__ == '__main__':
    unittest.main()
