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
package ch.nellen.silvan.games.polygon.game;

public interface IGameState {

	enum Phase {
		START, RUNNING, PAUSED, GAMEOVER
	}

	public class PhaseChange {
		public Phase oldPhase;
		public Phase newPhase;
	}

	public abstract void setPlayerAngularDir(int mAngularDir);

	public abstract int getPlayerAngluarDir();

	public abstract void setTotalTime(long time);

	public abstract long getTotalTime();

	public abstract void setCameraZ(float time);

	public abstract float getCameraZ();

	public abstract Phase getCurrentPhase();

	public abstract void setCurrentPhase(Phase newPhase);

}