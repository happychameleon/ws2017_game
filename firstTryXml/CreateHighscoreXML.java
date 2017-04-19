import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;

/**
 * Creates the XML file for the Highscores.
 *
 * Created by Julischka Saravia on 11.04.2017.
 */
public class CreateHighscoreXML {


    public static void main(String[] args) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            /**
             * Creates the root element called user
             */
            Element rootElement = doc.createElement("user");
            doc.appendChild(rootElement);

            /**
             * Creates the first child of the root element called username
             */
            Element username = doc.createElement("username");
            rootElement.appendChild(username);

            /**
             * Gives the element username the player name as an attribute
             */
            Attr attr = doc.createAttribute("name");
            attr.setValue("Songoku");
            username.setAttributeNode(attr);

            /**
             * Creates the 1st child of the username element called highscore. Points saved as TextNode.
             */
            Element highscore = doc.createElement("highscore");
            highscore.appendChild(doc.createTextNode("9000"));
            username.appendChild(highscore);

            /**
             * Creates the second child of the username element called date. date saved as TextNode.
             */
            Element date = doc.createElement("date");
            date.appendChild(doc.createTextNode("01.01.1111"));
            username.appendChild(date);

            /**
             * Creates the new file
             */
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(".\\highscoreXml.xml"));
            transformer.transform(source, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
