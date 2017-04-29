package groupg.controller;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;

/**
 * Created by AlazarGenene on 4/11/17.
 */
public class MyUndoableEditListner implements UndoableEditListener{
    protected UndoManager undo = new UndoManager();
    public void undoableEditHappened(UndoableEditEvent e){
        undo.addEdit(e.getEdit());
        //undoAction.up
    }

}
