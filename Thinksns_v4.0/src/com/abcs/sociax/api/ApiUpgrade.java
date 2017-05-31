package com.abcs.sociax.api;

import com.abcs.sociax.modle.VersionInfo;
import com.thinksns.sociax.thinksnsbase.exception.ApiException;

public interface ApiUpgrade {
	public static final String MOD_NAME = "Upgrade";
	public static final String GET_VERSION = "getVersion";

	/**
	 * 获取版本信息
	 * 
	 * @return
	 * @throws ApiException
	 */
	public VersionInfo getVersion() throws ApiException;
}