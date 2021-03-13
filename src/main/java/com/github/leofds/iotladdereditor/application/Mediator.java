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
package com.github.leofds.iotladdereditor.application;

import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JOptionPane;

import com.github.leofds.iotladdereditor.device.Device;
import com.github.leofds.iotladdereditor.i18n.Strings;
import com.github.leofds.iotladdereditor.ladder.LadderProgram;
import com.github.leofds.iotladdereditor.ladder.rung.Rung;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.LadderInstruction;
import com.github.leofds.iotladdereditor.view.ConsolePanel;
import com.github.leofds.iotladdereditor.view.DevicePanel;
import com.github.leofds.iotladdereditor.view.LadderEditorPanel;
import com.github.leofds.iotladdereditor.view.MemoryPanel;
import com.github.leofds.iotladdereditor.view.UI;
import com.github.leofds.iotladdereditor.view.tree.CustomTreeModel;
import com.github.leofds.iotladdereditor.view.tree.TreeFactory;

public class Mediator {
	
	private static Mediator me;
	private ProjectContainer project;
	private LadderInstruction selected;
	private LadderEditorPanel ladderEditorPanel;
	private MemoryPanel memoryPanel;
	private DevicePanel devicePanel;
	private UI ui;
	private ConsolePanel console;
	private OutputStream consoleStream;

	static {
		me = new Mediator();
	}

	public static Mediator getInstance(){
		return me;
	}

	public void setProject(ProjectContainer project) {
		this.project = project;
	}

	public ProjectContainer getProject() {
		return project;
	}

	public void setUi(UI ui) {
		this.ui = ui;
	}
	
	public void setLadderEditorPanel(LadderEditorPanel ladderEditorPanel) {
		this.ladderEditorPanel = ladderEditorPanel;
	}

	public void setMemoryPanel(MemoryPanel memoryPanel){
		this.memoryPanel = memoryPanel;
	}
	
	public void setDevicePanel(DevicePanel devicePanel) {
		this.devicePanel = devicePanel;
	}

	public void setConsole(final ConsolePanel console){
		this.console = console;
		this.consoleStream = new OutputStream() {

			@Override
			public void write(int b) throws IOException {
				console.append(""+(char)b);
			}
		};
	}
	
	public OutputStream getConsoleStream(){
		return consoleStream;
	}

	private void setChangedInTitle(){
		if(project.isChanged()){
			if(ui.getTitle().charAt(0)!='*'){
				ui.setTitle("*"+ui.getTitle());
			}	
		}
	}

	public void setChangedProgram() {
		me.console.clear();
		if(project != null) {
			project.setChanged(true);
			project.setCompiled(false);
			setChangedInTitle();
		}
	}
	
	public void closeProgram() {
		this.project = null;
		ladderEditorPanel.refresh();
	}
	
	public void setNoCompiled() {
		project.setCompiled(false);
	}
	
	public boolean isChangedProgram() {
		return project.isChanged();
	}

	public boolean isCompiled() {
		return project.isCompiled();
	}

	public void updateProjectAndViews(){
		project.getLadderProgram().getRungs().refresh();
		ladderEditorPanel.refresh();
		ladderEditorPanel.scrollScale();
		memoryPanel.getMemoryTree().setModel(new CustomTreeModel( TreeFactory.createMemoryTree(project.getLadderProgram()) ));
		//deviceTreePane.update();
		ui.setTitle(Strings.appName()+" - "+getProject().getName());
		ui.repaint();
		setChangedInTitle();
	}

	public void selectInstruction(LadderInstruction instruction){
		if(selected != null){
			selected.setSelected(false);
		}
		selected = instruction;
		selected.setSelected(true);
		updateProjectAndViews();
	}

	public LadderInstruction getSelectedInstruction(){
		return selected;
	}

	public void unselectInstruction(){
		if(selected != null){
			selected.setSelected(false);
			selected = null;
			updateProjectAndViews();
		}
	}

	public void closeAplication(){
		ui.close();
	}

	public void outputDialogMessage(String msg){
		JOptionPane.showMessageDialog(null, msg);
	}

	public void clearConsole(){
		me.console.clear();
	}

	public void outputConsoleMessage(String msg){
		me.console.append(msg+"\r\n");
	}

	private void recursiveUpdateDevice(Device device, Rung rung,LadderInstruction instruction) {
		if(instruction != null){
			recursiveUpdateDevice(device, rung, instruction.getDown());
			recursiveUpdateDevice(device, rung, instruction.getNext());
			instruction.updateDevice(device);
		}
	}
	
	public void updateDevice(Device device) {
		LadderProgram ladderProgram = project.getLadderProgram(); 
		for(Rung rung: ladderProgram.getRungs()){
			recursiveUpdateDevice(device, rung, rung.getFirst());
		}
		ladderProgram.setDevice(device);
		devicePanel.update();
	}
}
