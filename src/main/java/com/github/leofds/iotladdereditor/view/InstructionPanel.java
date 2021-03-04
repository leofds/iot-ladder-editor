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
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.github.leofds.iotladdereditor.i18n.Strings;
import com.github.leofds.iotladdereditor.ladder.rung.Rung;
import com.github.leofds.iotladdereditor.ladder.symbol.LadderSymbol;
import com.github.leofds.iotladdereditor.ladder.symbol.RungView;
import com.github.leofds.iotladdereditor.view.tree.CustomTreeModel;
import com.github.leofds.iotladdereditor.view.tree.CustomTreeNode;
import com.github.leofds.iotladdereditor.view.tree.CustomTreeRender;
import com.github.leofds.iotladdereditor.view.tree.InstructionTree;
import com.github.leofds.iotladdereditor.view.tree.TreeFactory;

public class InstructionPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private InstructionViewPanel viewPane;

	public InstructionPanel() {
		super(new BorderLayout());
		setBackground(new Color(255, 255, 255));

		viewPane = new InstructionViewPanel();

		JLabel label = new JLabel(Strings.instruction());
		label.setHorizontalAlignment(JLabel.LEFT);
		Font font = label.getFont();
		label.setFont(new Font(font.getFontName(), Font.BOLD, 12));

		final InstructionTree tree = new InstructionTree(new CustomTreeModel( TreeFactory.createInstructionTree() ));
		tree.setCellRenderer(new CustomTreeRender());
		tree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				CustomTreeNode node = (CustomTreeNode) tree.getLastSelectedPathComponent();
				try {
					if(node.getClazz() != null){
						if(node.getClazz().equals(Rung.class)){
							viewPane.setSymbol(new RungView());
						}else{
							viewPane.setSymbol((LadderSymbol) node.getClazz().newInstance());
						}
						viewPane.repaint();
					}
				} catch (Exception e1) {
				}
			}
		});
		JScrollPane treeScroller = new JScrollPane(tree);

		add(label,BorderLayout.NORTH);
		add(treeScroller);
		add(viewPane,BorderLayout.SOUTH);
	}

}
