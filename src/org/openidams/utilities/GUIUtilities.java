package org.openidams.utilities;


import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class GUIUtilities {

	private GUIUtilities(){};
	/**
	 * Centers the component <CODE>c</CODE> on the screen.  
	 * 
	 * @param  c  the component to center
	 * @see  #centerComponent(Component, Component)
	 */
	public static void centerComponent(Component c) {
		centerComponent(c, null);
	}
 
	/**
	 * Centers the component <CODE>c</CODE> on component <CODE>p</CODE>.  
	 * If <CODE>p</CODE> is <CODE>null</CODE>, the component <CODE>c</CODE> 
	 * will be centered on the screen.  
	 * 
	 * @param  c  the component to center
	 * @param  p  the parent component to center on or null for screen
	 * @see  #centerComponent(Component)
	 */
	public static void centerComponent(Component c, Component p) {
		if(c == null) {
			return;
		}
		Dimension d = (p != null ? p.getSize() : 
			Toolkit.getDefaultToolkit().getScreenSize()
		);
		c.setLocation(
			Math.max(0, (d.getSize().width/2)  - (c.getSize().width/2)), 
			Math.max(0, (d.getSize().height/2) - (c.getSize().height/2))
		);
	}
	
	/**
	 * Source code adapted from
	 * http://forum.java.sun.com/thread.jspa?threadID=537012&messageID=2599187
	 * @param component
	 * @param fileName
	 * @throws IOException
	 */	
	public static void createImage(JComponent component, String fileName)
		throws IOException
	{
		Dimension d = component.getSize();

		if (d.width == 0)
		{
			d = component.getPreferredSize();
			component.setSize( d );
		}

		Rectangle region = new Rectangle(0, 0, d.width, d.height);
		
		boolean opaqueValue = component.isOpaque();
		component.setOpaque( true );
		BufferedImage image = new BufferedImage(region.width, region.height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = image.createGraphics();
		g2d.setClip( region );
		component.paint( g2d );
		g2d.dispose();
		component.setOpaque( opaqueValue );

		int offset = fileName.lastIndexOf( "." );
		String type = offset == -1 ? "jpg" : fileName.substring(offset + 1);

		ImageIO.write(image, type, new File( fileName ));
		
	}

	
}
