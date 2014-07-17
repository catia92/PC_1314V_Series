﻿using System.Net;
using System.Net.Sockets;
using System.Windows.Forms;

namespace Serie_3_Cat
{
    public class ClientRequest
    {
        private const string _localhost = "127.0.0.1";
        public TcpClient _clientSocket { get; private set; }

        public ClientRequest(string address, int port)
        {
            if (address.Equals(""))
                address = _localhost;

            var endPoint = new IPEndPoint(IPAddress.Parse(address), port);
            try
            {
                _clientSocket = new TcpClient();
                _clientSocket.ConnectAsync(IPAddress.Parse(address), port);

                //Store storeOperation = new Store();
                //storeOperation.Register("xpto_file_example1.txt", endPoint);
            }
            catch (SocketException ex)
            {
                MessageBox.Show(@"Sorry! The EndPoint is invalid!", @"ERROR");
            }
        }
    }
}
