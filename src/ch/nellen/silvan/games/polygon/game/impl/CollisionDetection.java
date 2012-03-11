package ch.nellen.silvan.games.polygon.game.impl;

import ch.nellen.silvan.games.polygon.game.ICollisionDetection;
import ch.nellen.silvan.games.polygon.graphics.IPolygonModel;
import ch.nellen.silvan.games.polygon.graphics.IScene;
import ch.nellen.silvan.games.polygon.graphics.impl.PlayerModel;
import ch.nellen.silvan.games.polygon.graphics.impl.PolygonUnfilled;

public class CollisionDetection implements ICollisionDetection {

	@Override
	public boolean isPlayerCollided(IScene scene) {
		PolygonUnfilled[] polygons = scene.getPolygonModels();
		PlayerModel player = scene.getPlayerModel();
		for (int i = 0; i < polygons.length; ++i) {
			if (this.checkCollision(player, polygons[i]))
				return true;
		}
		return false;
	}

	private boolean checkCollision(PlayerModel player, PolygonUnfilled candidate) {
		if (candidate.isVisible() && (player.getRadius() + player.getSize() > candidate.getRadius())) {
			float relativeAngle = player.getAngle() - candidate.getAngle();
			if (relativeAngle < 0f) {
				relativeAngle += 360f;
			}
			if (relativeAngle >= 360f) {
				relativeAngle -= 360f;
			}
			int sector = (int) (relativeAngle / (360 / IPolygonModel.NUMBER_OF_VERTICES));
			if (sector < 0 || sector >= IPolygonModel.NUMBER_OF_VERTICES)
				assert (false);
			if (candidate.getEdgesEnabled()[sector]
					&& player.getRadius() < candidate.getRadius()
							+ candidate.getWidth()) {
				return true;
			}
		}
		return false;
	}

}
