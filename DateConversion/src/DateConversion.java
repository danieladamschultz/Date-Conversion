import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class DateConversion {

	 public static void main(String[] args) throws FileNotFoundException {

	      // Establish a Scanner to read from a file whose name is specified 
	      // by either the 1st command line argument or, if it is absent, the 
	      // user in response to a prompt.
	      String fileName = getString(args, "Enter filename:>");
	      Scanner input = new Scanner(new File(fileName));

	      String tradDate;   // input (calendar date in traditional form)
	      String normalDate; // output (calendar date in normal form)

	      // Keep reading traditional calendar dates (one per line) until 
	      // there are no more.
	      while(input.hasNextLine()) {
	         tradDate = input.nextLine();
	         if(tradDate.length() > 0) {    //Ignore empty strings
	            //RWM: Each of the two lines below is an alternative to the other.
	            //normalDate = normalFormOf(tradDate);
	            normalDate = normalFormOf2(tradDate);
	            System.out.println(quoted(normalDate) + " : " + quoted(tradDate));
	         }
	      }
	      System.out.println("Goodbye.");
	   }

	   /* Given a calendar date in traditional form, (e.g., "June 14, 1987"),
	   ** returns the normal form for that date (e.g., "19870614").  This method
	   ** can handle certain "imperfections" in the given date, namely
	   ** --The letters in the month name can be any combination of upper
	   **   and lower case (e.g., "sePTemBer").
	   ** --The comma can be absent.
	   ** --The space following the comma can be absent.
	   ** --"Extra" spaces can precede or follow any of the three
	   **   vital elements of the date.  (E.g., "  May   14  ,  1945  ")
	   */
	   public static String normalFormOf(String date) {
	      final char BLANK = ' ';
	      final char COMMA = ',';

	      String yearNumeral, dayNumeral, monthName, monthNumeral;

	      // Remove leading and trailing spaces from the given date
	      date = date.trim();

	      int posOfLastSpace;

	      // Extract the year and remove suffix of 'date' following the day numeral.
	      int posOfComma = date.lastIndexOf(COMMA);
	      if (posOfComma == -1) {
	         // There's no comma, so the year is given by the trimmed suffix of 
	         // 'date' following the last occurrence of a blank.
	         posOfLastSpace = date.lastIndexOf(BLANK);
	         yearNumeral = paddedWithZeros(date.substring(posOfLastSpace + 1), 4);
	         // Now chop off everything following the day numeral.
	         date = date.substring(0, posOfLastSpace).trim();
	      }
	      else {
	         // There is a comma, so the year is given by the trimmed suffix of 
	         // 'date' following that comma.
	         yearNumeral = paddedWithZeros(date.substring(posOfComma + 1).trim(), 4);
	         // Now chop off everything following the day numeral.
	         date = date.substring(0, posOfComma).trim();
	      }

	      // At this point, date should end with the day numeral.
	      // Find the space preceding the day numeral; the rest of 'date'
	      // is the day numeral.
	      posOfLastSpace = date.lastIndexOf(BLANK);
	      dayNumeral = paddedWithZeros(date.substring(posOfLastSpace + 1), 2);

	      // The month is given by the trimmed prefix of 'date' preceding
	      // its last space.
	      monthName = date.substring(0, posOfLastSpace).trim();
	      int monthNumber = monthNameToNum(monthName);
	      monthNumeral = paddedWithZeros(monthNumber + "", 2);
	      return yearNumeral + monthNumeral + dayNumeral;
	   }


	   /* Alternative version of the method above.
	   */
	   public static String normalFormOf2(String date) {
	      char BLANK = ' ';
	      char COMMA = ',';

	      // Replace any comma by a space and then get rid of all extraneous
	      // spaces.  The result will be have the form of this example: 
	      // "June 14 2011".  That is, there will be exactly two spaces, and
	      // they will separate month from day and day from year, respectively.

	      date = date.replace(COMMA, BLANK);
	      date = trimAll(date);               //See trimAll() method below

	      // Now find where the two spaces are.
	      int posOf1stSpace = date.indexOf(BLANK);
	      int posOf2ndSpace = date.lastIndexOf(BLANK);

	      // Extract each of the three components.
	      String yearNumeral = date.substring(posOf2ndSpace+1);
	      String dayNumeral = date.substring(posOf1stSpace+1,posOf2ndSpace);
	      String monthName = date.substring(0,posOf1stSpace);

	      // Translate the month name into the corresponding number. 
	      int monthNumber = monthNameToNum(monthName);

	      // For each component, form the numeral with the correct # of digits.
	      String monthNumeral = paddedWithZeros(monthNumber + "", 2);
	      dayNumeral = paddedWithZeros(dayNumeral, 2);
	      yearNumeral = paddedWithZeros(yearNumeral, 4);

	      // Concatenate and return the result
	      return yearNumeral + monthNumeral + dayNumeral;
	   }

	   /* Returns the string obtained from the given one by removing all leading
	   ** and trailing spaces and by reducing every other run of spaces into a
	   ** single space.
	   */
	   private static String trimAll(String s) {
	      final char SPACE = ' ';
	      s = s.trim();  // Remove leading and trailing spaces.
	      // Now reduce every run of spaces into a single space
	      // by "keeping" only the first space in each run.
	      String result = "";
	      boolean prevIsSpace = true;
	      for (int i=0; i != s.length(); i = i+1) {
	         char ch = s.charAt(i);
	         if (ch != SPACE) {
	            result = result + ch;
	            prevIsSpace = false;
	         }
	         else if (prevIsSpace) {
	            // do nothing, as ch is a space that follows a space
	         }
	         else {  
	            // ch is the 1st space in a run, so place it into result
	            result = result + ch;
	            prevIsSpace = true;
	         }
	      }
	      return result;
	   }

	   /* Given the name of a month (with any combination of upper and lower
	   ** case letters), returns the corresponding month number.
	   */
	   private static int monthNameToNum(String monthName) {
	      monthName = monthName.toLowerCase();
	      if (monthName.equals("january")) { return 1; }
	      else if (monthName.equals("february")) { return 2; }
	      else if (monthName.equals("march")) { return 3; }
	      else if (monthName.equals("april")) { return 4; }
	      else if (monthName.equals("may")) { return 5; }
	      else if (monthName.equals("june")) { return 6; }
	      else if (monthName.equals("july")) { return 7; }
	      else if (monthName.equals("august")) { return 8; }
	      else if (monthName.equals("september")) { return 9; }
	      else if (monthName.equals("october")) { return 10; }
	      else if (monthName.equals("november")) { return 11; }
	      else if (monthName.equals("december")) { return 12; }
	      else { return 0; }
	   }

	   /* Returns the string formed by prepending onto the given string as 
	   ** many leading 0's as are necessary to produce a string of the 
	   ** given desired length.  (Example: If str is "17" and desiredLen is 6, 
	   ** the result is "000017".)  If the given string is longer than the 
	   ** desired length, it is returned.
	   */
	   public static String paddedWithZeros(String str, int desiredLen) {
	      final char ZERO = '0';
	      String result;
	      if (str.length() < desiredLen) {
	         result = repeatedChar(ZERO, desiredLen - str.length()) + str;
	      }
	      else {
	         result = str;
	      }
	      return result;
	   }

	   /* Returns the string formed by prepending onto the given string as 
	   ** many occurrences of the given character are necessary to produce a 
	   ** string of the desired length.  (Example: If str is "abcd", desiredLen
	   ** is 9, and ch is '$', the result is "$$$$$abcd".)  If the given
	   ** string is longer than the desired length, it is returned.
	   */
	/* This method not needed, as the more specialized paddedWithZeros() method
	   is used instead.

	   private static String padLeft(String str, int desiredLen, char ch) {
	      if (str.length() < desiredLen) {
	         return repeatedChar(ch, desiredLen - str.length()) + str;
	      }
	      else {
	         return str;
	      }
	   }
	*/

	   /* Returns a string composed of n occurrences of ch.
	   ** (If n is zero or less, the empty string is returned.)
	   */
	   private static String repeatedChar(char ch, int n) {
	      StringBuilder result = new StringBuilder();
	      for (int i = 1; i <= n; i = i+1) {
	         result.append(ch);
	      }
	      return result.toString();
	   }


	   /* Given an integer numeral (i.e., a nonempty string each of whose 
	   ** characters is in the range '0'..'9', such as "456"), returns the 
	   ** corresponding int value (e.g., 456).  If the given string is not an
	   ** integer numeral, zero is returned.
	   */
	/* This method not neded.

	   private static int integerOf(String number) {
	      int result;
	      try {
	         result = Integer.parseInt(number);
	      }
	      catch (NumberFormatException e) {
	         result = 0;
	      }
	      return result;   
	   }
	*/
	   
	   /* Prints the given string (intended to be a user prompt) and returns
	   ** a line read using the given Scanner (intended to be the response).
	   */
	   private static String getString(String prompt, Scanner keyboard) {
	      System.out.print(prompt);
	      return keyboard.nextLine();
	   }
	   
	   /* If the given array of strings (expected to hold the command line
	   ** arguments) is nonempty, returns the zero-th element of that array.
	   ** Otherwise, prompts the user to enter a string at the keyboard and
	   ** returns the user's (trimmed) response.
	   ** (Note: In the context of this program, this method is called for 
	   ** the purpose of returning the name of the input file.)
	   */
	   private static String getString(String[] args, String prompt) {
	      String result;
	      if (args.length > 0) {
	         result = args[0];
	      } else {
	         Scanner keyboard = new Scanner(System.in);
	         result = getString(prompt,keyboard).trim();
	      }
	      return result;
	   }
	   

	   /* Returns the given string embedded between a pair of double quotes.
	   */
	   public static String quoted(String s) {
	      final char QUOTE = '\"';
	      return QUOTE + s + QUOTE;
	   }
	   
	}