package com.knoldus.parser.findsecbug;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.knoldus.parser.findsecbug.model.ReportData;

/**
 * @author com.knoldus
 */

public class FindSecBugParserSBT {
	
	private final static String SECURITY_WARNINGS = "Security Warnings";
	
	public static void main(String[] args) throws IOException {
		String inputFile = "/home/jitendra/Downloads/findBugsSec-bitbrew/common/report.html";
		ReportData rd = parseReport(inputFile);
		System.out.println(rd);
	}
/**
 * for given file path it will parse and give required data
 * @param inputFile
 * @return
 */
	public static ReportData parseReport(String inputFile) {
		
		ReportData data =new ReportData();
		Map<String, Map<String, List<String>>> parsedMap = new HashMap<>();
		Document doc = null;
		try {
			File input = new File(inputFile);
			//doc = Jsoup.connect(inputFile).get();
			doc = Jsoup.parse(input, "UTF-8", "");
			Elements ele = doc.getElementsByTag("table");

			//get total warning
			Element totalTable = ele.get(1);
			//String totals = ele1.getElementsByAttributeValue("class", "tablerow1").stream().findFirst().get().getElementsByIndexEquals(1).text();
			String total = totalTable.getElementsByAttributeValue("class", "tablerow1").get(0).getElementsByIndexEquals(1).text();
			data.setSize(Integer.valueOf(total));
			
			Element warningTable = ele.get(2);
			Elements allWarnings =  warningTable.getElementsByAttributeValue("class", "tablerow1");
			IntStream stream = IntStream.range(0,allWarnings.size());
			stream.forEach(index -> {
				
				if (parsedMap.get(SECURITY_WARNINGS) != null) {
					Map<String, List<String>> priorityMap = parsedMap.get(SECURITY_WARNINGS);

					if (priorityMap.get(allWarnings.get(index).getElementsByIndexEquals(1).text()) != null) {
						priorityMap.get(allWarnings.get(index).getElementsByIndexEquals(1).text()).add(allWarnings.get(index).getElementsByIndexEquals(2).text());
					} else {
						List<String> priorityBugLIst = new LinkedList<>();
						priorityBugLIst.add(allWarnings.get(index).getElementsByIndexEquals(2).text());
						priorityMap.put(allWarnings.get(index).getElementsByIndexEquals(1).text(), priorityBugLIst);
					}
					parsedMap.put(SECURITY_WARNINGS, priorityMap);
				} else {
					Map<String, List<String>> priorityMap = new HashMap<>();

					List<String> priorityBugLIst = new LinkedList<>();
					priorityBugLIst.add(allWarnings.get(index).getElementsByIndexEquals(2).text());
					priorityMap.put(allWarnings.get(index).getElementsByIndexEquals(1).text(), priorityBugLIst);
					parsedMap.put(SECURITY_WARNINGS, priorityMap);
				}
			});
			data.setParsedMap(parsedMap);
			
		} catch (IOException e) {
			data.setMessage(e.getMessage());
		}
		return data;
	}
}