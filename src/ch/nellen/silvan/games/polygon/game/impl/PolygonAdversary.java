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

import ch.nellen.silvan.games.polygon.graphics.impl.PolygonUnfilled;
import ch.nellen.silvan.games.polygon.graphics.IPolygonModel;

public class PolygonAdversary {

	static abstract class Configurator {
		protected void setDefaultRadius(PolygonUnfilled polygon,
				float maxVisibleR, float maxR) {
			polygon.setRadius((float) (maxR + (Math.random() * 0.1 + 0.27)
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

		void reset() {
		};
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
				int dir = 1;

				private void skip(PolygonUnfilled polygon, float maxVisibleR,
						float maxR) {
					int i = (int) (Math.random() * (configurators.length - 2)); // No
																				// spirals
					configurators[i].configureNextPolygon(polygon, maxVisibleR,
							maxR);
				}

				@Override
				public void configureNextPolygon(PolygonUnfilled polygon,
						float maxVisibleR, float maxR) {

					if (!mSpiralsAllowed || (phase == 0 && Math.random() < 0.5)) {
						skip(polygon, maxVisibleR, maxR);
						return;
					}

					if (phase == 0) {
						index = (int) (Math.random() * (IPolygonModel.NUMBER_OF_VERTICES));
						dir = Math.random() > 0.5 ? 1 : -1;
						maxR += (Math.random() * 0.1 + 0.45) * maxVisibleR;
					}

					setEdges(polygon.getEdgesEnabled());
					float relWidth = (phase % 2 == 0) ? 0.09f : 0.25f;
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
					index = (index + dir);
					index = index < 0 ? index += edgeEnabled.length : index
							% edgeEnabled.length;
				}

				void reset() {
					phase = 0;
				}
			}, new Configurator() {

				int phase = 0;
				int index = 0;
				int dir = 1;

				private void skip(PolygonUnfilled polygon, float maxVisibleR,
						float maxR) {
					int i = (int) (Math.random() * (configurators.length - 2)); // No
																				// spirals
					configurators[i].configureNextPolygon(polygon, maxVisibleR,
							maxR);
				}

				@Override
				protected void setEdges(boolean[] edgeEnabled) {
					for (int j = 0; j < edgeEnabled.length; ++j) {
						edgeEnabled[j] = false;
					}
					edgeEnabled[index] = true;
					index = (index + dir);
					index = index < 0 ? index += edgeEnabled.length : index
							% edgeEnabled.length;
				}

				@Override
				public void configureNextPolygon(PolygonUnfilled polygon,
						float maxVisibleR, float maxR) {

					if (!mSpiralsAllowed || (phase == 0 && Math.random() < 0.5)) {
						skip(polygon, maxVisibleR, maxR);
						return;
					}

					float width = 0.2f * maxVisibleR;

					if (phase == 0) {
						index = (int) (Math.random() * (IPolygonModel.NUMBER_OF_VERTICES));
						dir = Math.random() > 0.5 ? 1 : -1;
						maxR += (Math.random() * 0.1 + 0.45) * maxVisibleR;
					} else {
						maxR -= width *0.6;
					}

					setEdges(polygon.getEdgesEnabled());

					polygon.setWidth(width);
					polygon.setRadius(maxR);

					phase++;

					if (phase == 8) {
						currentConfig = getRandomConfig();
						reset();
					}
				}

				@Override
				void reset() {
					phase = 0;
				}

			} };

	static int currentConfig;

	private static boolean mSpiralsAllowed = false;
	

	public boolean isSpiralsAllowed() {
		return mSpiralsAllowed;
	}

	public void setSpiralsAllowed(boolean spiralsAllowed) {
		mSpiralsAllowed = spiralsAllowed;
	}

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
		mSpiralsAllowed  = false;
		for (Configurator c : configurators)
			c.reset();
	}

	static private int getRandomConfig() {
		return (int) ( Math.random()*configurators.length);
	}

}
