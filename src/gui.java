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
	final String instructions = "Welcome to Minefield. The purpose of this game is to avoid the mines and make it to the next level while using the proximity meter at the top to navigate(it will display the danger level accordingly with red the most dangerous and green as safe). To start, click the play button at the side of this menu.  Step on the grey tiles in order to get to the next level and try to avoid the mines(NOTE that the mines are currently unhidden for debugging purposes). The grass areas are safe to walk on. Click on the arrow keys to move the player accordingly(up, left, right, down). You cannot move past the rock blocks and doing so will only stop your player walking. Keep on running as far as you can and try to complete all 10 levels.";
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
