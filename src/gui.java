import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class gui implements ActionListener {
	JFrame frame;
	JPanel contentPane;
	JButton button, pause, stop;
	JLabel label;
	final String instructions = "Welcome to minefield. The purpose of this game is to avoid the mines\n and make it to the next level while using the proximity meter\n at the top to navigate.";
	minefield minefield;
	static sound song;
	public gui() {
		minefield = new minefield();
		frame = new JFrame("Minefield");
		label = new JLabel(instructions);
		button = new JButton("Show");
		pause = new JButton("Pause");
		stop = new JButton("Stop");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		button.addActionListener(this);
		pause.addActionListener(this);
		stop.addActionListener(this);
		button.setActionCommand("Play");
		pause.setActionCommand("Pause");
		stop.setActionCommand("Stop");
		contentPane.add(button);
		contentPane.add(label);
		contentPane.add(pause);
		contentPane.add(stop);
		frame.setContentPane(contentPane);
		frame.pack();
		frame.setVisible(true);
	}

	public static void runGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		gui gui = new gui();
	}


	public static void main(String[] args) {
		song=new sound(1);
		javax.swing.SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				runGUI();
				song.playSound();
			}
		});
	}

	public void actionPerformed(ActionEvent event) {
		String h = event.getActionCommand();
		if (h.equals("Play")) {
			minefield.start();
			System.out.println("done");
		}
		if (h.equals("Pause")) {
			minefield.pause();
		}
		if (h.equals("Stop"))
			minefield.stop();
	}

}
