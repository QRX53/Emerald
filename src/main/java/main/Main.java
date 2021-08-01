package main;


import java.io.*;
import java.util.*;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import static main.JavaSoundRecorder.RECORD_TIME;

public class Main {

    public static File datasetFile = new File("src/main/resources/knowledge.csv");
    public static File query = new File("src/main/resources/file.wav");
    public static TrainSet ts = new TrainSet(datasetFile);
    public static HashMap<String, String> dataset = ts.getKnowledge();
    public static QuickstartSample qs;

    static {
        try {
            qs = new QuickstartSample(query);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


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
    }

    public static String noAnswer(String s) throws IOException {

        Set<String> keys = dataset.keySet();
            tts("I don't currently understand your query, how would you like me to respond in the future?");
        String jj = null;
            jj = listen();
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