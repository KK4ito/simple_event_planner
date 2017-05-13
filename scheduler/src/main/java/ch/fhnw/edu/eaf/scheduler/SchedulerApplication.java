package ch.fhnw.edu.eaf.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan
@EnableScheduling
public class SchedulerApplication {

	public static String BASE_URL_EVENTMANAGEMENT;

	public static void main(String[] args) throws ParserConfigurationException, SAXException, XPathExpressionException {

		try {
			HttpURLConnection conn = (HttpURLConnection) new URL("http://localhost:8761/eureka/apps/EVENTMANAGEMENT").openConnection();
			XPath xpath = XPathFactory.newInstance().newXPath();
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(conn.getInputStream());
			BASE_URL_EVENTMANAGEMENT = xpath.evaluate("/application/instance/homePageUrl", document);
			SpringApplication.run(SchedulerApplication.class, args);
		} catch (IOException e) {
			throw new RuntimeException("Eureka Server not found: " + e.getMessage());
		}


	}
}
