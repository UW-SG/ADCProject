package com.company;

import com.server.UDPServer;

class ServerMain {
    public static void main(String args[]) {

        try {
            UDPServer udpServer = new UDPServer(args[0]);
            udpServer.start();


        } catch (Exception e) {
            System.out.println(e);
        }
    }


}
