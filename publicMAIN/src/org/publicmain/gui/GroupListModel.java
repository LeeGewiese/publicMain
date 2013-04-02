package org.publicmain.gui;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.AbstractListModel;

import org.publicmain.chatengine.ChatEngine;
import org.publicmain.common.LogEngine;
import org.publicmain.common.Node;

public class GroupListModel extends AbstractListModel<String>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4321214633320536027L;
	private ArrayList<String> groups = new ArrayList<String>();
	private Thread groupListWriter;
	
	public GroupListModel() {
		this.groupListWriter = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					groups.clear();
					for(String grpName : ChatEngine.getCE().getGroupList()){
						groups.add(grpName);
					}
					fireContentsChanged(this, 0, groups.size());
					synchronized (ChatEngine.getCE().getGroupList()) {
						try {
							ChatEngine.getCE().getGroupList().wait();
						} catch (InterruptedException e) {
							LogEngine.log(e);
						}
					}
				}
			}
		});
    	groupListWriter.start();
    	
	    Collections.sort(groups);
    }
    @Override
    public int getSize() {
    	return groups.size();
    }
    @Override
    public String getElementAt(int index) {
    return groups.get(index);
    }
}