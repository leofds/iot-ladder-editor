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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.github.leofds.iotladdereditor.i18n.Strings;
import com.github.leofds.iotladdereditor.ladder.config.Constants;
import com.github.leofds.iotladdereditor.ladder.symbol.LadderSymbol;

public class InstructionViewPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private LadderSymbol symbol; 

	public InstructionViewPanel() {
		super(new BorderLayout());
		TitledBorder viewBorder = BorderFactory.createTitledBorder(Strings.view());
		viewBorder.setTitleJustification(TitledBorder.LEFT);
		setBorder(viewBorder);
		setBackground(Color.white);
		setPreferredSize(new Dimension(Constants.blockWidth.intValue()*2, Constants.blockHeight.intValue()*2+10));
		setMinimumSize(new Dimension(Constants.blockWidth.intValue()*2+10, Constants.blockHeight.intValue()*2+10));
	}

	public void setSymbol(LadderSymbol symbol) {
		this.symbol = symbol;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		if(symbol != null){
			g2d.translate((this.getWidth() - (Constants.blockWidth*symbol.getWidth()))/2, (this.getHeight() - Constants.blockHeight*symbol.getHeight())/2);
			symbol.draw(g2d);
		}
		g2d.dispose();
	}
}
