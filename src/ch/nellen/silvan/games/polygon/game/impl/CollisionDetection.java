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
package ch.nellen.silvan.games.polygon.game.impl;

import ch.nellen.silvan.games.polygon.graphics.IPolygonModel;
import ch.nellen.silvan.games.polygon.graphics.impl.PlayerModel;
import ch.nellen.silvan.games.polygon.graphics.impl.PolygonUnfilled;

public class CollisionDetection {

	public boolean isPlayerCollided(Scene scene) {
		PolygonUnfilled[] polygons = scene.getPolygonModels();
		PlayerModel player = scene.getPlayerModel();
		for (PolygonUnfilled p : polygons) {
			if (this.checkCollision(player, p))
				return true;
		}
		return false;
	}

	private boolean checkCollision(PlayerModel player, PolygonUnfilled candidate) {
		if (candidate.isVisible()) {
			float tangAngle = (float) (90 * player.getTangentialSize() / (player
					.getRadius() * Math.PI * Math.PI));
			float[] radii = new float[] {
					player.getRadius() + player.getSize(),
					player.getRadius() - player.getSize() / 2,
					player.getRadius() - player.getSize() / 2 };
			float[] angles = new float[] { player.getAngle(),
					player.getAngle() + tangAngle,
					player.getAngle() - tangAngle };
			int i = 0;
			float sectorAngle = (360 / IPolygonModel.NUMBER_OF_VERTICES);
			for (float r: radii) {
				if (r > candidate.getRadius()
						&& r < candidate.getRadius()
								+ candidate.getWidth()) {

					float relativeAngle = angles[i] - candidate.getAngle();
					if (relativeAngle < 0f) {
						relativeAngle += 360f;
					}
					if (relativeAngle >= 360f) {
						relativeAngle -= 360f;
					}
					int sector = (int) (relativeAngle / sectorAngle);
					if (sector < 0
							|| sector >= IPolygonModel.NUMBER_OF_VERTICES)
						return false; // Should not happen...
					if (candidate.getEdgesEnabled()[sector]) {
						return true;
					}
				}
				i++;
			}

		}
		return false;
	}
}
