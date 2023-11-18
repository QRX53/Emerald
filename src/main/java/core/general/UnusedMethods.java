package core.general;

import org.apache.commons.codec.digest.DigestUtils;
import core.other.StreamGobbler;
import core.other.TDES;
import core.other.TrainSet;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import static main.Main.*;

public class UnusedMethods {

    public static String getRandom(String[] array) {
        int rnd = new Random().nextInt(array.length);
        return array[rnd];
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

    public static String createUncrackable(String string_to_encode) throws Exception {
        TDES td = new TDES();
        String rot13 = rot13(string_to_encode);
        String tdes = td.encrypt(rot13);
        String hashed = DigestUtils.sha256Hex(tdes);
        String megaHashed = DigestUtils.sha256Hex(hashed);
        String megaTdes = td.encrypt(megaHashed);
        string_to_encode = rot13(megaTdes);
        return rot13(rot13(rot13(DigestUtils.sha256Hex(td.encrypt(DigestUtils.sha256Hex(rot13(string_to_encode)))))));
    }

    public static String readFromFile(File myObj, String s) {
        try {
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                s = myReader.nextLine();
            }
            myReader.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return s;

    }

    public static String decrypt(String input, Supplier<StringBuilder> supplier) {
        // Same as rot13() method
        StringBuilder sb = supplier.get();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c >= 'a' && c <= 'm') c += 13;
            else if (c >= 'A' && c <= 'M') c += 13;
            else if (c >= 'n' && c <= 'z') c -= 13;
            else if (c >= 'N' && c <= 'Z') c -= 13;
            sb.append(c);
        }
        return sb.toString();
    }

    public static String genUUID() {
        return UUID.randomUUID().toString();
    }

    public static void ttsBeta(String toSpeak) throws IOException, InterruptedException {
        String homeDirectory = System.getProperty("user.home");
        Process process;
        if (isWindows) {
            process = Runtime.getRuntime()
                    .exec("");
        } else {
            process = Runtime.getRuntime()
                    .exec("cd src; cd main; cd java");
        }
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        int exitCode = process.waitFor();
        assert exitCode == 0;
    }

    public static String answerFromSet(String question) {
        ArrayList<String> ary = new ArrayList<>();
        Set<String> keys = dataset.keySet();
        int counter = 0;
        for (String key : keys) {
            String lowerKey = key.toLowerCase();
            String lowerQuestion = question.toLowerCase();
            if (lowerKey.contains(lowerQuestion)) {
                ary.add(key);
            }
        }
        int idx = TrainSet.rndInt(ary);
        if (ary.get(idx) != null) {
            return ary.get(idx);
        } else {
            return null;
        }
    }

    public static String noAnswer(String s) {

        tts("I don't currently understand your query, how would you like me to respond in the future?");

        Scanner sc = new Scanner(System.in);

        String newResp = sc.nextLine();

        learnf(s, newResp);
        return newResp;

    }
}
