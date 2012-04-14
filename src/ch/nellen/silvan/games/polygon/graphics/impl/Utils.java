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

public class Utils {
	public static float dotProd(float[] v1, float[] v2) {
		assert (v1.length == v2.length);
		float res = 0f;
		for (int i = 0; i < v1.length; ++i) {
			res += v1[i] * v2[i];
		}
		return res;
	}
	
	public static float[] sub(float[] v1, float[] v2) {
		assert (v1.length == v2.length);
		float[] res = new float[v1.length];
		for (int i = 0; i < v1.length; ++i) {
			res[i] = v1[i] - v2[i];
		}
		return res;
	}
	
	public static float[] perpVec2(float[] v) {
		assert (v.length == 2);
		float[] res = new float[v.length];
		res[0] = -v[1];
		res[1] = v[0];
		return res;
	}
	
	public float absSq(float[] v) {
		float res = 0;
		for (int i = 0; i < v.length; ++i) {
			res += v[i] * v[i];
		}
		return res;
	}
}
