import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class sound {
	static Clip clip;
	static int cliptype;

	/*
	 * have an int variable that determines the type of sound that will be
	 * played
	 */
	public sound(int type){
		cliptype=type;
		if (type == 1) {
			try {
				AudioInputStream audioInputStream = AudioSystem
						.getAudioInputStream(new File("data/challa.wav")
								.getAbsoluteFile());
				clip = AudioSystem.getClip();
				clip.open(audioInputStream);
			} catch (Exception e) {
			}
		} else if (type == 2) {
			try {
				AudioInputStream audioInputStream = AudioSystem
						.getAudioInputStream(new File("data/explosionsound.wav")
								.getAbsoluteFile());
				clip = AudioSystem.getClip();
				clip.open(audioInputStream);
			} catch (Exception e) {
			}
		}
	}

	public static void playSound() {
		try {
			clip.start();
			if(cliptype==1){
			clip.loop(clip.LOOP_CONTINUOUSLY);
			}
		} catch (Exception ex) {
			System.out.println("Error with playing sound.");
			ex.printStackTrace();
		}
	}
	public static void pause() {
		clip.stop();
	}
}
