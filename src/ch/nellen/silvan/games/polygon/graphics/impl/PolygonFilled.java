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
package ch.nellen.silvan.games.polygon.graphics.impl;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;

import ch.nellen.silvan.games.polygon.graphics.IRenderContext;
import ch.nellen.silvan.games.polygon.graphics.IRenderer;

public class PolygonFilled extends PolygonModel {

	public PolygonFilled(IRenderer r) {
		super(r);
	}

	public void render(IRenderContext rc) {
		FloatBuffer vBuffer = rc.getGlVertexBuffer();
		ShortBuffer iBuffer = rc.getGlIndicesBuffer();
		
		recalculateGeometry(vBuffer, iBuffer);
		
		GL10 gl = rc.getGl();

		// Draw the polygon
		gl.glColor4f(mColor.r, mColor.g, mColor.b, mColor.alpha);
		gl.glRotatef(mAngle, 0.0f, 0.0f, 1.0f);
		gl.glVertexPointer(3, GL10.GL_FLOAT, 0, vBuffer);
		gl.glDrawElements(GL10.GL_TRIANGLES, NUMBER_OF_VERTICES*3, GL10.GL_UNSIGNED_SHORT, iBuffer);
	}

	private void recalculateGeometry(FloatBuffer vertexBuffer, ShortBuffer indicesBuffer) {
		int iBase = 0;
		for (int i = 0; i < NUMBER_OF_VERTICES; ++i) {
			iBase = i * 3;
			vertexBuffer.put(iBase,polygonPrototype[i][0] * mRadius); // Xi.1
			vertexBuffer.put(iBase + 1,polygonPrototype[i][1] * mRadius); // Yi.1
			vertexBuffer.put(iBase + 2,mZCoord); // Zi.1
			
			indicesBuffer.put(iBase, (short) NUMBER_OF_VERTICES); // center point at last position
			indicesBuffer.put(iBase + 1, (short) i);
			indicesBuffer.put(iBase + 2, (short) (((short)(i+1))%NUMBER_OF_VERTICES));
		}
		// Center point
		iBase = NUMBER_OF_VERTICES*3;
		vertexBuffer.put(iBase,0.0f); // Xi.1
		vertexBuffer.put(iBase + 1, 0.0f); // Yi.1
		vertexBuffer.put(iBase + 2, 0.0f); // Zi.1
		
	}

	@Override
	public int vertexBufferRequirement() {
		return (NUMBER_OF_VERTICES+1)*3;/* one vertex per corner, plus central vertex, 3 coordinates each */
	}

	@Override
	public int indicesBufferRequirement() {
		return NUMBER_OF_VERTICES*3;/* 3 vertices per triangle */
	}

}
