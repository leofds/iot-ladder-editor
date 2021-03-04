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
package com.github.leofds.iotladdereditor.ladder.symbol.instruction.operator;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

import com.github.leofds.iotladdereditor.application.Mediator;
import com.github.leofds.iotladdereditor.compiler.domain.GenContext;
import com.github.leofds.iotladdereditor.compiler.domain.Kind;
import com.github.leofds.iotladdereditor.compiler.domain.Quadruple;
import com.github.leofds.iotladdereditor.compiler.domain.Symbol;
import com.github.leofds.iotladdereditor.compiler.domain.SymbolTable;
import com.github.leofds.iotladdereditor.compiler.exception.SemanticErrorException;
import com.github.leofds.iotladdereditor.compiler.generator.factory.QuadrupleFactory;
import com.github.leofds.iotladdereditor.device.DeviceMemory;
import com.github.leofds.iotladdereditor.device.Peripheral;
import com.github.leofds.iotladdereditor.device.PeripheralIO;
import com.github.leofds.iotladdereditor.i18n.Strings;
import com.github.leofds.iotladdereditor.ladder.LadderProgram;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.count.CountInstruction;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.operator.view.NotPropertyScreen;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.timer.TimerInstruction;
import com.github.leofds.iotladdereditor.ladder.util.BField;
import com.github.leofds.iotladdereditor.ladder.view.DialogScreen;

public class Not extends OperatorInstruction{

	private static final long serialVersionUID = 1L;
	
	public Not() {
		super();
		setLabel("NOT");
	}

	@Override
	public void paint(Graphics2D g2d){
		super.paint(g2d);
		String valueSrcA = getSourceA().getName().isEmpty() ? "?" : getSourceA().getName();
		String valueDst = getDestiny().getName().isEmpty() ? "?" : getDestiny().getName();
		
		g2d.setColor(new Color(0, 0, 255));
		g2d.setFont(new Font("Arial", Font.PLAIN, 12));
		int len = g2d.getFontMetrics().stringWidth(getLabel());
		g2d.drawString(getLabel(), (blockWidth*getWidth()-len)/2, blockHeight*getHeight()/4);
		g2d.drawString("Src A", (blockWidth)/4, blockHeight*getHeight()/2f);
		g2d.drawString("Dest", (blockWidth)/4, blockHeight*getHeight()/1.2f);

		g2d.setColor(new Color(0, 0, 0));
		len = g2d.getFontMetrics().stringWidth(valueSrcA);
		g2d.drawString(valueSrcA, blockWidth*getWidth()-len-15,  blockHeight*getHeight()/2f);
		len = g2d.getFontMetrics().stringWidth(valueDst);
		g2d.drawString(valueDst, blockWidth*getWidth()-len-15, blockHeight*getHeight()/1.2f);
	}

	@Override
	public List<Quadruple> generateIR(GenContext context) {
		List<Quadruple> quadruples = new ArrayList<Quadruple>();
		SymbolTable symbolTable = context.getSymbolTable();
		Symbol comment = symbolTable.addLiteral(getLabel());
		Symbol status = symbolTable.addIntVar(context.getCurrentStatus(), context.getScope());
		Symbol label = symbolTable.addLabel(context.genLabel(), context.getScope());

		Symbol sourceA;
		if(getSourceA().getName().charAt(0) > '9'){
			sourceA = symbolTable.add(new Symbol(getSourceA().getName(), Kind.VARIABLE, getSourceA().getType(), GenContext.GLOBAL_SCOPE));
		}else{
			sourceA = symbolTable.add(new Symbol(getSourceA().getName(), Kind.CONSTANT, getSourceA().getType(), null));
		}
		Symbol destiny = symbolTable.add(new Symbol(getDestiny().getName(), Kind.VARIABLE, getDestiny().getType(), GenContext.GLOBAL_SCOPE));

		quadruples.add(QuadrupleFactory.createComment( comment ));
		quadruples.add( QuadrupleFactory.createIfFalse(status, label) );
		quadruples.add(QuadrupleFactory.createNot( destiny , sourceA ));
		quadruples.add( QuadrupleFactory.createLabel(label) );
		return quadruples;
	}
	
	@Override
	public boolean addMemory(DeviceMemory memory, int x, int y) {
		if(contains(x, y)){
			BField srcA = new BField(getX()+10, (int)(getX()+(blockWidth*getWidth()-20)), (int)(getY()+(blockHeight*getHeight()/2f-10)), (int)(getY()+(blockHeight*getHeight()/2f)));
			BField dst = new BField(getX()+10, (int)(getX()+(blockWidth*getWidth()-20)), (int)(getY()+(blockHeight*getHeight()/1.2f-10)), (int)(getY()+(blockHeight*getHeight()/1.2f)));
			if(srcA.contain(x, y)){
				setSourceA(memory);
				return true;
			}
			if(dst.contain(x, y)){
				setDestiny(memory);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void analyze() throws SemanticErrorException {
		if(getSourceA() != null && 
				getDestiny() != null &&
				getSourceA().getName() != null && 
				getDestiny().getName() != null &&
				getSourceA().getType() != null && 
				getDestiny().getType() != null &&
				!getSourceA().getName().isEmpty() &&
				!getDestiny().getName().isEmpty()){
			return;
		}
		throw new SemanticErrorException(Strings.invalidInstructionAttribute());
	}
	
	@Override
	public DialogScreen getPropertyScreen() {
		return new NotPropertyScreen(getLabel());
	}
	
	@Override
	public void beforeShowScreen(DialogScreen dialog) {
		LadderProgram ladderProgram = Mediator.getInstance().getProject().getLadderProgram();
		NotPropertyScreen screen = (NotPropertyScreen) dialog;
		List<Peripheral> peripherals = Mediator.getInstance().getProject().getLadderProgram().getDevice().getPeripherals();
		for(Peripheral peripheral:peripherals){
			for(PeripheralIO peripheralIO:peripheral.getPeripheralItems()){
				screen.addMemoryA(peripheralIO);
				screen.addMemoryD(peripheralIO);
			}
		}
		List<DeviceMemory> intMems = ladderProgram.getIntegerMemory();
		for (DeviceMemory intMem : intMems) {
			screen.addMemoryA(intMem);
			screen.addMemoryD(intMem);
		}
		List<DeviceMemory> floatMems = ladderProgram.getFloatMemory();
		for (DeviceMemory flaotMem : floatMems) {
			screen.addMemoryA(flaotMem);
			screen.addMemoryD(flaotMem);
		}
		for(TimerInstruction timer: ladderProgram.getAllTimers()){
			screen.addMemoryA(timer.getPresetMemory());
			screen.addMemoryA(timer.getAccumMemory());
			screen.addMemoryA(timer.getDoneMemory());
			screen.addMemoryA(timer.getEnableMemory());
			screen.addMemoryD(timer.getPresetMemory());
			screen.addMemoryD(timer.getAccumMemory());
			screen.addMemoryD(timer.getDoneMemory());
			screen.addMemoryD(timer.getEnableMemory());
		}
		for(CountInstruction count: ladderProgram.getAllCounts()){
			screen.addMemoryA(count.getPresetMemory());
			screen.addMemoryA(count.getAccumMemory());
			screen.addMemoryA(count.getDoneMemory());
			screen.addMemoryA(count.getCountMemory());
			screen.addMemoryD(count.getPresetMemory());
			screen.addMemoryD(count.getAccumMemory());
			screen.addMemoryD(count.getDoneMemory());
			screen.addMemoryD(count.getCountMemory());
		}
		screen.setSourceA(getSourceA());
		screen.setDestiny(getDestiny());
	}
	
	@Override
	public void afterShowScreen(DialogScreen dialog) {
		NotPropertyScreen screen = (NotPropertyScreen) dialog;
		setSourceA(screen.getSelectedA());
		setDestiny(screen.getSelectedD());
	}
	
	@Override
	public String toString() {
		return "instruction=NOT, " + super.toString();
	}
}
