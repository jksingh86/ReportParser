package com.knoldus.parser.findsecbug;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.knoldus.parser.findsecbug.model.ReportData;

/**
 * @author Crunchify.com
 */

public class FindSecBugParser {
	public static void main(String[] args) throws IOException {
		String inputFile = "file:///home/jitendra/projects/cucumberui/target/spotbugsXml.xml";
		ReportData rd = parseReport(inputFile);
		System.out.println(rd);
	}

	public static ReportData parseReport(String inputFile) {
		ReportData data = new ReportData();
		// String inputFile =
		// "file:///home/jitendra/projects/cucumberui/target/spotbugsXml.xml";
		Map<String, Map<String, List<String>>> parsedMap = new HashMap<>();
		try {
			// BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

			Document doc = factory.newDocumentBuilder().parse(inputFile);
			System.out.print("Enter element name: ");
			String element = "BugInstance";// reader.readLine();
			NodeList nodes = doc.getElementsByTagName(element);

			IntStream stream = IntStream.range(0, nodes.getLength());
			stream.forEach(index -> {
				Node node = nodes.item(index);
				NamedNodeMap nodeMap = node.getAttributes();
				Node category = nodeMap.getNamedItem("category");
				System.out.println(category.getNodeValue());
				Node priority = nodeMap.getNamedItem("priority");
				System.out.println(priority.getNodeValue());
				Node longDesc = node.getChildNodes().item(1);
				System.out.println(longDesc.getTextContent());

				if (parsedMap.get(category.getNodeValue()) != null) {
					Map<String, List<String>> priorityMap = parsedMap.get(category.getNodeValue());

					if (priorityMap.get(priority.getNodeValue()) != null) {
						priorityMap.get(priority.getNodeValue()).add(longDesc.getTextContent());
					} else {
						List<String> priorityBugLIst = new LinkedList<>();
						priorityBugLIst.add(longDesc.getTextContent());
						priorityMap.put(priority.getNodeValue(), priorityBugLIst);
					}
					parsedMap.put(category.getNodeValue(), priorityMap);
				} else {
					Map<String, List<String>> priorityMap = new HashMap<>();

					List<String> priorityBugLIst = new LinkedList<>();
					priorityBugLIst.add(longDesc.getTextContent());
					priorityMap.put(priority.getNodeValue(), priorityBugLIst);
					parsedMap.put(category.getNodeValue(), priorityMap);
				}
				data.setSize(nodes.getLength());
				data.setParsedMap(parsedMap);
			});
		} catch (Exception e) {
			data.setMessage(e.getMessage());
		}
		return data;
	}
}