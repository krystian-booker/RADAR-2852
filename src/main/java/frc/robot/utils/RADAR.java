package frc.robot.utils;

import java.util.concurrent.ConcurrentHashMap;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.util.sendable.Sendable;

//Robotics Analysis and Data Acquisition Resource
public class RADAR {

    private static final ConcurrentHashMap<String, Object> localDataMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Object> remoteDataMap = new ConcurrentHashMap<>();
    private static final NetworkTable radarTable = NetworkTableInstance.getDefault().getTable("RADAR");

    static {
        clearTable();
    }

    public static void putBoolean(String key, boolean value) {
        localDataMap.put(key, value);
    }

    public static void putNumber(String key, double value) {
        localDataMap.put(key, value);
    }

    public static void putString(String key, String value) {
        localDataMap.put(key, value);
    }

    public static void putBooleanArray(String key, boolean[] value) {
        localDataMap.put(key, value);
    }

    public static void putNumberArray(String key, double[] value) {
        localDataMap.put(key, value);
    }

    public static void putStringArray(String key, String[] value) {
        localDataMap.put(key, value);
    }

    public static void putRaw(String key, byte[] value) {
        localDataMap.put(key, value);
    }

    public static boolean getBoolean(String key) {
        return (boolean) remoteDataMap.getOrDefault(key, false);
    }

    public static double getNumber(String key) {
        return (double) remoteDataMap.getOrDefault(key, 0.0);
    }

    public static String getString(String key) {
        return (String) remoteDataMap.getOrDefault(key, "");
    }

    public static boolean[] getBooleanArray(String key) {
        return (boolean[]) remoteDataMap.getOrDefault(key, new boolean[0]);
    }

    public static double[] getNumberArray(String key) {
        return (double[]) remoteDataMap.getOrDefault(key, new double[0]);
    }

    public static String[] getStringArray(String key) {
        return (String[]) remoteDataMap.getOrDefault(key, new String[0]);
    }

    public static byte[] getRaw(String key) {
        return (byte[]) remoteDataMap.getOrDefault(key, new byte[0]);
    }

    public static void update() {
        // Read updates from the NetworkTable
        for (String key : radarTable.getKeys()) {
            NetworkTableEntry entry = radarTable.getEntry(key);
            Object currentValueInTable = entry.getValue().getValue();
            remoteDataMap.put(key, currentValueInTable);
        }

        // Write changed values to the NetworkTable
        for (ConcurrentHashMap.Entry<String, Object> entry : localDataMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            NetworkTableEntry tableEntry = radarTable.getEntry(key);
            Object currentValueInTable = tableEntry.getValue().getValue();

            if (currentValueInTable == null || !currentValueInTable.equals(value)) {
                tableEntry.setValue(value);
            }
        }
    }

    public static void clearTable() {
        for (String key : radarTable.getKeys()) {
            radarTable.getEntry(key).setValue(null);

            // TODO: Test this functionality to replace writing a null value
            NetworkTableEntry entry = radarTable.getEntry(key);
            entry.unpublish();
        }
    }
}
