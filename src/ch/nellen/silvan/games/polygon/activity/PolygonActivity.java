package ch.nellen.silvan.games.polygon.activity;

import ch.nellen.silvan.games.R;
import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class PolygonActivity extends Activity {
	private GLSurfaceView mGLView;

	private class InfiniteLoopMusicPlayer {

		private MediaPlayer mMediaPlayer;

		protected void start() {
			if (mMediaPlayer == null) {
				mMediaPlayer = MediaPlayer.create(getApplicationContext(),
						R.raw.backgroundmusic1);
				mMediaPlayer.setLooping(true);
			}
			mMediaPlayer.start();// no need to call prepare();
									// create() does that for you
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
		}

		protected void pause() {
			if (mMediaPlayer != null)
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
		mGLView = new PolygonSurfaceView(this);
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