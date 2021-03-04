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

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

public class ConsolePanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private JScrollPane scroll;
	private JTextArea text;
	private DefaultCaret caret;

	public ConsolePanel() {
		super(new BorderLayout(0,0));

		setBorder(new EmptyBorder(0, 0, 0, 0));

		JPanel textPanel = new JPanel();
		add(textPanel, BorderLayout.CENTER);
		textPanel.setLayout(new BorderLayout(0, 0));

		text = new JTextArea();
		text.setEditable(false);
		caret = (DefaultCaret) text.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		scroll = new JScrollPane(text,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		textPanel.add(scroll, BorderLayout.CENTER);
	}

	private JScrollPane getScroll(){
		return scroll;
	}

	public void append(String msg){
		text.append(msg);
	}

	public void clear(){
		text.setText("");
	}
}
