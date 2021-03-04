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
package com.github.leofds.iotladdereditor.ladder.symbol.instruction.reset;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Arc2D;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import javax.swing.JMenuItem;

import com.github.leofds.iotladdereditor.application.Mediator;
import com.github.leofds.iotladdereditor.application.ProjectContainer;
import com.github.leofds.iotladdereditor.compiler.domain.GenContext;
import com.github.leofds.iotladdereditor.compiler.domain.Kind;
import com.github.leofds.iotladdereditor.compiler.domain.ProgramFunc;
import com.github.leofds.iotladdereditor.compiler.domain.Quadruple;
import com.github.leofds.iotladdereditor.compiler.domain.Symbol;
import com.github.leofds.iotladdereditor.compiler.domain.SymbolTable;
import com.github.leofds.iotladdereditor.compiler.exception.SemanticErrorException;
import com.github.leofds.iotladdereditor.compiler.generator.factory.QuadrupleFactory;
import com.github.leofds.iotladdereditor.device.Device;
import com.github.leofds.iotladdereditor.device.DeviceMemory;
import com.github.leofds.iotladdereditor.i18n.Strings;
import com.github.leofds.iotladdereditor.ladder.LadderProgram;
import com.github.leofds.iotladdereditor.ladder.rung.Rung;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.LadderInstruction;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.count.CountInstruction;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.reset.view.ResetPropertyScreen;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.timer.TimerInstruction;
import com.github.leofds.iotladdereditor.ladder.view.DialogScreen;

public class Reset extends LadderInstruction{

	private static final long serialVersionUID = 1L;

	public Reset() {
		super(1, 1, 0, 0, new DeviceMemory());
		setLabel("RES");
	}

	@Override
	public void paint(Graphics2D g2d){
		super.paint(g2d);
		String name = getMemory().getName().isEmpty() ? "?" : getMemory().getName();

		g2d.setColor(new Color(0, 0, 255));
		g2d.setStroke(new BasicStroke(1));
		g2d.draw(new Line2D.Double(0, blockHeight/2, blockWidth/4, blockHeight/2));
		g2d.draw(new Line2D.Double(blockWidth-blockWidth/4, blockHeight/2, blockWidth, blockHeight/2));
		g2d.draw(new Arc2D.Double(blockWidth/4, blockHeight/3, blockWidth/4, blockHeight/3, 110, 140, Arc2D.OPEN));
		g2d.draw(new Arc2D.Double(blockWidth/2, blockHeight/3, blockWidth/4, blockHeight/3, 290, 140, Arc2D.OPEN));

		g2d.setColor(new Color(0, 0, 255));
		g2d.setFont(new Font("Arial", Font.PLAIN, 12));
		int len = g2d.getFontMetrics().stringWidth(getLabel());
		g2d.drawString(getLabel(), (blockWidth-len)/2, blockHeight/1.7f);

		g2d.setColor(new Color(0, 0, 0));
		g2d.setFont(new Font("Arial", Font.PLAIN, 12));
		len = g2d.getFontMetrics().stringWidth(name);
		g2d.drawString(name, (blockWidth-len)/2, blockHeight/4);
	}

	@Override
	public List<Quadruple> generateIR(GenContext context) {
		SymbolTable symbolTable = context.getSymbolTable();
		Symbol comment 	= symbolTable.addLiteral("RESET ("+getMemory()+")");
		Symbol value 	= symbolTable.add(new Symbol(getMemory().getName(), Kind.VARIABLE, getMemory().getType(), GenContext.GLOBAL_SCOPE));
		Symbol zero 	= symbolTable.addIntConst("0");
		Symbol status	= symbolTable.addBoolVar(context.getCurrentStatus(), context.getScope());
		Symbol label 	= symbolTable.addLabel(context.genLabel(), context.getScope());

		List<Quadruple> quadruples = new ArrayList<Quadruple>();
		quadruples.add( QuadrupleFactory.createComment( comment ) );
		quadruples.add(QuadrupleFactory.createIfFalse(status, label));
		
		LadderProgram ladderProgram = Mediator.getInstance().getProject().getLadderProgram();
		if(getMemory().getType().equals(TimerInstruction.class)){
			TimerInstruction timer = ladderProgram.getTimerByName(getMemory().getName());
			Symbol accumValue = symbolTable.addIntConst(""+timer.getAccum());
			
			Symbol func		= symbolTable.addFunc(ProgramFunc.GETTIME.value, Long.class);
			Symbol done 	= symbolTable.addBoolVar(getMemory().getName()+":DN", GenContext.GLOBAL_SCOPE);
			Symbol accum 	= symbolTable.addBoolVar(getMemory().getName()+":AC", GenContext.GLOBAL_SCOPE);
			Symbol time 	= symbolTable.addBoolVar(getMemory().getName()+":TT", GenContext.GLOBAL_SCOPE);
			Symbol en 	= symbolTable.addBoolVar(getMemory().getName()+":EN", GenContext.GLOBAL_SCOPE);
			quadruples.add(QuadrupleFactory.createAssignment(zero, done));
			quadruples.add(QuadrupleFactory.createAssignment(accumValue, accum));
			quadruples.add(QuadrupleFactory.createAssignment(zero, en));
			quadruples.add(QuadrupleFactory.createCall(func, zero, time));
		}else if(getMemory().getType().equals(CountInstruction.class)){
			CountInstruction counter = ladderProgram.getCounterByName(getMemory().getName());
			Symbol accumValue = symbolTable.addIntConst(""+counter.getAccum());
			
			Symbol done 	= symbolTable.addBoolVar(getMemory().getName()+":DN", GenContext.GLOBAL_SCOPE);
			Symbol accum 	= symbolTable.addBoolVar(getMemory().getName()+":AC", GenContext.GLOBAL_SCOPE);
			Symbol count 	= symbolTable.addBoolVar(getMemory().getName()+":CC", GenContext.GLOBAL_SCOPE);
			quadruples.add(QuadrupleFactory.createAssignment(zero, done));
			quadruples.add(QuadrupleFactory.createAssignment(accumValue, accum));
			quadruples.add(QuadrupleFactory.createAssignment(zero, count));
		}else{
			quadruples.add(QuadrupleFactory.createAssignment(zero, value));
		}
		quadruples.add(QuadrupleFactory.createLabel(label));
		return quadruples;
	}

	@Override
	public boolean addMemory(DeviceMemory memory, int x, int y) {
		if(contains(x, y)){
			if(memory.getType().equals(TimerInstruction.class) || memory.getType().equals(CountInstruction.class)){
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
				!getMemory().getName().isEmpty() && 
				!getMemory().getName().contains("?")){
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
				project.getLadderProgram().getRungs().delete(Reset.this);
				me.updateProjectAndViews();
			}
		});
		property.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Reset.this.viewInstructionProperty();
			}
		});
		return itens;
	}
	
	@Override
	public DialogScreen getPropertyScreen() {
		return new ResetPropertyScreen(getLabel(),getMemory());
	}

	@Override
	public void beforeShowScreen(DialogScreen dialog) {
		ResetPropertyScreen screen = (ResetPropertyScreen) dialog;
		
		TreeSet<DeviceMemory> timerTree = new TreeSet<DeviceMemory>();
		TreeSet<DeviceMemory> countTree = new TreeSet<DeviceMemory>();
		
		for(Rung rung:Mediator.getInstance().getProject().getLadderProgram().getRungs()){
			for(LadderInstruction instruction:rung){
				if(getMemory() != null){
					if(instruction instanceof TimerInstruction){
						DeviceMemory mem = ((TimerInstruction) instruction).getMemory();
						if(mem != null && !mem.getName().isEmpty()){
							timerTree.add(((TimerInstruction) instruction).getMemory());
						}
					}else if(instruction instanceof CountInstruction){
						DeviceMemory mem = ((CountInstruction) instruction).getMemory();
						if(mem != null && !mem.getName().isEmpty()){
							countTree.add(((CountInstruction) instruction).getMemory());
						}
					}
				}
			}
		}
		for(DeviceMemory mTimer:timerTree){
			screen.addMemory(mTimer);
		}
		for(DeviceMemory cTimer:countTree){
			screen.addMemory(cTimer);
		}
	}

	@Override
	public void afterShowScreen(DialogScreen dialog) {
		ResetPropertyScreen screen = (ResetPropertyScreen) dialog;
		setMemory(screen.getSelected());
	}

	@Override
	public String toString() {
		return "instruction=(RESET), " + super.toString();
	}

	@Override
	public List<Quadruple> generateIRInit(GenContext context) {
		return null;
	}


	@Override
	public void updateDevice(Device device) {
	}

}
