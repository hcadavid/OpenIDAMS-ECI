package org.openidams.supervisor.gui;

import java.awt.*;

import javax.swing.*;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyledDocument;

import org.openidams.script.compiler.Factory;

import java.awt.event.KeyEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Hashtable;

public class View extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	JTabbedPane tabbedPane = new JTabbedPane();
	JPanel resultsPanel = new JPanel();
	BorderLayout borderLayout2 = new BorderLayout();
	JButton resultClearButton = new JButton();
	JTextArea resultsTextArea = StandardOutputTextArea.getInstance();
	JPanel eventsPanel = new JPanel();
	BorderLayout borderLayout3 = new BorderLayout();
	EventsTextArea eventsTextArea = null;
	JButton eventsClearButton = new JButton();
	JSplitPane jSplitPane1 = new JSplitPane();
	JScrollPane textPaneSP = new JScrollPane();
	BorderLayout borderLayout1 = new BorderLayout();
	JScrollPane eventsSP = new JScrollPane();
	JScrollPane resultsTA = new JScrollPane();
	
	
	/**
	 * View Controller
	 */
	private Controller controller = null;
	
	/**
	 * View Model
	 */
	@SuppressWarnings("unused")
	private Model model = null;
	
	/**
	 * Text edition area
	 */
	private JTextPane textPane = new JTextPane();
	
	/**
	 * Text style edition
	 */
	@SuppressWarnings("unused")
	private StyledDocument document = new DefaultStyledDocument();
	
	/**
	 * File Chooser
	 */
	private JFileChooser fileChooser = new JFileChooser();
	
	/**
	 * View original title
	 */
	private String caption=null;
	
	
	
	/**
	 * Instantiates a view with its respective controller and model
	 * @param text View title
	 * @param pController View Controller
	 * @param pModel View Model
	 */
	private View(String text, Controller pController, Model pModel) {
		// Graphic script
		super(text);		
		
		caption = text;
		
		// Attribute setting
		controller = pController;
		model = pModel;
		pController.setView(this);
		pController.setModel(pModel);
	 	pModel.setView(this);
		pModel.setController(pController);
		
		// Environment script
		try {
			setUpEnvironment();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// Set up the window.
		//init();
	}
	
	
		
    /**
     * View builder
     * @param text View title (caption)
     * @param pController View controller
     * @param pModel View Model
     */
    public static void create(String text, Controller pController, Model pModel) {
    	View frame = new View(text, pController, pModel);
    	
		SplashWindow sw=new SplashWindow(View.class.getResource("launchlogo.jpg").getPath().replaceAll("%20"," "),frame,3000);
		sw.setSize(500,400);
    	sw.setVisible(true);
		JFrame.setDefaultLookAndFeelDecorated(true);    	
    	frame.setSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize().width,Toolkit.getDefaultToolkit().getScreenSize().height-30));
    	frame.setVisible(true);
    }

	
	/**
	 * Initializes main frame components
	 * @throws Exception
	 */
    private void setUpEnvironment() throws Exception {
		
		this.setIconImage(new ImageIcon(View.class.getResource("idamsicon.jpg").getPath().replaceAll("%20"," ")).getImage());
		
		getContentPane().setLayout(borderLayout1);
		resultsPanel.setLayout(borderLayout2);
		resultClearButton.setText("clear");
		resultClearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resultClearButton_actionPerformed(e);
			}
		});
		resultsTextArea.setText("");
		eventsPanel.setLayout(borderLayout3);
		eventsClearButton.setText("clear");
		eventsClearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				eventsClearButton_actionPerformed(e);
			}
		});
		resultsTA.getViewport().add(resultsTextArea);
		jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		
		
		jSplitPane1.add(textPaneSP, JSplitPane.TOP);
		
		
		textPane.setFont( new Font( "Monospaced", Font.BOLD, 16 ));
		textPaneSP.getViewport().add(textPane);
		tabbedPane.add(eventsPanel, "Events");
		tabbedPane.add(resultsPanel, "Results(STDOUT)");
		
		eventsPanel.add(eventsClearButton, java.awt.BorderLayout.WEST);
		
		
		/*
		 * Configure events output mechanism
		 */
		eventsTextArea=new EventsTextArea();
		ProcessLogger.getInstance().addFilter(eventsTextArea);
		
		
		
		
		
		eventsSP.getViewport().add(eventsTextArea);
		
		
		
		jSplitPane1.add(tabbedPane, JSplitPane.BOTTOM);
		this.getContentPane().add(jSplitPane1);
		resultsPanel.add(resultsTA, java.awt.BorderLayout.EAST);
		resultsPanel.add(resultsTA, java.awt.BorderLayout.CENTER);
		resultsPanel.add(resultClearButton, java.awt.BorderLayout.WEST);
		eventsPanel.add(eventsSP, java.awt.BorderLayout.CENTER);
		tabbedPane.setSelectedIndex(0);
		
		this.validate();
		
		jSplitPane1.addComponentListener( new ComponentAdapter() {
			public void componentResized( ComponentEvent event ) {
				jSplitPane1.setDividerLocation( 0.8 );
			}
		} );
		
		eventsTextArea.setText("EVENTS:"+"\n");
		resultsTextArea.setText("RESULTS:"+"\n");
		
		this.setJMenuBar(setUpMenu());
		
		
        for (int i = 0; i < Factory.getInstance().getCompilers(); i++) {
            fileChooser.addChoosableFileFilter(new ScriptFilter(
                    Factory.getInstance().getName(i),
                    Factory.getInstance().getExtension(i)));
        }

        fileChooser.setAcceptAllFileFilterUsed(false);
        
        eventsTextArea.setEditable(false);
        resultsTextArea.setEditable(false);
		
	}
	
	
	/**
	 * Initializes the views attributes and behavior
	 */
	@SuppressWarnings("unused")
	private void init() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent e) {
				textPane.requestFocus();
			}
		});
		setVisible(true);
	}
	
	
	/*public static void main(String[] args) {
		View view = new View();
		view.setSize(640,480);
		view.setVisible(true);
	}*/
	
	/**
	 * Creates and returns the 's bar menu
	 * @return 's bar menu
	 */
	private JMenuBar setUpMenu() {
		JMenuBar menuBar = new JMenuBar();
		
		// Set up the lone menu.
		JMenu menu0 = new JMenu("File");
		JMenu menu1 = new JMenu("Edit");
		JMenu menu2 = new JMenu("Script");
		JMenu menu3 = new JMenu("Help");		
		JMenu menu4 = new JMenu("View");	
		
		menu0.setMnemonic(KeyEvent.VK_F);
		menu1.setMnemonic(KeyEvent.VK_E);
		menu2.setMnemonic(KeyEvent.VK_S);
		menu3.setMnemonic(KeyEvent.VK_H);
		menu4.setMnemonic(KeyEvent.VK_V);
		
		menuBar.add(menu0);
		menuBar.add(menu1);
		menuBar.add(menu2);
		menuBar.add(menu4);
		menuBar.add(menu3);

		// Set up the new menu item.
		JMenuItem menuItem = new JMenuItem(Controller.NEW);
		menuItem.setMnemonic(KeyEvent.VK_N);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				ActionEvent.ALT_MASK));
		menuItem.setActionCommand(Controller.NEW);
		menuItem.addActionListener(this);
		menu0.add(menuItem);
		
		// Set up the Load menu item.
		menuItem = new JMenuItem(Controller.OPEN);
		menuItem.setMnemonic(KeyEvent.VK_P);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				ActionEvent.ALT_MASK));
		menuItem.setActionCommand(Controller.OPEN);
		menuItem.addActionListener(this);
		menu0.add(menuItem);
		
		// Set up the Save menu item.
		menuItem = new JMenuItem(Controller.SAVE);
		menuItem.setMnemonic(KeyEvent.VK_S);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				ActionEvent.ALT_MASK));
		menuItem.setActionCommand(Controller.SAVE);
		menuItem.addActionListener(this);
		menu0.add(menuItem);
		
		// Set up the Save As menu item.
		menuItem = new JMenuItem(Controller.SAVE_AS);
		menuItem.setMnemonic(KeyEvent.VK_A);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.ALT_MASK));
		menuItem.setActionCommand(Controller.SAVE_AS);
		menuItem.addActionListener(this);
		menu0.add(menuItem);
		
		// Set up the second menu item.
		menuItem = new JMenuItem(Controller.QUIT);
		menuItem.setMnemonic(KeyEvent.VK_Q);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				ActionEvent.ALT_MASK));
		menuItem.setActionCommand(Controller.QUIT);
		menuItem.addActionListener(this);
		menu0.add(menuItem);
		

		

		//SETUP CUT, COPY AND PASTE FUNCTIONALITIES
		Action actions[] = textPane.getActions();

	    Action cutAction = TextUtilities.findAction(actions,
	        DefaultEditorKit.cutAction);
	    Action copyAction = TextUtilities.findAction(actions,
	        DefaultEditorKit.copyAction);
	    Action pasteAction = TextUtilities.findAction(actions,
	        DefaultEditorKit.pasteAction);
	    
	    		
		// Set up the Copy menu item.
		menuItem = new JMenuItem(copyAction);
		menuItem.setText(Controller.COPY);
		menuItem.setMnemonic(KeyEvent.VK_COPY);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_COPY,
				ActionEvent.CTRL_MASK));
		menuItem.setActionCommand(Controller.COPY);
		menu1.add(menuItem);
		
		// Set up the Cut menu item.
		menuItem = new JMenuItem(cutAction);
		menuItem.setText(Controller.CUT);
		menuItem.setMnemonic(KeyEvent.VK_CUT);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_CUT,
				ActionEvent.CTRL_MASK));
		menuItem.setActionCommand(Controller.CUT);
		menu1.add(menuItem);
		
		// Set up the Paste menu item.
		menuItem = new JMenuItem(pasteAction);
		menuItem.setText(Controller.PASTE);
		menuItem.setMnemonic(KeyEvent.VK_PASTE);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PASTE,
				ActionEvent.CTRL_MASK));
		menuItem.setActionCommand(Controller.PASTE);
		menu1.add(menuItem);
		
		// Set up the Compile menu item.
		menuItem = new JMenuItem(Controller.COMPILE);
		menuItem.setMnemonic(KeyEvent.VK_M);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M,
				ActionEvent.CTRL_MASK));
		menuItem.setActionCommand(Controller.COMPILE);
		menuItem.addActionListener(this);
		menu2.add(menuItem);
		
		// Set up the Run menu item.
		menuItem = new JMenuItem(Controller.RUN);
		menuItem.setMnemonic(KeyEvent.VK_R);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R,
				ActionEvent.CTRL_MASK));
		menuItem.setActionCommand(Controller.RUN);
		menuItem.addActionListener(this);
		menu2.add(menuItem);
		
		// Set up the About menu item.
		menuItem = new JMenuItem(Controller.ABOUT);
		menuItem.setMnemonic(KeyEvent.VK_A);
		menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A,
				ActionEvent.ALT_MASK));
		menuItem.setActionCommand(Controller.ABOUT);
		menuItem.addActionListener(this);
		menu3.add(menuItem);
		
		
		//Set up view menu
		menuItem = new JMenuItem(Controller.VIEWM);
		menuItem.setActionCommand(Controller.VIEWM);
		menuItem.addActionListener(this);
		menu4.add(menuItem);
		menuItem = new JMenuItem(Controller.VIEWPROCS);		
		menu4.add(menuItem);
		menuItem.addActionListener(this);
		
				
		return menuBar;
	}
	
	
	/**
	 * Returns the text contained in the view
	 * @return String text
	 */
	public String getText() {
		return textPane.getText();
	}
	
	/**
	 * Sets the text contained in the view
	 * @param s text to be setted
	 */
	public void setText(String s) {
		textPane.setText(s);
	}
	
	/*
	 * Show supervisor's credits
	 */
	public void showCredits(){
		AboutDialog ad=new AboutDialog("OpenIDAMS 0.07");
		ad.setSize(420,370);
		ad.setResizable(false);
		Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
		ad.setLocation((d.width-ad.getWidth())/2, (d.height-ad.getHeight())/2);
		ad.setVisible(true);
	}
	
	/**
	 * Gets the Filename to be opened
	 * @return Filename to be opened
	 */
	public String openFile() {
		String selected;
		String name;
		String filename;
		
		filename = null;
		
		if (fileChooser.showOpenDialog(View.this) == JFileChooser.APPROVE_OPTION) {
			name = fileChooser.getSelectedFile().getName();
			selected = fileChooser.getSelectedFile().getAbsolutePath();
			
			if (!selected.endsWith(ScriptFilter.getLastExtension())) {
				selected = selected + ScriptFilter.getLastExtension();
			}
			
			if ((new File(selected)).exists()) {
				filename = selected;
				setTitle(caption + " - " + name);
			}
		}
		
		return filename;
	}
	
	/**
	 * Gets the filename of the file to be saved
	 * @return Filename of the file to be saved
	 */
	public String saveFile() {
		String selected;
		String name;
		String filename;
		
		filename = null;
		
		if (fileChooser.showSaveDialog(View.this) == JFileChooser.APPROVE_OPTION) {
			name = fileChooser.getSelectedFile().getName();
			selected = fileChooser.getSelectedFile().getAbsolutePath();
			
			if (!selected.endsWith(ScriptFilter.getLastExtension())) {
				selected = selected + ScriptFilter.getLastExtension();
			}
			
			if (!(new File(selected)).exists() ||
					(JOptionPane.showConfirmDialog(null,
							"Do you want to replace " + name + "?", "Save File As",
							JOptionPane.YES_NO_OPTION) == 0)) {
				filename = selected;
				setTitle(caption + "-" + name);
			}
		}
		
		return filename;
	}
	
	/**
	 * Modifies the title of the window with the new file title
	 */
	public void newFile() {
		setTitle(caption);
	}
	
	/**
	 * Shows the desired text usidn a message dialog window
	 * @param text Text to be showed
	 */
	public void showMessage(String text) {
		//JOptionPane.showMessageDialog(null, text, "Message", JOptionPane.INFORMATION_MESSAGE);
		ProcessLogger.getInstance().reportEvent(text);
	}
	
	
    /**
     * Event listner
     * @param arg0 DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent arg0) {
        // TODO implement with Observer/MVC pattern
        controller.actionPerformed(arg0.getActionCommand());
    }

    
	public void eventsClearButton_actionPerformed(ActionEvent e) {
		eventsTextArea.setText("EVENTS:"+"\n");
	}
	
	public void resultClearButton_actionPerformed(ActionEvent e) {
		resultsTextArea.setText("RESULTS:"+"\n");
	}
	
	
}


class TextUtilities {
  private TextUtilities() {
  }

  public static Action findAction(Action actions[], String key) {
    Hashtable<Object,Action> commands = new Hashtable<Object,Action>();
    for (int i = 0; i < actions.length; i++) {
      Action action = actions[i];
      commands.put(action.getValue(Action.NAME), action);
    }
    return (Action) commands.get(key);
  }
}


