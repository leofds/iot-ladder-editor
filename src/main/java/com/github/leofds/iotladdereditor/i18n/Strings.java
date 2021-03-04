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
package com.github.leofds.iotladdereditor.i18n;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.stream.Collectors;

public class Strings {
	
	private static String baseName = "strings.StringsBundle";
	private static ResourceBundle bundle = ResourceBundle.getBundle( baseName, new Locale("en", "US") );
	
	public static void changeLocale(Locale locale) {
		bundle = ResourceBundle.getBundle( baseName, locale );
	}
	
	public static ResourceBundle getBundle() {
		return bundle;
	}

	public static List<ResourceBundle> getResourceBundles() {
		Set<ResourceBundle> rs = new HashSet<ResourceBundle>();

		for (Locale locale : Locale.getAvailableLocales()) {
			try {
				rs.add(ResourceBundle.getBundle(baseName, locale));
			} catch (MissingResourceException e) {
				e.printStackTrace();
			}
		}
		List<ResourceBundle> rsList = rs.stream().collect(Collectors.toList()); 
		Comparator<ResourceBundle> res = (ResourceBundle res1, ResourceBundle res2) -> res1.getString("lang").compareTo(res2.getString("lang"));
		rsList.sort(res);
		return rsList;
	}

	public static String appName() { return bundle.getString("appName"); }
	public static String lang() { return bundle.getString("lang"); }
	public static String compiling() { return bundle.getString("compiling"); }
	public static String error() { return bundle.getString("error"); }
	public static String warning() { return bundle.getString("warning"); }
	public static String invalidInstructionNumber() { return bundle.getString("invalidInstructionNumber"); }
	public static String invalidInstructionValue() { return bundle.getString("invalidInstructionValue"); }
	public static String invalidInstructionName() { return bundle.getString("invalidInstructionName"); }
	public static String invalidInstructionAttribute() { return bundle.getString("invalidInstructionAttribute"); }
	public static String instructionAlreadyExists() { return bundle.getString("instructionAlreadyExists"); }
	public static String successfullyCompleted() { return bundle.getString("successfullyCompleted"); }
	public static String terminatedWithError() { return bundle.getString("terminatedWithError"); }
	public static String user() { return bundle.getString("user"); }
	public static String username() { return bundle.getString("username"); }
	public static String host() { return bundle.getString("host"); }
	public static String port() { return bundle.getString("port"); }
	public static String remoteDirectory() { return bundle.getString("remoteDirectory"); }
	public static String password() { return bundle.getString("password"); }
	public static String invalidConnection() { return bundle.getString("invalidConnection"); }
	public static String undefinedConnectionDevice() { return bundle.getString("undefinedConnectionDevice"); }
	public static String device() { return bundle.getString("device"); }
	public static String instruction() { return bundle.getString("instruction"); }
	public static String view() { return bundle.getString("view"); }
	public static String remove() { return bundle.getString("remove"); }
	public static String property() { return bundle.getString("property"); }
	public static String properties() { return bundle.getString("properties"); }
	public static String memory() { return bundle.getString("memory"); }
	public static String file() { return bundle.getString("file"); }
	public static String open() { return bundle.getString("open"); }
	public static String neww() { return bundle.getString("neww"); }
	public static String examples() { return bundle.getString("examples"); }
	public static String save() { return bundle.getString("save"); }
	public static String saveas() { return bundle.getString("saveas"); }
	public static String cancel() { return bundle.getString("cancel"); }
	public static String exit() { return bundle.getString("exit"); }
	public static String project() { return bundle.getString("project"); }
	public static String build() { return bundle.getString("build"); }
	public static String buildall() { return bundle.getString("buildall"); }
	public static String run() { return bundle.getString("run"); }
	public static String buildRun() { return bundle.getString("buildRun"); }
	public static String stop() { return bundle.getString("stop"); }
	public static String configuration() { return bundle.getString("configuration"); }
	public static String connect() { return bundle.getString("connect"); }
	public static String connected() { return bundle.getString("connected"); }
	public static String connecting() { return bundle.getString("connecting"); }
	public static String notConnected() { return bundle.getString("notConnected"); }
	public static String disconnect() { return bundle.getString("disconnect"); }
	public static String input() { return bundle.getString("input"); }
	public static String output() { return bundle.getString("output"); }
	public static String source() { return bundle.getString("source"); }
	public static String destiny() { return bundle.getString("destiny"); }
	public static String constantValue() { return bundle.getString("constantValue"); }
	public static String sourceAInvalidValue() { return bundle.getString("sourceAInvalidValue"); }
	public static String sourceBInvalidValue() { return bundle.getString("sourceBInvalidValue"); }
	public static String destinyInvalid() { return bundle.getString("destinyInvalid"); }
	public static String help() { return bundle.getString("help"); }
	public static String about() { return bundle.getString("about"); }
	public static String info() { return bundle.getString("info"); }
	public static String author() { return bundle.getString("author"); }
	public static String deviceConfiguration() { return bundle.getString("deviceConfiguration"); }
	public static String peripheral() { return bundle.getString("peripheral"); }
	public static String io() { return bundle.getString("io"); }
	public static String failToCreateFile() { return bundle.getString("failToCreateFile"); }
	public static String instrucion() { return bundle.getString("instrucion"); }
	public static String presetZeroOrNegative() { return bundle.getString("presetZeroOrNegative"); }
	public static String connectionFail() { return bundle.getString("connectionFail"); }
	public static String connectionTimeOut() { return bundle.getString("connectionTimeOut"); }
	public static String unknownHost() { return bundle.getString("unknownHost"); }
	public static String language() { return bundle.getString("language"); }
	public static String langChangeMsg() { return bundle.getString("langChangeMsg"); }
	public static String deviceTool() { return bundle.getString("deviceTool"); }
	public static String selectDeviceExecutableTool() { return bundle.getString("selectDeviceExecutableTool"); }
	public static String path() { return bundle.getString("path"); }
	public static String choose() { return bundle.getString("choose"); }
	public static String saveCurrentProgram() { return bundle.getString("saveCurrentProgram"); }
	public static String saveChanges() { return bundle.getString("saveChanges"); }
	public static String code() { return bundle.getString("code"); }
	public static String wifi() { return bundle.getString("wifi"); }
	public static String mqtt() { return bundle.getString("mqtt"); }
	public static String codeGenerator() { return bundle.getString("codeGenerator"); }
	public static String pinMapping() { return bundle.getString("pinMapping"); }
	public static String general() { return bundle.getString("general"); }
	public static String name() { return bundle.getString("name"); }
	public static String delete() { return bundle.getString("delete"); }
	public static String addInput() { return bundle.getString("addInput"); }
	public static String addOutput() { return bundle.getString("addOutput"); }
	public static String ssid() { return bundle.getString("ssid"); }
	public static String iotEndpoint() { return bundle.getString("iotEndpoint"); }
	public static String publishTopic() { return bundle.getString("publishTopic"); }
	public static String subscribeTopic() { return bundle.getString("subscribeTopic"); }
	public static String titleSaveProjectProperties() { return bundle.getString("titleSaveProjectProperties"); }
	public static String confirmSaveProjectProperties() { return bundle.getString("confirmSaveProjectProperties"); }
	public static String pinNumber() { return bundle.getString("pinNumber"); }
	public static String invalidPinNumber() { return bundle.getString("invalidPinNumber"); }
	public static String topic() { return bundle.getString("topic"); }
	public static String ssl() { return bundle.getString("ssl"); }
	public static String enableSsl() { return bundle.getString("enableSsl"); }
	public static String rootCa() { return bundle.getString("rootCa"); }
	public static String useClientCrt() { return bundle.getString("useClientCrt"); }
	public static String clientCrt() { return bundle.getString("clientCrt"); }
	public static String clientPk() { return bundle.getString("clientPk"); }
	public static String brokerAddr() { return bundle.getString("brokerAddr"); }
	public static String brokerPort() { return bundle.getString("brokerPort"); }
	public static String clientId() { return bundle.getString("clientId"); }
	public static String telemetry() { return bundle.getString("telemetry"); }
	public static String enableTelemetry() { return bundle.getString("enableTelemetry"); }
	public static String telemetryPublishEvery() { return bundle.getString("telemetryPublishEvery"); }
	public static String seconds() { return bundle.getString("seconds"); }
	public static String integerFloatMemory() { return bundle.getString("integerFloatMemory"); }
	public static String errorSavingFile() { return bundle.getString("errorSavingFile"); }
	public static String errorReadingFile() { return bundle.getString("errorReadingFile"); }
	public static String errorOpeningFile() { return bundle.getString("errorOpeningFile"); }
	public static String ladderProgramExtension() { return bundle.getString("ladderProgramExtension"); }
	public static String invalidFileExtension() { return bundle.getString("invalidFileExtension"); }
	public static String theExtensionMustBe() { return bundle.getString("theExtensionMustBe"); }
	public static String doYouWantToOverwriteTheFile() { return bundle.getString("doYouWantToOverwriteTheFile"); }
	
	
	
	
	
}
