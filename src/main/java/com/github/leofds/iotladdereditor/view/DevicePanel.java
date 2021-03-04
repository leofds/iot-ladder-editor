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
import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.github.leofds.iotladdereditor.application.Mediator;
import com.github.leofds.iotladdereditor.device.Device;
import com.github.leofds.iotladdereditor.i18n.Strings;
import com.github.leofds.iotladdereditor.view.tree.CustomTreeModel;
import com.github.leofds.iotladdereditor.view.tree.CustomTreeRender;
import com.github.leofds.iotladdereditor.view.tree.DeviceTree;
import com.github.leofds.iotladdereditor.view.tree.TreeFactory;

public class DevicePanel extends JPanel{
	
	private static final long serialVersionUID = 1L;

	public DevicePanel() {
		super(new BorderLayout());
		create();
	}
	
	private void create() {
		setBackground(new Color(255, 255, 255));
		
		JLabel label = new JLabel(Strings.device());
		label.setHorizontalAlignment(JLabel.LEFT);
		Font font = label.getFont();
		label.setFont(new Font(font.getFontName(), Font.BOLD, 12));
		
		Device device = Mediator.getInstance().getProject().getLadderProgram().getDevice();
		
		DeviceTree tree = new DeviceTree(new CustomTreeModel( TreeFactory.createDeviceTree( device ) ));
		tree.setCellRenderer(new CustomTreeRender());
		JScrollPane treeScroller = new JScrollPane(tree);
		
		add(label,BorderLayout.NORTH);
		add(treeScroller);
	}
	
	public void update() {
		removeAll();
		create();
	}
	
	
}
