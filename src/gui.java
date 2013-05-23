import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class gui implements ActionListener {
	JFrame frame;
	JPanel contentPane;
	JButton button, pause, stop;
	JTextArea area;
	final String instructions = "Welcome to Minefield. The purpose of this game is to avoid the mines and make it to the next level while using the proximity meter at the top to navigate(it will display the danger level accordingly with red the most dangerous and green as safe). Keep on running as far as you can but rest assured that you will lose. Step on the grey tiles in order to get to the next level. Click on the arrow keys to move the player accordingly. You cannot move past the rock blocks.";
	minefield minefield;
	static sound song;

	public gui() {
		minefield = new minefield();
		frame = new JFrame("Minefield");
		button = new JButton("Play");
		pause = new JButton("Pause");
		stop = new JButton("Stop");
		area = new JTextArea(20, 20);
		area.setWrapStyleWord(true);
		area.setFont(new Font("Serif", Font.ITALIC, 16));
		area.setLineWrap(true);
		area.setText(instructions);
		area.setEditable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(BorderFactory.createEmptyBorder(100, 100, 100,
				100));
		button.addActionListener(this);
		pause.addActionListener(this);
		stop.addActionListener(this);
		button.setActionCommand("Play");
		pause.setActionCommand("Pause");
		stop.setActionCommand("Stop");
		contentPane.add(button);
		contentPane.add(pause);
		contentPane.add(stop);
		contentPane.add(area);
		frame.setContentPane(contentPane);
		frame.pack();
		frame.setVisible(true);
	}

	public static void runGUI() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		gui gui = new gui();
	}

	public static void main(String[] args) {
		song = new sound(1);
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
			song.pause();
		}
		if (h.equals("Pause")) {
			minefield.pause();
		}
		if (h.equals("Stop"))
			minefield.stop();
	}

}
