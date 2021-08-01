package main;


import java.io.*;
import java.util.*;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;

import java.io.IOException;

public class Main {

    public static File datasetFile = new File("src/main/resources/knowledge.csv");
    public static File query = new File("src/main/resources/file.wav");
    public static TrainSet ts = new TrainSet(datasetFile);
    public static HashMap<String, String> dataset = ts.getKnowledge();
    private static final boolean TRACE_MODE = true;

    private static String getResourcesPath() {
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        path = path.substring(0, path.length() - 2);
        return path + File.separator + "src" + File.separator + "main" + File.separator + "resources";
    }

    public static CharSequence getResponse(boolean running, String ms) {
        try {

            String resourcesPath = getResourcesPath();
            MagicBooleans.trace_mode = TRACE_MODE;
            Bot bot = new Bot("grill", resourcesPath);
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

//    public static QuickstartSample qs;

//    static {
//        try {
//            qs = new QuickstartSample(query);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    public static void run() {
        String s;
        Scanner sc = new Scanner(System.in);
        s = sc.nextLine();
        tts(answer(s));
    }


//    public static String listen() throws IOException {
//
//        final JavaSoundRecorder recorder = new JavaSoundRecorder();
//
//        Thread stopper = new Thread(() -> {
//            try {
//                Thread.sleep(RECORD_TIME);
//            } catch (InterruptedException ex) {
//                ex.printStackTrace();
//            }
//            recorder.finish();
//        });
//
//        stopper.start();
//
//        recorder.start();
//
//        return qs.getTranscription();
//    }

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
//        ArrayList<String> ary = new ArrayList<>();
//        Set<String> keys = dataset.keySet();
//        int counter = 0;
//        for (String key : keys) {
//            String lowerKey = key.toLowerCase();
//            String lowerQuestion = question.toLowerCase();
//            if (lowerKey.contains(lowerQuestion)) {
//                // return dataset.get(key);
//                ary.add(key);
//            }
//        }
//        int idx = TrainSet.rndInt(ary);
//        if (ary.get(idx) != null) {
//            return ary.get(idx);
//        } else {
//            return null;
//        }

        return (String) getResponse(true, question);

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

            } catch (Exception e1) {
                e1.printStackTrace();
            }

        } else {
            throw new IllegalStateException("Cannot find voice: kevin16");
        }
    }

    public static void main(String[] args) {

        while (true) {
            run();
        }

    }

}