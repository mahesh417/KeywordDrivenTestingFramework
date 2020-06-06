package com.qualitestgroup.kdt;


import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Vector;

import au.com.bytecode.opencsv.CSV;
import au.com.bytecode.opencsv.CSVWriteProc;
import au.com.bytecode.opencsv.CSVWriter;

import com.qualitestgroup.kdt.exceptions.*;
import com.qualitestgroup.util.fileio.FileTools;
import com.qualitestgroup.util.fileio.FileTools.FileExt;
import com.qualitestgroup.util.logging.Logger.LogLevel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 * Runs the KDT framework manually.
 * 
 * This application is used to test keywords by executing them one at a time. The resulting keyword
 * sequence can be saved to a CSV file. 
 * @author Matt
 *
 */
public class KDTManual extends JPanel implements ActionListener, WindowListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6933315753293025096L;

	private static JFrame mainFrame;
	
	private LinkedList<String[]> output;
	private Keyword kw;
	
	JTextField txtKW, txtApp, txtName, txtVal;
	JButton btnFind, btnClear, btnRun, btnAdd;
	JTable tblArgs;
	JLabel lblStatus;
	ArgModel args = new ArgModel();
	
	private KDTManual()
	{
		super(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		this.setBorder(new EmptyBorder(5,5,5,5));
		// Create labels
		JLabel lblKW, lblApp, lblArgs, lblName, lblVal;
		lblKW = new JLabel("Keyword:");
		lblApp = new JLabel("Application:");
		lblArgs = new JLabel("Arguments");
		lblName = new JLabel("Name:");
		lblVal = new JLabel("Value:");
		lblStatus = new JLabel("Ready");
		
		
		// Create Text fields		
		txtKW = new JTextField(10);
		
		txtApp = new JTextField(10);
		
		txtName = new JTextField(7);
		txtName.setEnabled(false);
		
		txtVal = new JTextField(7);
		txtVal.setEnabled(false);
		
		
		// Create Buttons
		
		btnClear = new JButton("Clear");
		btnClear.setActionCommand("clear");
		btnClear.addActionListener(this);
		
		btnFind = new JButton("Find");
		btnFind.setSize(btnClear.getSize());
		btnFind.setActionCommand("find");
		btnFind.addActionListener(this);
		
		btnRun = new JButton("Run Keyword");
		btnRun.setEnabled(false);		
		btnRun.setActionCommand("run");
		btnRun.addActionListener(this);
		
		btnAdd = new JButton("Add Argument");
		btnAdd.setEnabled(false);
		btnAdd.setActionCommand("add");
		btnAdd.addActionListener(this);
		
		// Create table
		tblArgs = new JTable(args);
		tblArgs.setPreferredScrollableViewportSize(new Dimension(200,50));
		tblArgs.setFillsViewportHeight(true);
		JScrollPane spArgs = new JScrollPane(tblArgs);
		spArgs.setEnabled(true);
		
		// Assemble panel.
		c.gridx = 0;
		c.gridy = 0;
		this.add(lblKW, c);
		c.gridx++;
		this.add(txtKW, c);
		c.gridx++;
		this.add(btnFind, c);
		
		c.gridx = 0;
		c.gridy++;
		this.add(lblApp, c);
		c.gridx++;
		this.add(txtApp, c);
		c.gridx++;
		this.add(btnClear, c);		

		c.gridx = 0;
		c.gridy++;
		this.add(lblArgs, c);
		
		c.gridy++;
		this.add(lblName, c);
		c.gridx = 1;
		this.add(lblVal, c);
		
		c.gridx = 0;
		c.gridy++;
		this.add(txtName, c);
		c.gridx++;
		this.add(txtVal, c);
		c.gridx++;
		this.add(btnAdd, c);
		
		c.gridx = 2;
		c.gridy += 2;
		this.add(btnRun, c);
		
		c.gridy++;
		this.add(lblStatus, c);
		
		c.gridx = 0;
		c.gridy -= 2;
		c.gridwidth = 2;
		c.gridheight = 2;
		this.add(spArgs, c);
		
		output = new LinkedList<String[]>();
		output.add(KDTConstants.scenColNames);
		
	}
	
	public static void main(String[] args) {
		run();
	}	
	
	public static void run()
	{
		//KDTDriver.ev = new EventCase("KDT", "./reports/");
		// Set up context
		TestContext runContext = TestContext.getContext();
		runContext.setData(new TestContext.Data());
		runContext.setLog(5);
		runContext.log(LogLevel.info,"KDT manual run");
		createWindow();
	}
	
	private static void createWindow()
	{
		// Create main frame
		mainFrame = new JFrame("KDT Manual Mode");		
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		KDTManual panel = new KDTManual();
		panel.setOpaque(true);
		mainFrame.addWindowListener(panel);
		mainFrame.setContentPane(panel);
		mainFrame.pack();
		mainFrame.setVisible(true);		
		
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		String action = e.getActionCommand();
		System.out.println(action);
		if(action.equals("find"))
		{
			if(btnFind.isEnabled())
			{
				try
				{
					btnFind.setEnabled(false);
					kw = Keyword.find2(txtKW.getText(), txtApp.getText());
					txtKW.setEnabled(false);
					txtApp.setEnabled(false);
					txtName.setEnabled(true);
					txtVal.setEnabled(true);
					btnRun.setEnabled(true);
					btnAdd.setEnabled(true);
				}
				catch(KDTParseException ex)
				{
					JOptionPane.showMessageDialog(mainFrame, "Keyword not found.");
					btnFind.setEnabled(true);
				}
			}
		}
		else if(action.equals("clear"))
		{
			clear(0);
		}
		else if(action.equals("add"))
		{
			if(btnAdd.isEnabled())
			{
				//int row = tblArgs.getRowCount();
				String argName = txtName.getText();
				String argValue = txtVal.getText();
				args.addArg(argName, argValue);				
				txtName.setText("");
				txtVal.setText("");
			}
		}
		else if(action.equals("run"))
		{
			if(btnRun.isEnabled())
			{
				lblStatus.setText("Running...");
				this.repaint();
				try{
					for(Vector<Object> vec : args)
					{
						kw.addArg(vec.get(0).toString(), vec.get(1).toString());
					}
					kw.run();
					// success
					output.add(new String[]{"MM_result", txtKW.getText(), txtApp.getText(), "", ""});
					for(Vector<Object> vec : args)
					{
						output.add(new String[]{"", "", "", vec.get(0).toString(), vec.get(1).toString()});
					}
					clear(1);
				}
				catch(KDTException ex)
				{
					// failure
					JOptionPane.showMessageDialog(mainFrame, "Caught Exception: " + ex.getMessage());
					clear(2);
				}
			}
			
		}
	}
	
	private void clear(int clear)
	{
		kw = null;		
		lblStatus.setText("Ready");
		txtKW.setEnabled(true);
		txtApp.setEnabled(true);		
		txtName.setEnabled(false);		
		txtVal.setEnabled(false);		
		btnFind.setEnabled(true);
		btnRun.setEnabled(false);
		btnAdd.setEnabled(false);		
		if(clear < 2)
		{		
			txtKW.setText("");			
			txtName.setText("");
			txtVal.setText("");
			args.clear();	
			if(clear < 1)
			{
				txtApp.setText("");
			}
		}
		
	}
	
	@Override
	public void windowClosing(WindowEvent e)
	{
		System.out.println("closing");
		TestContext.getContext().cleanup();
		//KDTDriver.ev.closeLoggers();
		saveFile();
	}
	
	private void saveFile()
	{
		JFileChooser FC = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV files", "csv");
		FC.setFileFilter(filter);
		int result = FC.showSaveDialog(null);
		if(result == JFileChooser.APPROVE_OPTION)
		{
			String fileName = FC.getSelectedFile().getAbsolutePath();
			if(FileTools.getFileExt(fileName) != FileExt.CSV)
			{
				fileName += ".csv";
			}
			CSV csv = CSV.create();
			csv.write(fileName, new CSVWriteProc() {
                @Override
				public void process(CSVWriter out) {
                    out.writeAll(output);
                }
			});
			
		}
	}
	
	private class ArgModel extends DefaultTableModel implements Iterable<Vector<Object>>
	{		
		/**
		 * 
		 */
		private static final long serialVersionUID = 8210587916235456868L;

		public ArgModel()
		{
			super();
			this.addColumn("Name");
			this.addColumn("Value");
		}
		
		public void clear()
		{
			dataVector.clear();
			fireTableDataChanged();
		}


		public void addArg(String argName, String argValue)
		{
			this.addRow(new String[]{argName, argValue});
		}

		@Override
		public Iterator<Vector<Object>> iterator() {
			// TODO Auto-generated method stub
			return null;
		}

		
		
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0){}

}
