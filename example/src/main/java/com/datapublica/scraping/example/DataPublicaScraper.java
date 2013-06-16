package com.datapublica.scraping.example;

import com.datapublica.scraping.io.ScrapingIOUtils;
import com.datapublica.scraping.xpath.XPathUtils;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.URL;

/**
 * @author thomas.dudouet@data-publica.com
 */
public class DataPublicaScraper implements Runnable {

    //private static final String SEARCH_URL = "http://www.data-publica.com/search?q=";
    private static final String SEARCH_URL = "http://www.data-publica.com/search?q=";
    private static final String CHARSET = "UTF-8";

    @Override
    public void run() {
        try {
            // Root url
            URL url = new URL(SEARCH_URL);
            //
            do {
                // Open connection
                InputStream stream = null;
                try{
                   stream = ScrapingIOUtils.getResourceAsStream(url);
                    // Get DOM document
                    final Document doc = XPathUtils.getDocumentHTML(stream, CHARSET);
                    // For each search result
                    final NodeList nodes = XPathUtils.getNodes(doc, "//article[@class='feed searchresult']/ol/li");
                    for(int i=0 ; i<nodes.getLength() ; i++) {
                        // Extract data
                        final String href = XPathUtils.getString(nodes.item(i), "./a/@href");
                        final String title = XPathUtils.getString(nodes.item(i), "./a/text()");
                        //
                        System.out.println("---");
                        System.out.println(title);
                        System.out.println(new URL(url, href));
                    }
                    // Extract next search result page
                    final String next = XPathUtils.getString(doc, "//ul[@class='pagenav']/li[@class='current']/following-sibling::li[1]/a/@href");
                    url = (next != null) ? new URL(url, next) : null;
                } finally {
                    IOUtils.closeQuietly(stream);
                }
            } while (url != null);
            //
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String... args) {
        new DataPublicaScraper().run();
    }
}
