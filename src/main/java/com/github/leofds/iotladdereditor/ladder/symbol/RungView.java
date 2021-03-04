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
package com.github.leofds.iotladdereditor.ladder.symbol;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

public class RungView extends LadderSymbol{

	private static final long serialVersionUID = 1L;

	public RungView() {
		super(2, 1, 0, 0);
	}

	@Override
	public void paint(Graphics2D g2d){
		super.paint(g2d);
		g2d.setColor(new Color(0, 0, 255));
		g2d.setStroke(new BasicStroke(1));
		g2d.draw(new Line2D.Double(0, 0, 0, blockHeight));
		g2d.draw(new Line2D.Double(0, blockHeight/2, blockWidth, blockHeight/2));
		
		g2d.draw(new Line2D.Double(blockWidth*getWidth(), 0, blockWidth*getWidth(), blockHeight));
		g2d.draw(new Line2D.Double(blockWidth*getWidth(), blockHeight/2, 0, blockHeight/2));
	}
}
