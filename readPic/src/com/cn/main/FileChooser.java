package com.cn.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.filechooser.FileFilter;



public class FileChooser extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	JButton open = null;
	public static void main(String[] args) {
		new FileChooser();
	}

	public FileChooser() {
		open = new JButton("选择excel");
		
		this.add(open);
		this.setBounds(400, 200, 100, 100);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		open.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setAcceptAllFileFilterUsed(false);
		jfc.addChoosableFileFilter(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (pathname.isDirectory() || pathname.getName().toUpperCase().endsWith(".XLS")
						|| pathname.getName().toUpperCase().endsWith(".XLSX")) {
					return true;
				}
				return false;
			}

			@Override
			public String getDescription() {
				return "Excel(*.xlsx, *.xls)";
			}
		});
		jfc.showDialog(new JLabel(), "选择");
		File file = jfc.getSelectedFile();
		
		if(file == null){
			return;
		}
	}
}