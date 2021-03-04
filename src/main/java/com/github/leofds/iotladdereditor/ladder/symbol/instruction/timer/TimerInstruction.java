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
package com.github.leofds.iotladdereditor.ladder.symbol.instruction.timer;

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
import com.github.leofds.iotladdereditor.compiler.exception.SemanticErrorException;
import com.github.leofds.iotladdereditor.compiler.exception.SemanticWarnigException;
import com.github.leofds.iotladdereditor.device.Device;
import com.github.leofds.iotladdereditor.device.DeviceMemory;
import com.github.leofds.iotladdereditor.i18n.Strings;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.LadderInstruction;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.timer.view.TimerPropertyScreen;
import com.github.leofds.iotladdereditor.ladder.view.DialogScreen;

public abstract class TimerInstruction extends LadderInstruction{

	private static final long serialVersionUID = 1L;
	
	private int preset;			//PRE
	private int accum;			//AC
	private int base;			//B
	private long time;			//TT
	private boolean done;		//DN
	private boolean enable;		//EN
	
	private DeviceMemory enableMemory = new DeviceMemory(":EN", Boolean.class);
	private DeviceMemory doneMemory = new DeviceMemory(":DN", Boolean.class);
	private DeviceMemory presetMemory = new DeviceMemory(":PRE", Integer.class);
	private DeviceMemory accumMemory = new DeviceMemory(":AC", Integer.class);
	private DeviceMemory timeBaseMemory = new DeviceMemory(":B", Long.class);
	private DeviceMemory timeMemory = new DeviceMemory(":TT", Long.class);

	public TimerInstruction() {
		super(2, 2, 0, 0, new DeviceMemory("",TimerInstruction.class));
		this.preset = 1;
		this.accum = 0;
		this.base = 100;
		this.done = false;
		this.enable = false;
		this.time = 0;
	}
	
	public DeviceMemory getEnableMemory(){
		enableMemory.setName(getMemory().getName()+":EN");
		return enableMemory;
	}
	
	public DeviceMemory getDoneMemory(){
		doneMemory.setName(getMemory().getName()+":DN");
		return doneMemory;
	}
	
	public DeviceMemory getPresetMemory(){
		presetMemory.setName(getMemory().getName()+":PRE");
		return presetMemory;
	}
	
	public DeviceMemory getAccumMemory(){
		accumMemory.setName(getMemory().getName()+":AC");
		return accumMemory;
	}
	
	public DeviceMemory getTimeMemory(){
		timeMemory.setName(getMemory().getName()+":TT");
		return timeMemory;
	}
	
	public DeviceMemory getTimeBaseMemory(){
		timeBaseMemory.setName(getMemory().getName()+":B");
		return timeBaseMemory;
	}

	public int getPreset() {
		return preset;
	}

	public void setPreset(int preset) {
		this.preset = preset;
	}

	public int getAccum() {
		return accum;
	}

	public void setAccum(int accum) {
		this.accum = accum;
	}

	public boolean isTimerDone() {
		return done;
	}

	public void setTimerDone(boolean timerDone) {
		this.done = timerDone;
	}

	public boolean isTimerEnable() {
		return enable;
	}

	public void setTimerEnable(boolean timerEnable) {
		this.enable = timerEnable;
	}

	public int getTimeBase() {
		return base;
	}

	public void setTimeBase(int timeBase) {
		this.base = timeBase;
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
	public void analyze() throws SemanticErrorException, SemanticWarnigException {
		if(preset >= -999999 && 
				preset <= 999999 && 
				accum >= -999999 && 
				accum <= 999999 &&
				base >= -999999 && 
				base <= 999999){
			if(getMemory() != null &&
					getMemory().getName() != null &&
					getMemory().getType() != null &&
					!getMemory().getName().isEmpty()){
				if(preset <= 0){
					throw new SemanticWarnigException(Strings.presetZeroOrNegative());
				}
				return;
			}
			throw new SemanticErrorException(Strings.invalidInstructionNumber());
		}
		throw new SemanticErrorException(Strings.invalidInstructionAttribute());
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
				project.getLadderProgram().getRungs().delete(TimerInstruction.this);
				me.updateProjectAndViews();
			}
		});
		property.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TimerInstruction.this.viewInstructionProperty();
			}
		});
		return itens;
	}
	
	@Override
	public DialogScreen getPropertyScreen() {
		return new TimerPropertyScreen(getLabel(),getMemory());
	}
	
	@Override
	public void beforeShowScreen(DialogScreen dialog) {
		TimerPropertyScreen screen = (TimerPropertyScreen) dialog;
		screen.setName(getMemory().getName());
		screen.setPreset(getPreset());
		screen.setAccum(getAccum());
		screen.setTimeBase(getTimeBase());
	}
	
	@Override
	public void afterShowScreen(DialogScreen dialog) {
		TimerPropertyScreen screen = (TimerPropertyScreen) dialog;
		getMemory().setName(screen.getName());
		setPreset(screen.getPreset());
		setAccum(screen.getAccum());
		setTimeBase(screen.getTimeBase());
	}
	
	@Override
	public boolean addMemory(DeviceMemory memory, int x, int y) {
		return false;
	}
	
	@Override
	public void updateDevice(Device device) {
	}
}
