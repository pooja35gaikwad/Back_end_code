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
				new FileReader("F:/DHANASHRI/Tracking/test1 21 Dec/HUMA WALKING straigh -8m/File001.csv"))) {
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

		List<List<String>> records = new ArrayList<>();

		// String for source directory
		String srcDir = "F:/DHANASHRI/Dhanashri Data/cwall5/";
		File folder = new File(srcDir);
		double[][] BScan = new double[201][32];
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			CSVDataBean csvDataBean = new CSVDataBean();
			if (file.isFile()) {
				BufferedReader br = new BufferedReader(new FileReader(file));
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

					Double magnitude = Math.sqrt(
							Float.valueOf(real1) * Float.valueOf(real1) + Float.valueOf(img1) * Float.valueOf(img1));
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

				csvDataBean.setFreq(freq9);
				csvDataBean.setReal(real9);
				csvDataBean.setImg(img9);
				csvDataBean.setMagnitude(mag9);
				csvDataBean.setFftMagnitude(fftMag9);

				for (double column : fftMag9)
					for (double j = 0; j < 32; j++) {
						BScan[(int) column][(int) j] = column;
					}

				csvDataBean.setFftMagnitude(fftMag9);
			}
			csvDataBeanList.add(csvDataBean);

		}

		return csvDataBeanList;

	}

	@GetMapping(path = "/getBScan")
	public double[][] getBScan() throws IOException, IOException {

		List<CSVDataBean> csvDataBeanList = new ArrayList();

		List<List<String>> records = new ArrayList<>();

		// String for source directory
		// F:/DHANASHRI/Tracking/test1 21 Dec/HUMA WALKING straigh -8m/
		// String srcDir = "F:/DHANASHRI/Dhanashri Data/cwall5/";
		String srcDir = "F:/DHANASHRI/Tracking/test1 21 Dec/HUMA WALKING straigh -8m/";
		File folder = new File(srcDir);

		double[][] BScan = new double[32][201];
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
			CSVDataBean csvDataBean = new CSVDataBean();
			if (file.isFile()) {
				BufferedReader br = new BufferedReader(new FileReader(file));
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

					Double magnitude = Math.sqrt(
							Float.valueOf(real1) * Float.valueOf(real1) + Float.valueOf(img1) * Float.valueOf(img1));
					mag9.add(magnitude);

				}

				Complex[] y = new Complex[freq9.size()];
				for (int z = 0; z < freq9.size(); z++) {

					Complex wk = new Complex(real9.get(z), img9.get(z));
					y[z] = wk;
				}
				int n = y.length;

				Complex[] result = FFT.dft(y);
				// Complex[] result = FFT.fft(y);
				// Complex[] result = FFT.ifft(y);
				for (int p = 0; p < freq9.size(); p++) {
					fftMag9.add(result[p].abs());
				}

				csvDataBean.setFreq(freq9);
				csvDataBean.setReal(real9);
				csvDataBean.setImg(img9);
				csvDataBean.setMagnitude(mag9);
				csvDataBean.setFftMagnitude(fftMag9);

			}
			csvDataBeanList.add(csvDataBean);

		}
		// for (CSVDataBean cSVDataBean : csvDataBeanList) {
		// for (double column = 1; column < cSVDataBean.getFftMagnitude().size();
		// column++)
		for (double j = 0; j < 32; j++) {
			for (double i = 0; i < 201; i++) {
				// double value = cSVDataBean.getFftMagnitude().get((int) column);
				// BScan[(int) column][(int) j] = column;
				// bScan[(int) j][(int) column] = value;
				// }
//			for (double column : cSVDataBean.getFftMagnitude())
//				for (double j = 0; j < 31; j++) {
//					BScan[(int) j][(int) column] = column;
//					//double[][] BScan = {{column}}
				CSVDataBean cSVDataBean = csvDataBeanList.get((int) j);
				double d = cSVDataBean.getFftMagnitude().get((int) i);
//				DecimalFormat df = new DecimalFormat("#.####");
//				df.setRoundingMode(RoundingMode.CEILING);
				double roundOff = Math.round(d * 10000.0) / 10000.0;
				BScan[(int) j][(int) i] = roundOff;
				// BScan[(int) j][(int) i] = df.format(d)
				// forEach(csvData -> csvData.getFftMagnitude().get())
				// }
			}
		}
		return BScan;
	}

}
