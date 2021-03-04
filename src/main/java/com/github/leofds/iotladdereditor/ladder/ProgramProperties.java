/*******************************************************************************
 * Copyright (C) 2021 Leonardo Fernandes
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package com.github.leofds.iotladdereditor.ladder;

import java.io.Serializable;

import com.github.leofds.iotladdereditor.compiler.domain.CodeOptions;

public class ProgramProperties implements Serializable{

	private static final long serialVersionUID = 1L;
	private String codeOption;
	private String wifiSsid;
	private String wifiPassword;
	private String brokerAddress;
	private Integer brokerPort;
	private String mqttClientID;
	private String mqttUsername;
	private String mqttPassword;
	private String mqttPubTopic;
	private String mqttSubTopic;
	private Boolean enableSsl;
	private String mqttCa;
	private Boolean useClientCert;
	private String mqttClientCert;
	private String mqttClientPrivateKey;
	private Boolean enableTelemetry;
	private Boolean telemetryPubInput;
	private Boolean telemetryPutOutput;
	private Boolean telemetryPubMemory;
	private Integer telemetrySeconds;
	
	public ProgramProperties() {
		wifiSsid = "";
		wifiPassword = "";
		brokerAddress = "";
		brokerPort = 1883;
		mqttUsername = "";
		mqttPassword = "";
		mqttClientID = "thing_ladder";
		mqttPubTopic = "";
		mqttSubTopic = "";
		enableSsl = false;
		mqttCa = "";
		useClientCert = false;
		mqttClientCert = "";
		mqttClientPrivateKey = ""; 
		enableTelemetry = true;
		telemetryPubInput = true;
		telemetryPutOutput = true;
		telemetryPubMemory = true;
		telemetrySeconds = 5;
	}
	public CodeOptions getCodeOption() {
		return CodeOptions.getByName(codeOption);
	}
	public void setCodeOption(CodeOptions codeOption) {
		this.codeOption = codeOption.name();
	}
	public String getWifiSsid() {
		return wifiSsid;
	}
	public void setWifiSsid(String wifiSsid) {
		this.wifiSsid = wifiSsid;
	}
	public String getWifiPassword() {
		return wifiPassword;
	}
	public void setWifiPassword(String wifiPassword) {
		this.wifiPassword = wifiPassword;
	}
	public String getBrokerAddress() {
		return brokerAddress;
	}
	public void setBrokerAddress(String brokerAddress) {
		this.brokerAddress = brokerAddress;
	}
	public Integer getBrokerPort() {
		return brokerPort;
	}
	public void setBrokerPort(Integer brokerPort) {
		this.brokerPort = brokerPort;
	}
	public String getMqttClientID() {
		return mqttClientID;
	}
	public void setMqttClientID(String mqttClientID) {
		this.mqttClientID = mqttClientID;
	}
	public String getMqttUsername() {
		return mqttUsername;
	}
	public void setMqttUsername(String mqttUsername) {
		this.mqttUsername = mqttUsername;
	}
	public String getMqttPassword() {
		return mqttPassword;
	}
	public void setMqttPassword(String mqttPassword) {
		this.mqttPassword = mqttPassword;
	}
	public String getMqttPubTopic() {
		return mqttPubTopic;
	}
	public void setMqttPubTopic(String mqttPubTopic) {
		this.mqttPubTopic = mqttPubTopic;
	}
	public String getMqttSubTopic() {
		return mqttSubTopic;
	}
	public void setMqttSubTopic(String mqttSubTopic) {
		this.mqttSubTopic = mqttSubTopic;
	}
	public Boolean isEnableSsl() {
		return enableSsl;
	}
	public void setEnableSsl(Boolean enableSsl) {
		this.enableSsl = enableSsl;
	}
	public String getMqttCa() {
		return mqttCa;
	}
	public void setMqttCa(String mqttCa) {
		this.mqttCa = mqttCa;
	}
	public Boolean isUseClientCert() {
		return useClientCert;
	}
	public void setUseClientCert(Boolean useClientCert) {
		this.useClientCert = useClientCert;
	}
	public String getMqttClientCert() {
		return mqttClientCert;
	}
	public void setMqttClientCert(String mqttClientCert) {
		this.mqttClientCert = mqttClientCert;
	}
	public String getMqttClientPrivateKey() {
		return mqttClientPrivateKey;
	}
	public void setMqttClientPrivateKey(String mqttClientPrivateKey) {
		this.mqttClientPrivateKey = mqttClientPrivateKey;
	}
	public Boolean getEnableTelemetry() {
		return enableTelemetry;
	}
	public void setEnableTelemetry(Boolean enableTelemetry) {
		this.enableTelemetry = enableTelemetry;
	}
	public Boolean getTelemetryPubInput() {
		return telemetryPubInput;
	}
	public void setTelemetryPubInput(Boolean telemetryPubInput) {
		this.telemetryPubInput = telemetryPubInput;
	}
	public Boolean getTelemetryPutOutput() {
		return telemetryPutOutput;
	}
	public void setTelemetryPutOutput(Boolean telemetryPutOutput) {
		this.telemetryPutOutput = telemetryPutOutput;
	}
	public Boolean getTelemetryPubMemory() {
		return telemetryPubMemory;
	}
	public void setTelemetryPubMemory(Boolean telemetryPubMemory) {
		this.telemetryPubMemory = telemetryPubMemory;
	}
	public Integer getTelemetrySeconds() {
		return telemetrySeconds;
	}
	public void setTelemetrySeconds(Integer telemetrySeconds) {
		this.telemetrySeconds = telemetrySeconds;
	}
}
