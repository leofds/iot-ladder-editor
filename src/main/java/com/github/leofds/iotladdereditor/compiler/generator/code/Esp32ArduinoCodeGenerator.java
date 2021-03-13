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
package com.github.leofds.iotladdereditor.compiler.generator.code;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Stack;
import java.util.stream.Stream;

import org.apache.maven.model.Model;

import com.github.leofds.iotladdereditor.application.ProjectContainer;
import com.github.leofds.iotladdereditor.compiler.SourceCode;
import com.github.leofds.iotladdereditor.compiler.domain.GenContext;
import com.github.leofds.iotladdereditor.compiler.domain.IR;
import com.github.leofds.iotladdereditor.compiler.domain.Kind;
import com.github.leofds.iotladdereditor.compiler.domain.Operator;
import com.github.leofds.iotladdereditor.compiler.domain.ProgramFunc;
import com.github.leofds.iotladdereditor.compiler.domain.Quadruple;
import com.github.leofds.iotladdereditor.compiler.domain.Symbol;
import com.github.leofds.iotladdereditor.compiler.domain.SymbolTable;
import com.github.leofds.iotladdereditor.compiler.generator.CodeGenerator;
import com.github.leofds.iotladdereditor.compiler.generator.LabelList;
import com.github.leofds.iotladdereditor.compiler.generator.util.CodeGeneratorUtils;
import com.github.leofds.iotladdereditor.device.Device;
import com.github.leofds.iotladdereditor.device.DeviceMemory;
import com.github.leofds.iotladdereditor.device.Peripheral;
import com.github.leofds.iotladdereditor.device.PeripheralIO;
import com.github.leofds.iotladdereditor.i18n.Strings;
import com.github.leofds.iotladdereditor.ladder.LadderProgram;
import com.github.leofds.iotladdereditor.ladder.ProgramProperties;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.count.CountInstruction;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.timer.TimerInstruction;
import com.github.leofds.iotladdereditor.util.AboutUils;

public class Esp32ArduinoCodeGenerator implements CodeGenerator{
	
	private static final String varTime = "LD_TIME";
	
	private ProjectContainer project;
	private LadderProgram ladderProgram;
	private Device device;
	private ProgramProperties properties;
	
	@Override
	public SourceCode generate(ProjectContainer project) {
		this.project = project;
		ladderProgram = project.getLadderProgram();
		device = project.getLadderProgram().getDevice();
		properties = ladderProgram.getProperties();
		SourceCode c = new SourceCode();

		createSourceFile(project,c);
		
		project.setSourceCode(c);
		return c;
	}
	
	private void createSourceFile(ProjectContainer p,SourceCode c){
		IR ir = p.getIr();

		c.createNewFile("plc/plc.ino");
		addFileDescription(c);
		if(isConnectionConfigured()) {
			addIoTIncludes(c);
		}
		addDefaultDefines(c);
		addPinDefines(c);
		if(isConnectionConfigured()) {
			addWifiConst(c);
			addMqttConst(c);
			addQueueAndPubStruct(c);
			addWifiStateConn(c);
			addMsgFunctionsPropotypes(c);
			addWifiClient(c);
			addPubSubClient(c);
		}
		addTimerStruct(ir, c);
		addCountStruct(ir, c);
		addGlobalSystemVariables(ir, c);
		addTimerSystemFunction(c);
		addGlobalVariables(ir, c);
		addGlobalStructs(ir, c);
		if(isConnectionConfigured()) {
			addTaskCom(c);
			addSendMsg(c);
			addMsgReceiv(ir, c);
			addTelemetryFunction(ir, c);
		}
		addUpdateSystem(c);

		addInputSystemFunction(p, c);
		addOutputSystemFunction(p, c);
		addScanTimeSystemFunction(ir, c);
		addRungs(ir, c);
		addInitSystemFunction(p, ir, c);
		addTaskScan(ir, c);
		
		addSetup(c);
		addLoop(c);
	}
	
	enum LadderStructs{
		TIMER("LD_TIMER"),
		COUNTER("LD_COUNTER");
		
		String value;
		
		private LadderStructs(String value){
			this.value = value;
		}
	};
	
	private boolean isConnectionConfigured() {
		if(	properties.getWifiSsid() != null && !properties.getWifiSsid().isEmpty() &&
			properties.getBrokerAddress()!= null && !properties.getBrokerAddress().isEmpty() &&
			properties.getBrokerPort() > 0 && properties.getBrokerPort() < 65536) {
			return true;
		}
		return false;
	}
	
	private String cname(String name) {
		name = name.replace(":", ".");
		if(name.charAt(0) == '_') {
			return "_LD"+name;
		}
		if(name.charAt(0) >= '0' && name.charAt(0) <= '9'){
			return name;
		}
		return "LD_"+name;
	}
	
	private String dType(Class<?> type){
		if(type.equals(Boolean.class)){
			return "uint8_t";
		}
		if(type.equals(Integer.class)){
			return "int32_t";
		}
		if(type.equals(Float.class)){
			return "float";
		}
		if(type.equals(Long.class)){
			return "uint64_t";
		}
		if(type.equals(Void.class)){
			return "void";
		}
		if(type.equals(TimerInstruction.class)){
			return LadderStructs.TIMER.value;
		}
		if(type.equals(CountInstruction.class)){
			return LadderStructs.COUNTER.value;
		}
		return null;
	}
	
	private void addFileDescription(SourceCode c){
		String version = "";
		String url = "";
		Model model = AboutUils.getProjectModel();
		if(model != null) {
			version = model.getVersion();
			url = model.getUrl();
		}
		c.addl( "// "+Strings.appName()+" ("+version+")\r\n" + 
				"//\r\n" + 
				"// Copyright (C) 2021  Leonardo Fernandes\r\n" + 
				"//\r\n" + 
				"// "+url+"\r\n" + 
				"//\r\n" + 
				"// Project: "+project.getName());
	}
	
	private void addIoTIncludes(SourceCode c) {
		c.newLine();
		c.addl("#include <WiFiClientSecure.h>");
		c.addl("#include <PubSubClient.h>");
		c.addl("#include <WiFi.h>");
		c.addl("#include <ArduinoJson.h>");
		
		
	}
	
	private void addDefaultDefines(SourceCode c) {
		c.newLine();
		c.addl("#if CONFIG_FREERTOS_UNICORE\r\n" + 
				"#define ARDUINO_RUNNING_CORE 0\r\n" + 
				"#else\r\n" + 
				"#define ARDUINO_RUNNING_CORE 1\r\n" + 
				"#endif");
	}
	
	private void addPinDefines(SourceCode c) {
		c.newLine();
		for(Peripheral peripheral : device.getPeripherals()) {
			for(PeripheralIO peripheralIO : peripheral.getPeripheralItems()) {
				c.addl("#define "+peripheralIO.getPath()+" "+peripheralIO.getPin());
			}
		}
	}

	private void addWifiConst(SourceCode c) {
		c.newLine();
		String ssid = properties.getWifiSsid() != null ? properties.getWifiSsid() : "";
		String pass = properties.getWifiPassword() != null ? properties.getWifiPassword() : "";
		
		c.addl("const char* ssid = \""+ssid+"\";");
		c.addl("const char* password = \""+pass+"\";");
		
	}
	
	private String readFileJava8(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
 
        try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append("\\\n\"").append(s).append("\\n\""));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }
	
	private void addMqttConst(SourceCode c) {
		c.newLine();
		
		String brokerAddr = properties.getBrokerAddress() != null ? properties.getBrokerAddress() : "";
		String clientId = properties.getMqttClientID() != null ? properties.getMqttClientID() : "";
		
		c.addl("const char* broker_address = \""+brokerAddr+"\";");
		c.addl("const uint16_t broker_port = "+properties.getBrokerPort()+";");
		c.addl("const char* device_id = \""+clientId+"\";");
		c.addl("");
		c.addl("const char* pub_topic = \""+properties.getMqttPubTopic()+"\";");
		c.addl("const char* sub_topic = \""+properties.getMqttSubTopic()+"\";");
		c.addl("");
		c.addl("const char* mqtt_username = \""+properties.getMqttUsername()+"\";");
		c.addl("const char* mqtt_password = \""+properties.getMqttPassword()+"\";");
		c.addl("");
		
		if(properties.isEnableSsl()) {
			String rootCa = "\"\"";
			if(properties.getMqttCa() != null) {
				rootCa = readFileJava8( properties.getMqttCa() );
			}
			c.addl("const char* root_ca = "+rootCa+";");
			c.addl("");
			
			if(properties.isUseClientCert()) {
				String clientCertificate = "\"\"";
				if(properties.getMqttClientCert() != null) {
					clientCertificate = readFileJava8( properties.getMqttClientCert() );
				}
				c.addl("const char* client_certificate = "+clientCertificate+";");
				c.addl("");
				
				String clientPrivateKey = "\"\"";
				if(properties.getMqttClientPrivateKey() != null) {
					clientPrivateKey = readFileJava8( properties.getMqttClientPrivateKey() );
				}
				c.addl("const char* client_private_key = "+clientPrivateKey+";");
			}
		}
	}
	
	private void addQueueAndPubStruct(SourceCode c) {
		c.newLine();
		c.addl("QueueHandle_t pubqueue;");
		c.newLine();
		c.addl( "typedef struct {\r\n" + 
				"  char *data;\r\n" + 
				"} PubMsg;");
	}

	private void addTimerStruct(IR ir,SourceCode c){
		for(Symbol symbol:ir.getSymbolTable()){
			if(symbol.getType() != null && symbol.getType().equals(TimerInstruction.class)){
				c.newLine();
				c.addl("// Timer struct");
				c.addl("typedef struct {");
				c.addl("  "+dType(Integer.class)+" PRE;");
				c.addl("  "+dType(Integer.class)+" AC;");
				c.addl("  "+dType(Integer.class)+" B;");
				c.addl("  "+dType(Integer.class)+" DN;");
				c.addl("  "+dType(Integer.class)+" EN;");
				c.addl("  "+dType(Long.class)+" TT;");
				c.addl("} "+LadderStructs.TIMER.value+";");
				return;
			}
		}
	}

	private void addCountStruct(IR ir,SourceCode c){
		for(Symbol symbol:ir.getSymbolTable()){
			if(symbol.getType() != null && symbol.getType().equals(CountInstruction.class)){
				c.newLine();
				c.addl("// Count struct");
				c.addl("typedef struct {");
				c.addl("  "+dType(Integer.class)+" PRE;");
				c.addl("  "+dType(Integer.class)+" AC;");
				c.addl("  "+dType(Integer.class)+" DN;");
				c.addl("  "+dType(Integer.class)+" CC;");
				c.addl("} "+LadderStructs.COUNTER.value+";");
				return;
			}
		}
	}
	
	private void addGlobalSystemVariables(IR ir,SourceCode c){
		c.newLine();
		c.addl("union {");
		c.addl("  uint32_t p[2];");
		c.addl("  uint64_t v;");
		c.addl("} "+varTime+";");
		
		if(isConnectionConfigured()) {
			if(properties.getEnableTelemetry()) {
				c.newLine();
				c.addl("uint64_t telemetryTime;");
			}
		}
	}
	
	private void addTimerSystemFunction(SourceCode c){
		c.newLine();
		c.addl("uint64_t "+ProgramFunc.GETTIME.value+"(){");
		c.addl("  return "+varTime+".v;");
		c.addl("}");
	}
	
	private List<Symbol> getGlobalVariables(IR ir,SourceCode c){
		List<Symbol> vars = new ArrayList<Symbol>();
		for(Symbol symbol:ir.getSymbolTable()){
			if(symbol.getKind() != null && symbol.getScope() != null){
				if(symbol.getKind().equals(Kind.VARIABLE)){
					if(symbol.getScope().equals(GenContext.GLOBAL_SCOPE)){
						if(!symbol.getType().equals(TimerInstruction.class) && !symbol.getType().equals(CountInstruction.class)){
							if(!symbol.getName().contains(":")){
								if(!symbol.getName().equals(varTime)){
									vars.add(symbol);
								}
							}
						}
					}
				}
			}
		}
		return vars;
	}
	
	private void addGlobalVariables(IR ir,SourceCode c){
		c.newLine();
		List<Symbol> vars = getGlobalVariables(ir, c);
		for (Symbol symbol : vars) {
			c.addl(dType(symbol.getType())+" "+cname(symbol.getName())+" = 0;");
		}
	}
	
	private void addGlobalStructs(IR ir,SourceCode c){
		c.newLine();
		for(Symbol symbol:ir.getSymbolTable()){
			if(symbol.getKind() != null && symbol.getScope() != null){
				if(symbol.getKind().equals(Kind.VARIABLE)){
					if(symbol.getScope().equals(GenContext.GLOBAL_SCOPE)){
						if(symbol.getType().equals(TimerInstruction.class) || symbol.getType().equals(CountInstruction.class)){
							if(!symbol.getName().contains(":")){
								c.addl(dType(symbol.getType())+" "+cname(symbol.getName())+";");
							}
						}
					}
				}
			}
		}
	}
	
	private void addUpdateSystem(SourceCode c){
		c.newLine();
		c.addl("void "+ProgramFunc.UPDATE.value+"(){");
		c.addl("  unsigned long now = millis();");
		c.addl("  if(now < "+varTime+".p[0]){");
		c.addl("    "+varTime+".p[1]++;");
		c.addl("  }");
		c.addl("  "+varTime+".p[0] = now;");
		c.addl("}");
	}
	
	private void addTelemetryFunction(IR ir, SourceCode c) {
		if(properties.getEnableTelemetry()) {
			c.newLine();
			c.addl( "void telemetry(){\r\n" + 
					"  if(conn_st == CONN_ST_BROKER_CONNECTED){\r\n" + 
					"    uint64_t ttime = getTime();\r\n" + 
					"    if(ttime >= (telemetryTime+"+(properties.getTelemetrySeconds()*1000)+")){\r\n" + 
					"      telemetryTime = ttime;");
			
			c.addl( "      DynamicJsonDocument doc(2048);");
			c.addl( "      char buffer[2048];");
			
			
			List<Symbol> vars = getGlobalVariables(ir, c);

			List<DeviceMemory> memories = new ArrayList<DeviceMemory>();
			if(properties.getTelemetryPubMemory()) {
				memories.addAll(ladderProgram.getIntegerMemory());
				memories.addAll(ladderProgram.getFloatMemory());
			}
			for( Peripheral peripheral: ladderProgram.getDevice().getPeripherals()) {
				if(properties.getTelemetryPutOutput() && peripheral.getSymbol().equalsIgnoreCase("Q")) {
					memories.addAll(peripheral.getPeripheralItems());
				}else if(properties.getTelemetryPubInput() && peripheral.getSymbol().equalsIgnoreCase("I")) {
					memories.addAll(peripheral.getPeripheralItems());
				}
			}
			
			for (Symbol symbol : vars) {
				for(DeviceMemory memory: memories) {
					if(symbol.getName().equals(memory.getName())){
						c.addl( "      doc[\""+symbol.getName()+"\"] = "+cname(symbol.getName())+";");
					}
				}
			}
			
			c.addl( "      serializeJson(doc, buffer, sizeof(buffer)-1);");
			
			c.addl( "      sendMsg(buffer);"); 
			c.addl( "    }\r\n" + 
					"  }\r\n" + 
					"}");
		}
	}
	
	private void addInitSystemFunction(ProjectContainer project,IR ir,SourceCode c){
		c.newLine();
		c.addl("void "+ProgramFunc.INIT.value+"(){");

		c.addl("  "+varTime+".v = 0;");
		Map<String, DeviceMemory> inputFiles = CodeGeneratorUtils.getInput(project, c);
		Map<String, DeviceMemory> outputFiles = CodeGeneratorUtils.getOutput(project, c);
		c.addl("  "+ProgramFunc.UPDATE.value+"();");

		for(Entry<String, DeviceMemory> entry: inputFiles.entrySet()){
			PeripheralIO peripheral = (PeripheralIO) entry.getValue();
			c.addl("  pinMode("+peripheral.getPath()+", INPUT);");
		}
		for(Entry<String, DeviceMemory> entry: outputFiles.entrySet()){
			PeripheralIO peripheral = (PeripheralIO) entry.getValue();
			c.addl("  pinMode("+peripheral.getPath()+", OUTPUT);");
		}
		if(isConnectionConfigured()) {
			if(properties.getEnableTelemetry()) {
				c.addl("  telemetryTime = getTime();");
			}
		}
		c.addl("}");
	}
	
	private void addInputSystemFunction(ProjectContainer project,SourceCode c){
		c.newLine();
		c.addl("void "+ProgramFunc.INPUT.value+"(){");

		Map<String, DeviceMemory> inputFiles = CodeGeneratorUtils.getInput(project, c);
		for(Entry<String, DeviceMemory> entry: inputFiles.entrySet()){
			PeripheralIO peripheral = (PeripheralIO) entry.getValue();
			c.addl("  "+cname(peripheral.getName())+" = digitalRead("+peripheral.getPath()+");");
		}
		c.addl("}");
	}
	
	private void addOutputSystemFunction(ProjectContainer project,SourceCode c){
		c.newLine();
		c.addl("void "+ProgramFunc.OUTPUT.value+"(){");

		Map<String, DeviceMemory> outputFiles = CodeGeneratorUtils.getOutput(project, c);
		for(Entry<String, DeviceMemory> entry: outputFiles.entrySet()){
			PeripheralIO peripheral = (PeripheralIO) entry.getValue();
			c.addl("  digitalWrite("+peripheral.getPath()+", "+cname(peripheral.getName())+");");
		}
		c.addl("}");
	}
	
	private void addScanTimeSystemFunction(IR ir,SourceCode c){
		for(Symbol symbol:ir.getSymbolTable()){
			if(symbol != null){
				if(symbol.getName() != null && symbol.getName().equals(ProgramFunc.SCANTIME.value)){
					c.newLine();
					c.addl("void "+ProgramFunc.SCANTIME.value+"(){");
					c.addl("  static uint64_t ltime = 0;");
					c.addl("  static uint32_t scanCount = 0;");
					c.addl("  uint64_t ctime = "+varTime+".v;");
					c.addl("  if(ctime - ltime > 1000){");
					c.addl("    char buf[64];");
					c.addl("    float st;");
					c.addl("    st = 1.0/scanCount;");
					c.addl("    sprintf(buf, \"ScanTime=%.2fms, %d/sec\", st*1000, scanCount);");
					c.addl("    Serial.println(buf);");
					c.addl("    scanCount = 0;");
					c.addl("    ltime = ctime;");
					c.addl("  }else{");
					c.addl("    scanCount++;");
					c.addl("  }");
					c.addl("}");
					return;
				}
			}
		}
	}
	
	
	private List<Symbol> getParamOfFunction(IR ir, Symbol func){
		Stack<Symbol> pars = new Stack<Symbol>();
		for(Quadruple quad:ir.getQuadruples()){
			if(quad.getOperator() != null){
				if(quad.getOperator().equals(Operator.PARAM)){
					pars.push(quad.getArgument1());
				}
				if(quad.getOperator().equals(Operator.CALL) && quad.getArgument1().equals(func)){
					int count = Integer.parseInt( quad.getArgument2().getName() );
					List<Symbol> fpar = new ArrayList<Symbol>();
					for(;count>0;count--){
						fpar.add(pars.pop());
					}
					return fpar;
				}
			}
		}
		return null;
	}
	
	private String getFunc(IR ir,Symbol symbol){
		String func = dType(symbol.getType())+" "+symbol.getName()+"(";
		List<Symbol> pars = getParamOfFunction(ir,symbol);
		if(pars != null && pars.size()!=0){
			for(int i=0;i<pars.size();i++){
				func += dType(pars.get(i).getType())+" ";
				func += pars.get(i).getName();
				if(i < (pars.size()-1)){
					func += ", ";
				}
			}
		}else{
			func += "void";
		}
		func += ")";
		return func;
	}
	
	private void addLocalVariables(IR ir,SourceCode c,String scope){
		SymbolTable symbols = ir.getSymbolTable();
		for (Symbol symbol : symbols) {
			if(symbol != null){
				if(symbol.getKind() != null){
					if(symbol.getKind().equals(Kind.VARIABLE)){
						if(symbol.getScope().equals(scope)){
							c.addl("  "+dType(symbol.getType())+" "+cname(symbol.getName())+";");
						}
					}
				}
			}
		}
	}
	
	private void addRungs(IR ir,SourceCode c){
		LabelList labels = new LabelList();
		Stack<String> pars = new Stack<String>();
		List<Quadruple> quadruples = ir.getQuadruples();
		for (Quadruple quadruple : quadruples) {
			Operator operator = quadruple.getOperator();
			Symbol argument1 = quadruple.getArgument1();
			Symbol argument2 = quadruple.getArgument2();
			Symbol result = quadruple.getResult();

			if(operator != null){

				switch(operator){
				case LABEL:
					switch(result.getKind()){
					case FUNCTION:
						if(!result.getName().equals("main")){
							c.newLine();
							c.add( getFunc(ir, result) );
							c.addl("{");
							addLocalVariables(ir, c, result.getName());
						}else{
							return;
						}
						break;
					case LABEL:
						int count = labels.count(result.getName());
						for(int i=count;i>0;i--){
							for(int j=0;j<i;j++) { c.add("  "); }
							c.addl("}");
							labels.remove(result.getName());
						}
						break;
					default:
						break;
					}
					break;
				case GOTO:
					for(int i=0;i<labels.size();i++) { 
						c.add("  ");
					}
					labels.removeLast();
					c.addl("}else{");
					labels.add(result.getName());
					break;
				case RETURN:
					c.addl("}");
					break;
				case ASSIGNMENT:
					for(int i=0;i<labels.size();i++) { 
						c.add("  ");
					}
					c.addl("  "+cname(result.getName())+" = "+cname(argument1.getName())+";");
					break;
				case NOT:
					for(int i=0;i<labels.size();i++){ 
						c.add("  ");
					}
					c.addl("  "+cname(result.getName())+" = ~((int)"+cname(argument1.getName())+");");	//FIXME verificar se funciona
					break;
				case PARAM:
					pars.push(argument1.getName());
					break;
				case CALL:
					for(int i=0;i<labels.size();i++){
						c.add("  ");
					}
					if(result != null){
						c.add("  "+cname(result.getName())+" =");
					}
					c.add("  "+argument1.getName()+"(");
					int countPars = Integer.parseInt(argument2.getName()); 
					for(int i=1;i<=countPars;i++){
						c.add(pars.pop());
						if(i!=countPars){
							c.add(", ");
						}
					}
					c.addl(");");
					break;
				case IF:
					for(int i=0;i<labels.size();i++){ 
						c.add("  ");
					}
					c.addl("  if(!"+cname(argument1.getName())+"){");
					labels.add(result.getName());
					break;
				case IFFALSE:
					for(int i=0;i<labels.size();i++){ 
						c.add("  ");
					}
					c.addl("  if("+cname(argument1.getName())+"){");
					labels.add(result.getName());
					break;
				case IFEQ:
					for(int i=0;i<labels.size();i++){ 
						c.add("  ");
					}
					c.addl("  if("+cname(argument1.getName())+" != "+cname(argument2.getName())+"){");
					labels.add(result.getName());
					break;
				case IFGEQ:
					for(int i=0;i<labels.size();i++){ 
						c.add("  ");
					}
					c.addl("  if("+cname(argument1.getName())+" < "+cname(argument2.getName())+"){");
					labels.add(result.getName());
					break;
				case IFG:
					for(int i=0;i<labels.size();i++){ 
						c.add("  ");
					}
					c.addl("  if("+cname(argument1.getName())+" <= "+cname(argument2.getName())+"){");
					labels.add(result.getName());
					break;
				case IFLE:
					for(int i=0;i<labels.size();i++){ 
						c.add("  ");
					}
					c.addl("  if("+cname(argument1.getName())+" > "+cname(argument2.getName())+"){");
					labels.add(result.getName());
					break;
				case IFL:
					for(int i=0;i<labels.size();i++){ 
						c.add("  ");
					}
					c.addl("  if("+cname(argument1.getName())+" >= "+cname(argument2.getName())+"){");
					labels.add(result.getName());
					break;
				case IFNEQ:
					for(int i=0;i<labels.size();i++){ 
						c.add("  ");
					}
					c.addl("  if("+cname(argument1.getName())+" == "+cname(argument2.getName())+"){");
					labels.add(result.getName());
					break;
				case SUB:
					for(int i=0;i<labels.size();i++){ 
						c.add("  ");
					}
					c.addl("  "+cname(result.getName())+" = "+cname(argument1.getName())+" - "+cname(argument2.getName())+";");
					break;
				case ADD:
					for(int i=0;i<labels.size();i++){ 
						c.add("  ");
					}
					c.addl("  "+cname(result.getName())+" = "+cname(argument1.getName())+" + "+cname(argument2.getName())+";");
					break;
				case MUL:
					for(int i=0;i<labels.size();i++){ 
						c.add("  ");
					}
					c.addl("  "+cname(result.getName())+" = "+cname(argument1.getName())+" * "+cname(argument2.getName())+";");
					break;
				case DIV:
					for(int i=0;i<labels.size();i++){ 
						c.add("  ");
					}
					c.addl("  "+cname(result.getName())+" = "+cname(argument1.getName())+" / "+cname(argument2.getName())+";");
					break;
				case AND:
					for(int i=0;i<labels.size();i++){ 
						c.add("  ");
					}
					c.addl("  "+cname(result.getName())+" = ((int)"+cname(argument1.getName())+" & (int)"+cname(argument2.getName())+");");
					break;
				case OR:
					for(int i=0;i<labels.size();i++){ 
						c.add("  ");
					}
					c.addl("  "+cname(result.getName())+" = ((int)"+cname(argument1.getName())+" | (int)"+cname(argument2.getName())+");");
					break;
				case XOR:
					for(int i=0;i<labels.size();i++){ 
						c.add("  ");
					}
					c.addl("  "+cname(result.getName())+" = ((int)"+cname(argument1.getName())+" ^ (int)"+cname(argument2.getName())+");");
					break;
				case COMMENT:
					break;
				default:
					break;
				}
			}
		}
	}
	
	private void addWifiStateConn(SourceCode c) {
		c.newLine();
		c.addl("enum CONN_ST {\r\n" + 
				"  CONN_ST_START,\r\n" + 
				"  CONN_ST_WIFI_CONNECTING,\r\n" + 
				"  CONN_ST_BROKER_CONNECTING,\r\n" + 
				"  CONN_ST_BROKER_CONNECTED,\r\n" + 
				"};\r\n" + 
				"\r\n" + 
				"CONN_ST conn_st = CONN_ST_START;");
	}
	
	private void addMsgFunctionsPropotypes(SourceCode c) {
		c.newLine();
		c.addl("void msgReceived(char* topic, byte* payload, unsigned int len);");
		c.addl("void sendMsg(char *msg);");
	}
	
	private void addWifiClient(SourceCode c) {
		c.newLine();
		if(properties.isEnableSsl()) {
			c.addl("WiFiClientSecure wiFiClient;");
		}else{
			c.addl("WiFiClient wiFiClient;");
		}
	}
	
	private void addPubSubClient(SourceCode c) {
		c.newLine();
		c.addl("PubSubClient pubSubClient(broker_address, broker_port, msgReceived, wiFiClient);");
	}
	
	private void addTaskCom(SourceCode c) {
		c.newLine();
		c.addl(	"void TaskCom(void *pvParameters) {\r\n" + 
				"  PubMsg msg;\r\n" +
				"  int status;\r\n" + 
				"  \r\n" + 
				"  for(;;){\r\n" + 
				"    if(conn_st == CONN_ST_BROKER_CONNECTED){\r\n" + 
				"      vTaskDelay(100);\r\n" + 
				"    }else{\r\n" +
				"      vTaskDelay(1000);\r\n" +
				"    }\r\n" +
				"    status = WiFi.status();\r\n" + 
				"\r\n" + 
				"    switch(conn_st){\r\n" + 
				"      \r\n" + 
				"      case CONN_ST_START:\r\n" +
				"        Serial.print(\"Wi-Fi connecting ...\");\r\n" +
				"        WiFi.begin(ssid, password);\r\n" + 
				"        conn_st = CONN_ST_WIFI_CONNECTING;\r\n" + 
				"        break;\r\n" + 
				"        \r\n" + 
				"      case CONN_ST_WIFI_CONNECTING:\r\n" + 
				"        if(status == WL_CONNECTED){\r\n" + 
				"          Serial.println(\" connected!\");\r\n" +
				"          Serial.print(\"Broker connecting ...\");\r\n" +
				"          conn_st = CONN_ST_BROKER_CONNECTING;\r\n" +
				"        }else{\r\n" +
				"          Serial.print(\".\");\r\n" + 
				"        }\r\n" + 
				"        break;\r\n" + 
				"        \r\n" + 
				"      case CONN_ST_BROKER_CONNECTING:\r\n" + 
				"        if(status == WL_CONNECTED){\r\n" + 
				"          if(pubSubClient.connected()){\r\n" + 
				"            Serial.println(\" connected!\");\r\n" +
		        "            pubSubClient.subscribe(sub_topic);\r\n" +
		        "            conn_st = CONN_ST_BROKER_CONNECTED;\r\n" + 
				"          }else{\r\n" +
				"            Serial.print(\".\");" );
		if( !properties.getMqttUsername().isEmpty() ) {
			c.addl("            pubSubClient.connect(device_id, mqtt_username, mqtt_password);");
		}else {
			c.addl("            pubSubClient.connect(device_id);");
		}
		c.addl( "          }\r\n" + 
				"        }else{\r\n" + 
				"          Serial.println(\"Wi-Fi disconnected!\");\r\n" + 
				"          WiFi.disconnect();\r\n" + 
				"          vTaskDelay(5000);\r\n" + 
				"          conn_st = CONN_ST_START;\r\n" + 
				"        }\r\n" + 
				"        break;\r\n" + 
				"\r\n" + 
				"      case CONN_ST_BROKER_CONNECTED:\r\n" + 
				"        if(status == WL_CONNECTED){\r\n" + 
				"          if(pubSubClient.connected()){\r\n" + 
				"            pubSubClient.loop();\r\n" + 
				"            if(xQueueReceive(pubqueue, &msg, 0) == pdPASS){\r\n" + 
				"              Serial.print(\"SEND-> \");\r\n" + 
				"              Serial.println(msg.data);\r\n" + 
				"              pubSubClient.publish(pub_topic, msg.data);\r\n" + 
				"              vPortFree(msg.data);\r\n" + 
				"            }\r\n" +
				"          }else{\r\n" + 
				"            Serial.println(\"Broker disconnected!\");\r\n" +
				"            Serial.print(\"Broker connecting ...\");\r\n" +
				"            conn_st = CONN_ST_BROKER_CONNECTING;\r\n" + 
				"          }\r\n" + 
				"        }else{\r\n" + 
				"          Serial.println(\"Wi-Fi disconnected!\");\r\n" + 
				"          WiFi.disconnect();\r\n" + 
				"          vTaskDelay(5000);\r\n" + 
				"          conn_st = CONN_ST_START;\r\n" + 
				"        }\r\n" + 
				"        break;\r\n" + 
				"    }\r\n" + 
				"  }\r\n" + 
				"}");
	}
	
	private void addTaskScan(IR ir, SourceCode c) {
		c.newLine();
		c.addl("void TaskScan(void *pvParameters){");
		c.addl("  for(;;){");
		c.addl("    vTaskDelay(1);");
		c.addl("    "+ProgramFunc.INPUT.value+"();");
		c.addl("    "+ProgramFunc.UPDATE.value+"();");
		
		for(Symbol symbol:ir.getSymbolTable()){
			if( symbol != null && 
				symbol.getKind() != null && 
				symbol.getKind().equals(Kind.FUNCTION) && 
				symbol.getName().contains(ProgramFunc.RUNG.value)){
				c.addl("    "+symbol.getName()+"();");
			}
		}
		if(isConnectionConfigured()) {
			if(properties.getEnableTelemetry()) {
				c.addl("    telemetry();");
			}
		}
		c.addl("    "+ProgramFunc.OUTPUT.value+"();");
		c.addl("  }");
		c.addl("}");		
	}
	
	private void addSendMsg(SourceCode c) {
		c.newLine();
		c.addl( "void sendMsg(char *msg){\r\n" + 
				"  PubMsg pm;\r\n" + 
				"  pm.data = (char*)pvPortMalloc(strlen(msg)+1);\r\n" + 
				"  memset(pm.data, '\\0', strlen(msg)+1);\r\n" +
				"  strcat(pm.data, msg);\r\n" + 
				"  if( xQueueSend( pubqueue, (void*)&pm, (TickType_t) 0 ) != pdTRUE){\r\n" + 
				"    vPortFree(pm.data);\r\n" + 
				"  }\r\n" + 
				"}");
	}
	
	private void addMsgReceiv(IR ir, SourceCode c) {
		c.newLine();
		c.addl( "void msgReceived(char* topic, byte* payload, unsigned int length) {\r\n" + 
				"  if(strcmp(topic,sub_topic)==0){\r\n" + 
				"    char message[length + 1];\r\n" + 
				"    memcpy(message, payload, length);\r\n" + 
				"    message[length] = '\\0';\r\n" + 
				"\r\n" +
				"    Serial.print(\"RECEIVED->\");\r\n" + 
				"    Serial.println(message);\r\n" + 
				"    DynamicJsonDocument doc(2048);\r\n" + 
				"    deserializeJson(doc, message);");
		c.newLine();
		
		List<Symbol> vars = getGlobalVariables(ir, c);
		
		List<DeviceMemory> memories = new ArrayList<DeviceMemory>();
		memories.addAll(ladderProgram.getIntegerMemory());
		memories.addAll(ladderProgram.getFloatMemory());
		
		for (Symbol symbol : vars) {
			for(DeviceMemory memory: memories) {
				if(symbol.getName().equals(memory.getName())){
					c.addl( "    if(doc.containsKey(\""+symbol.getName()+"\")){");
					c.addl( "      "+cname(symbol.getName())+" = ("+dType(symbol.getType())+") doc[\""+symbol.getName()+"\"];");
					c.addl( "    }");
				}
			}
		}
		c.addl( "  }");
		c.addl( "}");
		c.addl("");
	}
	
	private void addSetup(SourceCode c) {
		c.newLine();
		c.addl( "void setup() {\r\n" + 
				"  Serial.begin(115200);\r\n" + 
				"  "+ProgramFunc.INIT.value+"();\r\n" + 
				"  initContext();");
		if(properties.isEnableSsl()) {
			c.addl("  wiFiClient.setCACert(root_ca);");
			if(properties.isUseClientCert()) {
				c.addl("  wiFiClient.setCertificate(client_certificate);");
				c.addl("  wiFiClient.setPrivateKey(client_private_key);");
			}
		}
		if(isConnectionConfigured()) {
			c.addl("  pubqueue = xQueueCreate(1, sizeof(PubMsg));");
			c.addl("  xTaskCreatePinnedToCore(TaskCom,\"TaskCom\",8192,NULL,2,NULL,ARDUINO_RUNNING_CORE);");
		}
		c.addl("  xTaskCreatePinnedToCore(TaskScan,\"TaskScan\",4096,NULL,2,NULL,ARDUINO_RUNNING_CORE);");
		c.addl("}");

	}
	
	private void addLoop(SourceCode c) {
		c.newLine();
		c.addl("void loop() {");
		c.addl("}");
		c.addl("");
	}

}
