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

public class RGBAColor {
	
	public RGBAColor(float r, float g, float b, float alpha) {
		super();
		this.r = r;
		this.g = g;
		this.b = b;
		this.alpha = alpha;
	}
	
	public float r = 0;
	public float g = 0;
	public float b = 0;
	public float alpha = 0;
}
