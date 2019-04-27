package integration.com.taulukko.commons.ws.runner;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;

@FunctionalInterface
public interface Handler {

	public void handle(String arg0, Request arg1, HttpServletRequest arg2, HttpServletResponse arg3)
			throws IOException, ServletException ;
}
