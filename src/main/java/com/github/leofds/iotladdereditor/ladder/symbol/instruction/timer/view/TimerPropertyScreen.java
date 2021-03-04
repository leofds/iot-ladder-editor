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
package com.github.leofds.iotladdereditor.ladder.symbol.instruction.timer.view;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.github.leofds.iotladdereditor.application.Mediator;
import com.github.leofds.iotladdereditor.device.DeviceMemory;
import com.github.leofds.iotladdereditor.i18n.Strings;
import com.github.leofds.iotladdereditor.ladder.rung.Rung;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.LadderInstruction;
import com.github.leofds.iotladdereditor.ladder.view.DialogScreen;
import com.github.leofds.iotladdereditor.ladder.view.factory.FieldFactory;

public class TimerPropertyScreen extends DialogScreen{

	private static final long serialVersionUID = 1L;
	private JFormattedTextField ftName;
	private JFormattedTextField ftPreset;
	private JFormattedTextField ftAccum;
	private JFormattedTextField ftTimeBase;
	private DeviceMemory memory;
	
	public TimerPropertyScreen(String title, DeviceMemory memory) {
		super(title);
		this.memory = memory;
		
		JPanel panel = new JPanel(null);
		panel.setPreferredSize(new Dimension(200, 160));
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));

		JLabel lbName = new JLabel("Number:",SwingConstants.RIGHT);
		lbName.setBounds(10, 10, 56, 14);
		panel.add(lbName);
		JLabel lbPreset = new JLabel("Preset:",SwingConstants.RIGHT);
		lbPreset.setBounds(10, 35, 56, 14);
		panel.add(lbPreset);
		JLabel lbAccum = new JLabel("Accum:",SwingConstants.RIGHT);
		lbAccum.setBounds(10, 60, 56, 14);
		panel.add(lbAccum);
		JLabel lbBase = new JLabel("Base:",SwingConstants.RIGHT);
		lbBase.setBounds(10, 85, 56, 14);
		panel.add(lbBase);
		JLabel lbBaseMs = new JLabel("ms",SwingConstants.RIGHT);
		lbBaseMs.setBounds(149, 85, 46, 14);
		panel.add(lbBaseMs);

		ftName = FieldFactory.createLongField(1, 999);
		ftName.setHorizontalAlignment(SwingConstants.RIGHT);
		ftName.setBounds(76, 8, 86, 20);
		panel.add(ftName);

		ftPreset = FieldFactory.createLongField(-999999, 999999);
		ftPreset.setHorizontalAlignment(SwingConstants.RIGHT);
		ftPreset.setBounds(76, 33, 86, 20);
		panel.add(ftPreset);

		ftAccum = FieldFactory.createLongField(-999999, 999999);
		ftAccum.setHorizontalAlignment(SwingConstants.RIGHT);
		ftAccum.setBounds(76, 58, 86, 20);
		panel.add(ftAccum);

		ftTimeBase = FieldFactory.createLongField(1, 999999);
		ftTimeBase.setHorizontalAlignment(SwingConstants.RIGHT);
		ftTimeBase.setBounds(76, 83 , 86, 20);
		panel.add(ftTimeBase);

		JSeparator separator = new JSeparator();
		separator.setBounds(5, 110, 190, 2);
		panel.add(separator);

		JButton btnSaveButton = new JButton(Strings.save());
		btnSaveButton.setBounds(10, 120, 89, 23);
		btnSaveButton.addActionListener(getSaveAction());
		panel.add(btnSaveButton);

		JButton btnCancelButton = new JButton(Strings.cancel());
		btnCancelButton.setBounds(100, 120, 89, 23);
		btnCancelButton.addActionListener(getCancelAction());
		panel.add(btnCancelButton);

		setContentPane(panel);
	}
	
	@Override
	public boolean validateFilds() {
		String name = "T"+ftName.getText();
		Mediator me = Mediator.getInstance();
		for(Rung rung: me.getProject().getLadderProgram().getRungs()){
			for(LadderInstruction instruction: rung){
				DeviceMemory memory = instruction.getMemory();
				if(memory != null){
					if(!memory.equals(this.memory)){
						if(memory.getName().equals(name)){
							JOptionPane.showMessageDialog(null, Strings.instructionAlreadyExists());
							return false;
						}
					}
				}
			}
		}
		if(ftName.getText().isEmpty() || ftAccum.getText().isEmpty() || ftTimeBase.getText().isEmpty() || ftPreset.getText().isEmpty()){
			JOptionPane.showMessageDialog(null, Strings.invalidInstructionAttribute());
			return false;
		}
		return true;
	}

	public String getName() {
		return "T"+ftName.getText();
	}

	public void setName(String name) {
		this.ftName.setText(name.isEmpty() ? "" : name.substring(1));
	}

	public int getPreset() {
		return Integer.parseInt(ftPreset.getText());
	}

	public void setPreset(int preset) {
		this.ftPreset.setText(""+preset);
	}

	public int getAccum() {
		return Integer.parseInt(ftAccum.getText());
	}

	public void setAccum(int accum) {
		this.ftAccum.setText(""+accum);
	}

	public int getTimeBase() {
		return Integer.parseInt(ftTimeBase.getText());
	}

	public void setTimeBase(int base) {
		this.ftTimeBase.setText(""+base);
	}
}
