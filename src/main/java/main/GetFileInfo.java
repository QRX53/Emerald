package main;

import java.io.File;

public class GetFileInfo {

    private static String name;
    private static String path;
    private static boolean writable;
    private static boolean readable;
    private static long size;

    public GetFileInfo(File myObj) {
        if (myObj.exists()) {
            System.out.println("File name: " + myObj.getName());
            System.out.println("Absolute path: " + myObj.getAbsolutePath());
            System.out.println("Writeable: " + myObj.canWrite());
            System.out.println("Readable " + myObj.canRead());
            System.out.println("File size in bytes " + myObj.length());
        } else {
            System.out.println("The file does not exist.");
        }
    }

    public static String getName() {
        return name;
    }

    public static String getPath() {
        return path;
    }

    public static boolean isWritable() {
        return writable;
    }

    public static boolean isReadable() {
        return readable;
    }

    public static long getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "GetFileInfo{}";
    }

    public static void setName(String name) {
        GetFileInfo.name = name;
    }

    public static void setPath(String path) {
        GetFileInfo.path = path;
    }

    public static void setWritable(boolean writable) {
        GetFileInfo.writable = writable;
    }

    public static void setReadable(boolean readable) {
        GetFileInfo.readable = readable;
    }

    public static void setSize(long size) {
        GetFileInfo.size = size;
    }

    public GetFileInfo() {
        super();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}