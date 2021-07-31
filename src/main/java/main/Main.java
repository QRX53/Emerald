package main;

import java.io.*;
import java.util.*;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class Main {

    public static File datasetFile = new File("src/main/resources/knowledge.csv");
    public static TrainSet ts = new TrainSet(datasetFile);
    public static HashMap<String, String> dataset = ts.getKnowledge();

    public static String listen() {

        return "";
    }

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
        Set<String> keys = dataset.keySet();
        String ej = "";
        for (String key : keys) {
            ej = key;
            String lowerKey = key.toLowerCase();
            String lowerQuestion = question.toLowerCase();
            if (lowerKey.contains(lowerQuestion)) {
                return dataset.get(key);
            } else {
                returnNull("");
            }
        }
        return returnStr(ej);
    }

    public static String returnStr(String s) {
        return s;
    }
    public static String returnNull(String s) {
        String j = null;
        return j;

    }

    public static String noAnswer(String s) {

        Set<String> keys = dataset.keySet();
            tts("I don't currently understand your query, how would you like me to respond in the future?");
            String jj = listen();
            learnf(s, jj);
            return listen();
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

            } catch (Exception e1) {
                e1.printStackTrace();
            }

        } else {
            throw new IllegalStateException("Cannot find voice: kevin16");
        }
    }

    public static void main(String[] args) {


        Scanner sc = new Scanner(System.in);
        System.out.println("Enter: ");
        String s = sc.nextLine();

        tts(answer(s));


    }

}