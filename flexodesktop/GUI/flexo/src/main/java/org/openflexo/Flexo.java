/*
 * (c) Copyright 2010-2011 AgileBirds
 *
 * This file is part of OpenFlexo.
 *
 * OpenFlexo is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenFlexo is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenFlexo. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.openflexo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.openflexo.application.FlexoApplication;
import org.openflexo.components.AskParametersDialog;
import org.openflexo.components.SplashWindow;
import org.openflexo.components.WelcomeDialog;
import org.openflexo.foundation.param.TextFieldParameter;
import org.openflexo.foundation.utils.ProjectLoadingCancelledException;
import org.openflexo.localization.FlexoLocalization;
import org.openflexo.logging.FlexoLoggingFormatter;
import org.openflexo.logging.FlexoLoggingManager;
import org.openflexo.module.FlexoResourceCenterService;
import org.openflexo.module.ModuleLoader;
import org.openflexo.module.ProjectLoader;
import org.openflexo.module.UserType;
import org.openflexo.prefs.FlexoPreferences;
import org.openflexo.properties.FlexoProperties;
import org.openflexo.ssl.DenaliSecurityProvider;
import org.openflexo.toolbox.ResourceLocator;
import org.openflexo.toolbox.ToolBox;
import org.openflexo.utils.CancelException;
import org.openflexo.utils.TooManyFailedAttemptException;
import org.openflexo.view.FlexoFrame;

/**
 * Main class of the Flexo Application Suite
 * 
 * 
 * @author sguerin
 */
public class Flexo {
	private static final Logger logger = Logger.getLogger(Flexo.class.getPackage().getName());

	public static boolean isDev = false;

	private static File outLogFile;

	private static File errLogFile;

	private static String getResourcePath() {
		if (ToolBox.getPLATFORM() == ToolBox.MACOS) {

			try {
				Class fileManager = Class.forName("com.apple.eio.FileManager");
				if (fileManager == null) {
					return null;
				}
				Method m = fileManager.getDeclaredMethod("getResource", new Class[] { String.class, String.class });
				String s = (String) m.invoke(null, "English.dict", "Localized");
				s = s.substring(System.getProperty("user.dir").length() + 1);
				s = s.substring(0, s.length() - "Localized/English.dict".length());
				ResourceLocator.resetFlexoResourceLocation(new File(s));
				return s;
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// e.printStackTrace();
			}

		} else {
			ResourceLocator.resetFlexoResourceLocation(new File(System.getProperty("user.dir")));
			return System.getProperty("user.dir");
		}
		return null;
	}

	/**
	 * Launch method to start Flexo in multimodule mode. Program args are in dev: -userType MAINTAINER Dev -nosplash otherwise see windows
	 * launchers or build.xml for package args VM args: -Xmx512M (for big projects push it to 1024) For MacOS also add: -Xdock:name=Flexo
	 * -Dapple.laf.useScreenMenuBar=true
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		String userTypeName = null;
		boolean noSplash = false;
		if (args.length > 0) {
			// ATTENTION: Argument cannot start with "-D", nor start with "-X", nor start with "-agentlib" since they are reserved keywords
			// for JVM
			// See also WinFlexoLauncher classes.
			for (int i = 0; i < args.length; i++) {
				if (args[i].equals("-userType")) {
					userTypeName = args[i + 1];
				}
				if (args[i].equals("-nosplash")) {
					noSplash = true;
				} else if (args[i].equalsIgnoreCase("DEV")) {
					isDev = true;
				}
			}
		}
		ToolBox.setPlatform();
		if (ToolBox.getPLATFORM() != ToolBox.MACOS || !isDev) {
			getResourcePath();
		}

		remapStandardOuputs(isDev);
		ResourceLocator.printDirectoriesSearchOrder(System.err);
		try {
			DenaliSecurityProvider.insertSecurityProvider();
		} catch (Exception e) {
			if (logger.isLoggable(Level.WARNING)) {
				logger.log(Level.WARNING, "Could not insert security provider", e);
			}
		}
		UserType userTypeNamed = UserType.getUserTypeNamed(userTypeName);
		UserType.setCurrentUserType(userTypeNamed);
		FlexoProperties.load();
		initializeLoggingManager();
		FlexoApplication.initialize();
		initUILAF();
		if (ToolBox.getFrame(null) != null) {
			ToolBox.getFrame(null).setIconImage(userTypeNamed.getIconImage().getImage());
		}
		SplashWindow splashWindow = null;
		if (!noSplash) {
			splashWindow = new SplashWindow(FlexoFrame.getActiveFrame(), userTypeNamed, 10000);
		}
		if (isDev) {
			FlexoLoggingFormatter.logDate = false;
		}
		initProxyManagement();
		if (logger.isLoggable(Level.INFO)) {
			logger.info("Starting on " + ToolBox.getPLATFORM() + "... JVM version is " + System.getProperty("java.version"));
		}
		if (logger.isLoggable(Level.INFO)) {
			logger.info("Working directory is " + new File(".").getAbsolutePath());
		}
		if (logger.isLoggable(Level.INFO)) {
			logger.info("Heap memory is about: " + ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax() / (1024 * 1024) + "Mb");
		}
		getModuleLoader().setAllowsDocSubmission(FlexoProperties.instance().getAllowsDocSubmission());
		if (logger.isLoggable(Level.INFO)) {
			logger.info("Launching FLEXO Application Suite version " + FlexoCst.BUSINESS_APPLICATION_VERSION_NAME + "...");
		}
		getFlexoResourceCenterService().getFlexoResourceCenter();
		final SplashWindow splashWindow2 = splashWindow;
		SwingUtilities.invokeLater(new Runnable() {
			/**
			 * Overrides run
			 * 
			 * @see java.lang.Runnable#run()
			 */
			@Override
			public void run() {
				splashWindow2.setVisible(false);
				splashWindow2.dispose();
				if (getModuleLoader().fileNameToOpen == null) {
					new WelcomeDialog();
				} else {
					try {
						getProjectLoader().loadProject(new File(getModuleLoader().fileNameToOpen));
					} catch (ProjectLoadingCancelledException e) {
						// project need a conversion, but user cancelled the conversion.
						new WelcomeDialog();
					}
				}
			}
		});
	}

	private static FlexoResourceCenterService getFlexoResourceCenterService() {
		return FlexoResourceCenterService.instance();
	}

	private static ModuleLoader getModuleLoader() {
		return ModuleLoader.instance();
	}

	private static ProjectLoader getProjectLoader() {
		return ProjectLoader.instance();
	}

	private static void initUILAF() {
		try {
			UIManager.setLookAndFeel(AdvancedPrefs.getLookAndFeelString());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}

	private static void initProxyManagement() {
		AdvancedPrefs.applyProxySettings();
		final ProxySelector defaultSelector = ProxySelector.getDefault();
		ProxySelector.setDefault(new ProxySelector() {
			@Override
			public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
				System.err.println("Failed " + uri + " sa " + sa);
				defaultSelector.connectFailed(uri, sa, ioe);
			}

			@Override
			public List<Proxy> select(URI uri) {
				ArrayList<Proxy> proxies = new ArrayList<Proxy>(defaultSelector.select(uri));
				if (!proxies.contains(Proxy.NO_PROXY)) {
					proxies.add(Proxy.NO_PROXY);
				}
				return proxies;
			}
		});
		Authenticator.setDefault(new Authenticator() {
			private URL previous;
			int count = 0;

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				try {
					if (previous == getRequestingURL()) {
						count++;
					}
					if (previous != getRequestingURL() && AdvancedPrefs.getProxyLogin() != null && AdvancedPrefs.getProxyPassword() != null) {
						count = 0;
					} else {
						if (count < 3) {
							TextFieldParameter[] params = new TextFieldParameter[2];
							params[0] = new TextFieldParameter("login", "login", AdvancedPrefs.getProxyLogin());
							params[1] = new TextFieldParameter("password", "password", AdvancedPrefs.getProxyPassword());
							params[1].setIsPassword(true);
							AskParametersDialog dialog = AskParametersDialog.createAskParametersDialog(null,
									FlexoLocalization.localizedForKey("enter_proxy_login_password"),
									FlexoLocalization.localizedForKey("enter_proxy_login_password"), params);
							if (dialog.getStatus() == AskParametersDialog.VALIDATE) {
								AdvancedPrefs.setProxyLogin(params[0].getValue());
								AdvancedPrefs.setProxyPassword(params[1].getValue());
								AdvancedPrefs.save();
							} else {
								throw new CancelException();
							}
						} else {
							if (logger.isLoggable(Level.WARNING)) {
								logger.warning("Too many attempts (3) failed. Throwing exception to prevent locking.");
							}
							throw new TooManyFailedAttemptException();
						}
					}
					return new PasswordAuthentication(AdvancedPrefs.getProxyLogin(), AdvancedPrefs.getProxyPassword().toCharArray());
				} finally {
					previous = getRequestingURL();
				}
			}
		});
	}

	/**
	 *
	 */
	private static void remapStandardOuputs(boolean outputToConsole) {
		try {
			// First let's see if we will be able to write into the log directory
			File outputDir = FlexoPreferences.getLogDirectory();
			if (!outputDir.exists()) {
				outputDir.mkdirs();
			}
			if (outputDir.isFile()) {
				if (logger.isLoggable(Level.SEVERE)) {
					logger.severe("Can not write to " + outputDir.getAbsolutePath() + " because it is already a file.");
				}
			}
			if (!outputDir.canWrite()) {
				if (logger.isLoggable(Level.SEVERE)) {
					logger.severe("Can not write to " + outputDir.getAbsolutePath() + " because access is denied by the file system");
				}
			}
			String outString = outputDir.getAbsolutePath() + "/Flexo.out";
			String errString = outputDir.getAbsolutePath() + "/Flexo.err";

			outLogFile = getOutputFile(outString);
			if (outLogFile != null) {
				System.setOut(new PrintStream(new DoublePrintStream(new PrintStream(outLogFile), System.out)));
			}

			errLogFile = getOutputFile(errString);
			if (errLogFile != null) {
				System.setErr(new PrintStream(new DoublePrintStream(new PrintStream(errLogFile), System.err)));
			}
		} catch (Exception e) {
			if (logger.isLoggable(Level.SEVERE)) {
				logger.log(Level.SEVERE, e.getMessage(), e);
			}
		}
	}

	public static File getErrLogFile() {
		return errLogFile;
	}

	public static File getOutLogFile() {
		return outLogFile;
	}

	private static class DoublePrintStream extends OutputStream {

		private final PrintStream ps1;
		private final PrintStream ps2;

		public DoublePrintStream(PrintStream ps1, PrintStream ps2) {
			this.ps1 = ps1;
			this.ps2 = ps2;
		}

		@Override
		public void write(int b) throws IOException {
			ps1.print((char) b);
			ps2.print((char) b);
		}

	}

	private static File getOutputFile(String outString) throws IOException {
		int attempt = 0;
		File out = null;
		while (out == null && attempt < 100) {
			// Get a file channel for the file
			File file = new File(outString + (attempt == 0 ? "" : "." + attempt) + ".log");
			File lock = new File(outString + (attempt == 0 ? "" : "." + attempt) + ".log.lck");
			if (!file.exists()) {
				try {
					boolean done = file.createNewFile();
					if (done) {
						out = file;
					}
				} catch (RuntimeException e1) {
					e1.printStackTrace();
				}
			}
			if (!file.canWrite()) {
				out = null;
				attempt++;
				continue;
			}
			if (lock.exists()) {
				lock.delete();
			}
			if (lock.exists()) {
				out = null;
				attempt++;
				continue;
			} else {
				try {
					lock.createNewFile();
					boolean lockAcquired = false;
					FileOutputStream fos = new FileOutputStream(lock);
					try {
						FileLock fileLock = fos.getChannel().lock();
						lockAcquired = true;
					} catch (IOException e) {
					} finally {
						if (!lockAcquired) {
							fos.close();
						}
					}
					lock.deleteOnExit();
				} catch (RuntimeException e) {
					e.printStackTrace();
				}
			}
			out = file;
			attempt++;
		}
		return out;
	}

	public static void initializeLoggingManager() {
		try {
			FlexoLoggingManager.initialize();
		} catch (SecurityException e) {
			logger.severe("cannot read logging configuration file : " + System.getProperty("java.util.logging.config.file")
					+ "\nIt seems the file has read access protection.");
			e.printStackTrace();
		} catch (IOException e) {
			logger.severe("cannot read logging configuration file : " + System.getProperty("java.util.logging.config.file"));
			e.printStackTrace();
		}
	}

}
