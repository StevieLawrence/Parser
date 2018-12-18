package MyParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Tokenizer {

	State currentState, nextState;
	private String tokens;
	private State[][] table;

	/**
	 * A scanner that takes in a sequence of characters and breaks it down it into tokens
	 * using a provided state transition table
	 * @param the name of the file containing the state transitions table
	 * **/
	public Tokenizer(String filename) {
		readInTable(filename);
		this.currentState = State.Start;
		this.nextState = State.Start;
		tokens = "";
	}
	
	/**
	 * determines the tokens of the character sequence
	 * @param String representation of the program
	 * @return String of the tokens in the character sequence
	 * **/
	public String tokenize(String charSequence) {
		currentState = nextState = State.Start;
		tokens = "";
		char[] chars = charSequence.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			nextState = table[currentState.getIndex()][identifyChar(chars[i])];
			if (nextState != currentState && nextState != State.Start)
				tokens += nextState + " ";
			currentState = nextState;
			if (currentState == State.Error)
				break;
		}

		tokens = charSequence + " tokenizes as " + tokens;
		tokens = tokens.trim();
		tokens += "\n";
		return tokens;
	}

	/**
	 * Reads in the state transition table given its filename
	 * @param filename
	 * **/
	public void readInTable(String filename) {
		File f = new File(filename);
		try {
			Scanner sc = new Scanner(f);
			String line = sc.nextLine();
			String[] data = line.trim().split(",");
			table = new State[State.values().length][data.length];
			int row = 0;
			while (sc.hasNext()) {
				for (int col = 0; col < data.length; col++) {
					table[row][col] = State.valueOf(Integer.parseInt(data[col].trim()));
				}
				line = sc.nextLine();
				data = line.trim().split(",");
				row++;
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Identify a character as a letter, digit, operator, assignment, or white space
	 * @param the character to be identified
	 * @return integer that is the column index in the state transition table
	 * **/
	private int identifyChar(char c) {
		int INDEX = 6;
		if (Character.isAlphabetic(c))
			return INDEX = 1;
		else if (Character.isDigit(c))
			return INDEX = 2;
		else if ("+-*/".contains(String.valueOf(c)))
			return INDEX = 3;
		else if (String.valueOf(c).equals("="))
			return INDEX = 4;
		else if (Character.isWhitespace(c))
			return INDEX = 5;
		else
			return INDEX;
	}
}
