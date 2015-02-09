package socialeyser.bl.services.impl.sa.modsupport;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import socialeyser.model.Enrichment;
import socialeyser.model.Message;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

@SuppressWarnings("deprecation")
public class ARFFMaker {
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	public String makeARFF_training(Set<Message> trainingSet, String[] classes) throws Exception {
		String excelFile = makeExcel(trainingSet);
		String output = "ARFF_training.arff";
		
		FastVector atts;
		FastVector attValsSentiment;
		Instances data;
		double[] vals;

		// 1. set up attributes
		atts = new FastVector();

		// - string
		atts.addElement(new Attribute("Content", (FastVector) null));

		// - nominal
		attValsSentiment = new FastVector();
		for (String cl : classes) {
			attValsSentiment.addElement(cl);
		}
		atts.addElement(new Attribute("Class", attValsSentiment));

		// 2. create Instances object
		data = new Instances(excelFile.substring(0, excelFile.length()-5), atts, 0);

		// 3. fill with data
		FileInputStream fis = new FileInputStream(excelFile);

		InputStream wrappedStream = POIFSFileSystem.createNonClosingInputStream(fis);
		HSSFWorkbook wb = new HSSFWorkbook(wrappedStream);
		org.apache.poi.ss.usermodel.Sheet ws = wb.getSheetAt(0);

		int rowNum = ws.getLastRowNum() +1;
		System.out.println("Number of rows in Training set = " + rowNum);

		System.out.println("====================");

		for(int i = 0; i < rowNum; i++) {
			System.out.println("Processing row n° "+(i+1)); //TRACE
			Row row = ws.getRow(i);
			vals = new double [data.numAttributes()];
			
			// - string 
			Cell cell = row.getCell(0);

			String value = cell.toString();
			if(value.equals("")){
				vals[0] = data.attribute(0).addStringValue(" ??? none ???");
			}
			else {
				vals[0] = data.attribute(0).addStringValue(value);
			}

			// - nominal 
			Cell cellClass = row.getCell(1);
			vals[1] = attValsSentiment.indexOf(cellClass.toString());
			// add
			data.add(new DenseInstance(1.0, vals));
		}

		// 4. save to file
		System.out.println("Number of instances: " + data.numInstances());
		ArffSaver saver = new ArffSaver();
		saver.setInstances(data);
		saver.setFile(new File(output));
		saver.writeBatch();
		wb.close();
		System.out.println("===== Training set for SENTIMENT in ARFF format created =====");
		
		return output;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String makeARFF_classify(Set<Message> tweets, String[] classes) throws Exception {
		
		String excelFile = makeExcel_class(tweets);
		String output = "ARFF_classify.arff";
		FastVector atts;
		FastVector attValsSentiment;
		Instances data;
		double[] vals;

		// 1. set up attributes
		atts = new FastVector();

		// - string
		atts.addElement(new Attribute("Content", (FastVector) null));

		// - nominal
		attValsSentiment = new FastVector();
		for (String cl : classes) {
			attValsSentiment.addElement(cl);
		}
		atts.addElement(new Attribute("Class", attValsSentiment));

		// 2. create Instances object
		data = new Instances(excelFile.substring(0, excelFile.length()-5), atts, 0);
		data.setClassIndex(1);

		// 3. fill with data

		FileInputStream fis = new FileInputStream(excelFile);

		InputStream wrappedStream = POIFSFileSystem.createNonClosingInputStream(fis);
		HSSFWorkbook wb = new HSSFWorkbook(wrappedStream);
		org.apache.poi.ss.usermodel.Sheet ws = wb.getSheetAt(0);

		int rowNum = ws.getLastRowNum() + 1;

		for(int i = 0; i < rowNum; i++) {
			Row row = ws.getRow(i);
			vals = new double [data.numAttributes()];

			// - string 
			Cell cell = row.getCell(0);
			String value = cell.toString();
			if(value.equals("")){
				vals[0] = data.attribute(0).addStringValue("");
			}
			else
				vals[0] = data.attribute(0).addStringValue(value);

			// add
			data.add(new DenseInstance(1.0, vals));
		}

		// 4. save to file
		ArffSaver saver = new ArffSaver();
		saver.setInstances(data);
		saver.setFile(new File(output));
		saver.writeBatch();
		wb.close();
		System.out.println("===== Data set to classify in ARFF format created =====");
		return output;
	}
	
	private static String makeExcel(Set<Message> tweets) throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("dataSet");
		int rownum = 0;
		String output = "excelFile.xls";
		
		for (Message mess : tweets) {
			Row row = sheet.createRow(rownum++);
			int cellnum = 0;
			
			Enrichment enr = mess.getEnrichment();
			String normText = enr.getNormText();
			String sentiment = enr.getSentiment();
			
			Cell cell = row.createCell(cellnum++);
			cell.setCellValue(normText);
			
			Cell cellSent = row.createCell(cellnum++);
			cellSent.setCellValue(sentiment);
		}
		try {
			FileOutputStream out = new FileOutputStream(new File(output));
			wb.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		wb.close();
		return output;
	}
	
	private static String makeExcel_class(Set<Message> tweets) throws Exception {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("dataSet");
		int rownum = 0;
		String output = "excelFile_classifier.xls";
		for (Message mess : tweets) {
			Enrichment enr = mess.getEnrichment();
			Row row = sheet.createRow(rownum++);
			int cellnum = 0;
			Cell cell = row.createCell(cellnum++);
			cell.setCellValue(enr.getNormText());
		}
		try {
			FileOutputStream out = new FileOutputStream(new File(output));
			wb.write(out);
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		wb.close();
		return output;
	}
}
