package socialeyser.bl.services.impl.sa.modsupport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Normalizzazione contenuto testo dai dati grezzi
 * 
 * @author Claudia Raponi
 */

public class Normalizer {
//	private File stopWordFile;
//	private File slang;
//	private File slangTranslate;
	
	private InputStream stopWordFile;
	private InputStream slang;
	private InputStream slangTranslate;
	private List<String> stopwords;
	private HashMap<String,String> dictionary;

	public Normalizer(String stopWordFile, String slang, String slangTranslate) throws Exception {
		super();
		//Mio file stopword
//		this.stopWordFile = new File("stopWord2.txt");
		
		//File stopword default
		this.stopWordFile = new FileInputStream(new File(stopWordFile));
		this.slang = new FileInputStream(new File(slang));
		this.slangTranslate = new FileInputStream(new File(slangTranslate));
		this.dictionary = createSlangDictionary();
		this.stopwords = makeList(this.stopWordFile);
//		this.printStopwords("stoplist.txt");
	}

	/*
	 * Normalizzazione contenuto del tweet
	 */
	public String cleanText(String onlyRawText) throws IOException {
		String s;
		
		if (onlyRawText.equals("") || onlyRawText.equals(" ")) {
			return onlyRawText;
		}
		else {
			/*Mantenimento degli HashTag */
			//s = manageHashTag(onlyRawText);
			
			/*Encoding to Utf-8 */
			byte[] utf8 = onlyRawText.getBytes("UTF-8");
			s = new String(utf8);
			
			/*Rimozione delle entità HTML */
			s = removeHTMLCharacterEntities(s);
			
			/*Riduzione dei caratteri duplicati (max 2) */
			s = s.replaceAll("(.)\\1+", "$1$1");
					
			/*Normalizzazione del testo: hashtag, mention, url, / */
			s = normalizeText(s);
			
			/*Traduzione dei caratteri dello slang */
			ArrayList<String>  a = translateSlang(s); 
			
			/*Conversione in minuscolo  */
			s = a.get(0).toLowerCase(); 
			
			/*Rimozione delle stopWord */
			s = removeStopWord(s);
			
			/*Pulizia della punteggiatura */
			s = removePunctuation(s);
			
			/*Ulteriore ciclo di normalizzazione per eliminare residui */
			a = translateSlang(s); 
			s = a.get(0).toLowerCase(); 
			s = removeStopWord(s);
			s = removePunctuation(s);
			
			/*Riduzione degli spazi bianchi */
			s = s.replaceAll("  ","");
			
			return s;
		}
	}
	
	/*
	 * Traduzione dei termini relativi alle entità html
	 */
	public String removeHTMLCharacterEntities(String content) throws IOException {
		content = content
				.replaceAll("&nbsp;"," ")
				.replaceAll( "&lt;", "<")
				.replaceAll("&gt;",">")
				.replaceAll("&amp;","&")
				.replaceAll("&cent;","¢")
				.replaceAll("&pound;","£")
				.replaceAll("&yen;","¥")
				.replaceAll("&euro;","€")
				.replaceAll("&copy;","©")
				.replaceAll("&reg;","®");
		return content;
	}
	
	/*
	 * Traduzione dei termini dello slang
	 */
	private ArrayList<String> translateSlang(String onlyRawText) {
		ArrayList<String> g = new ArrayList<String>();
		String newText="";
		String s = onlyRawText;
		String[] token = s.split(" ");
		for (String a: token) {
			if (dictionary.containsKey(a)) {
				newText += dictionary.get(a) + " ";
			}
			else
				newText += a +" ";
		}
		g.add(newText);
		return g;
	}

	/*
	 * Rimozione delle stopWord
	 */
	public String removeStopWord(String text) throws IOException    {
		StringTokenizer tokens = new StringTokenizer(text, " ");
		String newText = "";
		while (tokens.hasMoreTokens()) {
			String temp = tokens.nextToken();
			if (!checkStopWord(temp)) {
				newText += temp + " ";
			}
		}
		text = "";
		text = newText;
		return text;
	}
	
	
	/*
	 * Pulizia dei termini contenenti gli hashtag:
	 * viene rimosso il simbolo # ma mantenuto il termine che costituisce il contenuto dell'hashtag
	 */
	public String manageHashTag(String text) {
		String t = text.replace("#","");
		return t;
	}
	
	
	/*
	 * Pulizia del testo del tweet:
	 * - @: mentions
	 * - http: url
	 * - #: hashtag
	 * - \: slash
	 */
	public String normalizeText(String onlyRawText) throws IOException    {
		String lowerCaseText = onlyRawText.toLowerCase();
		StringTokenizer tokens = new StringTokenizer(lowerCaseText, " ");
		String newText = "";
		while (tokens.hasMoreTokens()) {
			String temp = tokens.nextToken();
			if (!temp.contains("@") && !temp.contains("http") && !temp.contains("#") && !temp.contains("\\") ) {
				newText += temp + " ";
			}
		}
		lowerCaseText = "";
		lowerCaseText = newText;
		return lowerCaseText;
	}

	/*
	 * Rimozione della punteggiatura nel testo
	 */
	public String removePunctuation(String text) throws IOException    {
		return text.replaceAll("[^a-zA-Z ]", " ");
	}

	/*
	 * Metodo di supporto per la verifica delle stop word
	 */
	
	public boolean checkStopWord(String word) {
		return this.stopwords.contains(word);
	}

	/*
	 * Creazione del dizionario relativo ai termini dello slang
	 */
	public HashMap<String,String> createSlangDictionary() throws IOException {
		HashMap<String,String> dict = new HashMap<String,String>();
		
		try {
			ArrayList<String> textS = makeList(slang);
			ArrayList<String> textST = makeList(slangTranslate);
			for (int i=0; i < textS.size(); i++) {
				dict.put(textS.get(i), textST.get(i));
			}
		}
		catch (Exception e) {
			System.out.println("Problem found while creating dictionary");
		}
		return dict;
	}
	
	/*
	 * Metodo di supporto per createSlangDictionary
	 */
//	private ArrayList<String> makeList(File source) throws Exception {
//		BufferedReader reader = new BufferedReader(new FileReader(source));
//		ArrayList<String> list = new ArrayList<>();
//		
//		String text = "";
//		
//		while ((text = reader.readLine()) != null) {
//			list.add(text);
//		}
//		
//		reader.close();
//		return list;
//	}
	
	private ArrayList<String> makeList(InputStream source) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(source));
		ArrayList<String> list = new ArrayList<>();
		
		String text = "";
		
		while ((text = reader.readLine()) != null) {
			list.add(text);
		}
		
		return list;
	}
//	/*
//	 * Metodo di supporto per createSlangDictionary
//	 */
//	public String[] createSingleArray(File sFile) throws IOException {
//		BufferedReader reader = new BufferedReader(new FileReader(sFile));
//		String[] array = new String[5463];
//		String text;
//		int i=0;
//		while ((text = reader.readLine()) != null) {
//			array[i]=text;
//			i++;
//		}
//		reader.close();
//	return array;
//	}
}
