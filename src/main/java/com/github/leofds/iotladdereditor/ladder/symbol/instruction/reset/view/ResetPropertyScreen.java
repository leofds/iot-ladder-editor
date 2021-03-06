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
package com.github.leofds.iotladdereditor.ladder.symbol.instruction.reset.view;

import java.awt.Dimension;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.github.leofds.iotladdereditor.device.DeviceMemory;
import com.github.leofds.iotladdereditor.i18n.Strings;
import com.github.leofds.iotladdereditor.ladder.view.DialogScreen;

public class ResetPropertyScreen extends DialogScreen{

	private static final long serialVersionUID = 1L;
	private DeviceMemory memory;
	private JComboBox comboMemory;
	private DefaultComboBoxModel<DeviceMemory> model = new DefaultComboBoxModel<DeviceMemory>();
	
	public ResetPropertyScreen(String title,DeviceMemory memory) {
		super(title);
		this.memory = memory;
		
		JPanel panel = new JPanel(null);
		panel.setPreferredSize(new Dimension(230, 100));
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		JLabel lbOutput = new JLabel(Strings.instruction()+":",SwingConstants.RIGHT);
		lbOutput.setBounds(10, 12, 73, 14);
		panel.add(lbOutput);
		
		comboMemory = new JComboBox(model);
		comboMemory.setBounds(93, 9, 122, 20);
		panel.add(comboMemory);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(5, 50, 210, 2);
		panel.add(separator);
		
		JButton btnSaveButton = new JButton(Strings.save());
		btnSaveButton.setBounds(30, 60, 89, 23);
		btnSaveButton.addActionListener(getSaveAction());
		panel.add(btnSaveButton);

		JButton btnCancelButton = new JButton(Strings.cancel());
		btnCancelButton.setBounds(120, 60, 89, 23);
		btnCancelButton.addActionListener(getCancelAction());
		panel.add(btnCancelButton);
		
		setContentPane(panel);
	}
	
	@Override
	public boolean validateFilds() {
		if(comboMemory.getSelectedItem() == null){
			return false;
		}
		return true;
	}
	
	public void addMemory(DeviceMemory memory){
		model.addElement(memory);
	}
	
	public DeviceMemory getSelected(){
		return (DeviceMemory) comboMemory.getSelectedItem();
	}
}
