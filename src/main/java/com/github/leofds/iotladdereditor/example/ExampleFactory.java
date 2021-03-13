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
package com.github.leofds.iotladdereditor.example;

import org.apache.commons.lang3.RandomStringUtils;

import com.github.leofds.iotladdereditor.device.DeviceMemory;
import com.github.leofds.iotladdereditor.device.PeripheralIO;
import com.github.leofds.iotladdereditor.ladder.LadderProgram;
import com.github.leofds.iotladdereditor.ladder.ProgramProperties;
import com.github.leofds.iotladdereditor.ladder.rung.Rung;
import com.github.leofds.iotladdereditor.ladder.rung.Rungs;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.coils.Coil;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.contacts.NegativeTransitionContact;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.contacts.NormallyOpenContact;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.contacts.PositiveTransitionContact;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.count.CountUp;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.operator.And;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.operator.Xor;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.reset.Reset;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.timer.TimerOnDelay;

public class ExampleFactory {

	public static LadderProgram createExample(Example example) {
		switch(example) {
		case HelloWorld:
			return createExampleHelloWorld();
		case BlinkLed:
			return createBlinkLed();
		case AllKeys:
			return createAllKeys();
		case ParallelSerial:
			return createParallelAndSerial();
		case BinaryCounter:
			return createBinaryCounter();
		case IotLamp: 
			return createIoTLamp();
		}
		return null;
	}

	private static LadderProgram createExampleHelloWorld() {
		LadderProgram ladderProgram = new LadderProgram();
		
		NormallyOpenContact normaContact = new NormallyOpenContact();
		normaContact.setMemory(ladderProgram.getDevice().getPeripheralIOByName("I1"));
		
		Coil coil = new Coil();
		coil.setMemory(ladderProgram.getDevice().getPeripheralIOByName("Q1"));
		
		Rungs rungs = ladderProgram.getRungs();
		Rung rung0 = rungs.getList().get(0);
		rung0.insertOverride(rung0.getBaseInstruction(0), normaContact);
		rung0.insertOverride(rung0.getBaseInstruction(11), coil);
		return ladderProgram;
	}
	
	private static LadderProgram createBlinkLed() {
		LadderProgram ladderProgram = new LadderProgram();
		
		TimerOnDelay timerOnDelay1 = new TimerOnDelay();
		timerOnDelay1.getMemory().setName("T1");
		
		TimerOnDelay timerOnDelay2 = new TimerOnDelay();
		timerOnDelay2.getMemory().setName("T2");
		
		Coil coil = new Coil();
		coil.setMemory(ladderProgram.getDevice().getPeripheralIOByName("Q1"));
		
		Reset reset = new Reset();
		reset.setMemory(timerOnDelay1.getMemory());
		
		Rungs rungs = ladderProgram.getRungs();
		Rung rung0 = rungs.getList().get(0);
		rung0.insertOverride(rung0.getBaseInstruction(0), timerOnDelay1);
		rung0.insertOverride(rung0.getBaseInstruction(1), coil);
		rung0.insertOverride(rung0.getBaseInstruction(2), timerOnDelay2);
		rung0.insertOverride(rung0.getBaseInstruction(3), reset);
		return ladderProgram;
	}
	
	private static LadderProgram createAllKeys() {
		LadderProgram ladderProgram = new LadderProgram();

		Rungs rungs = ladderProgram.getRungs();
		for(int i=1; i<8; i++) {
			rungs.addRung();
		}
		for(int i=0; i<8; i++) {
			Rung rung0 = rungs.getList().get(i);
			
			NormallyOpenContact normallyOpenContact = new NormallyOpenContact();
			normallyOpenContact.setMemory(ladderProgram.getDevice().getPeripheralIOByName("I"+(i+1)));
			
			Coil coil = new Coil();
			coil.setMemory(ladderProgram.getDevice().getPeripheralIOByName("Q" + (i+1)));
			
			rung0.insertOverride(rung0.getBaseInstruction(0), normallyOpenContact);
			rung0.insertOverride(rung0.getBaseInstruction(11), coil);
		}
		return ladderProgram;
	}
	
	private static LadderProgram createParallelAndSerial() {
		LadderProgram ladderProgram = new LadderProgram();
		
		NormallyOpenContact normallyOpenContact1 = new NormallyOpenContact();
		normallyOpenContact1.setMemory(ladderProgram.getDevice().getPeripheralIOByName("I1"));
		
		NormallyOpenContact nomrNormallyOpenContact2 = new NormallyOpenContact();
		nomrNormallyOpenContact2.setMemory(ladderProgram.getDevice().getPeripheralIOByName("I2"));
		
		NormallyOpenContact normallyOpenContact3 = new NormallyOpenContact();
		normallyOpenContact3.setMemory(ladderProgram.getDevice().getPeripheralIOByName("I3"));
		
		NormallyOpenContact normallyOpenContact4 = new NormallyOpenContact();
		normallyOpenContact4.setMemory(ladderProgram.getDevice().getPeripheralIOByName("I4"));
		
		Coil coil1 = new Coil();
		coil1.setMemory(ladderProgram.getDevice().getPeripheralIOByName("Q1"));
		
		Coil coil2 = new Coil();
		coil2.setMemory(ladderProgram.getDevice().getPeripheralIOByName("Q2"));
		
		Rungs rungs = ladderProgram.getRungs();
		rungs.addRung();
		
		Rung rung0 = rungs.getList().get(0);
		rung0.insertOverride(rung0.getBaseInstruction(0), normallyOpenContact1);
		rung0.insertParallel(normallyOpenContact1, nomrNormallyOpenContact2);
		rung0.insertOverride(rung0.getBaseInstruction(3), coil1);
		
		Rung rung1 = rungs.getList().get(1);
		rung1.insertOverride(rung1.getBaseInstruction(0), normallyOpenContact3);
		rung1.insertOverride(rung1.getBaseInstruction(1), normallyOpenContact4);
		rung1.insertOverride(rung1.getBaseInstruction(3), coil2);
		return ladderProgram;
	}
	
	private static LadderProgram createBinaryCounter() {
		LadderProgram ladderProgram = new LadderProgram();

		TimerOnDelay timerOnDelay1 = new TimerOnDelay();
		timerOnDelay1.getMemory().setName("T1");
		
		CountUp countUp1 = new CountUp();
		countUp1.getMemory().setName("C1");
		
		TimerOnDelay timerOnDelay2 = new TimerOnDelay();
		timerOnDelay2.getMemory().setName("T2");
		
		Reset reset = new Reset();
		reset.setMemory(timerOnDelay1.getMemory());
		
		Rungs rungs = ladderProgram.getRungs();
		Rung rung0 = rungs.getList().get(0);
		rung0.insertOverride(rung0.getBaseInstruction(0), timerOnDelay1);
		rung0.insertOverride(rung0.getBaseInstruction(1), countUp1);
		rung0.insertOverride(rung0.getBaseInstruction(2), timerOnDelay2);
		rung0.insertOverride(rung0.getBaseInstruction(3), reset);
		
		rungs.addRung();
		rungs.addRung();
		
		for(int j=0;j<2;j++) {
			for(int i=0;i<4;i++) {
				rung0 = rungs.getList().get(j+1);
				And and = new And();
				and.setSourceA(countUp1.getAccumMemory());
				and.setSourceB(new DeviceMemory(""+Math.pow(2, i+(j*4)), Integer.class));
				and.setDestiny(ladderProgram.getDevice().getPeripheralIOByName("Q"+(i+1+(j*4))));
				rung0.insertOverride(rung0.getBaseInstruction(i), and);
			}
		}
		return ladderProgram;
	}
	
	private static LadderProgram createIoTLamp() {
		String topic = RandomStringUtils.randomAlphabetic(10);
		
		LadderProgram ladderProgram = new LadderProgram();
		ProgramProperties properties = ladderProgram.getProperties();
		properties.setBrokerAddress("test.mosquitto.org");
		properties.setBrokerPort(1883);
		properties.setMqttClientID(RandomStringUtils.randomAlphabetic(20));
		properties.setMqttPubTopic("/iot-ladder-editor/"+topic+"/pub");
		properties.setMqttSubTopic("/iot-ladder-editor/"+topic+"/sub");
		
		DeviceMemory deviceMemoryI01 = ladderProgram.getIntegerMemoryByName("MI01");
		
		NormallyOpenContact noc1 = new NormallyOpenContact();
		noc1.setMemory(deviceMemoryI01);
		
		Coil coil1 = new Coil();
		coil1.setMemory(ladderProgram.getDevice().getPeripheralIOByName("Q1"));
		
		TimerOnDelay timerOnDelay1 = new TimerOnDelay();
		timerOnDelay1.getMemory().setName("T1");
		timerOnDelay1.setPreset(3);
		
		PeripheralIO pripPeripheralIOI1 = ladderProgram.getDevice().getPeripheralIOByName("I1");
		
		PositiveTransitionContact positiveTransitionContact = new PositiveTransitionContact();
		positiveTransitionContact.setMemory(pripPeripheralIOI1);
		
		Xor xor = new Xor();
		xor.setSourceA(deviceMemoryI01);
		xor.setSourceB(pripPeripheralIOI1);
		xor.setDestiny(deviceMemoryI01);
		
		Reset reset1 = new Reset();
		reset1.setMemory(timerOnDelay1.getMemory());
		
		NegativeTransitionContact negativeTransitionContact = new NegativeTransitionContact();
		negativeTransitionContact.setMemory(pripPeripheralIOI1);
		
		Reset reset2 = new Reset();
		reset2.setMemory(timerOnDelay1.getMemory());
		
		Rungs rungs = ladderProgram.getRungs();
		rungs.addRung();
		rungs.addRung();
		
		Rung rung0 = rungs.getList().get(0);
		Rung rung1 = rungs.getList().get(1);
		Rung rung2 = rungs.getList().get(2);
		
		rung0.insertOverride(rung0.getBaseInstruction(0), noc1);
		rung0.insertOverride(rung0.getBaseInstruction(1), coil1);
		
		rung1.insertOverride(rung1.getBaseInstruction(0), timerOnDelay1);
		rung1.insertOverride(rung1.getBaseInstruction(1), positiveTransitionContact);
		rung1.insertOverride(rung1.getBaseInstruction(2), xor);
		rung1.insertOverride(rung1.getBaseInstruction(3), reset1);
		
		rung2.insertOverride(rung2.getBaseInstruction(0), negativeTransitionContact);
		rung2.insertOverride(rung2.getBaseInstruction(1), reset2);
		return ladderProgram;
	}
}
