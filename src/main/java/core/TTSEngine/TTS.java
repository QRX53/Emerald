package core.TTSEngine;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

public class TTS {
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
                System.out.println(e1.getMessage());
            }

        } else {
            throw new IllegalStateException("Cannot find voice: kevin16");
        }
    }
}
