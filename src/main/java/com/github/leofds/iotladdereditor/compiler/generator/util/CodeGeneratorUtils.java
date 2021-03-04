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
package com.github.leofds.iotladdereditor.compiler.generator.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.github.leofds.iotladdereditor.application.Mediator;
import com.github.leofds.iotladdereditor.application.ProjectContainer;
import com.github.leofds.iotladdereditor.compiler.SourceCode;
import com.github.leofds.iotladdereditor.compiler.domain.IR;
import com.github.leofds.iotladdereditor.compiler.domain.Operator;
import com.github.leofds.iotladdereditor.compiler.domain.Quadruple;
import com.github.leofds.iotladdereditor.compiler.domain.Symbol;
import com.github.leofds.iotladdereditor.device.DeviceMemory;
import com.github.leofds.iotladdereditor.device.IO;
import com.github.leofds.iotladdereditor.device.Peripheral;
import com.github.leofds.iotladdereditor.device.PeripheralIO;
import com.github.leofds.iotladdereditor.ladder.rung.Rung;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.LadderInstruction;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.compare.CompareInstruction;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.operator.OperatorInstruction;

public class CodeGeneratorUtils {

	public static void searchInputFiles(Map<String, DeviceMemory> mapFiles,LadderInstruction instruction){
		if(instruction != null){
			searchInputFiles(mapFiles, instruction.getDown());
			searchInputFiles(mapFiles, instruction.getNext());
			if(instruction.getMemory() instanceof PeripheralIO){
				PeripheralIO peripheralIO = (PeripheralIO) instruction.getMemory();
				if(peripheralIO.getIo().equals(IO.INPUT)){
					if(!mapFiles.containsKey(instruction.getMemory().getName())){
						mapFiles.put(instruction.getMemory().getName(), instruction.getMemory());
					}
				}
			}
			if(instruction instanceof OperatorInstruction && ((OperatorInstruction)instruction).getSourceA() instanceof PeripheralIO ){
				OperatorInstruction operatorInstruction = (OperatorInstruction) instruction;
				PeripheralIO peripheralIO = (PeripheralIO) operatorInstruction.getSourceA();
				if(peripheralIO.getIo().equals(IO.INPUT)){
					if(!mapFiles.containsKey(operatorInstruction.getSourceA())){
						mapFiles.put(operatorInstruction.getSourceA().getName(), operatorInstruction.getSourceA());
					}
				}
			}
			if(instruction instanceof OperatorInstruction && ((OperatorInstruction)instruction).getSourceB() instanceof PeripheralIO ){
				OperatorInstruction operatorInstruction = (OperatorInstruction) instruction;
				PeripheralIO peripheralIO = (PeripheralIO) operatorInstruction.getSourceB();
				if(peripheralIO.getIo().equals(IO.INPUT)){
					if(!mapFiles.containsKey(operatorInstruction.getSourceB())){
						mapFiles.put(operatorInstruction.getSourceB().getName(), operatorInstruction.getSourceB());
					}
				}
			}
			if(instruction instanceof CompareInstruction && ((CompareInstruction)instruction).getSourceA() instanceof PeripheralIO ){
				CompareInstruction compareInstruction = (CompareInstruction) instruction;
				PeripheralIO peripheralIO = (PeripheralIO) compareInstruction.getSourceA();
				if(peripheralIO.getIo().equals(IO.INPUT)){
					if(!mapFiles.containsKey(compareInstruction.getSourceA())){
						mapFiles.put(compareInstruction.getSourceA().getName(), compareInstruction.getSourceA());
					}
				}
			}
			if(instruction instanceof CompareInstruction && ((CompareInstruction)instruction).getSourceB() instanceof PeripheralIO ){
				CompareInstruction compareInstruction = (CompareInstruction) instruction;
				PeripheralIO peripheralIO = (PeripheralIO) compareInstruction.getSourceB();
				if(peripheralIO.getIo().equals(IO.INPUT)){
					if(!mapFiles.containsKey(compareInstruction.getSourceB())){
						mapFiles.put(compareInstruction.getSourceB().getName(), compareInstruction.getSourceB());
					}
				}
			}
		}
	}

	public static void searchOutputFiles(Map<String, DeviceMemory> mapFiles,LadderInstruction instruction){
		if(instruction != null){
			searchOutputFiles(mapFiles, instruction.getDown());
			searchOutputFiles(mapFiles, instruction.getNext());
			if(instruction.getMemory() instanceof PeripheralIO){
				PeripheralIO peripheralIO = (PeripheralIO) instruction.getMemory();
				if(peripheralIO.getIo().equals(IO.OUTPUT)){
					if(!mapFiles.containsKey(instruction.getMemory().getName())){
						mapFiles.put(instruction.getMemory().getName(), instruction.getMemory());
					}
				}
			}
			if(instruction instanceof OperatorInstruction && ((OperatorInstruction)instruction).getDestiny() instanceof PeripheralIO ){
				OperatorInstruction operatorInstruction = (OperatorInstruction) instruction;
				PeripheralIO peripheralIO = (PeripheralIO) operatorInstruction.getDestiny();
				if(peripheralIO.getIo().equals(IO.OUTPUT)){
					if(!mapFiles.containsKey(operatorInstruction.getDestiny())){
						mapFiles.put(operatorInstruction.getDestiny().getName(), operatorInstruction.getDestiny());
					}
				}
			}
		}
	}

	public static Map<String, DeviceMemory> getInput(ProjectContainer project, SourceCode c){
		Map<String, DeviceMemory> inputFiles = new HashMap<String, DeviceMemory>();
		for(Rung rung:project.getLadderProgram().getRungs()){
			searchInputFiles(inputFiles, rung.getFirst());
		}
		return inputFiles;
	}

	public static Map<String, DeviceMemory> getOutput(ProjectContainer project, SourceCode c){
		Map<String, DeviceMemory> outputFiles = new HashMap<String, DeviceMemory>();
		for(Rung rung:project.getLadderProgram().getRungs()){
			searchOutputFiles(outputFiles, rung.getFirst());
		}
		return outputFiles;
	}
	
	public static Map<String, DeviceMemory> getInputOutput(ProjectContainer project, SourceCode c, IO io){
		Map<String, DeviceMemory> inputs = new HashMap<String, DeviceMemory>();
		Mediator me = Mediator.getInstance();
		List<Peripheral> peripherals = me.getProject().getLadderProgram().getDevice().getPeripherals();
		for (Peripheral peripheral : peripherals) {
			List<PeripheralIO> peripheralItems = peripheral.getPeripheralItems();
			for (PeripheralIO item : peripheralItems) {
				if(item.getIo().equals(io)) {
					inputs.put(item.getName(), item);
				}
			}
		}
		return inputs;
	}

	public static List<Symbol> getParamOfFunction(IR ir, Symbol func){
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
	
	private static LadderInstruction getInstructionOnRung(LadderInstruction instruction, String memory){
		LadderInstruction it = null;
		if(instruction != null){
			
			DeviceMemory devMem = instruction.getMemory();
			if(devMem != null && devMem.getName().equals(memory)) {
				return instruction;
			}
			
			it = getInstructionOnRung(instruction.getDown(), memory);
			if(it != null) {
				return it;
			}
			it = getInstructionOnRung(instruction.getNext(), memory);
			if(it != null) {
				return it;
			}
		}
		return null;
	}
	
	public static LadderInstruction getInstruction(ProjectContainer project,String name){
		String memory = name;
		if(name.contains(":")) {
			memory = name.substring(0,name.indexOf(':'));
		}
		for(Rung rung:project.getLadderProgram().getRungs()){
			return getInstructionOnRung(rung.getFirst(), memory);
		}
		return null;
	}
}
