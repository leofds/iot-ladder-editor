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
package com.github.leofds.iotladdereditor.ladder.symbol.instruction.system;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;

import com.github.leofds.iotladdereditor.application.Mediator;
import com.github.leofds.iotladdereditor.application.ProjectContainer;
import com.github.leofds.iotladdereditor.device.DeviceMemory;
import com.github.leofds.iotladdereditor.i18n.Strings;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.LadderInstruction;

public abstract class SystemInstruction extends LadderInstruction{

	private static final long serialVersionUID = 1L;

	public SystemInstruction(DeviceMemory memory) {
		super(2, 2, 0, 0, memory);
	}

	@Override
	public void paint(Graphics2D g2d){
		super.paint(g2d);
		g2d.setColor(new Color(0, 0, 255));
		g2d.setStroke(new BasicStroke(1));
		g2d.draw(new Rectangle2D.Float(10, 10, blockWidth*getWidth()-20, blockHeight*getHeight()-20));
		g2d.draw(new Line2D.Double(0, blockHeight/2, 10, blockHeight/2));
		g2d.draw(new Line2D.Double(blockWidth*getWidth()-10, blockHeight/2, blockWidth*getWidth(), blockHeight/2));
	}

	@Override
	public List<JMenuItem> getMenuItens(final ProjectContainer project){
		List<JMenuItem> itens = new ArrayList<JMenuItem>();
		JMenuItem remove = new JMenuItem(Strings.remove());
		itens.add(remove);
		remove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Mediator me = Mediator.getInstance();
				me.setChangedProgram();
				project.getLadderProgram().getRungs().delete(SystemInstruction.this);
				me.updateProjectAndViews();
			}
		});
		return itens;
	}
}
