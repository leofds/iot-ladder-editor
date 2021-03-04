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
package com.github.leofds.iotladdereditor.ladder.symbol.instruction.operator.view;

import java.awt.Dimension;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.github.leofds.iotladdereditor.device.DeviceMemory;
import com.github.leofds.iotladdereditor.i18n.Strings;
import com.github.leofds.iotladdereditor.ladder.view.DialogScreen;
import com.github.leofds.iotladdereditor.ladder.view.listener.ComboCustomActionListener;
import com.github.leofds.iotladdereditor.ladder.view.listener.FloatKeyListener;

public class OperatorPropertyScreen extends DialogScreen{

	private static final long serialVersionUID = 1L;
	private JTextField textComboA;
	private JTextField textComboB;
	private JComboBox comboMemoryA;
	private JComboBox comboMemoryB;
	private JComboBox comboMemoryD;
	private DefaultComboBoxModel modelA = new DefaultComboBoxModel();
	private DefaultComboBoxModel modelB = new DefaultComboBoxModel();
	private DefaultComboBoxModel modelD = new DefaultComboBoxModel();

	public OperatorPropertyScreen(String title) {
		super(title);

		JPanel panel = new JPanel(null);
		panel.setPreferredSize(new Dimension(240, 160));
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));

		JLabel lbSrcA = new JLabel(Strings.source()+" A:",SwingConstants.RIGHT);
		lbSrcA.setBounds(5, 12, 76, 14);
		panel.add(lbSrcA);

		JLabel lbSrcB = new JLabel(Strings.source()+" B:",SwingConstants.RIGHT);
		lbSrcB.setBounds(5, 35, 76, 14);
		panel.add(lbSrcB);

		JLabel lbDst = new JLabel(Strings.destiny()+":",SwingConstants.RIGHT);
		lbDst.setBounds(5, 58, 76, 14);
		panel.add(lbDst);
		
		comboMemoryA = new JComboBox(modelA);
		comboMemoryA.setBounds(91, 9, 122, 20);
		comboMemoryA.addActionListener(new ComboCustomActionListener(comboMemoryA));
		textComboA = (JTextField)comboMemoryA.getEditor().getEditorComponent();
		comboMemoryA.getEditor().getEditorComponent().addKeyListener( new FloatKeyListener( textComboA ) );
		modelA.addElement(Strings.constantValue());
		comboMemoryA.setSelectedIndex(0);
		panel.add(comboMemoryA);

		comboMemoryB = new JComboBox(modelB);
		comboMemoryB.setBounds(91, 32, 122, 20);
		comboMemoryB.addActionListener(new ComboCustomActionListener(comboMemoryB));
		textComboB = (JTextField)comboMemoryB.getEditor().getEditorComponent();
		comboMemoryB.getEditor().getEditorComponent().addKeyListener( new FloatKeyListener( textComboB ) );
		modelB.addElement(Strings.constantValue());
		comboMemoryB.setSelectedIndex(0);
		panel.add(comboMemoryB);
		
		comboMemoryD = new JComboBox(modelD);
		comboMemoryD.setBounds(91, 55, 122, 20);
		panel.add(comboMemoryD);

		JSeparator separator = new JSeparator();
		separator.setBounds(5, 110, 230, 2);
		panel.add(separator);

		JButton btnSaveButton = new JButton(Strings.save());
		btnSaveButton.setBounds(50, 120, 89, 23);
		btnSaveButton.addActionListener(getSaveAction());
		panel.add(btnSaveButton);

		JButton btnCancelButton = new JButton(Strings.cancel());
		btnCancelButton.setBounds(140, 120, 89, 23);
		btnCancelButton.addActionListener(getCancelAction());
		panel.add(btnCancelButton);

		setContentPane(panel);
	}

	@Override
	public boolean validateFilds() {
		if(comboMemoryA.getSelectedItem() == null){
			return false;
		}
		if(comboMemoryB.getSelectedItem() == null){
			return false;
		}
		if(comboMemoryD.getSelectedItem() == null){
			JOptionPane.showMessageDialog(null, Strings.destinyInvalid());
			return false;
		}
		int index = comboMemoryA.getSelectedIndex();
		if(index==0||index==-1){
			if(textComboA.getText().isEmpty()){
				JOptionPane.showMessageDialog(null, Strings.sourceAInvalidValue());
				return false;
			}
		}
		index = comboMemoryB.getSelectedIndex();
		if(index==0||index==-1){
			if(textComboB.getText().isEmpty()){
				JOptionPane.showMessageDialog(null, Strings.sourceBInvalidValue());
				return false;
			}
		}
		return true;
	}
	
	public void addMemoryA(DeviceMemory memory){
		modelA.addElement(memory);
	}
	
	public DeviceMemory getSelectedA(){
		int index = comboMemoryA.getSelectedIndex();
		if(index==0||index==-1){
			String str = textComboA.getText();
			if(str.contains(".")) {
				return new DeviceMemory(""+Float.parseFloat(textComboA.getText()), Float.class);
			}
			return new DeviceMemory(""+Integer.parseInt(textComboA.getText()), Integer.class);
		}
		return (DeviceMemory) comboMemoryA.getSelectedItem();
	}

	public void addMemoryB(DeviceMemory memory){
		modelB.addElement(memory);
	}

	public DeviceMemory getSelectedB(){
		int index = comboMemoryB.getSelectedIndex();
		if(index==0||index==-1){
			String str = textComboB.getText();
			if(str.contains(".")) {
				return new DeviceMemory(""+Float.parseFloat(textComboB.getText()), Float.class);
			}
			return new DeviceMemory(""+Integer.parseInt(textComboB.getText()), Integer.class);
		}
		return (DeviceMemory) comboMemoryB.getSelectedItem();
	}
	
	public void addMemoryD(DeviceMemory memory){
		modelD.addElement(memory);
	}
	
	public DeviceMemory getSelectedD(){
		return (DeviceMemory) comboMemoryD.getSelectedItem();
	}
	
	public void setSourceA(DeviceMemory memory){
		for(int i=0;i<modelA.getSize();i++){
			if(modelA.getElementAt(i) instanceof DeviceMemory){
				DeviceMemory mem = (DeviceMemory) modelA.getElementAt(i);
				if(mem.equals(memory)){
					comboMemoryA.setSelectedIndex(i);
					return;
				}
			}
		}
		comboMemoryA.setSelectedItem(memory);
	}
	
	public void setSourceB(DeviceMemory memory){
		for(int i=0;i<modelB.getSize();i++){
			if(modelB.getElementAt(i) instanceof DeviceMemory){
				DeviceMemory mem = (DeviceMemory) modelB.getElementAt(i);
				if(mem.equals(memory)){
					comboMemoryB.setSelectedIndex(i);
					return;
				}
			}
		}
		comboMemoryB.setSelectedItem(memory);
	}
	
	public void setDestiny(DeviceMemory memory){
		for(int i=0;i<modelD.getSize();i++){
			if(modelD.getElementAt(i) instanceof DeviceMemory){
				DeviceMemory mem = (DeviceMemory) modelD.getElementAt(i);
				if(mem.equals(memory)){
					comboMemoryD.setSelectedIndex(i);
					return;
				}
			}
		}
	}
}
