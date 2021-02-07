package atoms.simple.utils.title;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

class ReflectionUtil {
	private static String versionString;

	private static final Map<String, Class<?>> loadedNMSClasses = new HashMap<>();

	private static final Map<String, Class<?>> loadedOBCClasses = new HashMap<>();

	private static final Map<Class<?>, Map<String, Method>> loadedMethods = new HashMap<>();

	public static String getVersion() {
		if (versionString == null) {
			String name = Bukkit.getServer().getClass().getPackage().getName();
			versionString = name.substring(name.lastIndexOf('.') + 1) + ".";
		}
		return versionString;
	}

	public static Class<?> getNMSClass(String nmsClassName) {
		Class<?> clazz;
		if (loadedNMSClasses.containsKey(nmsClassName))
			return loadedNMSClasses.get(nmsClassName);
		String clazzName = "net.minecraft.server." + getVersion() + nmsClassName;
		try {
			clazz = Class.forName(clazzName);
		} catch (Throwable t) {
			t.printStackTrace();
			return loadedNMSClasses.put(nmsClassName, null);
		}
		loadedNMSClasses.put(nmsClassName, clazz);
		return clazz;
	}

	public static Class<?> getOBCClass(String obcClassName) {
		Class<?> clazz;
		if (loadedOBCClasses.containsKey(obcClassName))
			return loadedOBCClasses.get(obcClassName);
		String clazzName = "org.bukkit.craftbukkit." + getVersion() + obcClassName;
		try {
			clazz = Class.forName(clazzName);
		} catch (Throwable t) {
			t.printStackTrace();
			loadedOBCClasses.put(obcClassName, null);
			return null;
		}
		loadedOBCClasses.put(obcClassName, clazz);
		return clazz;
	}

	public static Method getMethod(Class<?> clazz, String methodName, Class<?>... params) {
		if (!loadedMethods.containsKey(clazz))
			loadedMethods.put(clazz, new HashMap<>());
		Map<String, Method> methods = loadedMethods.get(clazz);
		if (methods.containsKey(methodName))
			return methods.get(methodName);
		try {
			Method method = clazz.getMethod(methodName, params);
			methods.put(methodName, method);
			loadedMethods.put(clazz, methods);
			return method;
		} catch (Exception e) {
			e.printStackTrace();
			methods.put(methodName, null);
			loadedMethods.put(clazz, methods);
			return null;
		}
	}

	public void sendPacket(Player player, Object packet) {
		try {
			Object handle = player.getClass().getMethod("getHandle").invoke(player);
			Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
			playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}