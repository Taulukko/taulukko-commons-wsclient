package com.taulukko.ws.client;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.lang3.StringUtils;

import com.taulukko.commons.util.config.ConfigObserver;

public class Config {

	private Properties properties = null;

	// //////////////////
	// SERVER//
	// //////////////////

	// Servidor em estado debug imprime mais logs e desliga coisas denecesírias
	// Default: false
	private boolean serverDebug = false;

	// Versao do sistema
	// Default: 0.4
	private String serverVersion = "0.4";

	// Data de Criaíao do sistema
	// Default: 01-08-2008
	private String serverCreated = "01-08-2008";

	// Tempo de espera em ms entre um envio de email e outro
	// Default: 60000 (1 min)
	private int emailSleepTime = 60000;

	// Enable Email, if true thread send emails
	// Default: true
	private boolean emailSendEnabled = true;

	// Show erros in browser. If enabled the browser show errors
	// Default: false
	private // Sugestion: Keep true in developer time
	boolean browserShowJSErrors = true;

	// ID do cluster do servidor
	private String clusterId = "01";

	// ////////
	// LOG//
	// ////////

	// forma de saída do log do root
	// Default: [%d{yyyy-MMM-dd hh:mm}]%-5p[%t]- %m%n
	// Sugestão para Debug (mais lento): [%d{yyyy-MMM-dd hh:mm}]%-5p[%t](%F:%L)
	// - %m%n
	private String rootPattern = "[%d{yyyy-MMM-dd hh:mm}]%-5p[%t]- %m%n";

	// Caminho do log do root
	// Default: /home/taulukko/logs/root.log
	private String rootPath = "/home/taulukko/logs/root.log";

	// Nível de log do root
	// Default: warning
	private String rootLevel = "warning";

	// forma de saída do log stdOut
	// Default: [%d{yyyy-MMM-dd hh:mm}]%-5p[%t]- %m%n
	// Sugestão para Debug (mais lento): [%d{yyyy-MMM-dd hh:mm}]%-5p[%t](%F:%L)
	// - %m%n
	private String stdOutPattern = "[%d{yyyy-MMM-dd hh:mm}]%-5p[%t]- %m%n";

	// Caminho do log
	// Default: /home/taulukko/logs/stdout.log
	private String stdOutPath = "/home/taulukko/logs/stdout.log";

	// Nível de log do root
	// Default: warning
	private String stdOutLevel = "warning";

	// forma de saída do log access
	// Default: [%d{yyyy-MMM-dd hh:mm}]%-5p[%t]- %m%n
	// Sugestão para Debug (mais lento): [%d{yyyy-MMM-dd hh:mm}]%-5p[%t](%F:%L)
	// - %m%n
	private String accessPattern = "[%d{yyyy-MMM-dd hh:mm}]%-5p[%t]- %m%n";

	// Caminho do log access
	// Default: /home/taulukko/logs/access.log
	private String accessPath = "/home/taulukko/logs/access.log";

	// Nível de log access
	// Default: warning
	private String accessLevel = "warning";

	// forma de saída do log sql
	// Default: [%d{yyyy-MMM-dd hh:mm}]%-5p[%t]- %m%n
	// Sugestão para Debug (mais lento): [%d{yyyy-MMM-dd hh:mm}]%-5p[%t](%F:%L)
	// - %m%n
	private String sqlPattern = "[%d{yyyy-MMM-dd hh:mm}]%-5p[%t]- %m%n";

	// Caminho do log
	// Default: /home/taulukko/logs/sql.log
	private String sqlPath = "/home/taulukko/logs/sql.log";

	// Nível de log
	// Default: warning
	private String sqlLevel = "warning";

	// ////////
	// LOG//
	// ////////
	// ////////
	// EMAIL DEFAULT//
	// ////////
	private String email = "example@taulukko.com.br";

	private String emailPassword = "examplePassword";
	private String emailSmtp = "smtp.example.com";

	private List<ConfigObserver> reloadObservers = new CopyOnWriteArrayList<>();

	// Máximos de emails na fila antes de aviso via SMS
	// Padrão: 10
	int emailMaxFIFO = 10;

	protected String projectName;

	protected String realPath;

	private static Config config = null;

	private Config() {
	}

	public static void startDefault(String projectName, String realPath) throws Exception {

		if (!realPath.endsWith("/") && !realPath.endsWith("\\")) {
			realPath += "/";
		}

		config = new Config();
		String uri = "file:///" + realPath + "config/" + projectName + ".properties";
		config.startByURI(new URI(uri), projectName);

	}

	public static String sanitizeURI(String uri) {

		uri = uri.replaceFirst(".:\\\\", "");
		uri = StringUtils.replace(uri, "\\", "/");
		return uri;
	}

	public static Config getLastConfig() {
		return config;
	}

	public void stopDefault() {
	}

	public String getURL(String server) {
		return properties.getProperty(server + ".url");
	}

	public String getContextName(String server) {
		return properties.getProperty(server + ".contextName");
	}

	protected void reloadProperties(String uri) throws URISyntaxException, Exception {

		info("Reloading config [" + this.projectName + "]...");

		for (ConfigObserver observer : this.reloadObservers) {
			observer.before();
		}

		startByURI(new URI(uri), this.projectName);

		for (ConfigObserver observer : this.reloadObservers) {
			observer.after();
		}

	}

	private static void info(String value) {
		System.out.println(value);
	}

	public void startByURI(URI uri, String projectname) throws Exception {
		startByURIInternal(uri, true, false, projectname);
	}

	public void startByURIInternal(URI uri, boolean tryAgain, boolean retryUsingJ2EE, String projectname)
			throws Exception {

		File file = new File(uri);

		if (!file.exists()) {

			System.out.println("[INFO] -  " + projectName + " - Fail load config file from " + uri.toString());

			if (tryAgain | retryUsingJ2EE) {
				if (retryUsingJ2EE) {
					startByURIInternal(new URI("file:///" + this.realPath.replace('\\', '/') + "WEB-INF/classes/config/"
							+ this.projectName + ".properties"), false, false, projectName);
				} else {
					startByURIInternal(new URI("file:///" + this.realPath.replace('\\', '/') + "config/"
							+ this.projectName + ".properties"), false, true, projectName);
				}
			} else {
				throw new Exception(uri.toString() + " not found");
			}
		}

		InputStream is = new FileInputStream(file.getAbsolutePath());
		this.properties = new Properties();
		this.properties.load(is);
		is.close();

		String property = getFromProperties("accessLevel", this.properties, this.accessLevel);
		if (property != null) {
			this.accessLevel = String.valueOf(property);
		}

		property = getFromProperties("clusterId", this.properties, this.clusterId);
		if (property != null) {
			this.clusterId = property;
		}

		property = getFromProperties("emailSleepTime", this.properties, String.valueOf(this.emailSleepTime));
		if (property != null) {
			this.emailSleepTime = Integer.valueOf(property);
		}

		property = getFromProperties("accessPath", this.properties, this.accessPath);
		if (property != null) {
			this.accessPath = String.valueOf(property);
		}

		property = getFromProperties("accessPattern", this.properties, this.accessPattern);
		if (property != null) {
			this.accessPattern = String.valueOf(property);
		}

		property = getFromProperties("rootLevel", this.properties, this.rootLevel);
		if (property != null) {
			this.rootLevel = String.valueOf(property);
		}

		property = getFromProperties("rootPath", this.properties, this.rootPath);
		if (property != null) {
			this.rootPath = String.valueOf(property);
		}

		property = getFromProperties("rootPattern", this.properties, this.rootPattern);
		if (property != null) {
			this.rootPattern = String.valueOf(property);
		}

		property = getFromProperties("serverCreated", this.properties, this.serverCreated);
		if (property != null) {
			this.serverCreated = String.valueOf(property);
		}

		property = getFromProperties("serverDebug", this.properties, String.valueOf(this.serverDebug));
		if (property != null) {
			this.serverDebug = Boolean.valueOf(property);
		}

		property = getFromProperties("serverVersion", this.properties, this.serverVersion);
		if (property != null) {
			this.serverVersion = String.valueOf(property);
		}

		property = getFromProperties("stdOutLevel", this.properties, this.stdOutLevel);
		if (property != null) {
			this.stdOutLevel = String.valueOf(property);
		}

		property = getFromProperties("stdOutPattern", this.properties, this.stdOutPattern);
		if (property != null) {
			this.stdOutPattern = String.valueOf(property);
		}

		property = getFromProperties("stdOutPath", this.properties, this.stdOutPath);
		if (property != null) {
			this.stdOutPath = String.valueOf(property);
		}

		property = getFromProperties("sqlLevel", this.properties, this.sqlLevel);
		if (property != null) {
			this.sqlLevel = String.valueOf(property);
		}

		property = getFromProperties("sqlPattern", this.properties, this.sqlPattern);
		if (property != null) {
			this.sqlPattern = String.valueOf(property);
		}

		property = getFromProperties("sqlPath", this.properties, this.sqlPath);
		if (property != null) {
			this.sqlPath = String.valueOf(property);
		}

		property = getFromProperties("emailSendEnabled", this.properties, String.valueOf(this.emailSendEnabled));
		if (property != null) {
			this.emailSendEnabled = Boolean.valueOf(property);
		}

		property = getFromProperties("browserShowJSErrors", this.properties, String.valueOf(this.browserShowJSErrors));
		if (property != null) {
			this.browserShowJSErrors = Boolean.valueOf(property);
		}

		property = getFromProperties("email", this.properties, this.email);
		if (property != null) {
			this.email = property;
		}

		property = getFromProperties("emailPassword", this.properties, this.emailPassword);
		if (property != null) {
			this.emailPassword = property;
		}

		property = getFromProperties("emailSmtp", this.properties, this.emailSmtp);
		if (property != null) {
			this.emailSmtp = property;
		}

		property = getFromProperties("emailMaxFIFO", this.properties, String.valueOf(this.emailMaxFIFO));
		if (property != null) {
			this.emailMaxFIFO = Integer.parseInt(property);
		}

	}

	@SuppressWarnings("unchecked")
	private <V> V getFromProperties(String key, Properties properties, V defaultValue) {
		if (!properties.containsKey(key)) {
			return defaultValue;
		}

		return (V) properties.get(key);
	}

	public void addObserver(String type, ConfigObserver observer) {

		if (observer != null && type.toLowerCase().equals("reload")) {
			this.reloadObservers.add(observer);
		}
	}

	public boolean removeObserver(String type, ConfigObserver observer) {
		if (observer != null && type.toLowerCase().equals("reload")) {
			return this.reloadObservers.remove(observer);
		}
		return false;
	}

	public void clearObserver(String type) {
		if (type.toLowerCase().equals("reload")) {
			this.reloadObservers.clear();
		}
	}

	public boolean isServerDebug() {
		return serverDebug;
	}

	public void setServerDebug(boolean serverDebug) {
		this.serverDebug = serverDebug;
	}

	public String getServerVersion() {
		return serverVersion;
	}

	public void setServerVersion(String serverVersion) {
		this.serverVersion = serverVersion;
	}

	public String getServerCreated() {
		return serverCreated;
	}

	public void setServerCreated(String serverCreated) {
		this.serverCreated = serverCreated;
	}

	public int getEmailSleepTime() {
		return emailSleepTime;
	}

	public void setEmailSleepTime(int emailSleepTime) {
		this.emailSleepTime = emailSleepTime;
	}

	public boolean isEmailSendEnabled() {
		return emailSendEnabled;
	}

	public void setEmailSendEnabled(boolean emailSendEnabled) {
		this.emailSendEnabled = emailSendEnabled;
	}

	public boolean isBrowserShowJSErrors() {
		return browserShowJSErrors;
	}

	public void setBrowserShowJSErrors(boolean browserShowJSErrors) {
		this.browserShowJSErrors = browserShowJSErrors;
	}

	public String getClusterId() {
		return clusterId;
	}

	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}

	public String getRootPattern() {
		return rootPattern;
	}

	public void setRootPattern(String rootPattern) {
		this.rootPattern = rootPattern;
	}

	public String getRootPath() {
		return rootPath;
	}

	public void setRootPath(String rootPath) {
		this.rootPath = rootPath;
	}

	public String getRootLevel() {
		return rootLevel;
	}

	public void setRootLevel(String rootLevel) {
		this.rootLevel = rootLevel;
	}

	public String getStdOutPattern() {
		return stdOutPattern;
	}

	public void setStdOutPattern(String stdOutPattern) {
		this.stdOutPattern = stdOutPattern;
	}

	public String getStdOutPath() {
		return stdOutPath;
	}

	public void setStdOutPath(String stdOutPath) {
		this.stdOutPath = stdOutPath;
	}

	public String getStdOutLevel() {
		return stdOutLevel;
	}

	public void setStdOutLevel(String stdOutLevel) {
		this.stdOutLevel = stdOutLevel;
	}

	public String getAccessPattern() {
		return accessPattern;
	}

	public void setAccessPattern(String accessPattern) {
		this.accessPattern = accessPattern;
	}

	public String getAccessPath() {
		return accessPath;
	}

	public void setAccessPath(String accessPath) {
		this.accessPath = accessPath;
	}

	public String getAccessLevel() {
		return accessLevel;
	}

	public void setAccessLevel(String accessLevel) {
		this.accessLevel = accessLevel;
	}

	public String getSqlPattern() {
		return sqlPattern;
	}

	public void setSqlPattern(String sqlPattern) {
		this.sqlPattern = sqlPattern;
	}

	public String getSqlPath() {
		return sqlPath;
	}

	public void setSqlPath(String sqlPath) {
		this.sqlPath = sqlPath;
	}

	public String getSqlLevel() {
		return sqlLevel;
	}

	public void setSqlLevel(String sqlLevel) {
		this.sqlLevel = sqlLevel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailPassword() {
		return emailPassword;
	}

	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

	public String getEmailSmtp() {
		return emailSmtp;
	}

	public void setEmailSmtp(String emailSmtp) {
		this.emailSmtp = emailSmtp;
	}

	public int getEmailMaxFIFO() {
		return emailMaxFIFO;
	}

	public void setEmailMaxFIFO(int emailMaxFIFO) {
		this.emailMaxFIFO = emailMaxFIFO;
	}
}
