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
package ch.nellen.silvan.games.polygon.activity;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import ch.nellen.silvan.games.R;

public class PolygonActivity extends Activity {
	private PolygonView mGLView;

	private class InfiniteLoopMusicPlayer {

		private MediaPlayer mMediaPlayer;

		protected void start() {
			if (mMediaPlayer == null) {
				mMediaPlayer = MediaPlayer.create(getApplicationContext(),
						R.raw.background_music);
				// no need to call prepare();
				// create() does that for you
				mMediaPlayer.setLooping(true);
			}
			if (!mMediaPlayer.isPlaying())
				mMediaPlayer.start();
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
		}

		protected void pause() {
			if (mMediaPlayer != null && mMediaPlayer.isPlaying())
				mMediaPlayer.pause();
		}

		protected void terminate() {
			if (mMediaPlayer != null) {
				mMediaPlayer.release();
				mMediaPlayer = null;
			}
			setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
		}
	}

	InfiniteLoopMusicPlayer musicPlayer = new InfiniteLoopMusicPlayer();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Fullscreen
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Create a GLSurfaceView instance and set it
		// as the ContentView for this Activity.
		mGLView = new PolygonView(this);
		mGLView.setGameStartedCallback(new Runnable() {
			@Override
			public void run() {
				musicPlayer.start();
			}
		});
		mGLView.setGameoverCallback(new Runnable() {
			@Override
			public void run() {
				musicPlayer.pause();
			}
		});
		setContentView(mGLView);

		musicPlayer.start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		// The following call pauses the rendering thread.
		// If your OpenGL application is memory intensive,
		// you should consider de-allocating objects that
		// consume significant memory here.
		mGLView.onPause();
		musicPlayer.pause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		// The following call resumes a paused rendering thread.
		// If you de-allocated graphic objects for onPause()
		// this is a good place to re-allocate them.
		mGLView.onResume();
		musicPlayer.start();
	}

	@Override
	protected void onStop() {
		musicPlayer.terminate();
		super.onStop();
	}
}