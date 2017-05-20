package ch.fhnw.edu.eaf.eventmgmt;

import org.pac4j.core.config.Config;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
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
public class EventmanagementApplication {

	@Autowired
	private Config config;

	public static String BASE_URL_MAILER;

	public static void main(String[] args) {

		try {
			HttpURLConnection conn = (HttpURLConnection) new URL("http://localhost:8761/eureka/apps/Mailer").openConnection();
			XPath xpath = XPathFactory.newInstance().newXPath();
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(conn.getInputStream());
			BASE_URL_MAILER = xpath.evaluate("/application/instance/homePageUrl", document);
			SpringApplication.run(EventmanagementApplication.class, args);
		} catch (IOException e) {
			throw new RuntimeException("Eureka Server not found: " + e.getMessage());
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}

}
