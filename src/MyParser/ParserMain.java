package MyParser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ParserMain {	
	
	public static void main(String[] args) {
		Tokenizer t = new Tokenizer("StateTransitionsTable.txt");	
		Recognizer r = new Recognizer("grammar.txt");
		try {
			FileWriter writer = new FileWriter(new File("output.txt"));
			Scanner sc = new Scanner(new File("dataFile.txt"));
			String tokens = "";
			while(sc.hasNextLine()) {
				tokens = t.tokenize(sc.nextLine());
				writer.write(tokens);
				writer.write(r.recognize(tokens));
			}
			sc.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
