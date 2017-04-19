package game.engine;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.HashMap;

/**
 * Creates the XML file for the Highscores.
 *
 * UNUSED Currently, intended for Milestone 4.
 *
 * Created by Julischka Saravia on 11.04.2017.
 */
public class CreateHighscoreXML {

	/**
	 * Receives parameters from the class serverGameController and writes them on the XML file
	 * @param playerScore HashMap with usernames and scores
	 * @param winningTeamName name of the winning team
	 */
	public static void createHighscoreXML(HashMap<String, Integer> playerScore, String winningTeamName) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			
			
			/**
			 * Creates the root element called user
			 */
			Element rootElement = doc.createElement("user");
			doc.appendChild(rootElement);
			
			for (String playerName : playerScore.keySet()) {
				
				String score = String.valueOf(playerScore.get(playerName));
				
				/**
				 * Creates child called username of the root element
				 */
				Element username = doc.createElement("username");
				rootElement.appendChild(username);
				
				/**
				 * Gives the element username the player name as an attribute
				 */
				Attr attr = doc.createAttribute("name");
				attr.setValue(playerName);
				username.setAttributeNode(attr);

				/**
				 * Creates the 1st child of the username element called team. Team saved as TextNode.
				 */
				Element team = doc.createElement("team");
				team.appendChild(doc.createTextNode(winningTeamName));
				username.appendChild(team);
				
				/**
				 * Creates new child of the username element called highscore. Points saved as TextNode.
				 */
				Element highscore = doc.createElement("highscore");
				highscore.appendChild(doc.createTextNode(score));
				username.appendChild(highscore);
				
				/**
				 * Creates new child of the username element called date. date saved as TextNode.
				 */
				Element date = doc.createElement("date");
				date.appendChild(doc.createTextNode("01.01.1111"));
				username.appendChild(date);
				
			}
			
			/**
			 * Creates the new file
			 */
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("./highscoreXml.xml"));
			transformer.transform(source, result);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
