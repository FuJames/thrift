package com.fqz.thrift.util;

import java.util.Arrays;
import java.util.List;

/**
 * @author qianzhong.fu
 *
 */
public class AuthorizationConstant {
	public static final String SUPER_TENANT = "carbon.super" ;
	public static final String CLOUD_PORTAL = "cloud" ;

	public static final String AUTHORIZATION_SERVICE_URL = 
			"http://10.20.131.121:9764/authorizationService/1.0/authorizeInstance/";
	public static final String USER_ROLE_PERMISSION_SERVICE_URL = 
			"https://10.20.131.165:9443/";
	
	public static final String FEATURE_URL_MAPPING_TYPE = "urlFeatureMapping";
	public static final String TENANT_ROOT_PATH = "/root/" ;
	public static final String TYPE_TENANT_OWN_INSTANCE = "ownInstance";
	public static final String INSTANCE_ROOT_PATH = "/permission/admin/";
	public static final String JSON_TYPE = "application/json;charset=utf-8" ;
	public static final String SUCCESS = "Success";
	public static final String UNAUTHORIZED = "Unauthorized";
	public static final String UNAUTHORIZED_CODE = "401";
	public static final String CONTENTTYPE = "content-type";

	public static final String CONFIG_FILE = "src/config.properties";
	public static final String AUTHORIZATION_URL = "AuthorizationUrl";
	
	public static final String TENANT_DOMAIN_HEADER = "DatayesUser";
	public static final String ROLES = "roles";
	
	public class Cache{
	        public static final String CACHE_MANAGER_NAME = "DATAYES_CLOUD_CACHEMANAGER";
	        public static final String CACHE_NAME = "DATAYES_SSO_CACHE";
	        public static final String USER_CACHE = "USER_CACHE";
	}
	public static final String PROPERTIES_PATH = "C:/sso/sso.properties";
	public static final String PROPERTIES_CLIENT_KEYSTORE_PATH = "sso.keystore.path";
	public static final String PROPERTIES_CLIENT_KEYSTORE_PWD = "sso.keystore.password";
	public static final String IGNORE = "DatayesIgnore";
	public static final List<String> HTTP_METHODS = Arrays.asList("GET","POST","PUT","DELETE");
	public class CommonPermission{
		public static final String EN_NAME = "common_permission" ;
		public static final String CH_NAME = "普通权限" ;
	}
}
