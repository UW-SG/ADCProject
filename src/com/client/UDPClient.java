package com.client;

import com.Operation;
import com.utility.DataPacket;
import com.utility.OperationUtils;
import com.uw.adc.rmi.model.Stats;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.Operation.PUT;

/**
 * Created by Anurita on 10/16/2016.
 */
public class UDPClient {


    //public static final String SEPARATOR = " ";
    private static final Log LOGGER = LogFactory.getLog(UDPClient.class);

    private List<Stats> statsList = new ArrayList<Stats>();
    Date beforeDate = null;
    Date afterDate = null;

    public void encodePacket(String packetString, String host, String port) {
        beforeDate = new Date();
       // String operation = packetString.substring(0, 3);
        String operation = packetString.split(OperationUtils.SEPARATOR)[0];
        String data = extractData(packetString);
        InetAddress destAddr = null;
        int destPort;
        DataPacket dataPacket = null;
        DatagramSocket clientSocket = null;
        Operation op = Operation.fromValue(operation.toUpperCase());
        try {
            switch (op) {
                case PUT:
                    dataPacket = new DataPacket(data);
                    dataPacket.setOperation(PUT);
                    dataPacket.setResponse(Boolean.TRUE);
                    break;
                case GET:
                    dataPacket = new DataPacket(data);
                    dataPacket.setOperation(Operation.GET);
                    dataPacket.setResponse(Boolean.TRUE);
                    break;
                case DELETE:
                    dataPacket = new DataPacket(data);
                    dataPacket.setOperation(Operation.DELETE);
                    dataPacket.setResponse(Boolean.TRUE);
                    break;
                default:
                    // TODO: log invalid operation
            }

            destAddr = InetAddress.getByName(host);
            destPort = Integer.parseInt(port);
            clientSocket = new DatagramSocket();
            OperationUtils.sendPacket(dataPacket, destAddr, destPort, clientSocket);
            clientSocket.setSoTimeout(2000);
            waitForAcknowledgement(clientSocket);

        } catch (Exception e) {
            //TODO
            throw new RuntimeException(e);
        }
    }

    private String extractData(String packetString) {
        // String after first ",". Eg for PUT,a,b this will return a,b
        String data = packetString.substring(packetString.indexOf(",") + 1);
        return data;
    }

    private void waitForAcknowledgement(DatagramSocket clientSocket) {
        try {
            byte[] receiveData = new byte[1024];

            DatagramPacket datagramPacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(datagramPacket);
            receiveData = datagramPacket.getData();
            DataPacket dataPacket = OperationUtils.deserialize(OperationUtils.uncompress(receiveData));
            System.out.println(dataPacket.getData());
            afterDate = new Date();
            long time = afterDate.getTime() - beforeDate.getTime();
            Stats currentStats = new Stats(dataPacket.getOperation().toString(), time);
            statsList.add(currentStats);

        } catch (Exception e) {
            //TODO
            throw new RuntimeException(e);
        }
    }

    public List<Stats> getStatsList() {
        return statsList;
    }



}
