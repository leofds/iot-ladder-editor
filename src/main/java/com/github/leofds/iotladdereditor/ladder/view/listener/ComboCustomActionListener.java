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
package com.github.leofds.iotladdereditor.ladder.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

public class ComboCustomActionListener implements ActionListener{

	private JComboBox<?> combo;
	
	public ComboCustomActionListener(JComboBox<?> combo) {
		this.combo = combo;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(combo.getSelectedIndex()){
		case -1:
			combo.setEditable(true);
			break;
		case 0:
			combo.setEditable(true);
			combo.setSelectedItem("");
			break;
		default:
			combo.setEditable(false);
		}
	}

}
