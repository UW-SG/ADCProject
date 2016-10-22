package com.utility;

import com.Operation;

import java.io.Serializable;

public class DataPacket implements Serializable {
    private Operation operation;
    private Boolean response;
    private String data;

    private static Integer transId = 0;

    public DataPacket(String data) {
        this.data = data;
        setTransId();
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Boolean getResponse() {
        return response;
    }

    public void setResponse(Boolean response) {
        this.response = response;
    }

    public static Integer getTransId() {
        return transId;
    }

    public static void setTransId() {
        DataPacket.transId = transId++;
    }

    /**
     * Casts the data depending on which type getData() is assigned.
     * Eg:
     * String data = packet.getData(); // cast to String
     * Integer data = packet.getData(); // cast to Integer
     *
     * @throws ClassCastException If type T is different from the type of
     *                            data object.
     * @return
     */
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
