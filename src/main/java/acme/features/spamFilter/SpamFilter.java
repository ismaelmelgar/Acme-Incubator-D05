
package acme.features.spamFilter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpamFilter {

	public String[] spamWordPieces(final String spamWord) {
		String[] spamWordPieces = spamWord.toLowerCase().split(",");
		return spamWordPieces;
	}

	public boolean isFreeSpam(final String[] separateSpamWord, final String text, final double threshold) {
		String[] textPieces = text.split(" ");

		int numApparitions = 0;
		for (String spamer : separateSpamWord) {
			String regex = spamer.trim(); //Expresion Regular
			Pattern patron = Pattern.compile(regex); //Convierte la expresion regular dada en un patron
			Matcher match = patron.matcher(text); //Objeto Matcher para encontrar coincidencias
			while (match.find()) { //Cuenta coincidencuias hasta que ya no queden
				if (match.group().equals("sexo")) {
					//No suma porque la expresion regular tambien encuentra "sex" y se contaria doble
				} else {
					numApparitions++;
				}

			}

		}

		//Es un texto prohibitivo si 1/10 palabras es spam (umbral al 0.1)
		boolean freeSpam = 10 * numApparitions / textPieces.length < threshold;
		return freeSpam;

	}

}
