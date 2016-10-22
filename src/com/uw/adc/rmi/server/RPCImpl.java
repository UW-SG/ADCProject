package com.uw.adc.rmi.server;

import com.uw.adc.rmi.RPC;
import com.uw.adc.rmi.model.DataTransfer;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

public class RPCImpl implements RPC {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private HashMap<String, String> resource = new HashMap<String, String>();

	public RPCImpl() throws RemoteException {}
	
	@Override
	public String sayHello() throws RemoteException {
		// TODO Auto-generated method stub
		return "Hello World";
	}
	
	@Override
	public DataTransfer getData(DataTransfer obj) throws RemoteException {
		// TODO Auto-generated method stub
		
		String value = (String) resource.get(obj.getKey());
		obj.setValue(value);
		
		return obj;
	}
	
	@Override
	public boolean putData(DataTransfer obj) throws RemoteException {
		// TODO Auto-generated method stub
		
		resource.put(obj.getKey(), obj.getValue());
				
		return true;
	}
	
	@Override
	public boolean deleteData(DataTransfer obj) throws RemoteException {
		// TODO Auto-generated method stub
		
		resource.remove(obj.getKey());
				
		return true;
	}
	
	public static void main(String args[]) 
    { 
        try 
        { 
        	RPCImpl obj = new RPCImpl();        	
            RPC stub = (RPC) UnicastRemoteObject.exportObject(obj, 0);
            // Bind this object instance to the name "HelloServer" 
        	Registry registry = LocateRegistry.getRegistry();
        	System.setProperty("java.rmi.server.hostname","10.0.0.2");
        	registry.bind("RPCServer", stub);
            //Naming.rebind("RPCServer", obj); 
        } 
        catch (Exception e) 
        { 
            System.out.println("RPCImpl err: " + e.getMessage()); 
            e.printStackTrace(); 
        } 
    }

}
