package ch.nellen.silvan.games.polygon.graphics;

import ch.nellen.silvan.games.polygon.graphics.impl.PlayerModel;
import ch.nellen.silvan.games.polygon.graphics.impl.PolygonFilled;
import ch.nellen.silvan.games.polygon.graphics.impl.PolygonUnfilled;

public interface IScene {
	public PolygonUnfilled[] getPolygonModels();

	public PlayerModel getPlayerModel();

	public PolygonFilled getCenterPolygon();

	public PolygonFilled getCenterPolygonBorder();

	public float getCenterRadius();

	public void onMaxVisibleRadiusChanged(float mMaxVisibleRadius);
}
