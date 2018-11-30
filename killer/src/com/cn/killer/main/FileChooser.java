package com.cn.killer.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cn.killer.excel.support.ExcelRead;

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
		calc(file);
	}
	
	private void calc(File file){
		int yh_idx = -1;
		int je_idx = -1;
		int rq_idx = -1;
		int fs_idx = -1;
		int sl_idx = -1;
		int dj_idx = -1;
		
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf =   new SimpleDateFormat("M月d日");
		
		Workbook wb = null;
		OutputStream out = null;
		
		try {
	        
			ExcelRead er = new ExcelRead();
			List<ArrayList<String>> list = er.readExcel(file);
			ArrayList<String> title = list.get(0);
			for (int i = 0; i < title.size(); i++) {
				if("开证银行".equals(title.get(i).trim())){
					yh_idx = i;
					continue;
				}
				if("总金额".equals(title.get(i).trim())){
					je_idx = i;
					continue;
				}
				if("押汇到期日".equals(title.get(i).trim())){
					rq_idx = i;
					continue;
				}
				if("贸易方式".equals(title.get(i).trim())){
					fs_idx = i;
					continue;
				}
				if("数量".equals(title.get(i).trim())){
					sl_idx = i;
					continue;
				}
				if("单价".equals(title.get(i).trim())){
					dj_idx = i;
					continue;
				}
			}
			
			if(yh_idx == -1 || je_idx == -1 
					|| rq_idx == -1 || fs_idx == -1 
					|| sl_idx == -1|| dj_idx ==-1){
				javax.swing.JOptionPane.showMessageDialog(null, "未找到关键列！");
				return;
			}
			int max_idx = Math.max(Math.max(Math.max(Math.max(yh_idx, rq_idx),fs_idx),sl_idx),dj_idx);
			List<ArrayList<String>> new_list = new ArrayList<ArrayList<String>>();
			
			for (int i = 1; i < list.size(); i++) {
				List<String> cont = list.get(i);
				
				String yh = "";
				String rq = "";
				String fs = "";
				String sl = "";
				String dj = "";
				
				if(max_idx >= cont.size()){
					continue;
				}
				yh = cont.get(yh_idx).trim();
				rq = cont.get(rq_idx).trim();
				fs = cont.get(fs_idx).trim();
				sl = cont.get(sl_idx).trim();
				dj = cont.get(dj_idx).trim();
				
				if("".equals(yh) || "".equals(sl) || "".equals(dj) 
						|| "".equals(rq) || "".equals(fs)){
					continue;
				}
				
				BigDecimal je = new BigDecimal(sl).multiply(new BigDecimal(dj));
				
				c.set(Calendar.YEAR, 1900);
				c.set(Calendar.MONTH, 0);
				c.set(Calendar.DAY_OF_MONTH, 1);
				c.add(Calendar.DAY_OF_MONTH, Integer.parseInt(rq)-2);
				
				rq = sdf.format(c.getTime());
				boolean flag = false;
				for (int j = 0; j < new_list.size(); j++) {
					if(yh.equals(new_list.get(j).get(0))
							&& rq.equals(new_list.get(j).get(1))){
						if("一般贸易OK".equals(fs)){
							new_list.get(j).set(2, String.valueOf(new BigDecimal(new_list.get(j).get(2)).add(je).doubleValue()));
						} else if("转口贸易T".equals(fs)){
							new_list.get(j).set(3, String.valueOf(new BigDecimal(new_list.get(j).get(3)).add(je).doubleValue()));
						} else {
							new_list.get(j).set(4, String.valueOf(new BigDecimal(new_list.get(j).get(4)).add(je).doubleValue()));
						}
						new_list.get(j).set(5, String.valueOf(new BigDecimal(new_list.get(j).get(5)).add(je).doubleValue()));
						flag = true;
						break;
					}
				}
				
				if(!flag){
					ArrayList<String> new_data = new ArrayList<String>();
					new_data.add(0, yh);
					new_data.add(1, rq);
					if("一般贸易OK".equals(fs)){
						new_data.add(2, String.valueOf(je.doubleValue()));
						new_data.add(3, "0");
						new_data.add(4, "0");
					} else if("转口贸易T".equals(fs)){
						new_data.add(2, "0");
						new_data.add(3, String.valueOf(je.doubleValue()));
						new_data.add(4, "0");
					} else {
						new_data.add(2, "0");
						new_data.add(3, "0");
						new_data.add(4, String.valueOf(je.doubleValue()));
					}
					new_data.add(5, String.valueOf(je.doubleValue()));
					new_list.add(new_data);
				}
			}
			
			if (file.getName().endsWith("xls")) {
                wb = new HSSFWorkbook();
                
            } else if(file.getName().endsWith("xlsx")) {
                wb = new XSSFWorkbook();
            }
			
			Sheet sheet = (Sheet) wb.createSheet("报表");
			
			CellStyle titleStyle = wb.createCellStyle(); 
			titleStyle.setAlignment(HSSFCellStyle.VERTICAL_CENTER); 
			titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); 
			Font titlefont = wb.createFont();
			titlefont.setFontName("黑体");
			titlefont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			titleStyle.setFont(titlefont);
			
			CellStyle contStyle = wb.createCellStyle(); 
			contStyle.setAlignment(HSSFCellStyle.VERTICAL_CENTER); 
			contStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); 
			Font contfont = wb.createFont();
			contfont.setFontName("黑体");
			contfont.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
			contStyle.setFont(contfont);
			
			Row row = sheet.createRow((short) 0);
			Row row1 = sheet.createRow((short) 1);
			
			sheet.addMergedRegion(new CellRangeAddress(0, 1, 0, 0));
			Cell cell = row.createCell((short)0);
			cell.setCellValue("银行");
			cell.setCellStyle(titleStyle);
			
			sheet.addMergedRegion(new CellRangeAddress(0, 1, 1, 1));
			cell = row.createCell((short)1);
			cell.setCellValue("日期");
			cell.setCellStyle(titleStyle);
			
			sheet.addMergedRegion(new CellRangeAddress(0, 0, 2, 4));
			cell = row.createCell((short)2);
			cell.setCellValue("贸易方式");
			cell.setCellStyle(titleStyle);
			
			cell = row1.createCell((short)2);
			cell.setCellValue("一般");
			cell.setCellStyle(titleStyle);
			
			cell = row1.createCell((short)3);
			cell.setCellValue("转口");
			cell.setCellStyle(titleStyle);
			
			cell = row1.createCell((short)4);
			cell.setCellValue("未定");
			cell.setCellStyle(titleStyle);
			
			sheet.addMergedRegion(new CellRangeAddress(0, 1, 5, 5));
			cell = row.createCell((short)5);
			cell.setCellValue("总额");
			cell.setCellStyle(titleStyle);
			
			int rowindex = 2;
			for (int i = 0; i < new_list.size(); i++) {
				Row row_ctx = sheet.createRow((short) rowindex++);
				cell = row_ctx.createCell((short)0);
				cell.setCellValue(new_list.get(i).get(0));
				cell.setCellStyle(contStyle);
				
				cell = row_ctx.createCell((short)1);
				cell.setCellValue(new_list.get(i).get(1));
				cell.setCellStyle(contStyle);
				
				cell = row_ctx.createCell((short)2);
				cell.setCellValue(new_list.get(i).get(2));
				cell.setCellStyle(contStyle);
				
				cell = row_ctx.createCell((short)3);
				cell.setCellValue(new_list.get(i).get(3));
				cell.setCellStyle(contStyle);
				
				cell = row_ctx.createCell((short)4);
				cell.setCellValue(new_list.get(i).get(4));
				cell.setCellStyle(contStyle);
				
				cell = row_ctx.createCell((short)5);
				cell.setCellValue(new_list.get(i).get(5));
				cell.setCellStyle(contStyle);
			}
			
			 // 构建指定文件
			File newFile = new File(file.getAbsolutePath().replace(".xls", "_new.xls"));
			
			// 根据文件创建文件的输出流            
			out = new FileOutputStream(newFile);         
			wb.write(out);
			JOptionPane.showMessageDialog(this, "执行完毕");
		} catch (Exception e) {             
			e.printStackTrace();   
			JOptionPane.showMessageDialog(this, e.getMessage());
		} finally {
			try {
				out.flush();
				out.close(); 
				wb.close();
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, e.getMessage());
			}
			this.dispose();
		}
	}
}