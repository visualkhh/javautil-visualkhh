package nioByPassTest;

import khh.communication.tcp.nio.bypass.NioByPass;
import khh.debug.LogK;

public class NioByPassTest {
	NioByPass bypass;
	public NioByPassTest() throws Exception {
		bypass = new NioByPass(9999, "ws://echo.websocket.org", 80);
		bypass.start();
		//start();
	}
	private void start() {
		
	}
	public static void main(String[] args) throws Exception {
		new NioByPassTest();
		System.out.println("a");
		 LogK.getInstance().debug("\r\n","\r\n".getBytes());

	}

}
