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
package com.github.leofds.iotladdereditor.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import com.github.leofds.iotladdereditor.i18n.Strings;
import com.github.leofds.iotladdereditor.view.event.Subject;
import com.github.leofds.iotladdereditor.view.event.Subject.SubMsg;

public class ToolBar extends JToolBar implements Observer{

	private static final long serialVersionUID = 1L;
	
	private Subject subject;
	private JButton btnStop;
	
	public ToolBar(final Subject subject) {
		super("Tools");
		this.subject = subject;
		this.subject.addObserver(this);
		
		btnStop = new JButton(new ImageIcon(ConsolePanel.class.getResource("/images/arrow-green.png")));
		btnStop.setPreferredSize(new Dimension(15,19));
		btnStop.setBorderPainted(false); 
		btnStop.setContentAreaFilled(false); 
		btnStop.setFocusPainted(false); 
		btnStop.setOpaque(false);
		btnStop.setEnabled(true);
		btnStop.setToolTipText(Strings.buildRun());
		btnStop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				subject.notifyChange(SubMsg.BUILD);
			}
		});
		add(btnStop);
	}

	@Override
	public void update(Observable o, Object arg) {

	}
}
