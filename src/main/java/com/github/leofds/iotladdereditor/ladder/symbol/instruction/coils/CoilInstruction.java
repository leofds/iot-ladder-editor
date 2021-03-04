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
package com.github.leofds.iotladdereditor.ladder.symbol.instruction.coils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import com.github.leofds.iotladdereditor.device.IO;
import com.github.leofds.iotladdereditor.device.Peripheral;
import com.github.leofds.iotladdereditor.device.PeripheralIO;
import com.github.leofds.iotladdereditor.i18n.Strings;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.LadderInstruction;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.coils.view.CoilPropertyScreen;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.count.CountInstruction;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.timer.TimerInstruction;
import com.github.leofds.iotladdereditor.ladder.view.DialogScreen;

public abstract class CoilInstruction extends LadderInstruction{

	private static final long serialVersionUID = 1L;

	public CoilInstruction() {
		super(1, 1, 0, 0, new DeviceMemory());
	}

	@Override
	public void paint(Graphics2D g2d){
		super.paint(g2d);
		String name = getMemory().getName().isEmpty() ? "?" : getMemory().getName();
		g2d.setColor(new Color(0, 0, 0));
		g2d.setFont(new Font("Arial", Font.PLAIN, 12));
		int len = g2d.getFontMetrics().stringWidth(name);
		g2d.drawString(name, (blockWidth-len)/2, blockHeight/4);
	}

	@Override
	public boolean addMemory(DeviceMemory memory, int x, int y) {
		if(contains(x, y)){
			if(!memory.getType().equals(TimerInstruction.class) && !memory.getType().equals(CountInstruction.class)){
				setMemory(memory);
				return true;
			}			
		}
		return false;
	}

	@Override
	public void analyze() throws SemanticErrorException {
		if(getMemory() != null && 
				getMemory().getName() != null &&
				getMemory().getType() != null &&
				!getMemory().getName().isEmpty()){
			return;
		}
		throw new SemanticErrorException(Strings.invalidInstructionValue());
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
				project.getLadderProgram().getRungs().delete(CoilInstruction.this);
				me.updateProjectAndViews();
			}
		});
		property.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CoilInstruction.this.viewInstructionProperty();
			}
		});
		return itens;
	}
	
	@Override
	public DialogScreen getPropertyScreen() {
		return new CoilPropertyScreen(getLabel(),getMemory());
	}
	
	@Override
	public void beforeShowScreen(DialogScreen dialog) {
		CoilPropertyScreen screen = (CoilPropertyScreen) dialog;
		List<Peripheral> peripherals = Mediator.getInstance().getProject().getLadderProgram().getDevice().getPeripherals();
		for(Peripheral peripheral:peripherals){
			for(PeripheralIO peripheralIO:peripheral.getPeripheralItems()){
				if(peripheralIO.getIo() == IO.OUTPUT){
					screen.addMemory(peripheralIO);
				}
			}
		}
	}
	
	@Override
	public void afterShowScreen(DialogScreen dialog) {
		CoilPropertyScreen screen = (CoilPropertyScreen) dialog;
		setMemory(screen.getSelected());
	}
	
	@Override
	public List<Quadruple> generateIRInit(GenContext context) {
		return null;
	}
	
	@Override
	public void updateDevice(Device device) {
		DeviceMemory deviceMemory = getMemory();
		if(deviceMemory instanceof PeripheralIO) {
			PeripheralIO currPeripheralIO = (PeripheralIO) deviceMemory;
			PeripheralIO newPeripheralIO = device.getPeripheralIOByName(currPeripheralIO.getName());
			if(newPeripheralIO != null) {
				setMemory(newPeripheralIO);
			}else{
				setMemory(new DeviceMemory());
			}
		}
	}
}
