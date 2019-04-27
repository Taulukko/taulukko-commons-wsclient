package integration.com.taulukko.commons.ws.runner;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class TestServer {
	private final static int PORT = 8181;

	private Server server;

	public void start(AbstractHandler handler) throws Exception {
		this.server = new Server(PORT);
		this.server.setHandler(handler);

		this.server.start(); 
	}

	public void stop() throws Exception {
		this.server.stop();
	}
}