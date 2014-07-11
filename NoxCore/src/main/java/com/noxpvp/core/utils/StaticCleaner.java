package com.noxpvp.core.utils;

import com.bergerkiller.bukkit.common.AsyncTask;
import com.bergerkiller.bukkit.common.PluginBase;
import com.noxpvp.core.reflection.FieldUtils;
import org.apache.commons.lang.Validate;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * This is a modified version. Obtained through ProtocolLibraries code. <br>
 * Exact version used is in the link below <br>
 * A copy may be obtained through github @ <a href="https://github.com/aadnk/ProtocolLib/blob/c137640feea9063e22b788c8d8643400cf145ba6/ProtocolLib/src/main/java/com/comphenix/protocol/CleanupStaticMembers.java">https://github.com/aadnk/ProtocolLib/</a
 * <br><br>
 * Used to fix ClassLoader leaks that may lead to filling up the permanent generation.
 *
 * @author Kristian
 */
public class StaticCleaner {
	private Logger log;
	private ClassLoader loader;
	private String[] internalClasses;
	private Class<?>[] publicClasses;

	public StaticCleaner(Plugin plugin, ClassLoader loader, String[] internals, Class<?>[] classes) {
		if (plugin instanceof PluginBase) log = ((PluginBase)plugin).getModuleLogger("Static Cleaner");
		else log = plugin.getLogger();

		Validate.notNull(log, "Plugin does not contain a logger. Plugin must have a logger!");

		this.loader = loader;
		this.internalClasses = internals;
		this.publicClasses = classes;
	}

	// HACK! HAACK!
	private static void setFinalStatic(Field field, Object newValue) throws IllegalAccessException {
		int modifier = field.getModifiers();
		boolean isFinal = Modifier.isFinal(modifier);

		Field modifiersField = isFinal ? FieldUtils.getField(Field.class, "modifiers", true) : null;

		// We have to remove the final field first
		if (isFinal) {
			FieldUtils.writeField(modifiersField, field, modifier & ~Modifier.FINAL, true);
		}

		Class<?> type = field.getType();
		if (type.isInstance(AsyncTask.class)) {
			AsyncTask t = (AsyncTask) field.get(null);
			if (t != null && t.isRunning())
				t.stop(); //STOP TASK OR IT WILL RUN FOREVER!
		}

		// Now we can safely modify the field
		FieldUtils.writeStaticField(field, newValue, true);

		// Revert modifier
		if (isFinal) {
			FieldUtils.writeField(modifiersField, field, modifier, true);
		}
	}

	public void resetAll() {
		resetClasses(publicClasses);
		resetClasses(getClasses(loader, internalClasses));
	}

	private void resetClasses(Class<?>[] classes) {
		// Reset each class one by one
		for (Class<?> clazz : classes) {
			resetClass(clazz);
		}
	}

	private void resetClass(Class<?> clazz) {
		for (Field field : clazz.getFields()) {
			Class<?> type = field.getType();

			// Only check static non-primitive fields. We also skip strings.
			if (Modifier.isStatic(field.getModifiers()) &&
					!type.isPrimitive() && !type.equals(String.class)) {
				try {
					setFinalStatic(field, null);
				} catch (IllegalAccessException e) {
					StringBuilder sb = new StringBuilder("Illegal Access exception has occured... ").append(field.getName()).append(" had the error with the following message.").append(e.getMessage());
					log.warning(sb.toString());
					e.printStackTrace();
				}
			}
		}
	}

	private Class<?>[] getClasses(ClassLoader loader, String[] names) {
		List<Class<?>> output = new ArrayList<Class<?>>();

		for (String name : names) {
			try {
				output.add(loader.loadClass(name));
			} catch (ClassNotFoundException e) {
				// Warn the user
				log.warning(new StringBuilder().append("ClassNotFoundException occured while looking for class \"").append(name).append("\"").append("Plugin may be out of date!").toString());
			}
		}

		return output.toArray(new Class<?>[0]);
	}
}
