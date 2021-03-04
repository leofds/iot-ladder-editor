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
import java.util.regex.Pattern;
import javax.swing.JTextField;

public class FloatKeyListener implements KeyListener{

	private JTextField textField;
	
	public FloatKeyListener(JTextField textField) {
		this.textField = textField;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		String str = new StringBuilder(textField.getText()).insert(textField.getCaretPosition(), e.getKeyChar()).toString();
		if(!Pattern.matches("[-]?([0-9]?){6}[.]?([0-9]?){3}", str)){	//FIXME verificar forma��o float
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
