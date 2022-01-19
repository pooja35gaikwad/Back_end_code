package com.TWRSPROJ2_BE;

import java.io.File;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

	@GetMapping(path = "/hello-world")
	public String helloWorld() {
		return "Hello world";
	}

	@GetMapping(path = "/getCsv")
	public CSVDataBean getCsvData() throws IOException, IOException {

		// Created a object of CSV Data Bean class
		CSVDataBean csvDataBean = new CSVDataBean();

		List<List<String>> records = new ArrayList<>();

		// String for source directory
		String srcDir = "F:/DHANASHRI/Dhanashri Data/cwall5/";
		File folder = new File(srcDir);
		
		
		File[] listOfFiles = folder.listFiles();
		
		//If no. of files are less than zero
		if (listOfFiles.length > 0) {
			for (int i = 0; i < listOfFiles.length; i++ ) {
				//To check if the list of file has file
				if (listOfFiles[i].isFile()) {
					try (BufferedReader br = new BufferedReader( 
							new FileReader(listOfFiles[i].getName()))) {
						String line = new String();
						while ((line = br.readLine()) != null) {
							// To remove unwanted data from CSV file
							if (!line.startsWith("!") && !line.contains("END") && !line.contains("BEGIN")) {
								String[] values = line.split(",");
								records.add(Arrays.asList(values));
							}
						}
				}
				}
			}
		}
		// Function to read CSV file from local
		try (BufferedReader br = new BufferedReader(

				new FileReader("F:/DHANASHRI/Dhanashri Data/cwall5/File001.csv"))) {
			String line = new String();

			while ((line = br.readLine()) != null) {
				// To remove unwanted data from CSV file
				if (!line.startsWith("!") && !line.contains("END") && !line.contains("BEGIN")) {
					String[] values = line.split(",");
					records.add(Arrays.asList(values));
				}
			}

			// created list according to the data types of Freq., Real and imaginary
			List<Integer> freq9 = new ArrayList();
			List<Float> real9 = new ArrayList();
			List<Float> img9 = new ArrayList();
			List<Double> mag9 = new ArrayList();
			List<Double> fftMag9 = new ArrayList();

			for (List<String> list : records) {

				// Converted list of freq into Integer
				String freq1 = list.get(0);
				int i = Integer.parseInt(freq1);
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
			// Complex[] my = new Complex[n];
			Complex[] result = FFT.dft(y);
			for (int p = 0; p < 201; p++) {
				// Complex[] result = FFT.fft(y.);
				// Complex cv = new Complex(result[p].);
				fftMag9.add(result[p].abs());
			}
//
//			final int[][] matrix = { { 255, 255, 255, 255, 255,255, 255, 255, 255, 255,255, 255, 255, 255, 255 },
//					{ 0, 0, 0, 0, 0,0, 0, 0, 0, 0,0, 0, 0, 0, 0 },
//					{ 255, 255, 255, 255, 255,255, 255, 255, 255, 255,255, 255, 255, 255, 255 },
//					{ 0, 0, 0, 0, 0,0, 0, 0, 0, 0,0, 0, 0, 0, 0 },
//					{ 255, 255, 255, 255, 255 ,255, 255, 255, 255, 255,255, 255, 255, 255, 255},
//					{ 0, 0, 0, 0, 0,0, 0, 0, 0, 0,0, 0, 0, 0, 0 },
//					{ 255, 255, 255, 255, 255 ,255, 255, 255, 255, 255,255, 255, 255, 255, 255},
//					{ 0, 0, 0, 0, 0,0, 0, 0, 0, 0,0, 0, 0, 0, 0 },
//					{ 255, 255, 255, 255, 255,255, 255, 255, 255, 255,255, 255, 255, 255, 255 },
//					{ 0, 0, 0, 0, 0,0, 0, 0, 0, 0,0, 0, 0, 0, 0 },
//					{ 255, 255, 255, 255, 255 ,255, 255, 255, 255, 255,255, 255, 255, 255, 255},
//					{ 0, 0, 0, 0, 0 ,0, 0, 0, 0, 0,0, 0, 0, 0, 0},
//					{ 255, 255, 255, 255, 255,255, 255, 255, 255, 255,255, 255, 255, 255, 255 },
//					{ 0, 0, 0, 0, 0,0, 0, 0, 0, 0,0, 0, 0, 0, 0 },
//					{ 255, 255, 255, 255, 255,255, 255, 255, 255, 255,255, 255, 255, 255, 255 }
////					{ 7, 8, 9, 6, 2, 67, 97, 2, 5, 97, 1, 8, 7, 8, 9, 6, 2, 6, 68, 1, 8, 79 },
////					{6, 68, 1, 8, 79, 67, 97, 2, 5, 97, 1, 8, 269, 3, 62, 4, 6, 68, 1, 8, 79 },
////					{ 7, 8, 9, 6, 2, 67, 97, 2, 5, 97, 1, 8, 269, 3, 62, 4, 6, 68, 1, 8, 79 },
////					{ 7, 8, 9, 6, 2, 67, 97, 2, 5, 97, 1, 8, 269, 3, 62, 4, 6, 68, 1, 8, 79 },
//					};

			int[][] PixelArray;
			//PixelArray = matrix;
			// BufferedImage bufferimage = ImageIO.read(new File("F:/DHANASHRI/Dhanashri
			// Data/images2.jpg"));
//			Integer height = matrix.length;
//			Integer width = 3;
			//Integer height = 15;
			//Integer width = matrix.length;
			//ConstructImage constructImage = new ConstructImage(height, width, matrix);

			// double[][] m = new double[30][30];
			// MLPlot p=new MLPlot();

			//constructImage.getClass();
			csvDataBean.setFreq(freq9);
			csvDataBean.setReal(real9);
			csvDataBean.setImg(img9);
			csvDataBean.setMagnitude(mag9);
			csvDataBean.setFftMagnitude(fftMag9);

		}
		return csvDataBean;
	}
}
