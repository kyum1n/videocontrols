import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

public class SerialComm {

	public interface SerialListener {
		public void onData(int val1, int val2);
	}

	public void connect(String portName, int baudrate, SerialListener listener) throws Exception {

		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
		if(portIdentifier.isCurrentlyOwned()) {
			System.out.println( "Error: Port is currently in use" );
		} else {
			int timeout = 2000;
			CommPort commPort = portIdentifier.open(this.getClass().getName(), timeout);

			if(commPort instanceof SerialPort) {
				SerialPort serialPort = (SerialPort)commPort;
				serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

				InputStream in = serialPort.getInputStream();
				new SerialReader(in, listener).start();
			} else {
				System.out.println( "Error: Only serial ports are handled by this example." );
			}
		}
	}

 	public static void listPorts() {
		Enumeration<?> portEnum = CommPortIdentifier.getPortIdentifiers();
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			System.out.println(currPortId.getName());
		}
 	}

	public static class SerialReader extends Thread {
		private InputStream in;
		private SerialListener mListener;
		public SerialReader(InputStream in, SerialListener listener) {
			this.mListener = listener;
			this.in = in;
		}

		public void run() {
			try {
				while (true) {
					while (in.read() != 0xaa) ;
					if (in.read() != 0xbb) continue;

					int val1 = in.read() << 8 | in.read();
					int val2 = in.read() << 8 | in.read();

					mListener.onData(val1, val2);
				}
			} catch( IOException e ) {
				e.printStackTrace();
			}
		}
	}
}
