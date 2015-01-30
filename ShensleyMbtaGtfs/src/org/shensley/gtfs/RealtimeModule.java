package org.shensley.gtfs;

import java.util.Set;

import org.onebusaway.guice.jsr250.JSR250Module;

import com.google.inject.AbstractModule;
import com.google.inject.Module;

public class RealtimeModule extends AbstractModule {
	public static void addModuleAndDependencies(Set<Module> modules) {
	  modules.add(new RealtimeModule());
	  JSR250Module.addModuleAndDependencies(modules);
	}

	@Override
	protected void configure() {

	}

	/**
	 * Implement hashCode() and equals() such that two instances of the module
	 * will be equal.
	 */
	@Override
	public int hashCode() {
	  return this.getClass().hashCode();
	}

	@Override
	public boolean equals(Object o) {
	  if (this == o)
	    return true;
	  if (o == null)
	     return false;
	  return this.getClass().equals(o.getClass());
	}
}
