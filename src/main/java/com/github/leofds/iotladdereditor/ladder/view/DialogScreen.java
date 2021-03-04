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
package com.github.leofds.iotladdereditor.ladder.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

import com.github.leofds.iotladdereditor.application.Mediator;

public class DialogScreen extends JDialog{

	private static final long serialVersionUID = 1L;
	
	private boolean result = false;
	
	public DialogScreen(String title) {
		setTitle(title);
	}
	
	public void setResult(boolean result){
		this.result = result;
	}
	
	public boolean getResult(){
		return result;
	}
	
	public boolean validateFilds(){
		return false;
	}
	
	public ActionListener getSaveAction(){
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(validateFilds()){
					setResult(true);
					Mediator.getInstance().setChangedProgram();
					DialogScreen.this.dispatchEvent(new WindowEvent(DialogScreen.this, WindowEvent.WINDOW_CLOSING));
				}
			}
		};
	}
	
	public ActionListener getCancelAction(){
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DialogScreen.this.dispatchEvent(new WindowEvent(DialogScreen.this, WindowEvent.WINDOW_CLOSING));
			}
		};
	}
}
