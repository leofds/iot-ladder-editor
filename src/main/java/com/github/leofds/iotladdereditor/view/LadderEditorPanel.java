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

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DragSourceMotionListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.github.leofds.iotladdereditor.application.Mediator;
import com.github.leofds.iotladdereditor.device.DeviceMemory;
import com.github.leofds.iotladdereditor.ladder.config.Constants;
import com.github.leofds.iotladdereditor.ladder.rung.Rung;
import com.github.leofds.iotladdereditor.ladder.rung.Rungs;
import com.github.leofds.iotladdereditor.ladder.symbol.RungView;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.HorizontalLink;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.LadderInstruction;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.LeftPowerRail;
import com.github.leofds.iotladdereditor.ladder.symbol.instruction.RightPowerRail;
import com.github.leofds.iotladdereditor.view.dnd.CollageTransferHandler;
import com.github.leofds.iotladdereditor.view.dnd.DragAndDropLock;
import com.github.leofds.iotladdereditor.view.dnd.GhostGlassPane;
import com.github.leofds.iotladdereditor.view.dnd.TravelBackToOrigin;

public class LadderEditorPanel extends JPanel implements MouseListener, MouseWheelListener, DropTargetListener, KeyListener, DragGestureListener, DragSourceMotionListener, DragSourceListener{

	private static final long serialVersionUID = 1L;
	private DragSource dragSource;
	private Mediator me = Mediator.getInstance();
	private float scale = 1.0f;
	private Dimension dimension;
	private JScrollPane scroll;

	public static Component createEditorPanel(Dimension dimension){
		return new LadderEditorPanel(dimension).getScrollPane();
	}

	public LadderEditorPanel(Dimension dimension) {
		this.dimension = dimension;
		dragSource = new DragSource();
		dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
		dragSource.addDragSourceMotionListener(this);
		setDropTarget(new DropTarget(this, this));
		setPreferredSize(dimension);
		setBackground(Color.white);
		addMouseListener(this);
		addMouseWheelListener(this);
		setFocusable(true);
		requestFocusInWindow();
		addKeyListener(this);

		scroll = new JScrollPane(this,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scroll.setPreferredSize(dimension);
	}

	public void refresh() {
		repaint();
	}
	
	public JScrollPane getScrollPane(){
		return scroll;
	}

	private void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		//		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		//		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		//		g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		AffineTransform at = new AffineTransform();
		at.scale(scale, scale);
		g2d.transform(at);
		
		if(me.getProject() != null) {
			me.getProject().getLadderProgram().getRungs().draw(g2d);
		}
		g2d.dispose();
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}

	public void scrollScale(){
		dimension = new Dimension((int) ((int) (Constants.blockWidth*(Constants.colsNumber+2))*scale),(int) (me.getProject().getLadderProgram().getRungs().getLines()*Constants.blockHeight*scale));
		setPreferredSize(dimension);
		scroll.setPreferredSize(dimension);
		scroll.setMaximumSize(dimension);
		scroll.setMinimumSize(dimension);
		scroll.getViewport().revalidate();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		int x = (int) (e.getX()/scale);
		int y = (int) (e.getY()/scale);

		requestFocusInWindow();

		if(me.getProject() != null) {
			switch(e.getButton()){
			case 1:
				if(e.getClickCount() == 1){
					LadderInstruction instruction = me.getProject().getLadderProgram().getRungs().getInstruction(x, y);
					if(instruction != null){
						if(instruction instanceof RightPowerRail){
							me.unselectInstruction();
						}else if(instruction instanceof HorizontalLink){
							me.unselectInstruction();
						}else{
							if(instruction.isSelected()){
								me.unselectInstruction();
							}else{
								me.selectInstruction(instruction);
							}
						}
					}else{
						me.unselectInstruction();
					}
				}else if(e.getClickCount() == 2){
					LadderInstruction instruction = me.getProject().getLadderProgram().getRungs().getInstruction(x, y);
					if(instruction != null){
						instruction.viewInstructionProperty();
					}
				}
				break;
			case 3:
				final LadderInstruction instruction = me.getProject().getLadderProgram().getRungs().getInstruction(x, y);
				if(instruction != null){
					JPopupMenu popup = new JPopupMenu();
					List<JMenuItem> menuItems = instruction.getMenuItens(me.getProject());
					if(menuItems != null){
						for(JMenuItem menu:menuItems){
							popup.add(menu);
						}
						popup.show(e.getComponent(), x, y);
						Mediator.getInstance().selectInstruction(instruction);
					}
				}
				break;
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK ){
			float sc = (float) (e.getWheelRotation() * 0.05); 
			if(scale-sc > 0.1f && scale-sc < 10.0f){
				scale -= sc;
				LadderEditorPanel.this.repaint();
				scrollScale();
			}
		}else{
			scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getValue()+e.getWheelRotation());
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

		final GhostGlassPane glassPane = (GhostGlassPane) SwingUtilities.getRootPane(this).getGlassPane();
		Point start = (Point) dsde.getLocation().clone();
		SwingUtilities.convertPointFromScreen(start, glassPane);
		DragAndDropLock.setLocked(false);

		if(!dsde.getDropSuccess()){
			Point end = glassPane.getOrigin();
			TravelBackToOrigin travel = new TravelBackToOrigin(glassPane, start, end);
			travel.addFinishListener(new AbstractAction() {

				@Override
				public void actionPerformed(ActionEvent e) {
					Point pd = glassPane.getPendent();
					me.setChangedProgram();
					me.getProject().getLadderProgram().getRungs().addInstruction((int)(pd.getX()+1), (int)(pd.getY()+1), glassPane.getInstruction());
					me.updateProjectAndViews();
				}
			});

			Timer backTimer = new Timer(1000 / 60, travel);
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

	@Override
	public void dragGestureRecognized(DragGestureEvent dge) {
		int x = (int) (dge.getDragOrigin().getX()/scale);
		int y = (int) (dge.getDragOrigin().getY()/scale);

		LadderInstruction instruction = me.getProject().getLadderProgram().getRungs().getInstruction(x, y);
		if(instruction != null){
			if(!(instruction instanceof RightPowerRail) &&
					!(instruction instanceof LeftPowerRail) &&
					!(instruction instanceof HorizontalLink)){
				if (DragAndDropLock.isLocked()) {
					DragAndDropLock.setDragAndDropStarted(false);
					return;
				}
				DragAndDropLock.setLocked(true);
				DragAndDropLock.setDragAndDropStarted(true);
				GhostGlassPane glassPane = (GhostGlassPane) SwingUtilities.getRootPane(this).getGlassPane();

				Point recover = getRecoverPoint(instruction);
				glassPane.setPendent(recover);
				me.setChangedProgram();
				me.getProject().getLadderProgram().getRungs().remove(instruction);
				me.updateProjectAndViews();
				me.unselectInstruction();
				instruction.setSelected(false);

				try {
					dge.startDrag(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR),new CollageTransferHandler(instruction),this);
					glassPane.setInstruction(instruction);
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
		}
	}

	private Point getRecoverPoint(LadderInstruction instruction){
		Rungs rungs = me.getProject().getLadderProgram().getRungs();
		Rung rung = rungs.getRungOfInstruction(instruction);
		if(rung != null){
			LadderInstruction before = rung.getToNext(instruction); 
			if(before!=null){
				return before.getNext().getPoint(); 
			}
			before = rung.getToDown(instruction); 
			if(before!=null){
				return before.getPoint(); 
			}
		}
		return null;
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
	}

	@Override
	public void dragExit(DropTargetEvent dte) {
	}


	@Override
	public void drop(DropTargetDropEvent dtde) {
		DataFlavor[] flavors = dtde.getCurrentDataFlavors();
		if (flavors == null) {
			return;
		}
		for(DataFlavor dataFlavor: flavors){
			if(dataFlavor.equals(CollageTransferHandler.COLLAGE_INSTRUCTION_FLAVOR)){
				dtde.acceptDrop(DnDConstants.ACTION_COPY);
				Transferable transferable = dtde.getTransferable();
				Point p = dtde.getLocation();
				int x = (int) (p.getX()/scale);
				int y = (int) (p.getY()/scale);

				try {
					// add instruction
					LadderInstruction instruction = (LadderInstruction) transferable.getTransferData(CollageTransferHandler.COLLAGE_INSTRUCTION_FLAVOR);
					if(instruction != null){
						me.setChangedProgram();
						if(me.getProject().getLadderProgram().getRungs().addInstruction(x, y, instruction)){
							me.updateProjectAndViews();

							GhostGlassPane glassPane = (GhostGlassPane) SwingUtilities.getRootPane(this).getGlassPane();

							
							// add pendent instruction
							glassPane.clear();
							glassPane.setVisible(false);
							DragAndDropLock.setLocked(false);
							dtde.dropComplete(true);
							me.setChangedProgram();
						}
						return;
					}

					// add rung
					RungView rung =  (RungView) transferable.getTransferData(CollageTransferHandler.COLLAGE_RUNG_FLAVOR);
					if(rung != null){
						me.setChangedProgram();
						if(me.getProject().getLadderProgram().getRungs().addRung(x, y)){
							me.updateProjectAndViews();

							GhostGlassPane glassPane = (GhostGlassPane) SwingUtilities.getRootPane(this).getGlassPane();
							glassPane.clear();
							glassPane.setVisible(false);
							DragAndDropLock.setLocked(false);
							dtde.dropComplete(true);
							me.setChangedProgram();
						}
					}

					// add peripheral IO
					DeviceMemory memory = (DeviceMemory) transferable.getTransferData(CollageTransferHandler.COLLAGE_MEMORY_FLAVOR);
					memory = me.getProject().getLadderProgram().getOriginMemory(memory.getName());
					if(memory != null){
						instruction = me.getProject().getLadderProgram().getRungs().getInstruction(x, y);
						if(instruction != null){
							if(instruction.addMemory(memory, x, y)){
								GhostGlassPane glassPane = (GhostGlassPane) SwingUtilities.getRootPane(this).getGlassPane();
								glassPane.clear();
								glassPane.setVisible(false);
								DragAndDropLock.setLocked(false);
								dtde.dropComplete(true);

								me.updateProjectAndViews();
								me.setChangedProgram();
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}


	@Override
	public void keyReleased(KeyEvent e) {

		LadderInstruction selectedInstruction = me.getSelectedInstruction();
		if(selectedInstruction != null){

			switch(e.getKeyCode()){
			// Delete
			case 127:	
				me.setChangedProgram();
				me.getProject().getLadderProgram().getRungs().delete(selectedInstruction);
				me.updateProjectAndViews();
				me.setChangedProgram();
				break;
			// Instruction Properties
			case 'p':
			case 'P':
				selectedInstruction.viewInstructionProperty();
				me.setChangedProgram();
				break;
			}
		}
	}
}
