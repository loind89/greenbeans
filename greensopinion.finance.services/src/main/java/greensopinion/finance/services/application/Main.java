package greensopinion.finance.services.application;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

import greensopinion.finance.services.encryption.EncryptionModule;
import greensopinion.finance.services.persistence.ConfigurationModule;
import greensopinion.finance.services.web.WebServiceModule;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Title Placeholder");

		Injector injector = createInjector(primaryStage);
		MainScene scene = injector.getInstance(MainScene.class);
		scene.initialize();
		primaryStage.setScene(scene);
		setSizeAndLocation(primaryStage);
		primaryStage.show();
	}

	Injector createInjector(Stage primaryStage) {
		List<Module> modules = new ArrayList<>();
		modules.add(new AbstractModule() {

			@Override
			protected void configure() {
				bind(Window.class).toInstance(primaryStage);
			}
		});
		modules.add(new SceneModule(getParameters()));
		modules.addAll(applicationModules());
		return Guice.createInjector(modules);
	}

	public static List<Module> applicationModules() {
		return ImmutableList.of(new EncryptionModule(), new ConfigurationModule(), new WebServiceModule());
	}

	private void setSizeAndLocation(Stage primaryStage) {
		Screen primaryScreen = Screen.getPrimary();
		Rectangle2D visualBounds = primaryScreen.getVisualBounds();

		primaryStage.setWidth(Constants.DEFAULT_WIDTH);
		primaryStage.setHeight(Math.min(Constants.DEFAULT_HEIGHT, visualBounds.getHeight()));
	}

	public static final void main(String[] args) {
		launch(args);
	}
}
