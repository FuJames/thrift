package com.fqz.thrift.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.wso2.carbon.CarbonConstants;
import org.wso2.carbon.context.PrivilegedCarbonContext;
import org.wso2.carbon.context.RegistryType;
import org.wso2.carbon.registry.core.Association;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.core.exceptions.RegistryException;
import org.wso2.carbon.user.api.UserStoreException;
import org.wso2.carbon.utils.multitenancy.MultitenantUtils;

import com.fqz.thrift.model.UserPermission;

/**
 * @author qianzhong.fu
 *
 */
public class AuthorizationUtil {
	
	public static boolean authorizeInstance(String tenantDomain, String enName) {
		if(tenantDomain == null || enName == null || tenantDomain.equals("") || enName.equals(""))
			return false;
		String sourcePath = AuthorizationConstant.TENANT_ROOT_PATH + tenantDomain;
		String type = AuthorizationConstant.TYPE_TENANT_OWN_INSTANCE;
		String targetPath = AuthorizationConstant.INSTANCE_ROOT_PATH + enName;
		try {
			List<String> associations = getAssociationsByType(sourcePath, type);
			if(associations.contains(targetPath))
				return true;
			return false;
		} catch (RegistryException e) {
			return false;
		}
	}
	
	public static boolean authorizeUser(UserPermission permission) {
		if(!validateUserPermission(permission))
			return false;
		try {
			String instanceEnName = permission.getInstanceEnName();
			List<String> featureUrlMapping = getFeatureUrlMappingOfInstance(instanceEnName);
			if(featureUrlMapping.size() == 0)
				return false;
			String featureEnName = getFeatureByMethodUrl(featureUrlMapping.get(0),
					permission.getMethod()+permission.getUrl());
			if(featureEnName == null){
				if(haveCommonPermission(permission.getTenantDomain(),
						permission.getUserName(),instanceEnName))
					return true;
				return false;
			}
			String permissionPath = AuthorizationConstant.INSTANCE_ROOT_PATH 
					+ instanceEnName + "/" + featureEnName;
			if(!authorizeUserPermission(permission.getTenantDomain()
					,permission.getUserName(), permissionPath))
				return false;
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	private static boolean haveCommonPermission(String tenantDomain,String userName
			,String instanceEnName) throws UserStoreException {
		String commonPermissionPath = AuthorizationConstant.INSTANCE_ROOT_PATH + 
				instanceEnName + "/" + AuthorizationConstant.CommonPermission.EN_NAME;
		return authorizeUserPermission(tenantDomain, userName, commonPermissionPath);
	}
	private static boolean validateUserPermission(UserPermission permission) {
		if(permission == null || permission.getInstanceEnName() == null 
				|| permission.getUserName() == null || permission.getMethod() == null
				|| permission.getUrl() == null || permission.getTenantDomain() == null)
			return false;
		String tenantDomain = permission.getTenantDomain();
		String userName = permission.getUserName();
		String instanceEnName = permission.getInstanceEnName();
		String method = permission.getMethod();
		if(StringUtil.isNullOrEmpty(tenantDomain) || StringUtil.isNullOrEmpty(userName)
				|| StringUtil.isNullOrEmpty(instanceEnName))
			return false;
		if(!AuthorizationConstant.HTTP_METHODS.contains(method.toUpperCase()))
			return false;
		return true;
		
	}
	private static List<String> getFeatureUrlMappingOfInstance(String instanceEnName) 
			throws RegistryException {
		String instanceRootPath = AuthorizationConstant.INSTANCE_ROOT_PATH + instanceEnName;
		return getAssociationsByType(instanceRootPath,
				AuthorizationConstant.FEATURE_URL_MAPPING_TYPE);
	}

	private static Set<Object> getPropertyKeysetOfResource(String resourcePath)throws RegistryException  {
		Registry registry = getRegistry();
		if(registry == null)
			return null;
		if(!registry.resourceExists(resourcePath))
			return null;
		Resource resource = registry.get(resourcePath);
		Properties properties = resource.getProperties();
		return properties.keySet();
	}
	private static boolean authorizeUserPermission(String tenantDomain,String userName, String permission)throws UserStoreException {
		if(tenantDomain == null || permission == null){
			return false;
		}
		
		if(!tenantDomain.equalsIgnoreCase(AuthorizationConstant.SUPER_TENANT)){
			PrivilegedCarbonContext.startTenantFlow();
			PrivilegedCarbonContext context = PrivilegedCarbonContext.getCurrentContext(); 
			context.setTenantDomain(tenantDomain,true);
			PrivilegedCarbonContext carbonContext = PrivilegedCarbonContext.getThreadLocalCarbonContext(); 
			if(carbonContext == null || carbonContext.getUserRealm() == null){
				return false;
			}
			boolean flag = carbonContext.getUserRealm().getAuthorizationManager().isUserAuthorized(
					MultitenantUtils.getTenantAwareUsername(userName),
					permission,CarbonConstants.UI_PERMISSION_ACTION);
			PrivilegedCarbonContext.endTenantFlow();
			return flag;
		}
		
		PrivilegedCarbonContext context = PrivilegedCarbonContext.getThreadLocalCarbonContext(); 
		if(context == null || context.getUserRealm() == null){
			return false;
		}
		return context.getUserRealm().getAuthorizationManager().isUserAuthorized(
				MultitenantUtils.getTenantAwareUsername(userName),
				permission,CarbonConstants.UI_PERMISSION_ACTION);
	}

	private static List<String> getAssociationsByType(String resourcePath,String type) 
			throws RegistryException{
		Registry registry = getRegistry();
		List<String> associations = new ArrayList<String>();
		Association assos[] = registry.getAssociations(resourcePath, type);
		for(Association association : assos){
			String targetPath = association.getDestinationPath();
			associations.add(targetPath);
		}
		return associations;
	}
	private static String getProperty(String resourcePath,String key)throws RegistryException{
		Registry registry = getRegistry();
		if(key == null || registry == null || !registry.resourceExists(resourcePath))
			return null;
		Resource resource = registry.get(resourcePath);
		return resource.getProperty(key);
	}
	private static Registry getRegistry(){
		Registry registry = null;
		PrivilegedCarbonContext context = PrivilegedCarbonContext.getThreadLocalCarbonContext(); 
		if(context == null){
			return registry;
		}
		registry = (Registry)context.getRegistry(RegistryType.SYSTEM_GOVERNANCE); 
		return registry;
	}
	/*
	 * return featureEnName or null by the given method and url
	 */
	private static String getFeatureByMethodUrl(String mappingPath,String methodUrl) throws RegistryException{
		if(mappingPath == null || mappingPath.equals(""))
			return null;
		Set<Object> set = getPropertyKeysetOfResource(mappingPath);
		for(Object obj : set){
			String key = (String)obj;
			if(methodUrl.startsWith(key))
				return getProperty(mappingPath, key);
		}
		return null;
	}
	
}
