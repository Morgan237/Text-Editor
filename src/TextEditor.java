import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.*;
import java.awt.*;
import java.io.*;

public class TextEditor extends JFrame implements ActionListener,KeyListener,WindowListener{
	
	JTextArea textArea;
	JScrollPane pane;
	JSpinner spinner;
	JLabel spinnerLabel;
	JPanel northPanel;
	JButton colorButton;
	JComboBox fontBox;
	JLabel comboLabel;
	JMenuBar menuBar;
	JMenu fileMenu;
	JMenuItem newItem;
	JMenuItem openItem;
	JMenuItem saveItem;
	JMenuItem exitItem;
	File directory = null;
	Boolean saved = true;
	
	TextEditor(){
//------Creating the Frame--------------------------------------------
		
//		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.setSize(550,450);
		this.setTitle("TextEditor");
		this.setLocationRelativeTo(null);
		this.setLayout(new BorderLayout());
		this.addWindowListener(this);
		
//------Creating the Text Area -------------------------------------------------------
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		textArea.addKeyListener(this);
		
//------Scrollpane for the text area---------------------------
		
		pane = new JScrollPane(textArea);
//		pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
//		pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
//		pane.setPreferredSize(new Dimension(410,410));
		pane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
//------JSpinner for choosing the size of the font-------------------------------
		
		spinner = new JSpinner();
//		spinner.setPreferredSize(new Dimension(50,25));
		spinner.setValue(20);
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				// TODO Auto-generated method stub
				textArea.setFont(new Font(textArea.getFont().getFamily(),Font.PLAIN,(int) spinner.getValue()));
			}
		});
		
//------Button for choosing the color of the font-------------------------------------
		
		colorButton = new JButton("Color");
		colorButton.addActionListener(this);
		colorButton.setFocusable(false);
		
//-----Creating JComboBox to pick a font-----------------------------------------------
		
		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		comboLabel = new JLabel("Font:");
		fontBox = new JComboBox(fonts);
		fontBox.addActionListener(this);
		fontBox.setSelectedItem("Bitstream Charter");
		
//------Adding stuffs to the northPanel that is at BorderLayout.NORTH---------------
		
		spinnerLabel = new JLabel("Font Size: ");
		northPanel = new JPanel();
//		northPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		northPanel.setPreferredSize(new Dimension(0,30));
//		northPanel.setBorder(BorderFactory.createEmptyBorder(0, 22, 0, 100));
		northPanel.add(spinnerLabel);
		northPanel.add(spinner);
		northPanel.add(Box.createHorizontalStrut(10)); //Spacing between the spinner and the button!
		northPanel.add(colorButton);
		northPanel.add(Box.createHorizontalStrut(10));
		northPanel.add(comboLabel);
		northPanel.add(fontBox);
		
//------Setting up the Menu Bar------------------------------------------------------
		menuBar = new JMenuBar();
		fileMenu = new JMenu("File");
		
		newItem = new JMenuItem("New File");
		openItem = new JMenuItem("Open file");
		saveItem = new JMenuItem("Save file");
		exitItem = new JMenuItem("Exit");
		
		newItem.addActionListener(this);
		openItem.addActionListener(this);
		saveItem.addActionListener(this);
		exitItem.addActionListener(this);
		
		fileMenu.add(newItem);
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.add(exitItem);
		
		this.setJMenuBar(menuBar);
		menuBar.add(fileMenu);

//------Adding everything to the frame-------------------------------------------
		
		this.add(BorderLayout.NORTH,northPanel);
		this.add(BorderLayout.CENTER,pane);
		this.setVisible(true);
	}

//	------------------------------------------------------------------------------------
//					!!!!ACTION PERFORMED!!!!
//	------------------------------------------------------------------------------------
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
//--------Changing the color--------------------------------------------------
		
		if(e.getSource() == colorButton) {
			JColorChooser chooser = new JColorChooser();
			Color color = chooser.showDialog(chooser, "Choose a color:",Color.black);
			textArea.setForeground(color);
		}
		
//-------Changing the font----------------------------------------------------------
		if(e.getSource() == fontBox) {
			String font = (String) fontBox.getSelectedItem();
			textArea.setFont(new Font(font,Font.PLAIN,(int) spinner.getValue()));
		}
//----------Functionality for the menu items-------------------------------------------
		if(e.getSource() == newItem) {
			
			new TextEditor();
		}
		
		if(e.getSource() == openItem) {
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
			chooser.setFileFilter(filter);
			
			int response = chooser.showOpenDialog(null);
			
			if(response == JFileChooser.APPROVE_OPTION) {
				File file = new File(chooser.getSelectedFile().getAbsolutePath());
				directory = file;
				try {
					textArea.setText("");
					FileReader reader = new FileReader(file);
					int data = reader.read();
					
					while(data != -1) {
						textArea.append(Character.toString((char)data));
						data = reader.read();
					}
					textArea.setFont(new Font((String)fontBox.getSelectedItem(),Font.PLAIN,(int)spinner.getValue()));
					reader.close();
					saved = true;
					isSaved();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		
		if(e.getSource() == saveItem) {
			if(textArea.getText().isEmpty()) {
				JOptionPane.showMessageDialog(null, "Nothing to save !");
			} else if(directory != null) {
				try {
					FileWriter writer = new FileWriter(directory);
					writer.write(textArea.getText());
					writer.close();
					saved = true;
					isSaved();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			} else if(directory == null){
				JFileChooser saver = new JFileChooser();
				int response = saver.showOpenDialog(null);
				if(response == JFileChooser.APPROVE_OPTION) {
					File file = new File(saver.getSelectedFile().getAbsolutePath());
					
					try {
						FileWriter writer = new FileWriter(file);
						writer.write(textArea.getText());
						writer.close();
						saved = true;
						directory = file;
						isSaved();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}	
			}
		}
		
		if(e.getSource() == exitItem) {
			if(saved == false) {
				int response = JOptionPane.showConfirmDialog(this, "The document has not yet been saved. Continue ?","Close without saving ?", 0, JOptionPane.WARNING_MESSAGE);
				if(response == JOptionPane.YES_OPTION) {
					this.dispose();
				}
			} else {
				this.dispose();
			}		}
	}
	
	public void isSaved() {
		if(directory != null) {
			if(saved) {
				this.setTitle(("TextEditor : " + directory.getName()));
			}else if(!saved) {
				if(!this.getTitle().contains("**")) {
					this.setTitle("TextEditor : " + directory.getName() + "**");
				}else {
					return;
				}
			}
		}else if(directory == null) {
			saved = false;
			this.setTitle("TextEditor : Untitled**");
		}
	}

@Override
public void keyPressed(KeyEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void keyReleased(KeyEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void keyTyped(KeyEvent arg0) {
	// TODO Auto-generated method stub
	saved = false;
	isSaved();
	
}

@Override
public void windowActivated(WindowEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void windowClosed(WindowEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void windowClosing(WindowEvent arg0) {
	// TODO Auto-generated method stub
	if(saved == false) {
		int response = JOptionPane.showConfirmDialog(this, "The document has not yet been saved. Continue ?","Close without saving ?", 0, JOptionPane.WARNING_MESSAGE);
		if(response == JOptionPane.YES_OPTION) {
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}
	} else {
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
}

@Override
public void windowDeactivated(WindowEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void windowDeiconified(WindowEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void windowIconified(WindowEvent arg0) {
	// TODO Auto-generated method stub
	
}

@Override
public void windowOpened(WindowEvent arg0) {
	// TODO Auto-generated method stub
	textArea.requestFocus();
}

}
