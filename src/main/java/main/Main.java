package main;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import java.io.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.*;
import java.util.UUID;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import groovyjarjarantlr4.v4.runtime.misc.Nullable;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;


public class Main {

    public static String ejk;
    public static File datasetFile = new File("src/main/resources/knowledge.csv");
    public static File query = new File("src/main/resources/file.wav");
    public static TrainSet ts = new TrainSet(datasetFile);
    public static HashMap<String, String> dataset = ts.getKnowledge();
    private static final boolean TRACE_MODE = true;
    private static long counter = 0L;
    private static Random rnd;
    public static setconfig scf = new setconfig();
    private static final String smbls = "ABCD37FH.F927RHFNV.WNZ83GGJ1038GNZV";
    private static final File uuid = new File("src/main/resources/uuid.uuid");
    public static final String localToken = "be7d0307-2e8e-46eb-b137-fa2a022f34c9";

    private static String getResourcesPath() {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() - 2);
        return path + File.separator + "src" + File.separator + "main" + File.separator + "resources";
    }

    public static String genUUID() {
        return UUID.randomUUID().toString();
    }

    public static String readFromFile(File myObj, String s) {
        try {
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                s = myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return s;

    }

    public static void WriteToFile(String toWrite, File file) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            assert writer != null;
            writer.write(toWrite);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static CharSequence getResponse(boolean running, String ms) {
        try {

            String resourcesPath = getResourcesPath();
            MagicBooleans.trace_mode = TRACE_MODE;
            Bot bot = new Bot("emerald", resourcesPath);
            Chat chatSession = new Chat(bot);
            bot.brain.nodeStats();
            String textLine;

            if (running) {
                while (true) {
                    textLine = ms;
                    if ((textLine == null) || (textLine.length() < 1))
                        textLine = MagicStrings.null_input;
                    else {
                        String response = chatSession.multisentenceRespond(textLine);
                        while (response.contains("&lt;"))
                            response = response.replace("&lt;", "<");
                        while (response.contains("&gt;"))
                            response = response.replace("&gt;", ">");
                        return response;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getRandom(String[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
    }

    /*
    public static QuickstartSample qs;

    static {
        try {
            qs = new QuickstartSample(query);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */


    public static void run() {
        String s;
        Scanner sc = new Scanner(System.in);
        while (true) {
            s = sc.nextLine();
            tts(answer(s));
            counter += 1;
        }
    }

/*
    public static String listen() throws IOException {

        final JavaSoundRecorder recorder = new JavaSoundRecorder();

        Thread stopper = new Thread(() -> {
            try {
                Thread.sleep(RECORD_TIME);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            recorder.finish();
        });

        stopper.start();

        recorder.start();

        return qs.getTranscription();
    }

 */

    public static void learnf(String key, String response) {
        try {
            FileWriter fw = new FileWriter(datasetFile, true);

            fw.write(key);
            fw.write(",");
            fw.write(" ");
            fw.write("\n" + response);

            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String answer(String question) {
        /*

        ArrayList<String> ary = new ArrayList<>();
        Set<String> keys = dataset.keySet();
        int counter = 0;
        for (String key : keys) {
            String lowerKey = key.toLowerCase();
            String lowerQuestion = question.toLowerCase();
            if (lowerKey.contains(lowerQuestion)) {
                // return dataset.get(key);
                ary.add(key);
            }
        }
        int idx = TrainSet.rndInt(ary);
        if (ary.get(idx) != null) {
            return ary.get(idx);
        } else {
            return null;
        }

         */

        try {
            return (String) getResponse(true, question);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String noAnswer(String s) {

        Set<String> keys = dataset.keySet();
            tts("I don't currently understand your query, how would you like me to respond in the future?");

            Scanner sc = new Scanner(System.in);

            String newResp = sc.nextLine();

            learnf(s, newResp);
            return newResp;

    }

    public static void tts(String ToSpeak) {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        Voice voice = VoiceManager.getInstance().getVoice("kevin16");
        if (voice != null) {
            voice.allocate();
            try {
                voice.setRate(130);
                voice.setPitch(150);
                voice.setVolume(3);
                voice.speak(ToSpeak);
                System.out.println(ToSpeak);

            } catch (Exception e1) {
                e1.printStackTrace();
            }

        } else {
            throw new IllegalStateException("Cannot find voice: kevin16");
        }
    }

    public Main() {
        super();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public static int indexOf(int ch) {
        return ejk.indexOf(ch);
    }

    public static int indexOf(int ch, int fromIndex) {
        return ejk.indexOf(ch, fromIndex);
    }

    public static int lastIndexOf(int ch) {
        return ejk.lastIndexOf(ch);
    }

    public static int lastIndexOf(int ch, int fromIndex) {
        return ejk.lastIndexOf(ch, fromIndex);
    }

    public static int indexOf(@NotNull String str) {
        return ejk.indexOf(str);
    }

    public static int indexOf(@NotNull String str, int fromIndex) {
        return ejk.indexOf(str, fromIndex);
    }

    public static int lastIndexOf(@NotNull String str) {
        return ejk.lastIndexOf(str);
    }

    public static int lastIndexOf(@NotNull String str, int fromIndex) {
        return ejk.lastIndexOf(str, fromIndex);
    }

    public static String substring(int beginIndex) {
        return ejk.substring(beginIndex);
    }

    public static String substring(int beginIndex, int endIndex) {
        return ejk.substring(beginIndex, endIndex);
    }

    public static CharSequence subSequence(int beginIndex, int endIndex) {
        return ejk.subSequence(beginIndex, endIndex);
    }

    public static String concat(@NotNull String str) {
        return ejk.concat(str);
    }

    public static String replace(char oldChar, char newChar) {
        return ejk.replace(oldChar, newChar);
    }

    public static boolean matches(@NotNull String regex) {
        return ejk.matches(regex);
    }

    public static boolean contains(@NotNull CharSequence s) {
        return ejk.contains(s);
    }

    public static String replaceFirst(@NotNull String regex, @NotNull String replacement) {
        return ejk.replaceFirst(regex, replacement);
    }

    public static String replaceAll(@NotNull String regex, @NotNull String replacement) {
        return ejk.replaceAll(regex, replacement);
    }

    public static String replace(@NotNull CharSequence target, @NotNull CharSequence replacement) {
        return ejk.replace(target, replacement);
    }

    public static String[] split(@NotNull String regex, int limit) {
        return ejk.split(regex, limit);
    }

    public static String[] split(@NotNull String regex) {
        return ejk.split(regex);
    }

    public static String join(CharSequence delimiter, CharSequence... elements) {
        return String.join(delimiter, elements);
    }

    public static String join(CharSequence delimiter, Iterable<? extends CharSequence> elements) {
        return String.join(delimiter, elements);
    }

    public static String toLowerCase(@NotNull Locale locale) {
        return ejk.toLowerCase(locale);
    }

    public static String toLowerCase() {
        return ejk.toLowerCase();
    }

    public static String toUpperCase(@NotNull Locale locale) {
        return ejk.toUpperCase(locale);
    }

    public static String toUpperCase() {
        return ejk.toUpperCase();
    }

    public static String trim() {
        return ejk.trim();
    }

    public static String strip() {
        return ejk.strip();
    }

    public static String stripLeading() {
        return ejk.stripLeading();
    }

    public static String stripTrailing() {
        return ejk.stripTrailing();
    }

    public static boolean isBlank() {
        return ejk.isBlank();
    }

    public static Stream<String> lines() {
        return ejk.lines();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public static boolean contentEquals(@NotNull StringBuffer sb) {
        return ejk.contentEquals(sb);
    }

    public static boolean contentEquals(@NotNull CharSequence cs) {
        return ejk.contentEquals(cs);
    }

    public static boolean equalsIgnoreCase(String anotherString) {
        return ejk.equalsIgnoreCase(anotherString);
    }

    public static int compareTo(@NotNull String anotherString) {
        return ejk.compareTo(anotherString);
    }

    public static int compareToIgnoreCase(@NotNull String str) {
        return ejk.compareToIgnoreCase(str);
    }

    public static boolean regionMatches(int toffset, @NotNull String other, int ooffset, int len) {
        return ejk.regionMatches(toffset, other, ooffset, len);
    }

    public static boolean regionMatches(boolean ignoreCase, int toffset, @NotNull String other, int ooffset, int len) {
        return ejk.regionMatches(ignoreCase, toffset, other, ooffset, len);
    }

    public static boolean startsWith(@NotNull String prefix, int toffset) {
        return ejk.startsWith(prefix, toffset);
    }

    public static boolean startsWith(@NotNull String prefix) {
        return ejk.startsWith(prefix);
    }

    public static boolean endsWith(@NotNull String suffix) {
        return ejk.endsWith(suffix);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public static Path toPath() {
        return datasetFile.toPath();
    }

    public static IntStream chars() {
        return ejk.chars();
    }

    public static IntStream codePoints() {
        return ejk.codePoints();
    }

    public static char[] toCharArray() {
        return ejk.toCharArray();
    }

    public static String format(@NotNull String format, Object... args) {
        return String.format(format, args);
    }

    public static String format(Locale l, @NotNull String format, Object... args) {
        return String.format(l, format, args);
    }

    public static String valueOf(Object obj) {
        return String.valueOf(obj);
    }

    public static String valueOf(@NotNull char[] data) {
        return String.valueOf(data);
    }

    public static String valueOf(@NotNull char[] data, int offset, int count) {
        return String.valueOf(data, offset, count);
    }

    public static String copyValueOf(@NotNull char[] data, int offset, int count) {
        return String.copyValueOf(data, offset, count);
    }

    public static String copyValueOf(@NotNull char[] data) {
        return String.copyValueOf(data);
    }

    public static String valueOf(boolean b) {
        return String.valueOf(b);
    }

    public static String valueOf(char c) {
        return String.valueOf(c);
    }

    public static String valueOf(int i) {
        return String.valueOf(i);
    }

    public static String valueOf(long l) {
        return String.valueOf(l);
    }

    public static String valueOf(float f) {
        return String.valueOf(f);
    }

    public static String valueOf(double d) {
        return String.valueOf(d);
    }

    public static String intern() {
        return ejk.intern();
    }

    public static String repeat(int count) {
        return ejk.repeat(count);
    }

    public static int compare(CharSequence cs1, CharSequence cs2) {
        return CharSequence.compare(cs1, cs2);
    }

    public static int length() {
        return ejk.length();
    }

    public static boolean createNewFile() throws IOException {
        return datasetFile.createNewFile();
    }

    public static boolean delete() {
        return datasetFile.delete();
    }

    public static void deleteOnExit() {
        datasetFile.deleteOnExit();
    }

    public static String[] list() {
        return datasetFile.list();
    }

    public static String[] list(@Nullable FilenameFilter filter) {
        return datasetFile.list(filter);
    }

    public static File[] listFiles() {
        return datasetFile.listFiles();
    }

    public static File[] listFiles(@Nullable FilenameFilter filter) {
        return datasetFile.listFiles(filter);
    }

    public static File[] listFiles(@Nullable FileFilter filter) {
        return datasetFile.listFiles(filter);
    }

    public static boolean mkdir() {
        return datasetFile.mkdir();
    }

    public static boolean mkdirs() {
        return datasetFile.mkdirs();
    }

    public static boolean renameTo(File dest) {
        return datasetFile.renameTo(dest);
    }

    public static boolean setLastModified(long time) {
        return datasetFile.setLastModified(time);
    }

    public static boolean setReadOnly() {
        return datasetFile.setReadOnly();
    }

    public static boolean setWritable(boolean writable, boolean ownerOnly) {
        return datasetFile.setWritable(writable, ownerOnly);
    }

    public static boolean setWritable(boolean writable) {
        return datasetFile.setWritable(writable);
    }

    public static boolean setReadable(boolean readable, boolean ownerOnly) {
        return datasetFile.setReadable(readable, ownerOnly);
    }

    public static boolean setReadable(boolean readable) {
        return datasetFile.setReadable(readable);
    }

    public static boolean setExecutable(boolean executable, boolean ownerOnly) {
        return datasetFile.setExecutable(executable, ownerOnly);
    }

    public static boolean setExecutable(boolean executable) {
        return datasetFile.setExecutable(executable);
    }

    public static boolean canExecute() {
        return datasetFile.canExecute();
    }

    public static File[] listRoots() {
        return File.listRoots();
    }

    public static long getTotalSpace() {
        return datasetFile.getTotalSpace();
    }

    public static long getFreeSpace() {
        return datasetFile.getFreeSpace();
    }

    public static long getUsableSpace() {
        return datasetFile.getUsableSpace();
    }

    public static File createTempFile(@NotNull String prefix, String suffix, File directory) throws IOException {
        return File.createTempFile(prefix, suffix, directory);
    }

    public static File createTempFile(@NotNull String prefix, String suffix) throws IOException {
        return File.createTempFile(prefix, suffix);
    }

    public static int compareTo(File pathname) {
        return datasetFile.compareTo(pathname);
    }

    public static boolean isEmpty() {
        return ejk.isEmpty();
    }

    public static char charAt(int index) {
        return ejk.charAt(index);
    }

    public static int codePointAt(int index) {
        return ejk.codePointAt(index);
    }

    public static int codePointBefore(int index) {
        return ejk.codePointBefore(index);
    }

    public static int codePointCount(int beginIndex, int endIndex) {
        return ejk.codePointCount(beginIndex, endIndex);
    }

    public static int offsetByCodePoints(int index, int codePointOffset) {
        return ejk.offsetByCodePoints(index, codePointOffset);
    }

    public static void getChars(int srcBegin, int srcEnd, @NotNull char[] dst, int dstBegin) {
        ejk.getChars(srcBegin, srcEnd, dst, dstBegin);
    }

    @Deprecated(since = "1.1")
    public static void getBytes(int srcBegin, int srcEnd, @NotNull byte[] dst, int dstBegin) {
        ejk.getBytes(srcBegin, srcEnd, dst, dstBegin);
    }

    public static byte[] getBytes(@NotNull String charsetName) throws UnsupportedEncodingException {
        return ejk.getBytes(charsetName);
    }

    public static byte[] getBytes(@NotNull Charset charset) {
        return ejk.getBytes(charset);
    }

    public static byte[] getBytes() {
        return ejk.getBytes();
    }

    public static String getName() {
        return datasetFile.getName();
    }

    public static String getParent() {
        return datasetFile.getParent();
    }

    public static File getParentFile() {
        return datasetFile.getParentFile();
    }

    public static String getPath() {
        return datasetFile.getPath();
    }

    public static boolean isAbsolute() {
        return datasetFile.isAbsolute();
    }

    public static String getAbsolutePath() {
        return datasetFile.getAbsolutePath();
    }

    public static File getAbsoluteFile() {
        return datasetFile.getAbsoluteFile();
    }

    public static String getCanonicalPath() throws IOException {
        return datasetFile.getCanonicalPath();
    }

    public static File getCanonicalFile() throws IOException {
        return datasetFile.getCanonicalFile();
    }

    @Deprecated
    public static URL toURL() throws MalformedURLException {
        return datasetFile.toURL();
    }

    public static URI toURI() {
        return datasetFile.toURI();
    }

    public static boolean canRead() {
        return datasetFile.canRead();
    }

    public static boolean canWrite() {
        return datasetFile.canWrite();
    }

    public static boolean exists() {
        return datasetFile.exists();
    }

    public static boolean isDirectory() {
        return datasetFile.isDirectory();
    }

    public static boolean isFile() {
        return datasetFile.isFile();
    }

    public static boolean isHidden() {
        return datasetFile.isHidden();
    }

    public static long lastModified() {
        return datasetFile.lastModified();
    }

    public static void main(String[] args) {

        if (localToken.equals(readFromFile(uuid, ejk))) {

            tts("Emerald system online: please allow time for configuration");
            try {
                run();
                while (true) {
                    if (counter == 0) {
                        tts("Emerald engine starting, response loading...");
                    }
                    break;
                }
            } catch (Exception e) {
                e.getMessage();
            }
        } else {
            tts("Invalid uuid structure, please try again");
        }
    }

}