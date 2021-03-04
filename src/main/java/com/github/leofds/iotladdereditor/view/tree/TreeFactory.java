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
package com.github.leofds.iotladdereditor.view.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.swing.ImageIcon;

import com.github.leofds.iotladdereditor.device.Device;
import com.github.leofds.iotladdereditor.device.DeviceMemory;
import com.github.leofds.iotladdereditor.device.Peripheral;
import com.github.leofds.iotladdereditor.device.PeripheralIO;
import com.github.leofds.iotladdereditor.ladder.LadderProgram;
import com.github.leofds.iotladdereditor.ladder.rung.Rung;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.coils.Coil;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.coils.NegatedCoil;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.coils.NegativeTransitionCoil;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.coils.PositiveTransitionCoil;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.coils.ResetCoil;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.coils.ResetRetentiveMemoryCoil;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.coils.RetentiveMemoryCoil;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.coils.SetCoil;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.coils.SetRetentiveMemoryCoil;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.compare.Equal;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.compare.GreaterEqual;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.compare.GreaterThan;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.compare.LessEqual;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.compare.LessThan;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.compare.NotEqual;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.contacts.NegativeTransitionContact;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.contacts.NormallyClosedContact;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.contacts.NormallyOpenContact;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.contacts.PositiveTransitionContact;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.count.CountDown;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.count.CountInstruction;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.count.CountUp;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.operator.Add;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.operator.And;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.operator.Assignment;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.operator.Div;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.operator.Mul;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.operator.Not;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.operator.Or;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.operator.Sub;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.operator.Xor;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.reset.Reset;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.system.ScanTime;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.timer.TimerInstruction;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.timer.TimerOffDelay;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.timer.TimerOnDelay;

public class TreeFactory {

	public static CustomTreeNode createDeviceTree(Device device){
		CustomTreeNode rootNode = new CustomTreeNode(device.getName(),new ImageIcon(TreeFactory.class.getResource("/images/Board_16x16.png")));
		for(Peripheral peripheral: device.getPeripherals()){
			CustomTreeNode node = new CustomTreeNode(peripheral.getName(),new ImageIcon(TreeFactory.class.getResource("/images/IO_16x16.png")));
			for(PeripheralIO peripheralItem:peripheral.getPeripheralItems()){
				if(peripheralItem.getPin() != null && !peripheralItem.getPin().isEmpty()) {
					node.addChild(new CustomTreeNode(peripheralItem.getName(),peripheralItem,new ImageIcon(TreeFactory.class.getResource("/images/IOB_16x16.png"))));
				}
			}
			rootNode.addChild(node);
		}
		return rootNode;
	}
	
	public static CustomTreeNode createMemoryTree(LadderProgram ladderProgram){
		TreeSet<TimerInstruction> timers = ladderProgram.getAllTimers();
		TreeSet<CountInstruction> counters = ladderProgram.getAllCounts();
		
		List<DeviceMemory> intMem = new ArrayList<DeviceMemory>();
		List<DeviceMemory> floatMem = new ArrayList<DeviceMemory>();
		CustomTreeNode rootNode = new CustomTreeNode("RAM",new ImageIcon(TreeFactory.class.getResource("/images/Memory_16x16.png")));
		CustomTreeNode intNode = new CustomTreeNode("IntegerMemory",new ImageIcon(TreeFactory.class.getResource("/images/Memory_16x16.png")));
		CustomTreeNode floatNode = new CustomTreeNode("FloatMemory",new ImageIcon(TreeFactory.class.getResource("/images/Memory_16x16.png")));
		int i = 1;
		for(DeviceMemory memory: ladderProgram.getIntegerMemory()){ 
			intMem.add(memory);
			intNode.addChild(new CustomTreeNode(String.format("%02d", i++), memory,new ImageIcon(TreeFactory.class.getResource("/images/MemoryB_16x16.png"))));
		}
		i = 1;
		for(DeviceMemory memory: ladderProgram.getFloatMemory()){
			floatMem.add(memory);
			floatNode.addChild(new CustomTreeNode(String.format("%02d", i++), memory,new ImageIcon(TreeFactory.class.getResource("/images/MemoryB_16x16.png"))));
		}
		ladderProgram.setIntegerMemory(intMem);
		ladderProgram.setFloatMemory(floatMem);
		CustomTreeNode timerNode = new CustomTreeNode("Timer",new ImageIcon(TreeFactory.class.getResource("/images/Timer_16x16.png")));
		CustomTreeNode countNode = new CustomTreeNode("Count",new ImageIcon(TreeFactory.class.getResource("/images/Counter_16x16.png")));
		
		for(TimerInstruction instruction:timers){
			String name = instruction.getMemory().getName();
			CustomTreeNode timer = new CustomTreeNode(name,new DeviceMemory(name, TimerInstruction.class),new ImageIcon(TreeFactory.class.getResource("/images/MemoryB_16x16.png")));
			timer.addChild(new CustomTreeNode("PRE",instruction.getPresetMemory(),new ImageIcon(TreeFactory.class.getResource("/images/MemoryB_16x16.png"))));
			timer.addChild(new CustomTreeNode("AC",instruction.getAccumMemory(),new ImageIcon(TreeFactory.class.getResource("/images/MemoryB_16x16.png"))));
			timer.addChild(new CustomTreeNode("DN",instruction.getDoneMemory(),new ImageIcon(TreeFactory.class.getResource("/images/MemoryB_16x16.png"))));
			timer.addChild(new CustomTreeNode("EN",instruction.getEnableMemory(),new ImageIcon(TreeFactory.class.getResource("/images/MemoryB_16x16.png"))));
			timerNode.addChild(timer);
		}
		
		for(CountInstruction instruction:counters){
			String name = instruction.getMemory().getName();
			CustomTreeNode count = new CustomTreeNode(name,new DeviceMemory(name, CountInstruction.class),new ImageIcon(TreeFactory.class.getResource("/images/MemoryB_16x16.png")));
			count.addChild(new CustomTreeNode("PRE",instruction.getPresetMemory(),new ImageIcon(TreeFactory.class.getResource("/images/MemoryB_16x16.png"))));
			count.addChild(new CustomTreeNode("AC",instruction.getAccumMemory(),new ImageIcon(TreeFactory.class.getResource("/images/MemoryB_16x16.png"))));
			count.addChild(new CustomTreeNode("DN",instruction.getDoneMemory(),new ImageIcon(TreeFactory.class.getResource("/images/MemoryB_16x16.png"))));
			count.addChild(new CustomTreeNode("CC",instruction.getCountMemory(),new ImageIcon(TreeFactory.class.getResource("/images/MemoryB_16x16.png"))));
			countNode.addChild(count);
		}
		
		rootNode.addChild(intNode);
		rootNode.addChild(floatNode);
		rootNode.addChild(timerNode);
		rootNode.addChild(countNode);
		return rootNode;
	}
	
	public static CustomTreeNode createInstructionTree(){
		
		CustomTreeNode rootNode = new CustomTreeNode("Ladder",new ImageIcon(TreeFactory.class.getResource("/images/Ladder_16x16.png")));
		CustomTreeNode contactsNode = new CustomTreeNode("Contacts",new ImageIcon(TreeFactory.class.getResource("/images/Contacts_16x16.png")));
		CustomTreeNode coilsNode = new CustomTreeNode("Coils",new ImageIcon(TreeFactory.class.getResource("/images/Coils_16x16.png")));
		CustomTreeNode timerNode = new CustomTreeNode("Timer",new ImageIcon(TreeFactory.class.getResource("/images/Timer_16x16.png")));
		CustomTreeNode countNode = new CustomTreeNode("Count",new ImageIcon(TreeFactory.class.getResource("/images/Counter_16x16.png")));
		CustomTreeNode compareNode = new CustomTreeNode("Compare",new ImageIcon(TreeFactory.class.getResource("/images/Compare_16x16.png")));
		CustomTreeNode logicNode = new CustomTreeNode("Logic",new ImageIcon(TreeFactory.class.getResource("/images/AND_16x16.png")));
		CustomTreeNode mathNode = new CustomTreeNode("Math",new ImageIcon(TreeFactory.class.getResource("/images/Math_16x16.png")));
		CustomTreeNode systemNode = new CustomTreeNode("System",new ImageIcon(TreeFactory.class.getResource("/images/Settings_16x16.png")));
		
		contactsNode.addChild(new CustomTreeNode("Normally open contact", NormallyOpenContact.class,new ImageIcon(TreeFactory.class.getResource("/images/NormallyOpenContact_24x24.png"))));
		contactsNode.addChild(new CustomTreeNode("Normally closed contact", NormallyClosedContact.class,new ImageIcon(TreeFactory.class.getResource("/images/NormallyClosedContact_24x24.png"))));
		contactsNode.addChild(new CustomTreeNode("Positive transition-sensing contact", PositiveTransitionContact.class,new ImageIcon(TreeFactory.class.getResource("/images/PositiveTransitionContact_24x24.png"))));
		contactsNode.addChild(new CustomTreeNode("Negative transition-sensing contact", NegativeTransitionContact.class,new ImageIcon(TreeFactory.class.getResource("/images/NegativeTransitionContact_24x24.png"))));
		
		coilsNode.addChild(new CustomTreeNode("Coil", Coil.class,new ImageIcon(TreeFactory.class.getResource("/images/Coil_24x24.png"))));
		coilsNode.addChild(new CustomTreeNode("Negated coil", NegatedCoil.class,new ImageIcon(TreeFactory.class.getResource("/images/NegatedCoil_24x24.png"))));
		coilsNode.addChild(new CustomTreeNode("SET (latch) coil", SetCoil.class,new ImageIcon(TreeFactory.class.getResource("/images/SetCoil_24x24.png"))));
		coilsNode.addChild(new CustomTreeNode("RESET (unlatch) coil", ResetCoil.class,new ImageIcon(TreeFactory.class.getResource("/images/ResetCoil_24x24.png"))));
		coilsNode.addChild(new CustomTreeNode("Retentive (memory) coil", RetentiveMemoryCoil.class,new ImageIcon(TreeFactory.class.getResource("/images/RetentiveMemoryCoil_24x24.png"))));
		coilsNode.addChild(new CustomTreeNode("SET retentive (memory) coil", SetRetentiveMemoryCoil.class,new ImageIcon(TreeFactory.class.getResource("/images/SetRetentiveMemoryCoil_24x24.png"))));
		coilsNode.addChild(new CustomTreeNode("RESET retentive (memory) coil", ResetRetentiveMemoryCoil.class,new ImageIcon(TreeFactory.class.getResource("/images/ResetRetentiveMemoryCoil_24x24.png"))));
		coilsNode.addChild(new CustomTreeNode("Positive transition-sensing coil", PositiveTransitionCoil.class,new ImageIcon(TreeFactory.class.getResource("/images/PositiveTransitionCoil_24x24.png"))));
		coilsNode.addChild(new CustomTreeNode("Negative transition-sensing coil", NegativeTransitionCoil.class,new ImageIcon(TreeFactory.class.getResource("/images/NegativeTransitionCoil_24x24.png"))));
		
		timerNode.addChild(new CustomTreeNode("Timer On Delay", TimerOnDelay.class,new ImageIcon(TreeFactory.class.getResource("/images/TimerOn_16x16.png"))));
		timerNode.addChild(new CustomTreeNode("Timer Off Delay", TimerOffDelay.class,new ImageIcon(TreeFactory.class.getResource("/images/TimerOff_16x16.png"))));
		timerNode.addChild(new CustomTreeNode("Reset", Reset.class,new ImageIcon(TreeFactory.class.getResource("/images/Reset_24x24.png"))));
		
		countNode.addChild(new CustomTreeNode("Count Up", CountUp.class,new ImageIcon(TreeFactory.class.getResource("/images/CUP_24x24.png"))));
		countNode.addChild(new CustomTreeNode("Count Down", CountDown.class,new ImageIcon(TreeFactory.class.getResource("/images/CDOWN_24x24.png"))));
		countNode.addChild(new CustomTreeNode("Reset", Reset.class,new ImageIcon(TreeFactory.class.getResource("/images/Reset_24x24.png"))));
		
		compareNode.addChild(new CustomTreeNode("Equal", Equal.class,new ImageIcon(TreeFactory.class.getResource("/images/EQ_24x24.png"))));
		compareNode.addChild(new CustomTreeNode("Greater Equal", GreaterEqual.class,new ImageIcon(TreeFactory.class.getResource("/images/GEQ_24x24.png"))));
		compareNode.addChild(new CustomTreeNode("Greater Than", GreaterThan.class,new ImageIcon(TreeFactory.class.getResource("/images/G_24x24.png"))));
		compareNode.addChild(new CustomTreeNode("Less Equal", LessEqual.class,new ImageIcon(TreeFactory.class.getResource("/images/LEQ_24x24.png"))));
		compareNode.addChild(new CustomTreeNode("Less Than", LessThan.class,new ImageIcon(TreeFactory.class.getResource("/images/L_24x24.png"))));
		compareNode.addChild(new CustomTreeNode("Not Equal", NotEqual.class,new ImageIcon(TreeFactory.class.getResource("/images/NEQ_24x24.png"))));
		
		logicNode.addChild(new CustomTreeNode("AND", And.class,new ImageIcon(TreeFactory.class.getResource("/images/AND_24x24.png"))));
		logicNode.addChild(new CustomTreeNode("NOT", Not.class,new ImageIcon(TreeFactory.class.getResource("/images/NOT_24x24.png"))));
		logicNode.addChild(new CustomTreeNode("OR", Or.class,new ImageIcon(TreeFactory.class.getResource("/images/OR_24x24.png"))));
		logicNode.addChild(new CustomTreeNode("XOR", Xor.class,new ImageIcon(TreeFactory.class.getResource("/images/XOR_24x24.png"))));
		
		mathNode.addChild(new CustomTreeNode("Assignment",  Assignment.class,new ImageIcon(TreeFactory.class.getResource("/images/EQ_24x24.png"))));
		mathNode.addChild(new CustomTreeNode("ADD", Add.class,new ImageIcon(TreeFactory.class.getResource("/images/ADD_24x24.png"))));
		mathNode.addChild(new CustomTreeNode("DIV", Div.class,new ImageIcon(TreeFactory.class.getResource("/images/DIV_24x24.png"))));
		mathNode.addChild(new CustomTreeNode("MUL", Mul.class,new ImageIcon(TreeFactory.class.getResource("/images/MUL_24x24.png"))));
		mathNode.addChild(new CustomTreeNode("SUB", Sub.class,new ImageIcon(TreeFactory.class.getResource("/images/SUB_24x24.png"))));
		
		systemNode.addChild(new CustomTreeNode("Scan Time", ScanTime.class, new ImageIcon(TreeFactory.class.getResource("/images/SettingsB_16x16.png"))));
		
		rootNode.addChild(new CustomTreeNode("Rung", Rung.class,new ImageIcon(TreeFactory.class.getResource("/images/Rung_16x16.png"))));
		rootNode.addChild(contactsNode);
		rootNode.addChild(coilsNode);
		rootNode.addChild(timerNode);
		rootNode.addChild(countNode);
		rootNode.addChild(compareNode);
		rootNode.addChild(logicNode);
		rootNode.addChild(mathNode);
		rootNode.addChild(systemNode);
		return rootNode;
	}
}
