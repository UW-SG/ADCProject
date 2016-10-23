package com.processor;

import com.utility.DataPacket;
import com.utility.DataStore;
import com.utility.OperationUtils;

import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPRequestHandler implements RequestHandler {
    // take DataStore and client address in constructor and save as private member
    private InetAddress clientAddr;
    private DatagramSocket serverSocket;
    private Integer clientPort;
    private DataStore dataStore;
    private DataPacket responsePacket;


    public UDPRequestHandler(InetAddress clientAddr,
                             Integer clientPort,
                             DatagramSocket serverSocket,
                             DataStore dataStore) {

        this.clientAddr = clientAddr;
        this.serverSocket = serverSocket;
        this.dataStore = dataStore;
        this.clientPort = clientPort;
    }

    @Override
    public void handleGet(DataPacket dataPacket) {
        String data = dataPacket.getData();
        String key = data.split(OperationUtils.SEPARATOR)[0].trim();
        responsePacket = dataStore.getValue(key);
        OperationUtils.sendPacket(responsePacket, this.clientAddr, this.clientPort, this.serverSocket);

    }

    @Override
    public void handlePut(DataPacket dataPacket) {
        String data = dataPacket.getData();
        responsePacket = dataStore.putValue(data.split(OperationUtils.SEPARATOR)[0].trim(),
                data.split(OperationUtils.SEPARATOR)[1].trim());

        OperationUtils.sendPacket(responsePacket, this.clientAddr, this.clientPort, this.serverSocket);

    }

    @Override
    public void handleDelete(DataPacket dataPacket) {
        String key = dataPacket.getData();
        responsePacket = dataStore.deleteValue(key);
        OperationUtils.sendPacket(responsePacket, this.clientAddr, this.clientPort, this.serverSocket);

    }

    @Override
    public void handleMalformed(DataPacket dataPacket) {

    }


    public InetAddress getClientAddr() {
        return clientAddr;
    }

    public void setClientAddr(InetAddress clientAddr) {
        this.clientAddr = clientAddr;
    }

    public DatagramSocket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(DatagramSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public Integer getClientPort() {
        return clientPort;
    }

    public void setClientPort(Integer clientPort) {
        this.clientPort = clientPort;
    }

    public DataStore getDataStore() {
        return dataStore;
    }

    public void setDataStore(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    public DataPacket getResponsePacket() {
        return responsePacket;
    }

    public void setResponsePacket(DataPacket responsePacket) {
        this.responsePacket = responsePacket;
    }
}
