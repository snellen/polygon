package ch.nellen.silvan.games.polygon.game.impl;

import ch.nellen.silvan.games.polygon.graphics.IPolygonModel;
import ch.nellen.silvan.games.polygon.graphics.impl.PlayerModel;
import ch.nellen.silvan.games.polygon.graphics.impl.PolygonUnfilled;

public class CollisionDetection {

	public boolean isPlayerCollided(Scene scene) {
		PolygonUnfilled[] polygons = scene.getPolygonModels();
		PlayerModel player = scene.getPlayerModel();
		for (int i = 0; i < polygons.length; ++i) {
			if (this.checkCollision(player, polygons[i]))
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
			for (int i = 0; i < radii.length; ++i) {
				if (radii[i] > candidate.getRadius()
						&& radii[i] < candidate.getRadius()
								+ candidate.getWidth()) {

					float relativeAngle = angles[i] - candidate.getAngle();
					if (relativeAngle < 0f) {
						relativeAngle += 360f;
					}
					if (relativeAngle >= 360f) {
						relativeAngle -= 360f;
					}
					int sector = (int) (relativeAngle / (360 / IPolygonModel.NUMBER_OF_VERTICES));
					if (sector < 0
							|| sector >= IPolygonModel.NUMBER_OF_VERTICES)
						return false; // Should not happen...
					if (candidate.getEdgesEnabled()[sector]) {
						return true;
					}
				}
			}

		}
		return false;
	}
}
