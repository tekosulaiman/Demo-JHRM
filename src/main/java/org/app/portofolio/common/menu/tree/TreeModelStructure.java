package org.app.portofolio.common.menu.tree;

import org.zkoss.zul.AbstractTreeModel;
import org.zkoss.zul.Button;

public class TreeModelStructure extends AbstractTreeModel<Object> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Button add;
	public TreeModelStructure() {
        super("Unit");
    }
    public boolean isLeaf(Object node) {
        return getLevel((String)node) >= 2; //at most 4 levels
    }
    public Object getChild(Object parent, int index) {
    	
    	
        return parent + "." + index;
    }
    public int getChildCount(Object parent) {
        return isLeaf(parent) ? 0: 1; //each node has 1 children
    }
    public int getIndexOfChild(Object parent, Object child) {
        String data = (String)child;
        int i = data.lastIndexOf('.');
        return Integer.parseInt(data.substring(1));
    }
    private int getLevel(String data) {
        for (int i = -1, level = 0;; ++level)
            if ((i = data.indexOf('.', i + 1)) < 0)
                return level;
    }
}
