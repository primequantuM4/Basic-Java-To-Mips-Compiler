package main;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class TextArea{
	public static MipsResult converted;
	public TextArea() {
		converted = new MipsResult();
	}
	
	public static void setUpWindowAndFrame() {
		JFrame frame = new JFrame("J2MIPS");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());
        frame.setResizable(false);
        frame.getContentPane().setBackground(Color.DARK_GRAY);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        JTextArea editableTextArea = new JTextArea(30, 30);
        JTextArea nonEditableTextArea = new JTextArea(30, 30);
        nonEditableTextArea.setEditable(false);
        editableTextArea.setBounds(80, 30, 300, 400);
        nonEditableTextArea.setBounds(400, 30, 300, 400);
        
        //Instructions for user
        JLabel java = new JLabel("Java code here");
        java.setBounds(80, 0, 200, 24);
        java.setForeground(Color.orange);
        java.setFont(new Font("Serif", Font.PLAIN, 16));
        java.setHorizontalAlignment(JLabel.CENTER);
        
        JLabel assembly = new JLabel("Converted Assembly code");
        assembly.setBounds(400, 0, 200, 24);
        assembly.setForeground(Color.orange);
        assembly.setFont(new Font("Serif", Font.PLAIN, 16));
        assembly.setHorizontalAlignment(JLabel.CENTER);
        
        
        //J2MIPS text here
        JLabel logoName = new JLabel("J");
        logoName.setBounds(10, 0, 200, 100);
        logoName.setForeground(Color.cyan);
        logoName.setFont(new Font("Serif", Font.PLAIN, 40));
        logoName.setVerticalAlignment(JLabel.CENTER);
        
        JLabel logoName1 = new JLabel("2");
        logoName1.setBounds(10, 60, 200, 100);
        logoName1.setForeground(Color.cyan);
        logoName1.setFont(new Font("Serif", Font.ITALIC, 40));
        logoName1.setVerticalAlignment(JLabel.CENTER);
        
        JLabel logoName2 = new JLabel("M");
        logoName2.setBounds(10, 120, 200, 100);
        logoName2.setForeground(Color.pink);
        logoName2.setFont(new Font("Serif", Font.ITALIC, 40));
        logoName2.setVerticalAlignment(JLabel.CENTER);

        JLabel logoName3 = new JLabel("I");
        logoName3.setBounds(10, 180, 200, 100);
        logoName3.setForeground(Color.pink);
        logoName3.setFont(new Font("Serif", Font.ITALIC, 40));
        logoName3.setVerticalAlignment(JLabel.CENTER);
        
        JLabel logoName4 = new JLabel("P");
        logoName4.setBounds(10, 240, 200, 100);
        logoName4.setForeground(Color.pink);
        logoName4.setFont(new Font("Serif", Font.ITALIC, 40));
        logoName4.setVerticalAlignment(JLabel.CENTER);
        
        JLabel logoName5 = new JLabel("S");
        logoName5.setBounds(10, 300, 200, 100);
        logoName5.setForeground(Color.pink);
        logoName5.setFont(new Font("Serif", Font.ITALIC, 40));
        logoName5.setVerticalAlignment(JLabel.CENTER);
        
        JButton convertButton = new JButton("Convert");
        convertButton.setBackground(Color.GREEN);
        convertButton.setForeground(Color.white);
        convertButton.setBounds(310, 440, 150, 50);
        convertButton.addActionListener((ActionListener) new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	nonEditableTextArea.setText("");
                String inputText = editableTextArea.getText();
                String parsed = "";
				try {
					parsed = new MipsResult().MipResult(inputText);
				} catch (Exception e1) {
					parsed = "ERROR: Look At console for more details";
                	nonEditableTextArea.setForeground(Color.red);
                	nonEditableTextArea.setText(parsed);
					e1.printStackTrace();
				}
                ArrayList<String> Errors = new ArrayList<>();
                Errors.add("Error: Tried to declare two elements in one line \n  or didn't provide enough information for the line \n conversion failed");
                Errors.add("Error: Can't Parse a one liner code, \n build failed");
                Errors.add("Error: Line started without a declaration of data type \n conversion failed");
                Errors.add("ERROR: Look At console for more details");
                if (Errors.contains(parsed)) {
                	nonEditableTextArea.setForeground(Color.red);
                	nonEditableTextArea.setText(parsed);
                }
                else{
                	nonEditableTextArea.setForeground(Color.black);
                	nonEditableTextArea.setText(parsed);
                }
            }
        });
        
        frame.add(logoName);
        frame.add(logoName1);
        frame.add(logoName2);
        frame.add(logoName3);
        frame.add(logoName4);
        frame.add(logoName5);
        frame.add(java);
        frame.add(assembly);
        frame.add(editableTextArea);
        frame.add(nonEditableTextArea);
        frame.add(convertButton);
      
        frame.setVisible(true);

	}
	}
