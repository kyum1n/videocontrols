import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

public class Main implements SerialComm.SerialListener, KeyListener {

	public static void main(String... args) {
		System.out.println("starting..");
		new Main();
		System.out.println("end");
	}

	private static int N = 3;
	private static int M = 3;

	private static int T_ON = 400;
	private static int T_OFF = 300;

	private VideoControl mVc;
	private SerialComm mSc;

	private int mV1Counter;
	private int mV2Counter;

	private boolean mV1On;
	private boolean mV2On;

	private boolean mPause1;
	private boolean mPause2;

	public Main() {
		try {
            BG bg = new BG(this);
			mVc = new VideoControl(new File("videos/"));

			mSc = new SerialComm();
			mSc.connect("/dev/ttyAMA0", 9600, this);
			mVc.run();
			System.exit(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onData(int val1, int val2) {
/*
		if (val1 < 100) System.out.print("0");
		if (val1 < 10) System.out.print("0");

		System.out.print(val1+" | ");
		if (val2 < 100) System.out.print("0");
		if (val2 < 10) System.out.print("0");
		System.out.println(val2);
*/
		if (mV1On) {
			if (val1 < T_OFF) mV1Counter++; else mV1Counter = 0;
			if (mV1Counter-1 < M && mV1Counter >= M) {
				mV1On = false;
				mV1Counter = 0;
//				System.out.println("v1 off");
				onChange(true, false);
			}
		} else {
			if (val1 > T_ON) mV1Counter++; else mV1Counter = 0;
			if (mV1Counter-1 < N && mV1Counter >= N) {
				mV1On = true;
				mV1Counter = 0;

//				System.out.println("v1 on");
				onChange(true, true);
			}
		}

		if (mV2On) {
			if (val2 < T_OFF) mV2Counter++; else mV2Counter = 0;
			if (mV2Counter-1 < M && mV2Counter >= M) {
				mV2On = false;
				mV2Counter = 0;
//				System.out.println("V2 off");
				onChange(false, false);
			}
		} else {
			if (val2 > T_ON) mV2Counter++; else mV2Counter = 0;
			if (mV2Counter-1 < N && mV2Counter >= N) {
				mV2On = true;
				mV2Counter = 0;

//				System.out.println("V2 on");
				onChange(false, true);
			}
		}
	}

	private void onChange(boolean sensor1, boolean value) {
		//something turned off
		if (!value) {
			if (mPause1 && sensor1) {
				mPause1 = false;
				return;
			}
			if (mPause2 && !sensor1) {
				mPause2 = false;
				return;
			}

			if (sensor1) mVc.previous();
			else mVc.next();
		} else {
			if (mV1On && mV2On) {
				mVc.pause();
				mPause1 = true;
				mPause2 = true;
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) mVc.stop();
	}

	@Override
	public void keyReleased(KeyEvent e) {

	}
}
