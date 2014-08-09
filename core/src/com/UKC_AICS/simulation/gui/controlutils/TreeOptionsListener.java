package com.UKC_AICS.simulation.gui.controlutils;

public interface TreeOptionsListener {

	public void onAdd(byte type, byte subtype, Object object);
	public void onRemove(byte type, byte subtype, Object object);
	public void onCheck(byte type, byte subtype, Object object, boolean isChecked, ControlState.State stateChanged);
}
