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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import com.github.leofds.iotladdereditor.compiler.domain.CodeOptions;
import com.github.leofds.iotladdereditor.device.Device;
import com.github.leofds.iotladdereditor.device.DeviceFactory;
import com.github.leofds.iotladdereditor.device.DeviceMemory;
import com.github.leofds.iotladdereditor.device.Peripheral;
import com.github.leofds.iotladdereditor.device.PeripheralIO;
import com.github.leofds.iotladdereditor.ladder.rung.Rung;
import com.github.leofds.iotladdereditor.ladder.rung.Rungs;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.LadderInstruction;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.count.CountInstruction;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.timer.TimerInstruction;

public class LadderProgram implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private Rungs rungs;
	private Device device;
	private List<DeviceMemory> integerMemory;
	private List<DeviceMemory> floatMemory;
	private ProgramProperties properties;
	
	public LadderProgram() {
		rungs = new Rungs();
		properties = new ProgramProperties();
		properties.setCodeOption( CodeOptions.values()[0] );
		device = DeviceFactory.createEsp32();
		
		integerMemory = new ArrayList<DeviceMemory>();
		floatMemory = new ArrayList<DeviceMemory>();
		for(int i=1;i<=16;i++){
			integerMemory.add( new DeviceMemory(String.format("MI%02d", i), Integer.class) );
		}
		for(int i=1;i<=16;i++){
			floatMemory.add( new DeviceMemory(String.format("MF%02d", i), Float.class) );
		}
	}
	
	public Rungs getRungs() {
		return rungs;
	}

	public void setRungs(Rungs rungs) {
		this.rungs = rungs;
	}

	public Device getDevice() {
		return device;
	}

	public void setDevice(Device device) {
		this.device = device;
	}

	public List<DeviceMemory> getIntegerMemory() {
		return integerMemory;
	}

	public DeviceMemory getIntegerMemoryByName(String name) {
		for(DeviceMemory mem: integerMemory) {
			if(mem.getName().equals(name)) {
				return mem;
			}
		}
		return null;
	}
	
	public void setIntegerMemory(List<DeviceMemory> integerMemory) {
		this.integerMemory = integerMemory;
	}

	public List<DeviceMemory> getFloatMemory() {
		return floatMemory;
	}
	
	public DeviceMemory getFloatMemoryByName(String name) {
		for(DeviceMemory mem: floatMemory) {
			if(mem.getName().equals(name)) {
				return mem;
			}
		}
		return null;
	}

	public void setFloatMemory(List<DeviceMemory> floatMemory) {
		this.floatMemory = floatMemory;
	}

	public ProgramProperties getProperties() {
		return properties;
	}

	public void setProperties(ProgramProperties properties) {
		this.properties = properties;
	}
	
	private void getTimerInstructions(LadderInstruction instruction, TreeSet<TimerInstruction>  list){
		if(instruction != null){
			getTimerInstructions(instruction.getDown(), list);
			getTimerInstructions(instruction.getNext(), list);
			if(instruction instanceof TimerInstruction){
				if(!instruction.getMemory().getName().isEmpty()){
					list.add((TimerInstruction) instruction);
				}
			}
		}
	}
	
	private void getCountInstructions(LadderInstruction instruction, TreeSet<CountInstruction> list){
		if(instruction != null){
			getCountInstructions(instruction.getDown(), list);
			getCountInstructions(instruction.getNext(), list);
			if(instruction instanceof CountInstruction){
				if(!instruction.getMemory().getName().isEmpty()){
					list.add((CountInstruction) instruction);
				}
			}
		}
	}
	
	public TreeSet<TimerInstruction> getAllTimers(){
		TreeSet<TimerInstruction> timers = new TreeSet<TimerInstruction>();
		for(Rung rung: rungs){
			getTimerInstructions(rung.getFirst(), timers);
		}
		return timers;
	}

	public TreeSet<CountInstruction> getAllCounts(){
		TreeSet<CountInstruction> counts = new TreeSet<CountInstruction>();
		for(Rung rung: rungs){
			getCountInstructions(rung.getFirst(), counts);
		}
		return counts;
	}
	
	public TimerInstruction getTimer(DeviceMemory memory){
		for(TimerInstruction timer:getAllTimers()){
			if(timer.getMemory().equals(memory)){
				return timer;
			}
		}
		return null;
	}
	
	public CountInstruction getCounter(DeviceMemory memory){
		for(CountInstruction count:getAllCounts()){
			if(count.getMemory().equals(memory)){
				return count;
			}
		}
		return null;
	}
	
	public TimerInstruction getTimerByName(String name) {
		for(Rung rung:rungs){
			Iterator<LadderInstruction> itr = rung.iterator();
			while(itr.hasNext()) {
				LadderInstruction inst = itr.next();
				if(inst instanceof TimerInstruction) {
					TimerInstruction timer = (TimerInstruction) inst;
					if(timer.getMemory().getName().equals(name)){
						return timer;
					}
				}
			}
		}
		return null;
	}
	
	public CountInstruction getCounterByName(String name) {
		for(Rung rung: rungs){
			Iterator<LadderInstruction> itr = rung.iterator();
			while(itr.hasNext()) {
				LadderInstruction inst = itr.next();
				if(inst instanceof CountInstruction) {
					CountInstruction counter = (CountInstruction) inst;
					if(counter.getMemory().getName().equals(name)){
						return counter;
					}
				}
			}
		}
		return null;
	}
	
	public DeviceMemory getOriginMemory(String name){
		for(Peripheral peripheral: device.getPeripherals()){
			for(PeripheralIO peripheralIO: peripheral.getPeripheralItems()){
				if(peripheralIO.getName().equals(name)){
					return peripheralIO;
				}
			}
		}
		for(DeviceMemory memory: integerMemory){
			if(memory.getName().equals(name)){
				return memory;
			}
		}
		for(DeviceMemory memory: floatMemory){
			if(memory.getName().equals(name)){
				return memory;
			}
		}
		for(TimerInstruction timer: getAllTimers()){
			if(timer.getMemory() != null && !timer.getMemory().getName().isEmpty()){
				if(timer.getMemory().getName().equals(name)){
					return timer.getMemory();
				}
			}
			if(timer.getPresetMemory() != null && !timer.getPresetMemory().getName().isEmpty()){
				if(timer.getPresetMemory().getName().equals(name)){
					return timer.getPresetMemory();
				}
			}
			if(timer.getAccumMemory() != null && !timer.getAccumMemory().getName().isEmpty()){
				if(timer.getAccumMemory().getName().equals(name)){
					return timer.getAccumMemory();
				}
			}
			if(timer.getDoneMemory() != null && !timer.getDoneMemory().getName().isEmpty()){
				if(timer.getDoneMemory().getName().equals(name)){
					return timer.getDoneMemory();
				}
			}
			if(timer.getEnableMemory() != null && !timer.getEnableMemory().getName().isEmpty()){
				if(timer.getEnableMemory().getName().equals(name)){
					return timer.getEnableMemory();
				}
			}
			if(timer.getTimeBaseMemory() != null && !timer.getTimeBaseMemory().getName().isEmpty()){
				if(timer.getTimeBaseMemory().getName().equals(name)){
					return timer.getTimeBaseMemory();
				}
			}
			if(timer.getTimeMemory() != null && !timer.getTimeMemory().getName().isEmpty()){
				if(timer.getTimeMemory().getName().equals(name)){
					return timer.getTimeMemory();
				}
			}
		}
		for(CountInstruction count: getAllCounts()){
			if(count.getMemory() != null && !count.getMemory().getName().isEmpty()){
				if(count.getMemory().getName().equals(name)){
					return count.getMemory();
				}
			}
			if(count.getPresetMemory() != null && !count.getPresetMemory().getName().isEmpty()){
				if(count.getPresetMemory().getName().equals(name)){
					return count.getPresetMemory();
				}
			}
			if(count.getAccumMemory() != null && !count.getAccumMemory().getName().isEmpty()){
				if(count.getAccumMemory().getName().equals(name)){
					return count.getAccumMemory();
				}
			}
			if(count.getDoneMemory() != null && !count.getDoneMemory().getName().isEmpty()){
				if(count.getDoneMemory().getName().equals(name)){
					return count.getDoneMemory();
				}
			}
			if(count.getCountMemory() != null && !count.getCountMemory().getName().isEmpty()){
				if(count.getCountMemory().getName().equals(name)){
					return count.getCountMemory();
				}
			}
		}
		return null;
	}
	
	public List<PeripheralIO> getIO(){
		List<PeripheralIO> ios = new ArrayList<PeripheralIO>();
		for(Rung rung: rungs){
			for(LadderInstruction instruction:rung){
				DeviceMemory memory = instruction.getMemory();
				if(memory != null){
					if(memory instanceof PeripheralIO){
						ios.add((PeripheralIO) memory);
					}
				}
			}
		}
		return ios;
	}
}
