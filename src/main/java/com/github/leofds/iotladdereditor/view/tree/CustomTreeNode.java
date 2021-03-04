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

import java.util.Enumeration;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;

public class CustomTreeNode extends DefaultMutableTreeNode{

	private static final long serialVersionUID = 1L;
	private String title;
	private Class<?> clazz;
	private Object obj;
	private ImageIcon icon;

	private Vector<TreeNode> children = new Vector<TreeNode>();
	private TreeNode parent;

	public CustomTreeNode(String title,ImageIcon icon) {
		super();
		this.title = title;
		this.icon = icon;
	}
	
	public CustomTreeNode(String title,Class<?> clazz,ImageIcon icon) {
		this.title = title;
		this.clazz = clazz;
		this.icon = icon;
	}
	
	public CustomTreeNode(String title,Object obj,ImageIcon icon) {
		this.title = title;
		this.obj = obj;
		this.clazz = obj.getClass();
		this.icon = icon;
	}
	
	public ImageIcon getIcon() {
		return icon;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public Object getObj() {
		return obj;
	}

	public String getTitle() {
		return title;
	}

	public void setParent(TreeNode parent) {
		this.parent = parent;
	}

	public void addChild(TreeNode chield){
		children.add(chield);
	}

	@Override
	public TreeNode getChildAt(int childIndex) {
		return children.elementAt(childIndex);
	}

	@Override
	public int getChildCount() {
		return children.size();
	}

	@Override
	public TreeNode getParent() {
		return parent;
	}

	@Override
	public int getIndex(TreeNode node) {
		return children.indexOf(node);
	}

	@Override
	public boolean getAllowsChildren() {
		return true;
	}

	@Override
	public boolean isLeaf() {
		return (children.size() == 0);
	}

	@Override
	public Enumeration<TreeNode> children() {
		return children.elements();
	}

	@Override
	public String toString() {
		return title;
	}
}
