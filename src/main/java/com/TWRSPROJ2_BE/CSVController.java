package com.TWRSPROJ2_BE;

import java.io.File;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class CSVController {

	// private static final DecimalFormat df = new
	// DecimalFormat(Double.parseDouble("00.0000"));

	@GetMapping(path = "/hello-world")
	public String helloWorld() {
		return "Hello world";
	}

	@GetMapping(path = "/getCsv")
	public CSVDataBean getCsvData() throws IOException, IOException {
		CSVDataBean csvDataBean = new CSVDataBean();

		List<List<String>> records = new ArrayList<>();
		// Function to read CSV file from local
		try (BufferedReader br = new BufferedReader(
				// F:\DHANASHRI\Tracking\test1 21 Dec\HUMA WALKING straigh -8m
				// new FileReader("F:/DHANASHRI/Tracking/test1 21 Dec/HUMA WALKING straigh
				// -8m/File001.csv"))) {
				new FileReader("F:/DHANASHRI/Tracking/DATASETS/PROJECT2 DATASET/T_2_MOVING(1,3)(1,1)_VERTICAL_DIF_sPED/File002.csv"))) {
			String line = new String();

			while ((line = br.readLine()) != null) {
				// To remove unwanted data from CSV file
				if (!line.startsWith("!") && !line.contains("END") && !line.contains("BEGIN")) {
					String[] values = line.split(",");
					records.add(Arrays.asList(values));
				}
			}

			// created list according to the data types of Freq., Real and imaginary
			List<Long> freq9 = new ArrayList();
			List<Float> real9 = new ArrayList();
			List<Float> img9 = new ArrayList();
			List<Double> mag9 = new ArrayList();
			List<Double> fftMag9 = new ArrayList();

			for (List<String> list : records) {

				// Converted list of freq into Integer
				String freq1 = list.get(0);
				Long i = Long.parseLong(freq1);
				freq9.add(i);

				// Converted list of real into Integer
				String real1 = list.get(1);
				Float j = Float.valueOf(real1);
				real9.add(j);

				// Converted list of imaginary into Integer
				String img1 = list.get(2);
				Float k = Float.valueOf(img1);
				img9.add(k);

				Double magnitude = Math
						.sqrt(Float.valueOf(real1) * Float.valueOf(real1) + Float.valueOf(img1) * Float.valueOf(img1));
				mag9.add(magnitude);

			}

			Complex[] y = new Complex[freq9.size()];
			for (int z = 0; z < freq9.size(); z++) {

				Complex wk = new Complex(real9.get(z), img9.get(z));
				y[z] = wk;
			}
			int n = y.length;
			Complex[] result = FFT.dft(y);
			for (int p = 0; p < 201; p++) {
				fftMag9.add(result[p].abs());
			}
			csvDataBean.setFreq(freq9);
			csvDataBean.setReal(real9);
			csvDataBean.setImg(img9);
			csvDataBean.setMagnitude(mag9);
			csvDataBean.setFftMagnitude(fftMag9);

		}
		return csvDataBean;
	}

	@GetMapping(path = "/getMultipleCsv")
	public List<CSVDataBean> getMultipleCsvData() throws IOException, IOException {

		List<CSVDataBean> csvDataBeanList = new ArrayList();

		// String for source directory
		String srcDir = "F:/DHANASHRI/Tracking/DATASETS/PROJECT2 DATASET/T_2_MOVING(1,3)(1,1)_VERTICAL_DIF_sPED/";
		File folder = new File(srcDir);
		double[][] BScan = new double[32][201];
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {

			if (file.isFile()) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				{
					List<List<String>> records = new ArrayList<>();
					String line = new String();
					while ((line = br.readLine()) != null) {
						// To remove unwanted data from CSV file
						if (!line.startsWith("!") && !line.contains("END") && !line.contains("BEGIN")) {
							String[] values = line.split(",");
							records.add(Arrays.asList(values));
						}
					}

					List<Long> freq9 = new ArrayList();
					List<Float> real9 = new ArrayList();
					List<Float> img9 = new ArrayList();
					List<Double> mag9 = new ArrayList();
					List<Double> fftMag9 = new ArrayList();

					for (List<String> list : records) {

						// Converted list of freq into Integer
						String freq1 = list.get(0);
						Long i = Long.parseLong(freq1);
						freq9.add(i);

						// Converted list of real into Integer
						String real1 = list.get(1);
						Float j = Float.valueOf(real1);
						real9.add(j);

						// Converted list of imaginary into Integer
						String img1 = list.get(2);
						Float k = Float.valueOf(img1);
						img9.add(k);

						Double magnitude = Math.sqrt(Float.valueOf(real1) * Float.valueOf(real1)
								+ Float.valueOf(img1) * Float.valueOf(img1));
						mag9.add(magnitude);

					}

					Complex[] y = new Complex[freq9.size()];
					for (int z = 0; z < freq9.size(); z++) {

						Complex wk = new Complex(real9.get(z), img9.get(z));
						y[z] = wk;
					}
					int n = y.length;

					Complex[] result = FFT.dft(y);
					for (int p = 0; p < freq9.size(); p++) {
						fftMag9.add(result[p].abs());
					}
					CSVDataBean csvDataBean = new CSVDataBean();
					csvDataBean.setFreq(freq9);
					csvDataBean.setReal(real9);
					csvDataBean.setImg(img9);
					csvDataBean.setMagnitude(mag9);
					csvDataBean.setFftMagnitude(fftMag9);
					csvDataBeanList.add(csvDataBean);

				}

			}
		}

		return csvDataBeanList;

	}

	@GetMapping(path = "/getBScan")
	public double[][] getBScan() throws IOException, IOException {

		List<CSVDataBean> csvDataBeanList = new ArrayList();

		// String for source directory
		String srcDir = "F:/DHANASHRI/Tracking/DATASETS/PROJECT2 DATASET/T_2_MOVING(1,3)(1,1)_VERTICAL_DIF_sPED/";
		File folder = new File(srcDir);

		// Matrix of A-Sca stacking
		double[][] BScan = new double[32][201];

		// Matrix to Image on UI panel i.e. Transpose of BScan Matrix
		double[][] transpose = new double[201][32];
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {

			if (file.isFile()) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				{
					List<List<String>> records = new ArrayList<>();
					String line = new String();
					while ((line = br.readLine()) != null) {
						// To remove unwanted data from CSV file
						if (!line.startsWith("!") && !line.contains("END") && !line.contains("BEGIN")) {
							String[] values = line.split(",");
							records.add(Arrays.asList(values));
						}
					}

					List<Long> freq9 = new ArrayList();
					List<Float> real9 = new ArrayList();
					List<Float> img9 = new ArrayList();
					List<Double> mag9 = new ArrayList();
					List<Double> fftMag9 = new ArrayList();
					List<Double> meanArray = new ArrayList();
					Double dummy;
					for (List<String> list : records) {

						// Converted list of freq into Integer
						String freq1 = list.get(0);
						Long i = Long.parseLong(freq1);
						freq9.add(i);

						// Converted list of real into Integer
						String real1 = list.get(1);
						Float j = Float.valueOf(real1);
						real9.add(j);

						// Converted list of imaginary into Integer
						String img1 = list.get(2);
						Float k = Float.valueOf(img1);
						img9.add(k);

						Double magnitude = Math.sqrt(Float.valueOf(real1) * Float.valueOf(real1)
								+ Float.valueOf(img1) * Float.valueOf(img1));
						mag9.add(magnitude);

					}

					Complex[] y = new Complex[freq9.size()];
					// For loop to generate Complex Numbers
					for (int z = 0; z < freq9.size(); z++) {

						Complex wk = new Complex(real9.get(z), img9.get(z));
						y[z] = wk;
					}
					int n = y.length;

					Complex[] result = FFT.dft(y);

					// For loop to save absolute of FFT
					for (int p = 0; p < freq9.size(); p++) {
						fftMag9.add(result[p].abs());
					}
					CSVDataBean csvDataBean = new CSVDataBean();
					csvDataBean.setFreq(freq9);
					csvDataBean.setReal(real9);
					csvDataBean.setImg(img9);
					csvDataBean.setMagnitude(mag9);
					csvDataBean.setFftMagnitude(fftMag9);
					csvDataBeanList.add(csvDataBean);

				}

			}
		}

		// For loop to store A-Scan in matrix i.e. B-Scan
		for (double j = 0; j < 32; j++) {
			for (double i = 0; i < 201; i++) {

				CSVDataBean cSVDataBean = csvDataBeanList.get((int) j);
				double d = cSVDataBean.getFftMagnitude().get((int) i);
				double roundOff = Math.round(d * 10000.0) / 10000.0;
				BScan[(int) j][(int) i] = roundOff;

			}
		}

		// Matrix of Mean
		double[][] MeanSum = new double[1][201];
		// Matrix of Ones
		double[][] OnesMatrix = new double[32][1];
		// Matrix of multiplication of Mean and Ones for reshaping(To obey matrix multiplication law)
		double[][] MeanOnesMult = new double[32][201];
		// Mean subtracted BScan
		double[][] MeanSubtractedBScan = new double[32][201];

		// For loop to create Ones matrix
		for (double m = 0; m < 32; m++) {
			OnesMatrix[(int) m][0] = 1;
		}

		double rows = BScan.length;
		double cols = BScan[0].length;
		double average;

		// For loop to calculate mean columnwise
		for (double i = 0; i < cols; i++) {
			double sumCol = 0;
			for (double j = 0; j < rows; j++) {
				sumCol = sumCol + BScan[(int) j][(int) i];
			}
			average = sumCol / 32;
			MeanSum[0][(int) i] = average;
		}

		// For loop for multiplication of Mean and Ones for reshaping(To obey matrix multiplication law)
		for (double i = 0; i < rows; i++) {
			for (double j = 0; j < cols; j++) {
				MeanOnesMult[(int) i][(int) j] = OnesMatrix[(int) i][0] * MeanSum[0][(int) j];
			}
		}
		
		// For loop to subtract Actual actual values from mean(Mean-Subtraction)
		for (double i = 0; i < rows; i++) {
			for (double j = 0; j < cols; j++) {
				MeanSubtractedBScan[(int) i][(int) j] = BScan[(int) i][(int) j] - MeanOnesMult[(int) i][(int) j];
			}
		}

//		// For loop to form transpose matrix for UI display
//		for (double m = 0; m < 32; m++) {
//			for (double n = 0; n < 201; n++) {
//				transpose[(int) n][(int) m] = MeanSubtractedBScan[(int) m][(int) n];
//			}
//		}
		
		for (double m = 0; m < 32; m++) {
			for (double n = 0; n < 201; n++) {
				transpose[(int) n][(int) m] = BScan[(int) m][(int) n];
			}
		} 
		
		
		return transpose;
	}
	
	@GetMapping(path = "/getMeanSubtractedBScan")
	public double[][] getMeanSubtractedBScan() throws IOException, IOException {

		List<CSVDataBean> csvDataBeanList = new ArrayList();

		// String for source directory
		String srcDir = "F:/DHANASHRI/Tracking/DATASETS/PROJECT2 DATASET/T_2_MOVING(1,3)(1,1)_VERTICAL_DIF_sPED/";
		File folder = new File(srcDir);

		// Matrix of A-Sca stacking
		double[][] BScan = new double[32][201];

		// Matrix to Image on UI panel i.e. Transpose of BScan Matrix
		double[][] transpose = new double[201][32];
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {

			if (file.isFile()) {
				BufferedReader br = new BufferedReader(new FileReader(file));
				{
					List<List<String>> records = new ArrayList<>();
					String line = new String();
					while ((line = br.readLine()) != null) {
						// To remove unwanted data from CSV file
						if (!line.startsWith("!") && !line.contains("END") && !line.contains("BEGIN")) {
							String[] values = line.split(",");
							records.add(Arrays.asList(values));
						}
					}

					List<Long> freq9 = new ArrayList();
					List<Float> real9 = new ArrayList();
					List<Float> img9 = new ArrayList();
					List<Double> mag9 = new ArrayList();
					List<Double> fftMag9 = new ArrayList();
					List<Double> meanArray = new ArrayList();
					Double dummy;
					for (List<String> list : records) {

						// Converted list of freq into Integer
						String freq1 = list.get(0);
						Long i = Long.parseLong(freq1);
						freq9.add(i);

						// Converted list of real into Integer
						String real1 = list.get(1);
						Float j = Float.valueOf(real1);
						real9.add(j);

						// Converted list of imaginary into Integer
						String img1 = list.get(2);
						Float k = Float.valueOf(img1);
						img9.add(k);

						Double magnitude = Math.sqrt(Float.valueOf(real1) * Float.valueOf(real1)
								+ Float.valueOf(img1) * Float.valueOf(img1));
						mag9.add(magnitude);

					}

					Complex[] y = new Complex[freq9.size()];
					// For loop to generate Complex Numbers
					for (int z = 0; z < freq9.size(); z++) {

						Complex wk = new Complex(real9.get(z), img9.get(z));
						y[z] = wk;
					}
					int n = y.length;

					Complex[] result = FFT.dft(y);

					// For loop to save absolute of FFT
					for (int p = 0; p < freq9.size(); p++) {
						fftMag9.add(result[p].abs());
					}
					CSVDataBean csvDataBean = new CSVDataBean();
					csvDataBean.setFreq(freq9);
					csvDataBean.setReal(real9);
					csvDataBean.setImg(img9);
					csvDataBean.setMagnitude(mag9);
					csvDataBean.setFftMagnitude(fftMag9);
					csvDataBeanList.add(csvDataBean);

				}

			}
		}

		// For loop to store A-Scan in matrix i.e. B-Scan
		for (double j = 0; j < 32; j++) {
			for (double i = 0; i < 201; i++) {

				CSVDataBean cSVDataBean = csvDataBeanList.get((int) j);
				double d = cSVDataBean.getFftMagnitude().get((int) i);
				double roundOff = Math.round(d * 10000.0) / 10000.0;
				BScan[(int) j][(int) i] = roundOff;

			}
		}

		// Matrix of Mean
		double[][] MeanSum = new double[1][201];
		// Matrix of Ones
		double[][] OnesMatrix = new double[32][1];
		// Matrix of multiplication of Mean and Ones for reshaping(To obey matrix multiplication law)
		double[][] MeanOnesMult = new double[32][201];
		// Mean subtracted BScan
		double[][] MeanSubtractedBScan = new double[32][201];

		// For loop to create Ones matrix
		for (double m = 0; m < 32; m++) {
			OnesMatrix[(int) m][0] = 1;
		}

		double rows = BScan.length;
		double cols = BScan[0].length;
		double average;

		// For loop to calculate mean columnwise
		for (double i = 0; i < cols; i++) {
			double sumCol = 0;
			for (double j = 0; j < rows; j++) {
				sumCol = sumCol + BScan[(int) j][(int) i];
			}
			average = sumCol / 32;
			MeanSum[0][(int) i] = average;
		}

		// For loop for multiplication of Mean and Ones for reshaping(To obey matrix multiplication law)
		for (double i = 0; i < rows; i++) {
			for (double j = 0; j < cols; j++) {
				MeanOnesMult[(int) i][(int) j] = OnesMatrix[(int) i][0] * MeanSum[0][(int) j];
			}
		}
		
		// For loop to subtract Actual actual values from mean(Mean-Subtraction)
		for (double i = 0; i < rows; i++) {
			for (double j = 0; j < cols; j++) {
				MeanSubtractedBScan[(int) i][(int) j] = BScan[(int) i][(int) j] - MeanOnesMult[(int) i][(int) j];
			}
		}

//		// For loop to form transpose matrix for UI display
//		for (double m = 0; m < 32; m++) {
//			for (double n = 0; n < 201; n++) {
//				transpose[(int) n][(int) m] = MeanSubtractedBScan[(int) m][(int) n];
//			}
//		}
		
		for (double m = 0; m < 32; m++) {
			for (double n = 0; n < 201; n++) {
				transpose[(int) n][(int) m] = MeanSubtractedBScan[(int) m][(int) n];
			}
		} 
		
		
		return transpose;
	}

	@PostMapping(path = "/setParameters")
	public FreqParameters setParameters(@RequestBody FreqParameters freqParams ) throws IOException, IOException {
		
		FreqParameters freqParams1 = new FreqParameters();
		freqParams1.setStartFreq(freqParams.getStartFreq());
		freqParams1.setStopFreq(freqParams.getStopFreq());
		freqParams1.setNumberOfpoints(freqParams.getNumberOfpoints());
		
		return freqParams1;
	}

}
