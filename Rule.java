import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Rule extends JFrame{
	public Rule(ModalityType mt) {
		getContentPane().setLayout(new FlowLayout());

		ImageIcon icon = new ImageIcon("rule.jpg");
		JLabel labelIcon = new JLabel(icon);
		getContentPane().add(labelIcon);

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setTitle("バトル将棋のルール");
		setSize(1000, 600);
		setVisible(true);
	}
}
