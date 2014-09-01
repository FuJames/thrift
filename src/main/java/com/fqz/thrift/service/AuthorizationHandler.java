package com.fqz.thrift.service;

import org.apache.thrift.TException;

import com.fqz.thrift.model.UserPermission;
import com.fqz.thrift.util.AuthorizationUtil;

public class AuthorizationHandler implements AuthorizationService.Iface{

	public int add(int n1, int n2) throws TException {
		System.out.println("add handler");
		return n1+n2;
	}

	public int multiply(int n1, int n2) throws TException {
		System.out.println("multiply handler");
		return n1*n2;
	}

	public boolean authorizeInstance(String tenantDomain, String instanceEnName)
			throws TException {
		return AuthorizationUtil.authorizeInstance(tenantDomain, instanceEnName);
	}

	public boolean authorizeUser(UserPermission permission) throws TException {
		return AuthorizationUtil.authorizeUser(permission);
	}

}
