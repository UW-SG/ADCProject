package com.utility;

import com.Operation;
import com.processor.UDPRequestHandler;
import com.uw.adc.rmi.util.Constants;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * Created by Anurita on 10/18/2016.
 */
public class OperationUtils {

    public static final String SEPARATOR = ",";

    public static void perform(UDPRequestHandler handler, DataPacket dataPacket) {

        Operation op = dataPacket.getOperation();

        switch (op) {

            case PUT:
                Constants.LOGGER.info(String.format("%s : Received request from : %s : %s to do %s ( %s )",
                       new SimpleDateFormat("yyyy/MM/dd: HH:mm:ss").format((new Date()).getTime())
                        , handler.getClientAddr().toString(), (handler.getClientPort()).toString(),
                        op.toString(), dataPacket.getData()));
                handler.handlePut(dataPacket);

                break;
            case GET:
                Constants.LOGGER.info(String.format("%s : Received request from : %s : %s to do %s ( %s )",
                        new SimpleDateFormat("yyyy/MM/dd: HH:mm:ss").format((new Date()).getTime())
                        , handler.getClientAddr().toString(),
                        (handler.getClientPort()).toString(),
                        op.toString(),
                        dataPacket.getData()));
                handler.handleGet(dataPacket);
                break;
            case DELETE:
                Constants.LOGGER.info(String.format("%s : Received request from : %s : %s to do %s ( %s )",
                        new SimpleDateFormat("yyyy/MM/dd: HH:mm:ss").format((new Date()).getTime())
                        , handler.getClientAddr().toString(), (handler.getClientPort()).toString(),
                        op.toString(), dataPacket.getData()));
                handler.handleDelete(dataPacket);
                break;
            case OTHER:
                handler.handleMalformed(dataPacket);
                break;
        }

    }

    public static byte[] serialize(DataPacket packet) throws IOException {
        ByteArrayOutputStream out = null;
        ObjectOutput os = null;
        try {
            out = new ByteArrayOutputStream();

            os = new ObjectOutputStream(out);
            //os.writeObject(1);
            os.writeObject(packet);
            os.flush();

            return out.toByteArray();
        } finally {
            os.close();
            out.close();

        }
    }

    public static byte[] compress(byte[] bytes) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Deflater deflate = new Deflater();
            deflate.setLevel(Deflater.BEST_SPEED);
            deflate.setInput(bytes);
            deflate.finish();
            byte[] tmp = new byte[4 * 1024];
            while (!deflate.finished()) {
                int size = deflate.deflate(tmp);
                out.write(tmp, 0, size);
            }
            out.close();
            return out.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Error while save");
        }
        System.exit(-1);
        return null;
    }

    public static byte[] uncompress(byte[] bytes) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Inflater inflate = new Inflater();
            inflate.setInput(bytes);

            byte[] buffer = new byte[4 * 1024];
            while (!inflate.finished()) {
                int size = inflate.inflate(buffer);
                out.write(buffer, 0, size);
            }
            out.close();
            return out.toByteArray();
        } catch (Exception ex) {
            //TODO
            ex.printStackTrace();

        }
        return null;
    }

    public static DataPacket deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInput objectInputStream = new ObjectInputStream(in);
        DataPacket dataPacket = (DataPacket) objectInputStream.readObject();
        /*for (int i=0; i<size; i++) {

            dataPacket = (DataPacket) objectInputStream.readObject();
        }*/
        return dataPacket;
    }

    public static void sendPacket(DataPacket outputPacket, InetAddress destAddr,
                                  Integer destPort, DatagramSocket sourceSocket) {

        try {

            byte output[] = OperationUtils.compress(OperationUtils.serialize(outputPacket));

            DatagramPacket outputDatagramPacket = new DatagramPacket(output, output.length, destAddr, destPort);
            sourceSocket.send(outputDatagramPacket);
        } catch (Exception e) {
            //TODO logging
            throw new RuntimeException(e);
        }
    }


}
