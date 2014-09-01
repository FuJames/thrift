package com.fqz.thrift.client;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import com.fqz.thrift.service.AuthorizationService;

public class Client {
	public static void main(String args[]) {
		try {
			TTransport transport;
			transport = new TSocket("cloud-greg-dev.datayes.com", 1234);
			transport.open();

			TProtocol protocol = new TBinaryProtocol(transport);
			AuthorizationService.Client client = new 
					AuthorizationService.Client(protocol);
			System.out.println(client.add(100, 200));
			System.out.println(client.multiply(100, 2));
			System.out.println(client.authorizeInstance("datayes.com", "instance"));
			transport.close();
		} catch (Exception e) {
		}
	}
}
