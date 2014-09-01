package com.fqz.thrift.server;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TTransportException;

import com.fqz.thrift.service.AuthorizationHandler;
import com.fqz.thrift.service.AuthorizationService;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class Server {
	public static void main(String args[]){
		try {  
	         TServerSocket serverTransport = new TServerSocket(1234);  
	         AuthorizationService.Processor processor = new AuthorizationService
	        		 .Processor(new AuthorizationHandler());  
	         TServer server = new TThreadPoolServer(new TThreadPoolServer
	        		 .Args(serverTransport).processor(processor));  
	         System.out.println("Starting the server...");
	         server.serve();  
	     } catch (TTransportException e) {  
	     } 
	}
}
