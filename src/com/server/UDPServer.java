package com.server;

import com.processor.RequestHandler;
import com.processor.UDPRequestHandler;
import com.utility.DataPacket;
import com.utility.DataStore;
import com.utility.OperationUtils;
import com.uw.adc.rmi.util.Constants;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by Anurita on 10/16/2016.
 */
public class UDPServer {

    private final DatagramSocket udpServerSocket;
    private final DataStore dataStore;

    public UDPServer(String port) {
        dataStore = new DataStore();

        try {
            udpServerSocket = new DatagramSocket(Integer.parseInt(port));
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

    }

    public void start() {
        try {
            while (true) {

                byte input[] = new byte[1024];
                DatagramPacket inputPacket = new DatagramPacket(input, input.length);
                System.out.println("Waiting for client");
                udpServerSocket.receive(inputPacket);

                new Thread(() -> {
                    //System.out.println("Thread id is : " + Thread.currentThread().getId());
                    processRequest(inputPacket);
                }).start();

            }

        } catch (Exception e) {
            // TODO: 10/18/2016 logging
            throw new RuntimeException(e);
        } finally {
            udpServerSocket.close();
        }
    }

    private void processRequest(DatagramPacket inputPacket) {
        try {
            byte inBlock[] = inputPacket.getData();
            DataPacket dataPacket = OperationUtils.deserialize(OperationUtils.uncompress(inBlock));
            //String inputRequest = new String(inBlock, 0, inBlock.length);

            RequestHandler handler = new UDPRequestHandler(inputPacket.getAddress(),
                    inputPacket.getPort(),
                    udpServerSocket,
                    dataStore);
            //TODO logging
            Constants.STATSLOGGER.info(System.currentTimeMillis() + " : Received request from : " + inputPacket.getAddress().toString() +
                    " : " + ((Integer) inputPacket.getPort()).toString());
            OperationUtils.perform(handler, dataPacket);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
