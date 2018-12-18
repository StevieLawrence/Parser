package MyParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * @author Steven Lawrence
 */
public class Recognizer {

	Map<String, String[]> grammar;
	String startState;
	String oldTokens, currentTokens;

	/**
	 * Constructor for a recognizer object that uses the grammar rules given to
	 * determine what belongs to the grammar
	 * 
	 * @param filename
	 *            the name of the file containing the grammar rules
	 */
	public Recognizer(String filename) {
		File f = new File(filename);
		List<String> grammarRules = new ArrayList<String>();
		try {
			Scanner sc = new Scanner(f);
			while (sc.hasNext()) {
				String line = sc.nextLine();
				grammarRules.add(line.trim());
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		grammar = new HashMap<String, String[]>(); // map non-terminals to their rules
		String[] rule;
		for (int i = 0; i < grammarRules.size(); i++) {
			rule = grammarRules.get(i).split(",");
			grammar.put(rule[0], Arrays.copyOfRange(rule, 1, rule.length));
			if (i == 0)
				startState = rule[0];
		}

	}

	/**
	 * Take the given string of tokens and determine if it belongs to the grammar or
	 * not
	 * 
	 * @param tokenStatement
	 *            the string of tokens
	 * @return a string that confirms if the statement is valid or invalid
	 */
	public String recognize(String tokenStatement) {
		tokenStatement = tokenStatement.replace("\n", "");
		if (tokenStatement.contains(" Error")) // no point if the statement isn't in the language
			return "";

		String[] tokenInfo = tokenStatement.split(" tokenizes as ");
		String charSequence = tokenInfo[0];
		currentTokens = tokenInfo[1];
		oldTokens = "";

		// while the token string has not been reduced to the startStatement
		while (!(oldTokens + currentTokens).equals(startState)) {
			findMatch(currentTokens); // search for a grammar rule match on the right hand side

			// If tokens were continuously removed with no matches in the search so that its
			// empty then its not in the grammar.
			if (currentTokens.equals("")) {
				return "\t\t" + charSequence + " is an Invalid Statement\n";
			}
		}
		return "\t\t" + charSequence + " is a Valid Statement\n";
	}

	/**
	 * Searches for a matching grammar rule. If no rule matches then remove the left
	 * most token.
	 * 
	 * @param tokenStr
	 *            the string of tokens
	 */
	public void findMatch(String tokenStr) {
		String[] rules;
		// loop through all of the grammar rules
		for (String key : grammar.keySet()) {
			rules = grammar.get(key); // get the rules per non-terminal
			for (String rule : rules) {
				// if a match is found then replace the current tokens with that rule
				if (tokenStr.equals(rule)) {
					currentTokens = oldTokens + " " + key;
					currentTokens = currentTokens.trim();
					oldTokens = "";
					// System.out.println("found: " + currentTokens);
					return;
				}
			}
		}
		// if no match is found then remove the left most token
		removeToken(tokenStr);
	}

	/**
	 * Remove the left most token from the current tokens.
	 * 
	 * @param tokenStr
	 *            a string that contains tokens
	 */
	public void removeToken(String tokenStr) {
		String[] splitT = tokenStr.split(" ", 2);

		// check to see if there is more than one token
		if (splitT.length > 1) {
			oldTokens += " " + splitT[0]; // add the removed token to oldTokens
			oldTokens = oldTokens.trim();
			currentTokens = splitT[1];
			// System.out.println("old: " + oldTokens + " new: " + currentTokens);
		} else
			currentTokens = ""; // if there is only one token left then set current tokens to empty
	}

}
