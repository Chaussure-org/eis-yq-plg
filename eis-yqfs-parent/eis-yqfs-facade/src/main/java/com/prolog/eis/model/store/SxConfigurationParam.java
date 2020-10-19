package com.prolog.eis.model.store;

import com.prolog.framework.core.annotation.Column;

public class SxConfigurationParam {

	@Column("CONFIG_KEY")
	private String configKey;
	
	private String configValue;

	public SxConfigurationParam() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SxConfigurationParam(String configKey, String configValue) {
		super();
		this.configKey = configKey;
		this.configValue = configValue;
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}
	
	
}
