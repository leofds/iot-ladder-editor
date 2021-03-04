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
package com.github.leofds.iotladdereditor.view.tree;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DragSourceMotionListener;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.tree.TreeModel;

import com.github.leofds.iotladdereditor.device.PeripheralIO;
import com.github.leofds.iotladdereditor.view.dnd.CollageTransferHandler;
import com.github.leofds.iotladdereditor.view.dnd.DragAndDropLock;
import com.github.leofds.iotladdereditor.view.dnd.GhostGlassPane;
import com.github.leofds.iotladdereditor.view.dnd.TravelBackToOrigin;

public class DeviceTree extends JTree implements DragGestureListener, DragSourceMotionListener, DragSourceListener{

	private static final long serialVersionUID = 1L;
	private DragSource dragSource;
	
	public DeviceTree(TreeModel newModel) {
		super(newModel);
		dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
		dragSource.addDragSourceMotionListener(this);
	}

	@Override
	public void dragGestureRecognized(DragGestureEvent dge) {
		if (DragAndDropLock.isLocked()) {
			DragAndDropLock.setDragAndDropStarted(false);
			return;
		}
		DragAndDropLock.setLocked(true);
		DragAndDropLock.setDragAndDropStarted(true);
		GhostGlassPane glassPane = (GhostGlassPane) SwingUtilities.getRootPane(this).getGlassPane();
		
		try {
			CustomTreeNode customTreeNode = (CustomTreeNode) getSelectionPath().getLastPathComponent();
			Object obj = customTreeNode.getObj();
			
			if(obj != null && obj instanceof PeripheralIO){
				PeripheralIO peripheralItem = (PeripheralIO) obj;
				dge.startDrag(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR),new CollageTransferHandler(peripheralItem),this);
				glassPane.setMemoryItem(peripheralItem);
			}else{
				DragAndDropLock.setLocked(false);
				DragAndDropLock.setDragAndDropStarted(false);
				return;
			}
			glassPane.setVisible(true);
			Point p = (Point) dge.getDragOrigin().clone();
			SwingUtilities.convertPointToScreen(p, this);
			SwingUtilities.convertPointFromScreen(p, glassPane);
			glassPane.setLocation(p);
			glassPane.setOrigin(p);
			glassPane.repaint();
		} catch (Exception e) {
			DragAndDropLock.setLocked(false);
			DragAndDropLock.setDragAndDropStarted(false);
		}
	}
	
	@Override
	public void dragEnter(DragSourceDragEvent dsde) {
	}

	@Override
	public void dragOver(DragSourceDragEvent dsde) {
	}

	@Override
	public void dropActionChanged(DragSourceDragEvent dsde) {
	}

	@Override
	public void dragExit(DragSourceEvent dse) {
	}

	@Override
	public void dragDropEnd(DragSourceDropEvent dsde) {
		if (!DragAndDropLock.isDragAndDropStarted()) {
			return;
		}
		DragAndDropLock.setDragAndDropStarted(false);
		
		GhostGlassPane glassPane = (GhostGlassPane) SwingUtilities.getRootPane(this).getGlassPane();
		Point start = (Point) dsde.getLocation().clone();
		SwingUtilities.convertPointFromScreen(start, glassPane);
		DragAndDropLock.setLocked(false);
		
		if(!dsde.getDropSuccess()){
			Point end = glassPane.getOrigin();
			Timer backTimer = new Timer(1000 / 60, new TravelBackToOrigin(glassPane, start, end));
			backTimer.start();
		}else{
			glassPane.setLocation(start);
			glassPane.repaint();
		}
	}

	@Override
	public void dragMouseMoved(DragSourceDragEvent dsde) {
		if (!DragAndDropLock.isDragAndDropStarted()) {
			return;
		}
		GhostGlassPane glassPane = (GhostGlassPane) SwingUtilities.getRootPane(this).getGlassPane();
		Point p = (Point) dsde.getLocation().clone();
		SwingUtilities.convertPointFromScreen(p, glassPane);
		glassPane.setLocation(p);
		glassPane.repaint();
	}	
}
