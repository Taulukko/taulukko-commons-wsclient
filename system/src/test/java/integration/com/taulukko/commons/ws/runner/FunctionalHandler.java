package integration.com.taulukko.commons.ws.runner;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

public class FunctionalHandler extends AbstractHandler {

	private Handler handler = null;

	public FunctionalHandler(Handler handler) {
		this.handler = handler;
	}

	@Override
	public void handle(String arg0, Request arg1, HttpServletRequest arg2, HttpServletResponse arg3)
			throws IOException, ServletException {
		this.handler.handle(arg0, arg1, arg2, arg3);

	}

}
