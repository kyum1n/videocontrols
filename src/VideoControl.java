import java.io.File;

public class VideoControl {

	public static final long PAUSE_TIME = 10*60*1000;
//	public static final long PAUSE_TIME = 2000;

	private	String[] mVideos;
	private int mCurIdx;
	private Process mProcess;
	private boolean mNext;
	private boolean mPrev;
	private boolean mKill;
	private boolean mPaused;
	private long mPausedTimer;

	public VideoControl(File videosPath) {
		File[] files = videosPath.listFiles();
        int num = 0;
        for (File f : files) if (f.isFile()) num++;

		mVideos = new String[num];
        num = 0;
        for (File f : files) if (f.isFile()) mVideos[num++] = f.getAbsolutePath();

		start();
	}

	private void start() {
		mCurIdx = 0;
		play(mVideos[mCurIdx]);
	}

	public void stop() {
		mKill = true;
	}

	public void pause() {
		mPaused = !mPaused;
		try {
			mProcess.getOutputStream().write('p');
			mProcess.getOutputStream().flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void next() {
		mNext = true;
	}

	public void previous() {
		mPrev = true;
	}

	private void play(String name) {
		try {
			mPaused = false;
			ProcessBuilder pb = new ProcessBuilder("/usr/bin/omxplayer",/* "--win", "0 0 768 432",*/ "-o", "local", name);
			mProcess = pb.start();

//			mProcess = Runtime.getRuntime().exec("cmd /c ping 1.1.1.1 -n 1 -w 10000 > nul");
//			mProcess = Runtime.getRuntime().exec("sleep 10");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			//is running, just sleep
			while (mProcess.isAlive() && !mNext && !mPrev && !mKill) {
				try { Thread.sleep(100);} catch (Exception e) {e.printStackTrace();}
				if (mPaused) {
					mPausedTimer += 100;
					if (mPausedTimer >= PAUSE_TIME) {
						mPausedTimer = 0;
						killPlayer();
						play(mVideos[mCurIdx]);
					}
				}
			}

			//finished, if still alive, kill it (with fire!)
			if (mProcess.isAlive()) {
				killPlayer();
			}

			//stop called, we exit now
			if (mKill) return;

			//select next/prev
			if (mPrev) {
				mCurIdx = (mCurIdx -1 + mVideos.length) % mVideos.length;
			} else {
				mCurIdx = (mCurIdx +1) % mVideos.length;
			}

			mPrev = mNext = false;

			play(mVideos[mCurIdx]);
		}
	}

	private void killPlayer() {
		try {
//			Runtime.getRuntime().exec(new String[]{"killall", "omxplayer.bin"});
            mProcess.getOutputStream().write('q');
            mProcess.getOutputStream().flush();
			mProcess.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
