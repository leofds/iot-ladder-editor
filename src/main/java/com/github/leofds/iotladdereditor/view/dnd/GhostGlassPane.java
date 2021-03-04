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
package com.github.leofds.iotladdereditor.view.dnd;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

import com.github.leofds.iotladdereditor.device.DeviceMemory;
import com.github.leofds.iotladdereditor.ladder.config.Constants;
import com.github.leofds.iotladdereditor.ladder.symbol.RungView;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.LadderInstruction;

public class GhostGlassPane extends JPanel{

	private static final long serialVersionUID = 1L;
	private Point location = new Point(0, 0);
	private Point origin = new Point(0, 0);
	private Point pendent = new Point(0, 0);
	private LadderInstruction instruction;
	private RungView rung;
	private DeviceMemory memory;
	
	public GhostGlassPane() {
		setOpaque(false);
		setFocusable(false);
	}

	public void setLocation(Point location) {
		this.location = location;
	}
	
	public Point getOrigin() {
		return origin;
	}

	public void setOrigin(Point origin) {
		this.origin = origin;
	}

	public Point getLocation() {
		return location;
	}

	public Point getPendent() {
		return pendent;
	}

	public void setPendent(Point pendent) {
		this.pendent = pendent;
	}

	public void setInstruction(LadderInstruction instruction){
		this.instruction = instruction;
	}
	
	public LadderInstruction getInstruction() {
		return instruction;
	} 
	
	public void setRung(RungView rung){
		this.rung = rung;
	}
	
	public void setMemoryItem(DeviceMemory memory) {
		this.memory = memory;
	}

	/**
	 * Limpa o objeto arrastado
	 */
	public void clear(){
		instruction = null;
		rung = null;
		memory = null;
		origin = null;
		pendent = null;
	}
	
	/**
	 * Desenha sobre a tela o objeto arrastado
	 * @param g2d Contexto gr�fico
	 */
	private void paint(Graphics2D g2d){
		if(instruction != null){
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.9f));
			g2d.setColor(new Color(255, 255, 255));
			g2d.fill(new Rectangle2D.Float(0, 0, Constants.blockWidth*instruction.getWidth(), Constants.blockHeight*instruction.getHeight()));
			instruction.draw(g2d);
		}else if(rung != null){
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.9f));
			g2d.setColor(new Color(255, 255, 255));
			g2d.fill(new Rectangle2D.Float(0, 0, Constants.blockWidth*rung.getWidth(), Constants.blockHeight*rung.getHeight()));
			rung.draw(g2d);
		}else if(memory != null){
			int len = g2d.getFontMetrics().stringWidth(memory.getName());
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.9f));
			g2d.setColor(new Color(255, 255, 0));
			g2d.fill(new Rectangle2D.Float(0, 20, len + 20, 30));
			g2d.setColor(new Color(0, 0, 0));
			g2d.setFont(new Font("Arial", Font.PLAIN, 12));
			g2d.drawString(memory.getName(), 7, 40);
		}
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.translate(location.getX(), location.getY());
		paint(g2d);
		g2d.dispose();
	}
}

