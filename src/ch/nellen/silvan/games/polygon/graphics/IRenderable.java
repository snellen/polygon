/*  This file is part of Polygon, an action game for Android phones. 
 
    Copyright (C) 2012  Silvan Nellen

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>
*/
package ch.nellen.silvan.games.polygon.graphics;

import android.content.Context;

public interface IRenderable {

	// Initialize, load resources etc.
	// Called right before the first render pass
	public abstract void init(Context context);
	
	// Called when Open 
	public abstract void onTextureCleared();

	public abstract void render(IRenderContext gl);

	public abstract int vertexBufferRequirement();

	public abstract int indicesBufferRequirement();

	public abstract boolean isVisible();

	public abstract void isVisible(boolean flag);

}