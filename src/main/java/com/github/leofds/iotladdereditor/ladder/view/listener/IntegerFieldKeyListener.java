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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFormattedTextField;

public class IntegerFieldKeyListener implements KeyListener{

	@Override
	public void keyTyped(KeyEvent e) {
		JFormattedTextField field = (JFormattedTextField)e.getSource();

		
		int code = (int)e.getKeyChar();
		if(code == 45){
			if(!field.getText().isEmpty()){
				field.setText(""+(Integer.parseInt(field.getText())*-1) );
			}
			e.consume();
		}
		//String value = field.getText().replace(".", "");
		//Long currentValue = Long.parseLong(value.isEmpty() ? "0" : value);
		//if(code < 48 || code > 57 || currentValue <= Long.MAX_VALUE){
		if(code < 48 || code > 57){
			e.consume();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}
}
