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

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import com.github.leofds.iotladdereditor.ladder.config.Constants;

public abstract class LadderSymbol implements Serializable{

	private static final long serialVersionUID = 1L;
	protected Float blockWidth = Constants.blockWidth;
	protected Float blockHeight = Constants.blockHeight;
	
	private int width;
	private int height;
	private int row;
	private int col;
	private boolean sel;
	
	public LadderSymbol(int width,int height,int row,int col) {
		this.width = width;
		this.height = height;
		this.row = row;
		this.col = col;
		this.sel = false;
	}
	
	public void paint(Graphics2D g2d){
		if(sel){
			g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.2f));
			g2d.setColor(new Color(0, 255, 0));
			g2d.fill(new Rectangle2D.Float(0, 0, width * blockWidth, height * blockHeight));
		}
	}
	
	public void draw(Graphics g){
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.translate(getX(), getY());
		paint(g2d);
		g2d.dispose();
	}
	
	public void setPosition(int row,int col){
		this.row = row;
		this.col = col;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getX(){
		return (int) (col * blockWidth);
	}
	
	public int getY(){
		return (int) (row * blockHeight);
	}

	public Point getPoint(){
		return new Point(getX(),getY());
	}
	
	public void setPoint(Point p){
		this.col = (int) (p.getX() / blockWidth);
		this.row = (int) (p.getY() / blockHeight);
	}
	
	public void setSelected(boolean selected){
		this.sel = selected;
	}
	
	public boolean isSelected(){
		return sel;
	}
	
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getCol() {
		return col;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public boolean contains(int x,int y){
		int _x = getX();
		int _y = getY();
		
		if(x >= _x && x <= _x+(blockWidth*width)){
			if(y >= _y && y <= _y+(blockHeight*height)){
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "x="+getX()+", y="+getY();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + col;
		result = prime * result + height;
		result = prime * result + row;
		result = prime * result + width;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LadderSymbol other = (LadderSymbol) obj;
		if (col != other.col)
			return false;
		if (height != other.height)
			return false;
		if (row != other.row)
			return false;
		if (width != other.width)
			return false;
		return true;
	}
}
