package nz.ac.auckland.lmz

/**
 * Provides access to a number of quick class reflection utilities required by Grayles.
 * <p>Author: <a href="http://gplus.to/tzrlk">Peter Cummuskey</a></p>
 */
//@CompileStatic
public abstract class ClassUtils {

    public static <Type> Type safeCast(def target, Class<Type> clazz) {
        return clazz.isAssignableFrom(target.class) ? clazz.cast(target) : null;
    }

    /**
     * This method copies property values from one class to another, provided that everything matches up. If
     * @param target The target object to copy the properties into.
     * @param data The data to copy into the target object.
     * @param properties The list of properties to allow a copy for.
     */
    public static <T> T safeCopy(T target, Map data, Collection<String> properties) {
        target.metaPropertyValues.each { PropertyValue property ->
            if (data.containsKey(property.name) && ( !properties || property.name in properties )) {
                property.value = data[property.name];
            }
        }

        return target;
    }

    /**
     * Simply calls {@link #safeCopy(Object, Map, List)} with the varargs turned into a list.
     * @param target The target object to copy the properties into.
     * @param data The data to copy into the target object.
     * @param properties The list of properties to allow a copy for.
     */
    public static <T> T safeCopy(T target, Map data, String... properties) {
        return safeCopy(target, data, properties.toList());
    }

    /**
     * Generates a list of classes, starting at the one provided, and progressing up the inheritance chain. The last
     * entry in the list should always be {@link Object}, and the first should be the specified class. Recursively
     * calls itself with the superclass of the target class to build the list. If interfaces are being included, they
     * will be added between each class and its superclass, and the interfaces themselves will also be processed
     * recursively.
     * @param target The class to build the ancestry chain for.
     * @param includeInterfaces Whether to include interfaces in the scan.
     * @return The full ancestry list for the provided class, ending with {@link Object}.
     */
    public static List<Class> listAncestry(Class target, boolean includeInterfaces = false) {
        List<Class> ancestry = [ target ];

        if (includeInterfaces) {
            target.interfaces.each { Class interfaceClass ->
                ancestry += listAncestry(interfaceClass, true);
            }
        }

        if (target != Object) {
            ancestry += listAncestry(target.superclass);
        }

        return ancestry;
    }

    /**
     * Injects a specific argument into a closure by type. If the type is not specified, the type of the provided param
     * will be used (not good when searching for an interface). Will always inject the param into the first occurrence
     * of the specified type.
     * @param closure The closure to inject the param into.
     * @param param The param to inject into the closure.
     * @param paramType The class of the param.
     * @return The curried closure.
     */
    public static Closure curryInjection(Closure closure, Object param, Class paramType = param.class) {
        return closure.ncurry(findIndexOfClosestMatch(closure.parameterTypes, paramType), param);
    }

    protected static int findIndexOfClosestMatch(Class[] types, Class match) {
        return types.findIndexOf { Class clazz ->
            return match.isAssignableFrom(clazz);
        }
    }

    /**
     * Curries a {@link Closure} with the provided params, but won't curry more than the number of params declared in
     * the closure itself.
     * @param closure The closure to curry.
     * @param params The params to curry into the closure.
     * @return The curried closure.
     * @see Closure#curry
     */
    public static <V> Closure<V> variableCurry(Closure<V> closure, Object... params) {

        int maxParams = Math.min(closure.maximumNumberOfParameters, params.length);

        if (maxParams < 1) {
            return closure;
        }

        Object[] limitedParams = Arrays.copyOfRange(params, 0, maxParams);

        return limitedParams.inject(closure) { Closure<V> curried, Object param ->
            return curried.curry(param);
        } as Closure<V>;

    }

    /** Cannot be instantiated */
    private ClassUtils() {}

}
