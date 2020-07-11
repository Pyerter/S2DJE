package sharp.utility;

import sharp.game.App;

import java.io.File;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineEvent;

public class Sound {

    public static Clip play(String file) {
	try {
	    file = App.getAudioPath() + file;
	    final File audioFile = new File(file);

	    final Clip clip = AudioSystem.getClip();

	    clip.addLineListener(e -> {
		    if (e.getType() == LineEvent.Type.STOP) {
			clip.close();
		    }
		});

	    clip.open(AudioSystem.getAudioInputStream(audioFile));
	    clip.start();
	    return clip;
	} catch (Exception e) {
	    System.out.println(e.getMessage());
	}
	return null;
    }

}
