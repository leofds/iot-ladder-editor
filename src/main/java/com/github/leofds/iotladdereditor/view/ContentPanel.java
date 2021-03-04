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
import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.border.EmptyBorder;

import com.github.leofds.iotladdereditor.application.Mediator;
import com.github.leofds.iotladdereditor.ladder.config.Constants;
import com.github.leofds.iotladdereditor.view.event.BuildEvent;
import com.github.leofds.iotladdereditor.view.event.Subject;

public class ContentPanel extends JPanel{

	private static final long serialVersionUID = 1L;

	public ContentPanel() {
		super(new BorderLayout());
		setBorder(new EmptyBorder(0,0,0,0));

		Subject subject = Subject.getInstance();
		new BuildEvent(subject);

		LadderEditorPanel ladderEditorPanel = new LadderEditorPanel(new Dimension((int) (Constants.blockWidth*(Constants.colsNumber+2)),100));
		ConsolePanel consolePanel = new ConsolePanel();

		JSplitPane splitPane1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,ladderEditorPanel.getScrollPane(),consolePanel);
		splitPane1.setOneTouchExpandable(true);
		splitPane1.setResizeWeight(0.7);

		InstructionPanel instructionPanel = new InstructionPanel();
		DevicePanel devicePanel = new DevicePanel();
		MemoryPanel memoryPanel = new MemoryPanel();

		Mediator me = Mediator.getInstance();
		me.setMemoryPanel(memoryPanel);
		me.setDevicePanel(devicePanel);
		me.setConsole(consolePanel);
		me.setLadderEditorPanel(ladderEditorPanel);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.addTab(null, new ImageIcon(getClass().getResource("/images/Ladder_16x16.png") ), instructionPanel);
		tabbedPane.addTab(null, new ImageIcon(getClass().getResource("/images/Board_16x16.png") ), devicePanel);
		tabbedPane.addTab(null, new ImageIcon(getClass().getResource("/images/Memory_16x16.png") ), memoryPanel);

		JSplitPane splitPane2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,tabbedPane,splitPane1);
		splitPane2.setOneTouchExpandable(true);
		splitPane2.setDividerLocation(250);
		splitPane2.setPreferredSize(new Dimension(1200, 500));
		splitPane2.setBorder(new EmptyBorder(0,0,0,0));

		//        add(toolBar, BorderLayout.PAGE_START);
		add(splitPane2, BorderLayout.CENTER);
	}

}
