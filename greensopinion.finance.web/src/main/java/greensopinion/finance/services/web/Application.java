package greensopinion.finance.services.web;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;

import org.glassfish.hk2.api.ServiceLocator;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Binding;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Provides;

import greensopinion.finance.services.application.Main;

public class Application extends javax.ws.rs.core.Application {

	private final Injector injector;

	@Inject
	public Application(ServiceLocator serviceLocator) {
		injector = createInjector();
	}

	private Injector createInjector() {
		return Guice.createInjector(Main.applicationModules());
	}

	@Override
	public Set<Object> getSingletons() {
		ImmutableSet.Builder<Object> builder = ImmutableSet.<Object> builder();
		Map<Key<?>, Binding<?>> bindings = injector.getBindings();
		for (Key<?> key : bindings.keySet()) {
			if (hasAnnotation(key, ImmutableList.of(Path.class, Provides.class, Consumes.class)) && isEligible(key)) {
				builder.add(injector.getInstance(key));
			}
		}
		return builder.build();
	}

	private boolean isEligible(Key<?> key) {
		Class<?> type = key.getTypeLiteral().getRawType();
		return !ImportFilesWebService.class.isAssignableFrom(type);
	}

	private boolean hasAnnotation(Key<?> key, List<Class<? extends Annotation>> annotations) {
		Class<?> type = key.getTypeLiteral().getRawType();
		for (Class<? extends Annotation> annotation : annotations) {
			if (type.getAnnotation(annotation) != null) {
				return true;
			}
		}
		return false;
	}
}
