package main;


import java.io.*;
import java.util.*;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import com.google.api.gax.paging.Page;
import com.google.auth.appengine.AppEngineCredentials;
import com.google.auth.oauth2.ComputeEngineCredentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.common.collect.Lists;
import java.io.FileInputStream;
import java.io.IOException;

import static main.JavaSoundRecorder.RECORD_TIME;

public class Main {

    public static File datasetFile = new File("src/main/resources/knowledge.csv");
    public static File query = new File("src/main/resources/file.wav");
    public static TrainSet ts = new TrainSet(datasetFile);
    public static HashMap<String, String> dataset = ts.getKnowledge();
    public static QuickstartSample qs;

    static void authImplicit() {
        // If you don't specify credentials when constructing the client, the client library will
        // look for credentials via the environment variable GOOGLE_APPLICATION_CREDENTIALS.
        Storage storage = StorageOptions.getDefaultInstance().getService();

        System.out.println("Buckets:");
        Page<Bucket> buckets = storage.list();
        for (Bucket bucket : buckets.iterateAll()) {
            System.out.println(bucket.toString());
        }
    }
    // [END auth_cloud_implicit]

    // [START auth_cloud_explicit]
    static void authExplicit(String jsonPath) throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(jsonPath))
                .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        System.out.println("Buckets:");
        Page<Bucket> buckets = storage.list();
        for (Bucket bucket : buckets.iterateAll()) {
            System.out.println(bucket.toString());
        }
    }
    // [END auth_cloud_explicit]

    // [START auth_cloud_explicit_compute_engine]
    static void authCompute() {
        // Explicitly request service account credentials from the compute engine instance.
        GoogleCredentials credentials = ComputeEngineCredentials.create();
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        System.out.println("Buckets:");
        Page<Bucket> buckets = storage.list();
        for (Bucket bucket : buckets.iterateAll()) {
            System.out.println(bucket.toString());
        }
    }
    // [END auth_cloud_explicit_compute_engine]

    // [START auth_cloud_explicit_app_engine]
    static void authAppEngineStandard() throws IOException {
        // Explicitly request service account credentials from the app engine standard instance.
        GoogleCredentials credentials = AppEngineCredentials.getApplicationDefault();
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();

        System.out.println("Buckets:");
        Page<Bucket> buckets = storage.list();
        for (Bucket bucket : buckets.iterateAll()) {
            System.out.println(bucket.toString());
        }
    }

    static {
        try {
            qs = new QuickstartSample(query);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void run() {
        String s;
        try {
            s = listen();
            tts(answer(s));
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

        while (true) {
            run();
        }

    }

}