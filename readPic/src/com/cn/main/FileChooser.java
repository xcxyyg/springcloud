package com.cn.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Date;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import com.cn.baidu.api.ReadWords;
import com.cn.ocr.OCRUtil;



public class FileChooser extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	
	JButton open1 = null;
	JButton open2 = null;
	JButton open3 = null;
	JButton open4 = null;
	public static void main(String[] args) {
		new FileChooser();
	}

	public FileChooser() {
		this.setTitle("获取图片文字");
		JPanel panel = new JPanel();
		open1 = new JButton("选择印刷字体图片");
		open2 = new JButton("选择手写字体图片");
		open3 = new JButton("选择网络背景图片");
		open4 = new JButton("选择印刷字体图片（离线）");
		
		panel.add(open1);
		panel.add(open2);
		panel.add(open3);
//		panel.add(open4);
		this.setBounds(400, 200, 220, 140);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		open1.addActionListener(this);
		open2.addActionListener(this);
		open3.addActionListener(this);
//		open4.addActionListener(this);
		
		this.setContentPane(panel);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JFileChooser jfc = new JFileChooser();
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setAcceptAllFileFilterUsed(false);
		jfc.addChoosableFileFilter(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if (pathname.isDirectory() || ".JPG,.PNG".indexOf(pathname.getName().toUpperCase().substring(pathname.getName().indexOf(".")))>-1) {
					return true;
				}
				return false;
			}

			@Override
			public String getDescription() {
				return "pic(*.jpg, *.png)";
			}
		});
		jfc.showDialog(new JLabel(), "选择");
		File file = jfc.getSelectedFile();
		
		if(file == null){
			return;
		}
		
		String words = "";
		ReadWords read = new ReadWords();
		if("选择印刷字体图片".equals(e.getActionCommand())) {
			Map<String,String> ret = read.basicAccurateGeneral(file.getAbsolutePath());
			if (!"0000".equals(ret.get("error_code"))) {
				JOptionPane.showMessageDialog(this, ret.get("error_msg"));
				return;
			}
			words = ret.get("data");
		} else if("选择手写字体图片".equals(e.getActionCommand())) {
			Map<String,String> ret = read.handWriting(file.getAbsolutePath());
			if (!"0000".equals(ret.get("error_code"))) {
				JOptionPane.showMessageDialog(this, ret.get("error_msg"));
				return;
			}
			words = ret.get("data");
		} else if("选择网络背景图片".equals(e.getActionCommand())) {
			Map<String,String> ret = read.webImage(file.getAbsolutePath());
			if (!"0000".equals(ret.get("error_code"))) {
				JOptionPane.showMessageDialog(this, ret.get("error_msg"));
				return;
			}
			words = ret.get("data");
		} else if("选择印刷字体图片（离线）".equals(e.getActionCommand())) {
			OCRUtil ocr = new OCRUtil();
			try {
				words = ocr.recognizeText(file);
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage());
				e1.printStackTrace();
				return;
			}
			
		}
		
		String filePath = file.getAbsolutePath().substring(0, file.getAbsolutePath().indexOf("."))+"_"+(new Date().getTime())+".txt";
		this.contentToTxt(filePath, words);
		
		JOptionPane.showMessageDialog(this, "生成文件路径："+filePath);
	}
	
	 public void contentToTxt(String filePath, String content) {
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filePath),true));
            writer.write("\n"+content);
            writer.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}