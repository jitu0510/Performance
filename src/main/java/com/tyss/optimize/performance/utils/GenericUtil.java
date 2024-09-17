package com.tyss.optimize.performance.utils;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.tyss.optimize.performance.dto.PreProcessor;

public class GenericUtil {
	
    private static final String ALPHABETS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{}|;:,.<>?/";
	
	 public String determineTargetType(String jsonString) {
	        if (jsonString.contains("simpleControllerName"))
	            return "SimpleController";
	        else if (jsonString.contains("ifControllerName"))
	            return "IfController";
	        else if (jsonString.contains("forEachControllerName"))
	        	 return "ForEachController";
	        else if(jsonString.contains("onlyOnceControllerName"))
	        	 return "OnlyOnceController";
	        else if(jsonString.contains("throughputControllerName"))
	        	 return "ThroughputController";
	        else if(jsonString.contains("transactionControllerName"))
	        	 return "TransactionController";
	        else if(jsonString.contains("whileControllerName"))
	        	return "WhileController";
	        	        
	        return "Unknown";
	    }
	 
	 public void generateRandomCharacters(PreProcessor preProcessor) {
		 SecureRandom random = new SecureRandom();
	        StringBuilder sb = new StringBuilder(preProcessor.getLength());
	        String allCharacters = "";
	        int iterator = 0;

	        // Ensure at least one of each character type
	        if (preProcessor.isAlphabets() && preProcessor.isNumbers() && preProcessor.isSpecialCharacters() && preProcessor.getLength() >= 3) {
	            sb.append(ALPHABETS.charAt(random.nextInt(ALPHABETS.length())));
	            sb.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
	            sb.append(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));
	            iterator = 3;
	        } else if (preProcessor.isAlphabets() && preProcessor.isNumbers() && !preProcessor.isSpecialCharacters() && preProcessor.getLength() >= 2) {
	            sb.append(ALPHABETS.charAt(random.nextInt(ALPHABETS.length())));
	            sb.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
	            iterator = 2;
	        } else if (preProcessor.isAlphabets() && preProcessor.isSpecialCharacters() && !preProcessor.isNumbers() && preProcessor.getLength() >= 2) {
	            sb.append(ALPHABETS.charAt(random.nextInt(ALPHABETS.length())));
	            sb.append(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));
	            iterator = 2;
	        } else if (!preProcessor.isAlphabets() && preProcessor.isSpecialCharacters() && preProcessor.isNumbers() && preProcessor.getLength() >= 2) {
	            sb.append(NUMBERS.charAt(random.nextInt(NUMBERS.length())));
	            sb.append(SPECIAL_CHARACTERS.charAt(random.nextInt(SPECIAL_CHARACTERS.length())));
	            iterator = 2;
	        }

	        // Build the pool of all characters
	        if (preProcessor.isAlphabets()) {
	            allCharacters += ALPHABETS;
	        }
	        if (preProcessor.isNumbers()) {
	            allCharacters += NUMBERS;
	        }
	        if (preProcessor.isSpecialCharacters()) {
	            allCharacters += SPECIAL_CHARACTERS;
	        }

	        // Fill the remaining characters
	        for (int i = iterator; i < preProcessor.getLength(); i++) {
	            sb.append(allCharacters.charAt(random.nextInt(allCharacters.length())));
	        }

	        // Shuffle the characters to ensure randomness
	        List<Character> charList = new ArrayList<>();
	        for (char c : sb.toString().toCharArray()) {				
	            charList.add(c);
	        }
	        Collections.shuffle(charList);

	        StringBuilder finalString = new StringBuilder(preProcessor.getLength());
	        for (char c : charList) {
	            finalString.append(c);
	        }

	        preProcessor.getRandomOutputString().setValue(finalString.toString());
	 }
	 
	

}
