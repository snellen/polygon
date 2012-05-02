package ch.nellen.silvan.games.polygon.sound;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import ch.nellen.silvan.games.R;

public class SoundManager {

	private class InfiniteLoopMusicPlayer {

		private MediaPlayer mMediaPlayer;

		protected void start() {
			if (mMediaPlayer == null) {
				mMediaPlayer = MediaPlayer.create(mContext,
						R.raw.background_music);
				// no need to call prepare();
				// create() does that for you
				mMediaPlayer.setLooping(true);
			}
			if (!mMediaPlayer.isPlaying())
				mMediaPlayer.start();
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
		}

		public boolean isPaused() {
			return (mMediaPlayer != null && !mMediaPlayer.isPlaying());
		}
	}

	private InfiniteLoopMusicPlayer musicPlayer = new InfiniteLoopMusicPlayer();
	private Context mContext = null;
	private SoundPool mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC,
			0);
	private int explosionIndex = -1;
	private AudioManager mAudioManager;

	public SoundManager(Context ctx) {
		this.mContext = ctx;
		mAudioManager = (AudioManager) ctx
				.getSystemService(Context.AUDIO_SERVICE);
		explosionIndex = mSoundPool.load(mContext, R.raw.explode2, 1);
	}

	public void playExplosionSound() {
		float streamVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume
				/ mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
		mSoundPool.play(explosionIndex, streamVolume, streamVolume, 1, 0, 1f);
	}

	public void pauseBackgroundMusic() {
		musicPlayer.pause();
	}
	
	public boolean isBackgroundMusicPaused() {
		return (musicPlayer.isPaused());
	}

	public void resumeBackgroundMusic() {
		musicPlayer.start();
	}

	public void stop() {
		musicPlayer.terminate();
	}

}
