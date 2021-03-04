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

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Timer;

public class TravelBackToOrigin implements ActionListener{
	
    private long start;
	private Point startPoint;
    private Point endPoint;
    private GhostGlassPane glassPane;
	private AbstractAction action; 
    
    private static final double INITIAL_SPEED = 500.0;
    private static final double INITIAL_ACCELERATION = 6000.0;
        
	public TravelBackToOrigin(GhostGlassPane glassPane,Point startPoint, Point endPoint) {
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.glassPane = glassPane;
		this.start = System.currentTimeMillis();
	}

	public void addFinishListener(AbstractAction action){
		this.action = action;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
         long elapsed = System.currentTimeMillis() - start;
         double time = (double) elapsed / 1000.0;

         double a = (endPoint.y - startPoint.y) / (double) (endPoint.x - startPoint.x);
         double b = endPoint.y - a * endPoint.x;
         
         int travelX = (int) (INITIAL_ACCELERATION * time * time / 2.0 + INITIAL_SPEED * time);
         if (startPoint.x > endPoint.x) {
             travelX = -travelX;
         }
         
         int travelY = (int) ((startPoint.x + travelX) * a + b);
         int distanceX = (int) Math.abs(startPoint.x - endPoint.x);

         if (Math.abs(travelX) >= distanceX) {
             ((Timer) e.getSource()).stop();
             if(action != null){
            	 action.actionPerformed(new ActionEvent(this, 0, null) );
             }             
             glassPane.clear();
             glassPane.setVisible(false);
             DragAndDropLock.setLocked(false);
         }else{
	         glassPane.setLocation(new Point(startPoint.x + travelX,travelY));    
	         glassPane.repaint();
         }
	}
}
