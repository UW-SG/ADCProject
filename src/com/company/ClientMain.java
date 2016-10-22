package com.company;

import com.client.UDPClient;
import com.uw.adc.rmi.model.Stats;
import com.uw.adc.rmi.util.Constants;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

import static com.uw.adc.rmi.util.Constants.STATSLOGGER;

/**
 * Created by Anurita on 10/20/2016.
 */
public class ClientMain {
    public static void main(String args[]) {

        try {

            String csvFile = "D:\\Anurita\\UW\\Fall 2016\\ADC\\kvp-operations.csv";
            String host = args[0];
            String port = args[1];
            BufferedReader bufferedReader = new BufferedReader(new FileReader(csvFile));
            UDPClient udpClient = new UDPClient();
            String currentData;
            while((currentData = bufferedReader.readLine()) != null) {

                udpClient.encodePacket(currentData, host, port);
            }
            /*String outstr = "PUT(101, Anurita)";
            String host = args[0];
            String port = args[1];
            UDPClient udpClient = new UDPClient();
            udpClient.encodePacket(outstr, host, port);
            outstr = "PUT(201, Mohit)";
            udpClient.encodePacket(outstr, host, port);
            udpClient.encodePacket("GET(101)", host, port);
            udpClient.encodePacket("GET(201)", host, port);
            udpClient.encodePacket("DELETE(101)", host, port);
            udpClient.encodePacket("PUT(301, Ruta)", host, port);
            udpClient.encodePacket("PUT(401, Ratin)", host, port);
            udpClient.encodePacket("GET(301)", host, port);
            udpClient.encodePacket("DELETE(101)", host, port);
            udpClient.encodePacket("DELETE(201)", host, port);
            udpClient.encodePacket("DELETE(301)", host, port);
            udpClient.encodePacket("DELETE(401)", host, port);*/
            ClientMain.computePerformance(udpClient);

        } catch (Exception e) {

            System.out.println("Client error: " + e);
        }
    }

    private static void computePerformance(UDPClient udpClient) {

        Constants.STATSLOGGER.info("---------PERFORMANCE ANALYSIS---------");

        int i = 0;
        long getTotalTime = 0, putTotalTime = 0, delTotalTime = 0;
        int getRequestCount = 0, putRequestCount = 0, delRequestCount = 0;
        long getAvgTime, putAvgTime, delAvgTime;
        List<Stats> statsList = udpClient.getStatsList();
        while (i < statsList.size()) {

            Stats statsObj = statsList.get(i);
            switch (statsObj.getOperation()) {
                case "GET":
                    getTotalTime = getTotalTime + statsObj.getTime();
                    ++getRequestCount;
                    break;
                case "PUT":
                    putTotalTime = putTotalTime + statsObj.getTime();
                    ++putRequestCount;
                    break;
                case "DEL":
                    delTotalTime = delTotalTime + statsObj.getTime();
                    ++delRequestCount;
            }

            ++i;
        }

        if (getRequestCount > 0)
           System.out.println("Average Compute time for UDP GET request:" + getTotalTime / getRequestCount + "ms");
        if (putRequestCount > 0)
            System.out.println("Average Compute time for UDP PUT request:" + putTotalTime / putRequestCount + "ms");
        if (delRequestCount > 0)
            System.out.println("Average Compute time for UDP DELETE request:" + delTotalTime / delRequestCount + "ms");

        STATSLOGGER.info("---------PERFORMANCE ANALYSIS COMPLETE---------");

    }
}
