package greensopinion.finance.services;

import static com.google.common.base.Preconditions.checkNotNull;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

public class WebApplicationRegion extends Region {

	private static final String JS_MEMBER_NAME_APP_SERVICE_LOCATOR = "appServiceLocator";
	private static final String JS_MEMBER_NAME_CONSOLE_BRIDGE = "consoleBridge";

	private final WebView webView = new WebView();
	private final WebEngine webEngine = webView.getEngine();

	public WebApplicationRegion(ServiceLocator serviceLocator) {
		checkNotNull(serviceLocator);
		installConsoleBridge();
		webEngine.load(Constants.webViewLocation());
		installServiceLocator(serviceLocator);
		getChildren().add(webView);
	}

	private void installServiceLocator(ServiceLocator serviceLocator) {
		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {

			@Override
			public void changed(ObservableValue<? extends State> observable, State oldValue, State newValue) {
				if (State.SUCCEEDED.equals(newValue)) {
					JSObject windowObject = getJsWindow();
					windowObject.setMember(JS_MEMBER_NAME_APP_SERVICE_LOCATOR, serviceLocator);
				}
			}
		});
	}

	private void installConsoleBridge() {
		JSObject windowObject = getJsWindow();
		windowObject.setMember(JS_MEMBER_NAME_CONSOLE_BRIDGE, new ConsoleBridge());
		webEngine.executeScript("console.log = function(message) {\nconsoleBridge.log(message);\n};");
	}

	private JSObject getJsWindow() {
		return (JSObject) webEngine.executeScript("window");
	}

	@Override
	protected void layoutChildren() {
		double w = getWidth();
		double h = getHeight();
		layoutInArea(webView, 0, 0, w, h, 0, HPos.CENTER, VPos.CENTER);
	}

	@Override
	protected double computePrefWidth(double height) {
		return Constants.DEFAULT_WIDTH;
	}

	@Override
	protected double computePrefHeight(double width) {
		return Constants.DEFAULT_HEIGHT;
	}
}
