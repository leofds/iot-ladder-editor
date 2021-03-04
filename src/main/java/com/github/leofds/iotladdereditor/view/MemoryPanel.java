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
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.tree.DefaultTreeModel;

import com.github.leofds.iotladdereditor.application.Mediator;
import com.github.leofds.iotladdereditor.i18n.Strings;
import com.github.leofds.iotladdereditor.ladder.LadderProgram;
import com.github.leofds.iotladdereditor.view.tree.CustomTreeModel;
import com.github.leofds.iotladdereditor.view.tree.CustomTreeRender;
import com.github.leofds.iotladdereditor.view.tree.MemoryTree;
import com.github.leofds.iotladdereditor.view.tree.TreeFactory;

public class MemoryPanel extends JPanel{
	
	private static final long serialVersionUID = 1L;
	
	private MemoryTree tree;
	
	public MemoryPanel() {
		super(new BorderLayout());
		setBackground(new Color(255, 255, 255));
		
		JLabel label = new JLabel(Strings.memory());
		label.setHorizontalAlignment(JLabel.LEFT);
		Font font = label.getFont();
		label.setFont(new Font(font.getFontName(), Font.BOLD, 12));
		
		LadderProgram ladderProgram = Mediator.getInstance().getProject().getLadderProgram();
		
		tree = new MemoryTree(new CustomTreeModel( TreeFactory.createMemoryTree( ladderProgram ) ));
		tree.setCellRenderer(new CustomTreeRender());
		JScrollPane treeScroller = new JScrollPane(tree);
		
		add(label,BorderLayout.NORTH);
		add(treeScroller);
	}
	
	public void updateModel(DefaultTreeModel model){
		tree.setModel(model);
	}
	
	private void doDrawing(Graphics g) {
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}
	
	public MemoryTree getMemoryTree() {
		return tree;
	}
}
