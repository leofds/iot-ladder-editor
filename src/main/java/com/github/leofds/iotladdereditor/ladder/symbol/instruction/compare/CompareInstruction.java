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
package com.github.leofds.iotladdereditor.ladder.symbol.instruction.compare;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;

import com.github.leofds.iotladdereditor.application.Mediator;
import com.github.leofds.iotladdereditor.application.ProjectContainer;
import com.github.leofds.iotladdereditor.compiler.domain.GenContext;
import com.github.leofds.iotladdereditor.compiler.domain.Quadruple;
import com.github.leofds.iotladdereditor.compiler.exception.SemanticErrorException;
import com.github.leofds.iotladdereditor.device.Device;
import com.github.leofds.iotladdereditor.device.DeviceMemory;
import com.github.leofds.iotladdereditor.device.Peripheral;
import com.github.leofds.iotladdereditor.device.PeripheralIO;
import com.github.leofds.iotladdereditor.i18n.Strings;
import com.github.leofds.iotladdereditor.ladder.LadderProgram;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.LadderInstruction;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.compare.view.ComparePropertyScreen;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.count.CountInstruction;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.timer.TimerInstruction;
import com.github.leofds.iotladdereditor.ladder.util.BField;
import com.github.leofds.iotladdereditor.ladder.view.DialogScreen;

public abstract class CompareInstruction extends LadderInstruction{

	private static final long serialVersionUID = 1L;

	private DeviceMemory sourceA;
	private DeviceMemory sourceB;

	public CompareInstruction() {
		super(2, 2, 0, 0, null);
		this.sourceA = new DeviceMemory();
		this.sourceB = new DeviceMemory();
	}

	public DeviceMemory getSourceA() {
		return sourceA;
	}

	public void setSourceA(DeviceMemory sourceA) {
		this.sourceA = sourceA;
	}

	public DeviceMemory getSourceB() {
		return sourceB;
	}

	public void setSourceB(DeviceMemory sourceB) {
		this.sourceB = sourceB;
	}

	public void setMemory(DeviceMemory memory) {
	}

	@Override
	public void paint(Graphics2D g2d){
		super.paint(g2d);
		g2d.setColor(new Color(0, 0, 255));
		g2d.setStroke(new BasicStroke(1));
		g2d.draw(new Rectangle2D.Float(10, 10, blockWidth*getWidth()-20, blockHeight*getHeight()-20));
		g2d.draw(new Line2D.Double(0, blockHeight/2, 10, blockHeight/2));
		g2d.draw(new Line2D.Double(blockWidth*getWidth()-10, blockHeight/2, blockWidth*getWidth(), blockHeight/2));
	}

	@Override
	public void analyze() throws SemanticErrorException {
		if(sourceA != null && 
				sourceB != null &&
				sourceA.getName() != null && 
				sourceB.getName() != null &&
				sourceA.getType() != null && 
				sourceB.getType() != null &&
				!sourceA.getName().isEmpty() &&
				!sourceB.getName().isEmpty()){
			return;
		}
		throw new SemanticErrorException(Strings.invalidInstructionAttribute());
	}

	@Override
	public void resetMemory(String name) {
		if(sourceA != null && !sourceA.getName().isEmpty()){
			if(sourceA.getName().equals(name)){
				sourceA = new DeviceMemory();
			}
		}
		if(sourceB != null && !sourceB.getName().isEmpty()){
			if(sourceB.getName().equals(name)){
				sourceB = new DeviceMemory();
			}
		}
		super.resetMemory(name);
	}

	@Override
	public boolean addMemory(DeviceMemory memory, int x, int y) {
		if(contains(x, y)){
			if(memory.getType().equals(TimerInstruction.class) || memory.getType().equals(CountInstruction.class)){
				return false;
			}	
			BField srcA = new BField(getX()+10, (int)(getX()+(blockWidth*getWidth()-20)), (int)(getY()+(blockHeight*getHeight()/1.5f-10)), (int)(getY()+(blockHeight*getHeight()/1.5f)));
			BField srcB = new BField(getX()+10, (int)(getX()+(blockWidth*getWidth()-20)), (int)(getY()+(blockHeight*getHeight()/1.2f-10)), (int)(getY()+(blockHeight*getHeight()/1.2f)));
			if(srcA.contain(x, y)){
				setSourceA(memory);
				return true;
			}
			if(srcB.contain(x, y)){
				setSourceB(memory);
				return true;
			}
		}
		return false;
	}

	@Override
	public List<JMenuItem> getMenuItens(final ProjectContainer project){
		List<JMenuItem> itens = new ArrayList<JMenuItem>();
		JMenuItem remove = new JMenuItem(Strings.remove());
		JMenuItem property = new JMenuItem(Strings.property());
		itens.add(remove);
		itens.add(property);
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Mediator me = Mediator.getInstance();
				me.setChangedProgram();
				project.getLadderProgram().getRungs().delete(CompareInstruction.this);
				me.updateProjectAndViews();
			}
		});
		property.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CompareInstruction.this.viewInstructionProperty();
			}
		});
		return itens;
	}

	@Override
	public DialogScreen getPropertyScreen() {
		return new ComparePropertyScreen(getLabel());
	}

	@Override
	public void beforeShowScreen(DialogScreen dialog) {
		LadderProgram ladderProgram = Mediator.getInstance().getProject().getLadderProgram();
		ComparePropertyScreen screen = (ComparePropertyScreen) dialog;
		List<Peripheral> peripherals = Mediator.getInstance().getProject().getLadderProgram().getDevice().getPeripherals();
		for(Peripheral peripheral:peripherals){
			for(PeripheralIO peripheralIO:peripheral.getPeripheralItems()){
				screen.addMemoryA(peripheralIO);
				screen.addMemoryB(peripheralIO);
			}
		}
		List<DeviceMemory> intMems = ladderProgram.getIntegerMemory();
		for (DeviceMemory intMem : intMems) {
			screen.addMemoryA(intMem);
			screen.addMemoryB(intMem);
		}
		List<DeviceMemory> floatMems = ladderProgram.getFloatMemory();
		for (DeviceMemory flaotMem : floatMems) {
			screen.addMemoryA(flaotMem);
			screen.addMemoryB(flaotMem);
		}
		for(TimerInstruction timer: ladderProgram.getAllTimers()){
			screen.addMemoryA(timer.getPresetMemory());
			screen.addMemoryA(timer.getAccumMemory());
			screen.addMemoryA(timer.getDoneMemory());
			screen.addMemoryA(timer.getEnableMemory());
			screen.addMemoryB(timer.getPresetMemory());
			screen.addMemoryB(timer.getAccumMemory());
			screen.addMemoryB(timer.getDoneMemory());
			screen.addMemoryB(timer.getEnableMemory());
		}
		for(CountInstruction count: ladderProgram.getAllCounts()){
			screen.addMemoryA(count.getPresetMemory());
			screen.addMemoryA(count.getAccumMemory());
			screen.addMemoryA(count.getDoneMemory());
			screen.addMemoryA(count.getCountMemory());
			screen.addMemoryB(count.getPresetMemory());
			screen.addMemoryB(count.getAccumMemory());
			screen.addMemoryB(count.getDoneMemory());
			screen.addMemoryB(count.getCountMemory());
		}
		screen.setSourceA(getSourceA());
		screen.setSourceB(getSourceB());
	}

	@Override
	public void afterShowScreen(DialogScreen dialog) {
		ComparePropertyScreen screen = (ComparePropertyScreen) dialog;
		setSourceA(screen.getSelectedA());
		setSourceB(screen.getSelectedB());
	}

	@Override
	public List<Quadruple> generateIRInit(GenContext context) {
		return null;
	}
	
	@Override
	public void updateDevice(Device device) {
		if(sourceA instanceof PeripheralIO) {
			PeripheralIO currPeripheralIO = (PeripheralIO) sourceA;
			PeripheralIO newPeripheralIO = device.getPeripheralIOByName(currPeripheralIO.getName());
			if(newPeripheralIO != null) {
				sourceA = newPeripheralIO;
			}else{
				sourceA = new DeviceMemory();
			}
		}
		if(sourceB instanceof PeripheralIO) {
			PeripheralIO currPeripheralIO = (PeripheralIO) sourceB;
			PeripheralIO newPeripheralIO = device.getPeripheralIOByName(currPeripheralIO.getName());
			if(newPeripheralIO != null) {
				sourceB = newPeripheralIO;
			}else{
				sourceB = new DeviceMemory();
			}
		}
	}
}
