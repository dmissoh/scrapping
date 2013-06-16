package com.datapublica.scraping.example;

import com.datapublica.scraping.io.ScrapingIOUtils;
import com.datapublica.scraping.xpath.XPathUtils;
import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarmitonScraper implements Runnable {

    private static final String SEARCH_URL = "http://www.marmiton.org/recettes/recette_financiers-revisites-aux-noisettes-et-pepites-de-chocolat_305030.aspx";
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
                System.out.println("");
                System.out.println("The tags of the recipe: " + XPathUtils.getString(doc, "//div[@class='m_content_recette_breadcrumb']/text()"));
                System.out.println("");
                String ingredientPath = "//p[@class='m_content_recette_ingredients']";
                //System.out.println("The tags of the recipe: " + XPathUtils.getString(doc, ingredientPath));
                System.out.println("Ingredients info: " + XPathUtils.getString(doc, ingredientPath + "/span/text()"));
                //System.out.println("Ingredients info: " + XPathUtils.getString(doc, ingredientPath + "/text()"));

                final NodeList nodes = XPathUtils.getNodes(doc, ingredientPath);
                for(int i=0 ; i<nodes.getLength() ; i++) {
                    // Extract data
                    //final String title = XPathUtils.getString(nodes.item(i), "/text()");
                    //System.out.println(title);
                    System.out.println("---->" + nodes.item(i).getChildNodes());
                    System.out.println("**** ---->" + nodes.item(i).getTextContent());

                    final NodeList nds = nodes.item(i).getChildNodes();
                    for(int j=0 ; j<nds.getLength() ; j++) {
                        System.out.println("---->" + nds.item(j));
                    }

                    //System.out.println("---->" +  XPathUtils.getString(nodes.item(i), "/text()"));
                }

                System.out.println("");

                String pageContent = ScrapingIOUtils.getResourceAsString(url);
                Pattern p = Pattern.compile("href=\"([^\"]*)\"");
                Matcher m = p.matcher(pageContent);
                while (m.find())
                {
                    String href = m.group(1);
                    if(href.endsWith(".aspx") && href.startsWith("/recettes/")){
                        URL target = new URL(url, href);
                        System.out.println(target);
                    }
                }
            } finally {
                IOUtils.closeQuietly(stream);
            }
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
        new MarmitonScraper().run();
    }
}
