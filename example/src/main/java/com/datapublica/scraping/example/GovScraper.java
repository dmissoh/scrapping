package com.datapublica.scraping.example;

import com.datapublica.scraping.io.ScrapingIOUtils;
import com.datapublica.scraping.xpath.XPathUtils;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.URL;

public class GovScraper implements Runnable {

    private static final String SEARCH_URL = "http://lannuaire.service-public.fr/navigation/gouvernement.html";
    private static final String CHARSET = "UTF-8";

    public void run() {
        try {
            // Root url
            URL url = new URL(SEARCH_URL);
            // Open connection
            InputStream stream = null;
            try {
                stream = ScrapingIOUtils.getResourceAsStream(url);
                final Document doc = XPathUtils.getDocumentHTML(stream, CHARSET);
                System.out.println("The title of the page: " + XPathUtils.getString(doc, "//head/title/text()"));
                final NodeList nodes = XPathUtils.getNodes(doc, "//div[@class='bloc-contenu']/ul/li/a/@href");
                //System.out.println("-> : " + XPathUtils.getString(doc, "//div[@id='colonne21']/div[2]/a/@href"));
                for (int i = 0; i < nodes.getLength(); i++) {
                    Node node = nodes.item(i);
                    URL target = new URL(url, node.getTextContent());
                    //System.out.println("The url extracted from the main page: " + target);
                    exploreURL( target );
                }
            } finally {
                IOUtils.closeQuietly(stream);
            }
            //
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exploreURL(URL url) throws Exception {
        InputStream stream = null;
        try {
            stream = ScrapingIOUtils.getResourceAsStream(url);
            final Document doc = XPathUtils.getDocumentHTML(stream, CHARSET);
            System.out.println("The title of the page: " + XPathUtils.getString(doc, "//h2[@id='titrePage']/text()"));

        } finally {
            IOUtils.closeQuietly(stream);
        }
    }

    public static void main(String... args) {
        new GovScraper().run();
    }
}
