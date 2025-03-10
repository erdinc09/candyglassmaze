package com.parabolagames.glassmaze.framework;

import java.lang.reflect.Method;
import java.util.HashMap;

@Deprecated
public class Dagger2Helper {

  private static HashMap<Class<?>, HashMap<Class<?>, Method>> methodsCache = new HashMap<>();

  private Dagger2Helper() {
    throw new AssertionError();
  }

  /**
   * This method is based on
   * https://github.com/square/mortar/blob/master/dagger2support/src/main/java/mortar/dagger2support/Dagger2.java
   * file that has been released with Apache License Version 2.0, January 2004
   * http://www.apache.org/licenses/ by Square, Inc.
   *
   * <p>Magic method that creates a component with its dependencies set, by reflection. Relies on
   * Dagger2 naming conventions.
   */
  public static <T> T buildComponent(Class<T> componentClass, Object... dependencies) {
    buildMethodsCache(componentClass);

    String fqn = componentClass.getName();

    String packageName = componentClass.getPackage().getName();
    // Accounts for inner classes, ie MyApplication$Component
    String simpleName = fqn.substring(packageName.length() + 1);
    String generatedName = (packageName + ".Dagger" + simpleName).replace('$', '_');

    try {
      Class<?> generatedClass = Class.forName(generatedName);
      Method builderMethod = generatedClass.getMethod("builder");
      builderMethod.setAccessible(true);
      Object builder = builderMethod.invoke(null);

      for (Method method : builder.getClass().getMethods()) {
        method.setAccessible(true);
        Class<?>[] params = method.getParameterTypes();
        if (params.length == 1) {
          Class<?> dependencyClass = params[0];
          for (Object dependency : dependencies) {
            if (dependencyClass.isAssignableFrom(dependency.getClass())) {
              method.invoke(builder, dependency);
              break;
            }
          }
        }
      }
      // noinspection unchecked
      //      @SuppressWarnings("unchecked")
      Method builderMetod = builder.getClass().getMethod("build");
      builderMetod.setAccessible(true);
      @SuppressWarnings("unchecked")
      T ret = (T) builderMetod.invoke(builder);
      return ret;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static <T> void buildMethodsCache(Class<T> componentClass) {
    if (!Dagger2Helper.methodsCache.containsKey(componentClass)) {
      HashMap<Class<?>, Method> methods = new HashMap<>();
      for (Method method : componentClass.getMethods()) {
        Class<?>[] params = method.getParameterTypes();
        if (params.length == 1) methods.put(params[0], method);
      }
      Dagger2Helper.methodsCache.put(componentClass, methods);
    }
  }

  public static void inject(Class<?> componentClass, Object component, Object target) {

    HashMap<Class<?>, Method> methods = methodsCache.get(componentClass);
    if (methods == null)
      throw new RuntimeException(
          "Component " + componentClass + " has not been built with " + Dagger2Helper.class);

    Class<?> targetClass = target.getClass();
    Method method = methods.get(targetClass);
    if (method == null)
      throw new RuntimeException(
          "Method for " + targetClass + " injection does not exist in " + componentClass);

    try {
      method.invoke(component, target);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
