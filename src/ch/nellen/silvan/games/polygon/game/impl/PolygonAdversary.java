package ch.nellen.silvan.games.polygon.game.impl;

import ch.nellen.silvan.games.polygon.graphics.impl.PolygonUnfilled;
import ch.nellen.silvan.games.polygon.graphics.IPolygonModel;

public class PolygonAdversary {

	static abstract class Configurator {
		protected void setDefaultRadius(PolygonUnfilled polygon,
				float maxVisibleR, float maxR) {
			polygon.setRadius((float) (maxR + (Math.random() * 0.1 + 0.35)
					* maxVisibleR));
			polygon.setWidth((float) (Math.random() * 0.005 + 0.09)
					* maxVisibleR);
		}

		protected abstract void setEdges(boolean[] edgeEnabled);

		public void configureNextPolygon(PolygonUnfilled polygon,
				float mMaxVisibleRadius, float mMaxRadius) {
			setEdges(polygon.getEdgesEnabled());
			setDefaultRadius(polygon, mMaxVisibleRadius, mMaxRadius);
			currentConfig = getRandomConfig();
		}
		
		void reset(){};
	}

	static Configurator[] configurators = new Configurator[] {
			new Configurator() {
				// One way out
				@Override
				public void setEdges(boolean[] edgeEnabled) {
					for (int j = 0; j < edgeEnabled.length; ++j) {
						edgeEnabled[j] = true;
					}
					edgeEnabled[(int) (Math.random() * edgeEnabled.length)] = false;
				}
			}, new Configurator() {
				// Line
				@Override
				public void setEdges(boolean[] edgeEnabled) {
					for (int j = 0; j < edgeEnabled.length; ++j) {
						edgeEnabled[j] = false;
					}
					int randIndex = (int) (Math.random() * edgeEnabled.length);
					edgeEnabled[randIndex] = true;
					edgeEnabled[(randIndex + edgeEnabled.length / 2)
							% edgeEnabled.length] = true;
				}
			}, new Configurator() {
				// Triangle
				@Override
				public void setEdges(boolean[] edgeEnabled) {
					int i = (int) (Math.random() * edgeEnabled.length);
					for (int j = 0; j < edgeEnabled.length; ++j) {
						edgeEnabled[i] = (j % 2 == 0);
						i = (i + 1) % edgeEnabled.length;
					}
				}
			}, new Configurator() {
				// Two ways out
				@Override
				public void setEdges(boolean[] edgeEnabled) {
					for (int j = 0; j < edgeEnabled.length; ++j) {
						edgeEnabled[j] = true;
					}
					int randIndex = (int) (Math.random() * edgeEnabled.length);
					edgeEnabled[randIndex] = false;
					edgeEnabled[(randIndex + 2) % edgeEnabled.length] = false;
				}
			}, new Configurator() {
				// Spiral
				int phase = 0;
				int index = 0;

				private void skip(PolygonUnfilled polygon, float maxVisibleR,
						float maxR) {
					int i = (int) (currentConfig + Math.random()
							* (configurators.length - 1))
							% configurators.length;
					configurators[i].configureNextPolygon(polygon, maxVisibleR,
							maxR);
				}

				@Override
				public void configureNextPolygon(PolygonUnfilled polygon,
						float maxVisibleR, float maxR) {

					if (phase == 0 && Math.random() < 0.7) {
						skip(polygon, maxVisibleR, maxR);
						return;
					}

					if (phase == 0) {
						index = (int) (Math.random() * (IPolygonModel.NUMBER_OF_VERTICES));
						maxR += (Math.random() * 0.1 + 0.45) * maxVisibleR;
					}

					setEdges(polygon.getEdgesEnabled());
					float relWidth = (phase % 2 == 0) ? 0.09f : 0.2f;
					polygon.setRadius(maxR);
					polygon.setWidth(relWidth * maxVisibleR);

					phase++;
					if (phase == 5) {
						currentConfig = getRandomConfig();
						reset();
					}
				}

				@Override
				public void setEdges(boolean[] edgeEnabled) {
					for (int j = 0; j < edgeEnabled.length; ++j) {
						edgeEnabled[j] = (phase % 2 == 0);
					}
					if (phase % 2 == 0) {
						edgeEnabled[index] = false;
					} else {
						edgeEnabled[index] = true;
					}
					index = (index - 1);
					if (index < 0)
						index += edgeEnabled.length;
				}
				
				void reset() {
					phase = 0;
				}
			} };

	static int currentConfig;

	public PolygonAdversary() {
		super();
		currentConfig = getRandomConfig();
	}

	void configureNextPolygon(PolygonUnfilled polygon, float mMaxVisibleRadius,
			float mMaxRadius) {
		configurators[currentConfig].configureNextPolygon(polygon,
				mMaxVisibleRadius, mMaxRadius);
	}
	
	void reset() {
		for(int i = 0; i < configurators.length;++i)
			configurators[i].reset();
	}

	static private int getRandomConfig() {
		return (int) (Math.random() * configurators.length);
	}

}
