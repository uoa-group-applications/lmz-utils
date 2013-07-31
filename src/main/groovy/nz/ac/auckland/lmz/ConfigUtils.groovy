package nz.ac.auckland.lmz

import java.lang.annotation.Annotation
import java.lang.reflect.Field
import java.lang.reflect.Method

import static nz.ac.auckland.grayles.ClassUtils.listAncestry;

/**
 * This is a set of utilities for scanning and collection specific annotation config.
 * <p>Author: <a href="http://gplus.to/tzrlk">Peter Cummuskey</a></p>
 */
public abstract class ConfigUtils {

    /**
     * Searches for a particular annotation on the provided class.
     * @param target The class to search on.
     * @param configType The annotation to look for.
     * @return The annotation instance for the class, or null if not found.
     */
    public static <A extends Annotation> A findClassConfig(Class target, Class<A> configType) {
        return target.declaredAnnotations.find { Annotation annotation ->
            return configType.isInstance(annotation);
        } as A;
    }

    /**
     * Searches for a particular annotation on the provided field.
     * @param target The field to search on.
     * @param configType The annotation to look for.
     * @return The annotation instance for the field, or null if not found.
     */
    public static <A extends Annotation> A findFieldConfig(Field target, Class<A> configType) {
        return target.declaredAnnotations.find { Annotation annotation ->
            return configType.isInstance(annotation);
        } as A;
    }

    /**
     * Searches for a particular annotation on the provided method.
     * @param target The method to search on.
     * @param configType The annotation to look for.
     * @return The annotation instance for the method, or null if not found.
     */
    public static <A extends Annotation> A findMethodConfig(Method target, Class<A> configType) {
        return target.declaredAnnotations.find { Annotation annotation ->
            return configType.isInstance(annotation);
        } as A;
    }

    /**
     * Scans the provided class, and all its parent classes for a specific annotation, returning both the annotation
     * instance, and the class to which it has been applied. Won't return any classes that don't have the annotation.
     * @param target The class to scan the inheritance structure for.
     * @param configType The annotation type to scan for.
     * @return A map of the classes and annotation instances.
     */
    public static <A extends Annotation> Map<Class, A> scanClassConfig(Class target, Class<A> configType) {
        return listAncestry(target).collectEntries() { Class clazz ->
            A config = findClassConfig(target, configType);
            return config ? [ clazz, config ] : null;
        }
    }

    /**
     * Scans the provided class, and all its parent classes for fields that possess a specific annotation, returning
     * both the annotation instances, and the fields to which they was applied. Won't return fields that don't have the
     * annotation.
     * @param target The class to scan the fields and inheritance structure of.
     * @param configType The class of the annotation to scan for.
     * @return A map of the fields and annotation instances.
     */
    public static <A extends Annotation> Map<Field, A> scanFieldConfig(Class target, Class<A> configType) {
        return listAncestry(target).inject([:]) { Map<Field, A> entries, Class clazz ->
            return entries + clazz.declaredFields.collectEntries() { Field field ->
                A config = findFieldConfig(field, configType);
                return config ? [ field, config ] : null;
            }
        }
    }

    /**
     * Scans the provided class, it's interfaces, and all parent classes and their interfaces for methods annotated
     * with the provided annotation. Will return all the methods that have the annotation applied, and the applied
     * annotation in a map.
     * @param target The class to start the scan on.
     * @param configType The annotation to scan for.
     * @return A map of annotated methods, and their annotations.
     */
    public static <A extends Annotation> Map<Method, A> scanMethodConfig(Class target, Class<A> configType) {
        return listAncestry(target, true).inject([:]) { Map<Method, A> entries, Class clazz ->
            return entries + clazz.declaredMethods.collectEntries() { Method method ->
                A config = findMethodConfig(method, configType);
                return config ? [ method, config ] : null;
            }
        }
    }

    /** This class cannot be instantiated. */
    private ConfigUtils() {};

}
