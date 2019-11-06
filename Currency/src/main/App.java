package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;

public class App {

	//add one day to the date
	public static Date addDay(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.DATE, 1);
		return cal.getTime();
	}

	//reduce the date by one day 
	public static Date decDay(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	//check if the received "currFrom.csv" file contains required information
	public static boolean CheckFrom() throws IOException {
		int count = 0;
		boolean toReturn = false;
		BufferedReader csvReader = new BufferedReader(new FileReader("currFrom.csv"));
		String line = csvReader.readLine();
		while (line != null) {
			count++;
			line = csvReader.readLine();
		}
		csvReader.close();
		if (count > 10) {
			toReturn = true;
		}
		return toReturn;
	}

	//check if the received "currUntil.csv" file contains required information
	public static boolean CheckUntil() throws IOException {
		int count = 0;
		boolean toReturn = false;
		BufferedReader csvReader = new BufferedReader(new FileReader("currUntil.csv"));
		String line = csvReader.readLine();
		while (line != null) {
			count++;
			line = csvReader.readLine();
		}
		csvReader.close();
		if (count > 10) {
			toReturn = true;
		}
		return toReturn;
	}

	public static void main(String[] args) throws IOException, ParseException {
		List<String> currencyCode = Arrays.asList("AUD", "BGN", "BRL", "CAD", "CHF", "CNY", "CZK", "DKK", "GBP", "HKD",
				"HRK", "HUF", "IDR", "ILS", "INR", "ISK", "JPY", "KRW", "MXN", "MYR", "NOK", "NZD", "PHP", "PLN", "RON",
				"RUB", "SEK", "SGD", "THB", "TRY", "USD", "ZAR");

		System.out.println("Type in the currency codes you are interested in from the list bellow:");
		System.out.println("Australijos doleris	AUD\r\n" + "Bulgarijos levas	BGN\r\n" + "Brazilijos realas	BRL\r\n"
				+ "Kanados doleris	CAD\r\n" + "Šveicarijos frankas	CHF\r\n" + "Kinijos  ženminbi juanis	CNY\r\n"
				+ "Čekijos krona	CZK\r\n" + "Danijos krona	DKK\r\n"
				+ "Didžiosios Britanijos svaras sterlingų	GBP\r\n" + "Honkongo doleris	HKD\r\n"
				+ "Kroatijos kuna	HRK\r\n" + "Vengrijos forintas	HUF\r\n" + "Indonezijos rupija	IDR\r\n"
				+ "Izraelio  naujasis šekelis	ILS\r\n" + "Indijos rupija	INR\r\n" + "Islandijos krona	ISK\r\n"
				+ "Japonijos jena	JPY\r\n" + "Pietų Korėjos vonas	KRW\r\n" + "Meksikos pesas	MXN\r\n"
				+ "Malaizijos ringitas	MYR\r\n" + "Norvegijos krona	NOK\r\n"
				+ "Naujosios Zelandijos doleris	NZD\r\n" + "Filipinų pesas	PHP\r\n" + "Lenkijos zlotas	PLN\r\n"
				+ "Rumunijos lėja	RON\r\n" + "Rusijos rublis	RUB\r\n" + "Švedijos krona	SEK\r\n"
				+ "Singapūro doleris	SGD\r\n" + "Tailando batas	THB\r\n" + "Turkijos lira	TRY\r\n"
				+ "JAV doleris	USD\r\n" + "Pietų Afrikos Respublikos randas	ZAR\r\n" + "");
		System.out.println("Type 'enough' to stop.");

		//Currency codes input
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String text = null;
		List<String> workingCurrency = new ArrayList<String>();
		do {
			text = in.readLine();
			if (currencyCode.contains(text) && (workingCurrency.contains(text) == false)) {
				workingCurrency.add(text);
			} else if (text.equals("enough")) {
				break;
			} else {
				if (workingCurrency.contains(text)) {
					System.out.println("Currency already selected.");
				} else {
					System.out.println("Wrong input. Try again.");
				}
			}
		} while (currencyCode.size() != workingCurrency.size());

		//input from date
		System.out.println("From what date? (Type date with 'yyyy-mm-dd' format since 2014-09-30 until today's date)");
		String date = null;
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
		Date minDate = ft.parse("2014-09-30");
		Date today = Calendar.getInstance().getTime();
		Date dateFrom = null;
		Date dateUntil = null;
		boolean error = true;
		do {
			try {
				date = in.readLine();
				dateFrom = ft.parse(date);
				if ((dateFrom.after(minDate) || dateFrom.equals(minDate))
						&& (dateFrom.before(today) || dateFrom.equals(today))) {
					error = false;
				} else {
					System.out.println("Wrong date");
				}
			} catch (ParseException e) {
				System.out.println("Wrong date format");
			}
		} while (error);

		//input until which date
		System.out.println("Until what date? (Type date with 'yyyy-mm-dd' format since 2014-09-30 until today's date)");
		date = null;
		error = true;
		do {
			try {
				date = in.readLine();
				dateUntil = ft.parse(date);
				if ((dateUntil.after(minDate) || dateUntil.equals(minDate))
						&& (dateUntil.before(today) || dateUntil.equals(today)) && (dateUntil.after(dateFrom))) {
					error = false;
				} else if (dateUntil.before(dateFrom)) {
					System.out.println("Date must not be before " + dateFrom);
				} else {
					System.out.println("Wrong date");
				}
			} catch (ParseException e) {
				System.out.println("Wrong date format");
			}
		} while (error);

		//get "currFrom.csv" file by the download link
		error = true;
		do {
			File currFrom = new File("currFrom.csv");
			URL urlFrom = new URL("https://www.lb.lt/lt/currency/daylyexport/?csv=1&class=Eu&type=day&date_day="
					+ ft.format(dateFrom));
			try {
				FileUtils.copyURLToFile(urlFrom, currFrom);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (CheckFrom()) {
				error = false;
			} else if (dateFrom.after(minDate)) {
				dateFrom = decDay(dateFrom);
			} else {
				dateFrom = addDay(dateFrom);
			}
		} while (error);

		//get "currUntil.csv" by the download link
		error = true;
		do {
			File currUntil = new File("currUntil.csv");
			URL urlUntil = new URL("https://www.lb.lt/lt/currency/daylyexport/?csv=1&class=Eu&type=day&date_day="
					+ ft.format(dateUntil));
			try {
				FileUtils.copyURLToFile(urlUntil, currUntil);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (CheckUntil()) {
				error = false;
			} else if (dateUntil.before(today)) {
				dateUntil = addDay(dateUntil);
			} else {
				dateUntil = decDay(dateUntil);
			}
		} while (error);

		//parse the information from a CSV file
		List<Information> infoFrom = new ArrayList<Information>();
		try (Reader reader = Files.newBufferedReader(Paths.get("currFrom.csv"));
				CSVParser csvParser = new CSVParser(reader, CSVFormat.EXCEL.withDelimiter(';').withFirstRecordAsHeader()
						.withIgnoreHeaderCase().withTrim());) {
			for (CSVRecord csvRecord : csvParser) {
				// Accessing Values by Column Index
				Information inffrom = new Information();
				inffrom.setCodas(csvRecord.get("Valiutos kodas"));
				inffrom.setSantykis(csvRecord.get("Santykis"));
				inffrom.setData(csvRecord.get("Data"));
				infoFrom.add(inffrom);
			}
		}

		//parse the information from a CSV file
		List<Information> infoUntil = new ArrayList<Information>();
		try (Reader reader = Files.newBufferedReader(Paths.get("currUntil.csv"));
				CSVParser csvParser = new CSVParser(reader, CSVFormat.EXCEL.withDelimiter(';').withFirstRecordAsHeader()
						.withIgnoreHeaderCase().withTrim());) {
			for (CSVRecord csvRecord : csvParser) {
				// Accessing Values by Column Index
				Information infuntil = new Information();
				infuntil.setCodas(csvRecord.get("Valiutos kodas"));
				infuntil.setSantykis(csvRecord.get("Santykis"));
				infuntil.setData(csvRecord.get("Data"));
				infoUntil.add(infuntil);
			}
		}
		
		//print the difference between currency values
		System.out.println("The difference between " + ft.format(dateFrom) + " and " + ft.format(dateUntil) + ":");
		for (int i = 0; i < workingCurrency.size(); i++) {
			for (int j = 0; j < infoFrom.size(); j++) {
				if (infoFrom.get(j).getCodas().equals(workingCurrency.get(i))) {
					Double result = Double.parseDouble(infoFrom.get(j).getSantykis().replaceAll(",", "."))
							- Double.parseDouble(infoUntil.get(j).getSantykis().replaceAll(",", "."));
					System.out.println(infoFrom.get(j).getCodas() + " " + String.format("%.5f", result));
				}
			}
		}
	}
}
