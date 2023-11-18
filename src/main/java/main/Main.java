package main;

import java.io.*;
import java.io.IOException;
import java.util.*;

import org.alicebot.ab.Bot;
import org.alicebot.ab.Chat;
import org.alicebot.ab.MagicBooleans;
import org.alicebot.ab.MagicStrings;
import core.other.TrainSet;

import javax.speech.Central;
import javax.speech.EngineException;
import javax.speech.EngineModeDesc;
import javax.speech.recognition.*;

import static core.TTSEngine.TTS.tts;

public class Main extends ResultAdapter {

    public static boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
    public static File datasetFile = new File("src/main/resources/knowledge.csv");
    public static File query = new File("src/main/resources/file.wav");
    public static TrainSet ts = new TrainSet(datasetFile);
    public static HashMap<String, String> dataset = ts.getKnowledge();
    private static final boolean TRACE_MODE = true;
    private static Recognizer rec;
    protected static File audio;

    public void resultAccepted(ResultEvent e) {
        Result r = (Result) (e.getSource());
        ResultToken[] tokens = r.getBestTokens();

        for (ResultToken token : tokens) System.out.print(token.getSpokenText() + " ");
        System.out.println();

        try {
            rec.deallocate();
        } catch (EngineException engineException) {
            System.out.println(engineException.getMessage());
        }
    }

    public static String rot13(String input) {
        // This is from stackoverflow!!
        StringBuilder sb = new StringBuilder();
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
            Bot bot = new Bot("emerald", resourcesPath);
            Chat chatSession = new Chat(bot);
            bot.brain.nodeStats();
            String textLine;

            if (running) {
                while (true) {
                    textLine = ms;
                    if ((textLine == null) || (textLine.isEmpty())) {
                        textLine = MagicStrings.null_input;
                    } else {
                        String response = chatSession.multisentenceRespond(textLine);
                        String[] responseMapped = response.split(" ");
                        String reponseNLED[] = response.split("\n");
                        System.err.println(response);
                        int counter = 0;

                        for (String mapped : responseMapped) {
                            if (mapped.contains("unknown")) {
                                responseMapped[counter] = "internal processing error";
                                break;
                            }
                            counter++;
                        }

                        while (response.contains("&lt;"))
                            response = response.replace("&lt;", "<");
                        while (response.contains("&gt;"))
                            response = response.replace("&gt;", ">");

                        response = reponseNLED[reponseNLED.length - 1];
                        return response;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void run() {
        String s;
        Scanner sc = new Scanner(System.in);
        int counter = 0;
        while (true) {
            if (counter == 0) {
                tts("Processing... Please allow up to a minute to index");
                tts(answer("Hello."));
            }
            s = sc.nextLine();
            tts(answer(s));
            counter += 1;
        }
    }


    public static String listen(File file) throws IOException {
        String s = "";

        try {
            // Create a recognizer that supports English.
            rec = Central.createRecognizer(
                    new EngineModeDesc(Locale.ENGLISH));

            // Start up the recognizer
            rec.allocate();

            // Load the grammar from a file, and enable it
            FileReader reader = new FileReader(file);
            RuleGrammar gram = rec.loadJSGF(reader);
            gram.setEnabled(true);

            // Add the listener to get results
            rec.addResultListener(new Main());

            // Commit the grammar
            rec.commitChanges();

            // Request focus and start listening
            rec.requestFocus();
            rec.resume();
            rec.deallocate();
            s = rec.toString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return s;
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
            System.out.println(e.getMessage());
        }
    }

    public static String answer(String input) {
        try {
            return (String) getResponse(true, input);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void main(String[] args) throws Exception {

        try {
            tts("Emerald system online: please allow time for configuration");
            try {
                run();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


}