package com.utility;

import com.Operation;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Anurita on 10/16/2016.
 */
public class DataStore {

    private Map<String, String> students = new ConcurrentHashMap<>();
    private DataPacket responsePacket = null;
    public DataPacket getValue(String key) {

        String value = students.get(key);
        responsePacket = new DataPacket(String.format("Packet retrieved Successfully for GET(key=%s) value=%s",key, value));
        responsePacket.setResponse(Boolean.TRUE);
        responsePacket.setOperation(Operation.GET);
        return responsePacket;
    }

    public DataPacket putValue(String key, String value) {
        students.put(key, value);
        responsePacket = new DataPacket("Packet Inserted Successfully for : PUT (" +
                key + "," + value + ")" );
        responsePacket.setResponse(Boolean.TRUE);
        responsePacket.setOperation(Operation.PUT);
        return responsePacket;
    }

    public DataPacket deleteValue(String key)
    {
        students.remove(key);
        responsePacket = new DataPacket("Packet deleted Successfully for : DELETE (" +
                key + ")" );
        responsePacket.setResponse(Boolean.TRUE);
        responsePacket.setOperation(Operation.DELETE);
        return responsePacket;
    }
}
