package ch.nellen.silvan.games.polygon.graphics.impl;

import ch.nellen.silvan.games.polygon.graphics.IRenderable;

public abstract class Renderable implements IRenderable {

	private boolean mVisible = true;
	@Override
	public boolean isVisible() {
		return mVisible;
	}

	@Override
	public void isVisible(boolean flag) {
		mVisible = flag; 
	}

}
