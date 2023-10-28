package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Parser {
	public static Map<String, String> valueStore;
	
	//instead of using tokens define a type using sets
	public static Set<String> availableTokens;
	public static Set<Character> allowedEndLineTokens;
	public static Set<Character> operatorToken;
	public static Set<Character> extraOperators;
	
	
	//syntax
	public static Set<String> allowedFunctionSyntax;
	public static Set<String> allowedDeclaredSyntax;
	public static Set<String> allowedIntegerOperationSyntax;
	public static Set<String> allowedLoopSyntax;
	
	//Maps that keep track of Declared Variable and Functions
	public static Set<String> declaredFunction;
	public static Map<String, Integer> integerValues;
	public static Map<String, String> stringValues;
	
	//extra data sent to other classes
	public static String error;
	public static ArrayList<String[]> fileParsed;
	
	
	public Parser() {
		fileParsed = new ArrayList<>();
		
		//creating declaring Function Set
		declaredFunction = new HashSet<>();
		
		//creating the map of integer and string declared variables
		integerValues = new HashMap<>();
		stringValues = new HashMap<>();
		
		//creating the expression syntax
		allowedDeclaredSyntax = new HashSet<>();
		allowedLoopSyntax = allowedLoopSyntax();
		allowedFunctionSyntax = allowedFunctionSyntax();
		
		// setting up values that are axiomatically defined
		valueStore = setUpMapping();
		availableTokens = setUpAvailableTokens();
		allowedEndLineTokens = setUpAllowedEndOfLineTokens();
		operatorToken = setupOperatorToken();
		extraOperators = setUpExtraOperators();
		error = "";
	}
	private Set<Character> setupOperatorToken(){
		Set<Character> OperatorToken = new HashSet<>();
		OperatorToken.add('+');
		OperatorToken.add('/');
		OperatorToken.add('-');
		OperatorToken.add('*');
		OperatorToken.add('%');
		
		return OperatorToken;
	}
	
	private Set<Character> setUpExtraOperators() {
		Set<Character> Tokens = new HashSet<>();  
		Tokens.add('/');
		Tokens.add('%');
		Tokens.add('*');
		Tokens.add('(');
		Tokens.add(')');
		Tokens.add('"');
		
		return Tokens;
	}
	
	private Map<String, String> setUpMapping(){
		Map<String, String> kind = new HashMap<>();
		kind.put("int", "declarer");
		kind.put("String", "declarer");
		kind.put("public", "function");
		kind.put("private", "function");
		kind.put("protected", "function");
		kind.put("static", "function");
		kind.put("if", "condition");
		kind.put("for", "loop");
		kind.put("}", "endOfLine");
		kind.put("System.out.println", "print");
		return kind;
	}
	
	private Set<String> setUpAvailableTokens() {
		Set<String> Tokens = new HashSet<>();

		Tokens.add("int");
		Tokens.add("String");
		Tokens.add("for");
		Tokens.add("if");
		Tokens.add("}");
		Tokens.add("System.out.println");
		
		return Tokens;
	}
	
	private Set<Character> setUpAllowedEndOfLineTokens(){
		Set<Character> endTokens = new HashSet<>();
		
		endTokens.add('{');
		endTokens.add('}');
		endTokens.add(';');
		
		return endTokens;
	}
	
	private Set<String> allowedFunctionSyntax(){
		// can only support void methods
		Set<String> ExpressionSyntax = new HashSet<>();
		ExpressionSyntax.add("public static");
		ExpressionSyntax.add("public static void");
		ExpressionSyntax.add("public void");
		ExpressionSyntax.add("void");
		ExpressionSyntax.add("static void");
		//for private methods
		ExpressionSyntax.add("protected static void");
		ExpressionSyntax.add("protected void");
		
		//for protected methods
		ExpressionSyntax.add("public static void");
		ExpressionSyntax.add("public void");
		
		return ExpressionSyntax;
	}
	private static void allowedDeclaredSyntax(String VariableSyntax){
		
		//for integer data types
		allowedDeclaredSyntax.add("int " + VariableSyntax + " =");
		allowedDeclaredSyntax.add("int "+ VariableSyntax);
		
		allowedDeclaredSyntax.add("String " + VariableSyntax + " =");
		allowedDeclaredSyntax.add(VariableSyntax + " =");
		allowedDeclaredSyntax.add(VariableSyntax +"();");
	}
	
	private Set<String> allowedLoopSyntax(){
		Set<String> ExpressionSyntax = new HashSet<>();
		ExpressionSyntax.add("for ( int");
		return ExpressionSyntax;
	}
	
	public static void parseAndConvert(String input) throws Exception {
		String[] inputtoArr = input.split("\\r?\\n|\\r");
		if (inputtoArr.length == 1) {
			error = "Can't Parse a one line code";
			System.out.println(Arrays.toString(inputtoArr));
			throw new Exception("Can't Parse a one line code");
		}
		for(String lines: inputtoArr) {
			ArrayList<String> lexedLine = Lexer(lines);
			if (lexedLine.size() != 0) {
				lexedLine.add(checkOperation(lexedLine.get(0)));
				if (lexedLine.get(lexedLine.size() -1).equals("function")) {
					System.out.println("Checking FunctionSyntax: ");
					lexedLine.remove(lexedLine.size() - 1);
					lexedLine.add(checkMethodSyntax(lexedLine));
					lexedLine.add("function");
				}else if (lexedLine.get(lexedLine.size() -1).equals("declarer")) {
					System.out.println("Evaluating Expressions: ");
					lexedLine.remove(lexedLine.size() - 1);
					char operation = checkDeclareSyntax(lexedLine);
					System.out.println(operation);
					if (operation == ' ') {
						lexedLine.add("declarer");
					}else if(operation == 'f') {
						lexedLine.add("functionCall");
					}else {
						lexedLine.add("declareOperation");
					}
				}else if (lexedLine.get(lexedLine.size() - 1).equals("condition")) {
					lexedLine.remove(lexedLine.size() - 1);
					checkConditionSyntax(lexedLine);
					lexedLine.add("conditional");
				}else if (lexedLine.get(lexedLine.size() - 1).equals("loop")) {
					System.out.println("Checking loop");
					lexedLine.remove(lexedLine.size() - 1);
					lexedLine.add(evaluateLoopExpression(lexedLine));
					lexedLine.add("loop");
				}else if (lexedLine.get(lexedLine.size() - 1).equals("print")) {
					lexedLine.remove(lexedLine.size() - 1);
					evaluatePrintExpression(lexedLine);
					lexedLine.add("print");
				}
				//convert to string[]
				String[] line = new String[lexedLine.size()];
				
				for (int i = 0; i < line.length; i++) {
					line[i] = lexedLine.get(i);
				}
				fileParsed.add(line);
				System.out.println(lexedLine);
			}
				
		}
			
	}
	
	
	
	private static void evaluatePrintExpression(ArrayList<String> lexedLine) throws Exception{
		int position = 1;
		
		if(!lexedLine.get(position).equals("("))
			throw new Exception("ERROR: Invalid Print Syntax Opening: ");
		if(!lexedLine.get(lexedLine.size() - 1).equals(";"))
			throw new Exception("ERROR: Illegal Closing Syntax for a print statement");
		
		position++;
		String innerStatement = "";
		while(position < lexedLine.size()) {
			if(lexedLine.get(position).equals(")")) break;
			
			innerStatement += lexedLine.get(position);
			position++;
			
		}
		System.out.println(innerStatement + " Hello");
		if(position != lexedLine.size() - 2)
			throw new Exception("ERROR: Print Statement Hasn't reached end of line <" + lexedLine + ">");
		
		if (innerStatement.equals(""))
			System.out.println("Printed new line");
		else{
			System.out.println("Printing value: ");
			System.out.println(innerStatement);
		}
	}
	
	private static ArrayList<String> Lexer(String split) throws Exception {
		//since we wont know what the string would be like after white space removal
		String potentialCommandOrOperator = "";
		List<String> parsedLine = new ArrayList<>();
		String returnedString[];
		int i = 0;
		
		while (i < split.length()) {
			if(Character.isWhitespace(split.charAt(i))) {
				while (i < split.length() && Character.isWhitespace(split.charAt(i))) i++;
			}else if(Character.isLetterOrDigit(split.charAt(i)) || split.charAt(i) == '.') {
				while(i < split.length() && Character.isLetterOrDigit(split.charAt(i)) || split.charAt(i) == '.'){
					potentialCommandOrOperator += Character.toString(split.charAt(i));
					i++;
				}
			}else if(Character.isDigit(split.charAt(i))) {
				while (i < split.length() && Character.isDigit(split.charAt(i))) {
					potentialCommandOrOperator += Character.toString(split.charAt(i));
					i++;
				}
			}else if(split.charAt(i) == '+') {
				if (i < split.length() - 1) {
					i++;
					if(split.charAt(i) == '+') {
						potentialCommandOrOperator = "++";
						i++;
					}
					else potentialCommandOrOperator = "+";
				}else potentialCommandOrOperator = "+"; 
				
			}else if(split.charAt(i) == '-') {
				if (i < split.length() - 1) {
					i++;
					if(split.charAt(i) == '-') {
						potentialCommandOrOperator = "--";
						i++;
					}
					else potentialCommandOrOperator = "-";
				}else potentialCommandOrOperator = "-"; 
				
			}else if(split.charAt(i) == '<') {
				if (i < split.length() - 1) {
					i++;
					if(split.charAt(i) == '=') {
						potentialCommandOrOperator = "<=";
						i++;
					}
					else potentialCommandOrOperator = "<";
				}else potentialCommandOrOperator = "<"; 
				
			}else if(split.charAt(i) == '>') {
				if (i < split.length() - 1) {
					i++;
					if(split.charAt(i) == '=') {
						potentialCommandOrOperator = ">=";
						i++;
					}
					else potentialCommandOrOperator = ">";
				}else potentialCommandOrOperator = ">"; 
				
			}else if(split.charAt(i) == '=') {
				if (i < split.length() - 1) {
					i++;
					if(split.charAt(i) == '=') {
						potentialCommandOrOperator = "==";
						i++;
					}
					else potentialCommandOrOperator = "=";
				}else potentialCommandOrOperator = "="; 
				
			}else if(split.charAt(i) == '&') {
				if (i < split.length() - 1) {
					i++;
					if(split.charAt(i) == '&') {
						potentialCommandOrOperator = "&&";
						i++;
					}
					else potentialCommandOrOperator = "&";
				}else potentialCommandOrOperator = "&"; 
				
			}else if(split.charAt(i) == '|') {
				if (i < split.length() - 1) {
					i++;
					if(split.charAt(i) == '|') {
						potentialCommandOrOperator = "||";
						i++;
					}
					else potentialCommandOrOperator = "|";
				}else potentialCommandOrOperator = "|"; 
				
			}else if(extraOperators.contains(split.charAt(i)) ||
					allowedEndLineTokens.contains(split.charAt(i))) {
				potentialCommandOrOperator = Character.toString(split.charAt(i));
				i++;		
			}else {
				throw new Exception("ERROR: UnidentifiedToken < " + split.charAt(i) + " >");
			}
			
			if (parsedLine.size() == 0
					&& !availableTokens.contains(potentialCommandOrOperator)
					&& !potentialCommandOrOperator.matches("^[a-zA-Z]*$")) {
				throw new Exception ("ERROR: Illegal Start of Expression <" + potentialCommandOrOperator + " >");
			}
			if (!potentialCommandOrOperator.equals("")) {
				parsedLine.add(potentialCommandOrOperator);
				potentialCommandOrOperator = "";
			}
			//check syntax
		}
		returnedString = new String[parsedLine.size()];
		
		for (int j = 0; j < returnedString.length; j++) {
			returnedString[j] = parsedLine.get(j);

		}
		if (parsedLine.size() != 0) {
			if (!allowedEndLineTokens.contains(returnedString[returnedString.length - 1].charAt(0))) {
				throw new Exception ("ERROR: End line token Unidentified < " + returnedString[returnedString.length - 1].charAt(0) + " >");
			}
		}
		return (ArrayList<String>) parsedLine;
		
	}
	
	
	public static String checkOperation(String beginningOfLine) throws Exception {
		if (valueStore.containsKey(beginningOfLine)) return valueStore.get(beginningOfLine);
		if (beginningOfLine.matches("^[a-zA-Z]*$")) return "declarer";
		throw new Exception ("Used Illegal closing syntax for a line <" + beginningOfLine + " >");
	}
	
	
	private static void checkConditionSyntax(ArrayList<String> lexedLine) throws Exception{
		int position = 1;
		if(!lexedLine.get(position).equals("(")) throw new Exception ("ERROR: Illegal start of condition statements <"+ lexedLine+">");
		if(!lexedLine.get(lexedLine.size() - 1).equals("{")) {
			throw new Exception ("ERROR: Illegal declaration of condition statement <"+ lexedLine + ">");
		}
		position++;
		ArrayList<String> comparedTokens = new ArrayList<>();
		String BooleanOperation = "";
		while (position < lexedLine.size() && !lexedLine.get(position).equals(")")){
			if(position != lexedLine.size() - 2 && lexedLine.get(position).equals(")")) 
				//since we wont be checking for multiple conditions one bracket should satisfy
				//so throw an error whenever you get a ) before end of condition
				throw new Exception("ERROR: Unbalanced parentheses error");
			if(!lexedLine.get(position).matches("^[a-zA-Z0-9]*$")) {
				BooleanOperation = lexedLine.get(position);
			}
			else 
				comparedTokens.add(lexedLine.get(position));
			position ++;
		}
		if (BooleanOperation.length() == 0) throw new Exception ("ERROR: Boolean operator not given <" + lexedLine + ">");
		if (comparedTokens.size() != 2) throw new Exception ("ERROR: Expected two arguments found: <" + comparedTokens.size() + "> <" + lexedLine + ">");
		System.out.println(BooleanOperation);
		System.out.println(comparedTokens);
		evaluateBooleanExpression(BooleanOperation, comparedTokens);
	}
	
	
	
	public static String checkMethodSyntax(ArrayList<String> line) throws Exception {
		String modifier = line.get(0); 
		int position = 1;
		boolean foundVoid = false;
		while (position < line.size() && allowedFunctionSyntax.contains(modifier + " " + line.get(position))) {
			modifier += " " + line.get(position);
			if (line.get(position).equals("void")) foundVoid = true;
			position ++;
		}
		if (availableTokens.contains(line.get(position)))
				throw new Exception("ERROR: Illegal use of data types as names for method < " + line.get(position)+" >");
		if(!line.get(position).matches("^[a-zA-Z]*$")) 
			throw new Exception ("ERROR: Illegal declaration of Method < " + line.get(position) + ">");
		
		//check for open and closed parenthesis
		//function won't accept any input 
		//it should be guaranteed that it has a 'void' return type
		//should also be guaranteed that it doesn't use a data type as a name
		
		if (!foundVoid) throw new Exception ("ERROR: Method has null return type: < " + line.get(position)+ " >");
		
		String functionName = line.get(position);
		if(declaredFunction.contains(functionName))
			throw new Exception("ERROR: Method name has been used in previous for another method: < " + functionName +" >");
		position ++;
		String ExpectedClosingTag = "(){";
		String OutPutClosingTag = "";
		
		while (position < line.size() && !line.get(position).equals("function")) {
			OutPutClosingTag += line.get(position);
			position ++;
		}
		if(!OutPutClosingTag.equals(ExpectedClosingTag))
			throw new Exception ("ERROR: Illegal Expression closing <" + OutPutClosingTag + " >");
		
		declaredFunction.add(functionName);
		System.out.println("Defined function: " + functionName);
		return functionName;
		
	}
	
	public static char checkDeclareSyntax(ArrayList<String> line) throws Exception{
		int position = 0;
		if (line.get(0).equals("String") || line.get(0).equals("int"))
			position = 1;
		String assumedVariableName = "";
		char operatorType = ' ';
		while(position < line.size()){
			allowedDeclaredSyntax(line.get(position));
			if (line.get(position).equals("=") || line.get(position).equals("("))break;
			assumedVariableName += line.get(position);
			position ++;
		} 
		// first check if the declared value is a function call
		if(position < line.size() && line.get(position).equals("(")){
			System.out.println("here");
			String bracket = "";
			while(position < line.size()) {
				allowedDeclaredSyntax(line.get(position));
				bracket += line.get(position);
				position ++;
			}
			System.out.println(assumedVariableName + bracket);
			if(position == line.size() && allowedDeclaredSyntax.contains(assumedVariableName + bracket)) {
				if(!declaredFunction.contains(assumedVariableName))
					throw new Exception("ERROR: Invalid function call <" + assumedVariableName + ">");
				return 'f';
			}
		}
		if(position < line.size() &&!line.get(position).equals("="))
			throw new Exception ("ERROR: Unknown Assignment Type <" + line.get(position) + ">");
		if(!allowedDeclaredSyntax.contains(assumedVariableName + " ="))
			throw new Exception ("ERROR: tried to declare two variables in one line <" + line + ">");
		//here we have to define another method that returns a value 
		// define two methods one for string and one for integer
		// First hold everything in a string and then send it depending on what data type its on
		String rightExpression = "";
		position ++;
		while (position < line.size() && !line.get(position).equals(";")) {
			rightExpression += line.get(position);
			position ++;
		}
		if (position < line.size() - 1) throw new Exception ("ERROR: Stopped Before end of line was reached <" + line + ">");
		
		if (line.get(0).equals("int")) {
			if (integerValues.containsKey(assumedVariableName)|| stringValues.containsKey(assumedVariableName))
				throw new Exception("ERROR: Tried to declare a previously declared variable: <"+ assumedVariableName + ">");
			operatorType = evaluateIntegerExpression(assumedVariableName, rightExpression);
		}else if(line.get(0).equals("String")){
			if (stringValues.containsKey(assumedVariableName))
				throw new Exception("ERROR: Tried to declare a previously declared variable: <"+ assumedVariableName + ">");
			evaluateStringExpression(assumedVariableName, rightExpression);
		}else {
			if (rightExpression.charAt(0) != '"') {
				if(!integerValues.containsKey(assumedVariableName) && !stringValues.containsKey(assumedVariableName))
					throw new Exception("ERROR: Tried to use a variable before assignment <" + assumedVariableName + ">");
				operatorType = evaluateIntegerExpression(assumedVariableName, rightExpression);
			}
			else {
				throw new Exception("Strings are immutable cannot reassign value");
			}
		}
		return operatorType;
	}
	
	private static char evaluateIntegerExpression(String variableName,String rightExpression) throws Exception {
		int position = 0;
		char operatorToken = ' ';
		while(position < rightExpression.length()) {
			if(!Character.isLetterOrDigit(rightExpression.charAt(position))) {
				operatorToken  = rightExpression.charAt(position);
				break;
			}
			position ++;
		}
		
		String[] numericOperations;
		if (operatorToken == ' ') {
			if(rightExpression.matches("^[a-zA-Z]*$" )&& !integerValues.containsKey(rightExpression))
				throw new Exception("Cant Operate on a value that is not declared");
			if(integerValues.containsKey(rightExpression)) {	
				System.out.println("Declared " + variableName+ ": "+ integerValues.get(rightExpression));
				integerValues.put(variableName, integerValues.get(rightExpression));
			}
			else {
				System.out.println("Declared " + variableName+ ": "+ rightExpression);
				integerValues.put(variableName, Integer.parseInt(rightExpression));
			}
		}
		if (operatorToken == '+') {
			numericOperations = new String[2];
			numericOperations[0] = rightExpression.substring(0, position);
			numericOperations[1] = rightExpression.substring(position + 1, rightExpression.length());
			
			int result = 0;
			if(!numericOperations[0].matches("^[a-zA-Z0-9]*$") || !numericOperations[1].matches("^[a-zA-Z0-9]*$"))
				throw new Exception("Cant Operate on unknown values or data types: <" + Arrays.toString(numericOperations)+ ">");
			if(numericOperations[0].matches("^[a-zA-Z]*$") && !integerValues.containsKey(numericOperations[0])) {
				throw new Exception("Cant Operate on a value that is not declared");
			}
			if(numericOperations[1].matches("^[a-zA-Z]*$") && !integerValues.containsKey(numericOperations[1])) {
				throw new Exception("Cant Operate on a value that is not declared");
			}
			if(integerValues.containsKey(numericOperations[0]) && !integerValues.containsKey(numericOperations[1])) {
				 result = integerValues.get(numericOperations[0]) +  Integer.parseInt(numericOperations[1]);
				System.out.println("Declared " + variableName + ": " + result);
			}else if(!integerValues.containsKey(numericOperations[0]) && integerValues.containsKey(numericOperations[1])) {
				 result = integerValues.get(numericOperations[1]) +  Integer.parseInt(numericOperations[0]);
				System.out.println("Declared " + variableName + ": " + result);
			}else if(integerValues.containsKey(numericOperations[0]) && integerValues.containsKey(numericOperations[1])) {
				 result = integerValues.get(numericOperations[0]) +  integerValues.get(numericOperations[1]);
				System.out.println("Declared " + variableName + ": " + result);
			}else if(!integerValues.containsKey(numericOperations[0]) && !integerValues.containsKey(numericOperations[1])) {
				 result = Integer.parseInt(numericOperations[1]) +  Integer.parseInt(numericOperations[0]);
				System.out.println("Declared " + variableName + ": " + result);
			}
			integerValues.put(variableName, result);
		}else if (operatorToken == '-') {
			numericOperations = rightExpression.split("-");
			int result = 0;
			if(!numericOperations[0].matches("^[a-zA-Z0-9]*$") || !numericOperations[1].matches("^[a-zA-Z0-9]*$"))
				throw new Exception("Cant Operate on unknown values or data types: <" + Arrays.toString(numericOperations)+ ">");
			if(numericOperations[0].matches("^[a-zA-Z]*$") && !integerValues.containsKey(numericOperations[0])) {
				throw new Exception("Cant Operate on a value that is not declared");
			}
			if(numericOperations[1].matches("^[a-zA-Z]*$") && !integerValues.containsKey(numericOperations[1])) {
				throw new Exception("Cant Operate on a value that is not declared");
			}
			if(integerValues.containsKey(numericOperations[0]) && !integerValues.containsKey(numericOperations[1])) {
				 result = integerValues.get(numericOperations[0]) -  Integer.parseInt(numericOperations[1]);
				System.out.println("Declared " + variableName + ": " + result);
			}else if(!integerValues.containsKey(numericOperations[0]) && integerValues.containsKey(numericOperations[1])) {
				 result = integerValues.get(numericOperations[1]) -  Integer.parseInt(numericOperations[0]);
				System.out.println("Declared " + variableName + ": " + result);
			}else if(integerValues.containsKey(numericOperations[0]) && integerValues.containsKey(numericOperations[1])) {
				 result = integerValues.get(numericOperations[0]) -  integerValues.get(numericOperations[1]);
				System.out.println("Declared " + variableName + ": " + result);
			}else if(!integerValues.containsKey(numericOperations[0]) && !integerValues.containsKey(numericOperations[1])) {
				 result = Integer.parseInt(numericOperations[1]) - Integer.parseInt(numericOperations[0]);
				System.out.println("Declared " + variableName + ": " + result);
			}
			integerValues.put(variableName, result);
		}else if (operatorToken == '/') {
			numericOperations = rightExpression.split("/");
			int result = 0;
			if(!numericOperations[0].matches("^[a-zA-Z0-9]*$") || !numericOperations[1].matches("^[a-zA-Z0-9]*$"))
				throw new Exception("Cant Operate on unknown values or data types: <" + Arrays.toString(numericOperations)+ ">");
			if(numericOperations[0].matches("^[a-zA-Z]*$") && !integerValues.containsKey(numericOperations[0])) {
				throw new Exception("Cant Operate on a value that is not declared");
			}
			if(numericOperations[1].matches("^[a-zA-Z]*$") && !integerValues.containsKey(numericOperations[1])) {
				throw new Exception("Cant Operate on a value that is not declared");
			}
			if(integerValues.containsKey(numericOperations[0]) && !integerValues.containsKey(numericOperations[1])) {
				result = integerValues.get(numericOperations[0]) /  Integer.parseInt(numericOperations[1]);
				System.out.println("Declared " + variableName + ": " + result);
			}else if(!integerValues.containsKey(numericOperations[0]) && integerValues.containsKey(numericOperations[1])) {
				result = integerValues.get(numericOperations[1]) /  Integer.parseInt(numericOperations[0]);
				System.out.println("Declared " + variableName + ": " + result);
			}else if(integerValues.containsKey(numericOperations[0]) && integerValues.containsKey(numericOperations[1])) {
				result = integerValues.get(numericOperations[0]) /  integerValues.get(numericOperations[1]);
				System.out.println("Declared " + variableName + ": " + result);
			}else if(!integerValues.containsKey(numericOperations[0]) && !integerValues.containsKey(numericOperations[1])) {
				result = Integer.parseInt(numericOperations[1]) / Integer.parseInt(numericOperations[0]);
				System.out.println("Declared " + variableName + ": " + result);
			}
			integerValues.put(variableName, result);
		}else if (operatorToken == '*') {
			numericOperations = new String[2];
			numericOperations[0] = rightExpression.substring(0, position);
			numericOperations[1] = rightExpression.substring(position + 1, rightExpression.length());
			
			int result = 0;
			if(!numericOperations[0].matches("^[a-zA-Z0-9]*$") || !numericOperations[1].matches("^[a-zA-Z0-9]*$"))
				throw new Exception("Cant Operate on unknown values or data types: <" + Arrays.toString(numericOperations)+ ">");
			if(numericOperations[0].matches("^[a-zA-Z]*$") && !integerValues.containsKey(numericOperations[0])) {
				throw new Exception("Cant Operate on a value that is not declared");
			}
			if(numericOperations[1].matches("^[a-zA-Z]*$") && !integerValues.containsKey(numericOperations[1])) {
				throw new Exception("Cant Operate on a value that is not declared");
			}
			if(integerValues.containsKey(numericOperations[0]) && !integerValues.containsKey(numericOperations[1])) {
				result = integerValues.get(numericOperations[0]) *  Integer.parseInt(numericOperations[1]);
				System.out.println("Declared " + variableName + ": " + result);
			}else if(!integerValues.containsKey(numericOperations[0]) && integerValues.containsKey(numericOperations[1])) {
				result = integerValues.get(numericOperations[1]) *  Integer.parseInt(numericOperations[0]);
				System.out.println("Declared " + variableName + ": " + result);
			}else if(integerValues.containsKey(numericOperations[0]) && integerValues.containsKey(numericOperations[1])) {
				result = integerValues.get(numericOperations[0]) *  integerValues.get(numericOperations[1]);
				System.out.println("Declared " + variableName + ": " + result);
			}else if(!integerValues.containsKey(numericOperations[0]) && !integerValues.containsKey(numericOperations[1])) {
				result = Integer.parseInt(numericOperations[1]) * Integer.parseInt(numericOperations[0]);
				System.out.println("Declared " + variableName + ": " + result);
			}
			integerValues.put(variableName, result);
		}else if (operatorToken == '%') {
			numericOperations = rightExpression.split("%");
			int result = 0;
			if(!numericOperations[0].matches("^[a-zA-Z0-9]*$") || !numericOperations[1].matches("^[a-zA-Z0-9]*$"))
				throw new Exception("Cant Operate on unknown values or data types: <" + Arrays.toString(numericOperations)+ ">");
			if(numericOperations[0].matches("^[a-zA-Z]*$") && !integerValues.containsKey(numericOperations[0])) {
				throw new Exception("Cant Operate on a value that is not declared");
			}
			if(numericOperations[1].matches("^[a-zA-Z]*$") && !integerValues.containsKey(numericOperations[1])) {
				throw new Exception("Cant Operate on a value that is not declared");
			}
			if(integerValues.containsKey(numericOperations[0]) && !integerValues.containsKey(numericOperations[1])) {
				result = integerValues.get(numericOperations[0]) %  Integer.parseInt(numericOperations[1]);
				System.out.println("Declared " + variableName + ": " + result);
			}else if(!integerValues.containsKey(numericOperations[0]) && integerValues.containsKey(numericOperations[1])) {
				result = integerValues.get(numericOperations[1]) %  Integer.parseInt(numericOperations[0]);
				System.out.println("Declared " + variableName + ": " + result);
			}else if(integerValues.containsKey(numericOperations[0]) && integerValues.containsKey(numericOperations[1])) {
				result = integerValues.get(numericOperations[0]) %  integerValues.get(numericOperations[1]);
				System.out.println("Declared " + variableName + ": " + result);
			}else if(!integerValues.containsKey(numericOperations[0]) && !integerValues.containsKey(numericOperations[1])) {
				result = Integer.parseInt(numericOperations[1]) % Integer.parseInt(numericOperations[0]);
				System.out.println("Declared " + variableName + ": " + result);
			}
			integerValues.put(variableName, result);
		}
		return operatorToken;
	}
	
	private static void evaluateStringExpression(String variableName, String rightExpression) throws Exception{
		if (rightExpression.charAt(0) != '"') throw new Exception("ERROR: Illegal way of Starting a string <" + rightExpression +">");
		int position = 1;
		while (position < rightExpression.length() && rightExpression.charAt(position) != '"') {
			if (rightExpression.charAt(position) == '"' && position != rightExpression.length() -1)
				throw new Exception("ERROR: Invalid String Syntax");
			position ++;
		}
		if(rightExpression.charAt(position) != '"')
			throw new Exception ("ERROR: Invalid String Closing");
		stringValues.put(variableName, rightExpression.substring(1, position));
		System.out.println("Declared " + variableName + ": " +rightExpression.substring(1, position));
	}
	
	private static void evaluateBooleanExpression(String BooleanOperation, ArrayList<String> comparedTokens) throws Exception {
		if (!comparedTokens.get(0).matches("-?\\d+") && !integerValues.containsKey(comparedTokens.get(0))) {
			throw new Exception("ERROR: Tried to compare a value that does not exist: "+ comparedTokens.get(0));
		}
		if (!comparedTokens.get(1).matches("-?\\d+") && !integerValues.containsKey(comparedTokens.get(1))) {
			throw new Exception("ERROR: Tried to compare a value that does not exist: "+ comparedTokens.get(1));
		}
		boolean result = false;
		if (BooleanOperation.equals("<")) {
			if(integerValues.containsKey(comparedTokens.get(0)) && !integerValues.containsKey(comparedTokens.get(1))) {
				 result = integerValues.get(comparedTokens.get(0)) <  Integer.parseInt(comparedTokens.get(1));
			}else if(!integerValues.containsKey(comparedTokens.get(0)) && integerValues.containsKey(comparedTokens.get(1))) {
				 result = integerValues.get(comparedTokens.get(1)) <  Integer.parseInt(comparedTokens.get(0));
			}else if(integerValues.containsKey(comparedTokens.get(0)) && integerValues.containsKey(comparedTokens.get(1))) {
				 result = integerValues.get(comparedTokens.get(0)) <  integerValues.get(comparedTokens.get(1));
			}else if(!integerValues.containsKey(comparedTokens.get(0)) && !integerValues.containsKey(comparedTokens.get(1))) {
				 result = Integer.parseInt(comparedTokens.get(0)) <  Integer.parseInt(comparedTokens.get(1));
			}
			System.out.println("Compared: " + comparedTokens + " " + BooleanOperation + ": " + result);
			
		}else if (BooleanOperation.equals("<=")) {
			if(integerValues.containsKey(comparedTokens.get(0)) && !integerValues.containsKey(comparedTokens.get(1))) {
				 result = integerValues.get(comparedTokens.get(0)) <=  Integer.parseInt(comparedTokens.get(1));
			}else if(!integerValues.containsKey(comparedTokens.get(0)) && integerValues.containsKey(comparedTokens.get(1))) {
				 result = integerValues.get(comparedTokens.get(1)) <=  Integer.parseInt(comparedTokens.get(0));
			}else if(integerValues.containsKey(comparedTokens.get(0)) && integerValues.containsKey(comparedTokens.get(1))) {
				 result = integerValues.get(comparedTokens.get(0)) <=  integerValues.get(comparedTokens.get(1));
			}else if(!integerValues.containsKey(comparedTokens.get(0)) && !integerValues.containsKey(comparedTokens.get(1))) {
				 result = Integer.parseInt(comparedTokens.get(0)) <=  Integer.parseInt(comparedTokens.get(1));
			}
			System.out.println("Compared: " + comparedTokens + " " + BooleanOperation + ": " + result);
			
		}else if (BooleanOperation.equals(">")) {
			if(integerValues.containsKey(comparedTokens.get(0)) && !integerValues.containsKey(comparedTokens.get(1))) {
				 result = integerValues.get(comparedTokens.get(0)) >  Integer.parseInt(comparedTokens.get(1));
			}else if(!integerValues.containsKey(comparedTokens.get(0)) && integerValues.containsKey(comparedTokens.get(1))) {
				 result = integerValues.get(comparedTokens.get(1)) >  Integer.parseInt(comparedTokens.get(0));
			}else if(integerValues.containsKey(comparedTokens.get(0)) && integerValues.containsKey(comparedTokens.get(1))) {
				 result = integerValues.get(comparedTokens.get(0)) >  integerValues.get(comparedTokens.get(1));
			}else if(!integerValues.containsKey(comparedTokens.get(0)) && !integerValues.containsKey(comparedTokens.get(1))) {
				 result = Integer.parseInt(comparedTokens.get(0)) >  Integer.parseInt(comparedTokens.get(1));
			}
			System.out.println("Compared: " + comparedTokens + " " + BooleanOperation + ": " + result);
			
		}else if (BooleanOperation.equals(">=")) {
			if(integerValues.containsKey(comparedTokens.get(0)) && !integerValues.containsKey(comparedTokens.get(1))) {
				 result = integerValues.get(comparedTokens.get(0)) >=  Integer.parseInt(comparedTokens.get(1));
			}else if(!integerValues.containsKey(comparedTokens.get(0)) && integerValues.containsKey(comparedTokens.get(1))) {
				 result = integerValues.get(comparedTokens.get(1)) >=  Integer.parseInt(comparedTokens.get(0));
			}else if(integerValues.containsKey(comparedTokens.get(0)) && integerValues.containsKey(comparedTokens.get(1))) {
				 result = integerValues.get(comparedTokens.get(0)) >=  integerValues.get(comparedTokens.get(1));
			}else if(!integerValues.containsKey(comparedTokens.get(0)) && !integerValues.containsKey(comparedTokens.get(1))) {
				 result = Integer.parseInt(comparedTokens.get(0)) >=  Integer.parseInt(comparedTokens.get(1));
			}
			System.out.println("Compared: " + comparedTokens + " " + BooleanOperation + ": " + result);
			
		}else if (BooleanOperation.equals("<=")) {
			if(integerValues.containsKey(comparedTokens.get(0)) && !integerValues.containsKey(comparedTokens.get(1))) {
				 result = integerValues.get(comparedTokens.get(0)) <=  Integer.parseInt(comparedTokens.get(1));
			}else if(!integerValues.containsKey(comparedTokens.get(0)) && integerValues.containsKey(comparedTokens.get(1))) {
				 result = integerValues.get(comparedTokens.get(1)) <=  Integer.parseInt(comparedTokens.get(0));
			}else if(integerValues.containsKey(comparedTokens.get(0)) && integerValues.containsKey(comparedTokens.get(1))) {
				 result = integerValues.get(comparedTokens.get(0)) <=  integerValues.get(comparedTokens.get(1));
			}else if(!integerValues.containsKey(comparedTokens.get(0)) && !integerValues.containsKey(comparedTokens.get(1))) {
				 result = Integer.parseInt(comparedTokens.get(0)) <=  Integer.parseInt(comparedTokens.get(1));
			}
			System.out.println("Compared: " + comparedTokens + " " + BooleanOperation + ": " + result);
			
		}else if (BooleanOperation.equals("==")) {
			if(integerValues.containsKey(comparedTokens.get(0)) && !integerValues.containsKey(comparedTokens.get(1))) {
				 result = integerValues.get(comparedTokens.get(0)) ==  Integer.parseInt(comparedTokens.get(1));
			}else if(!integerValues.containsKey(comparedTokens.get(0)) && integerValues.containsKey(comparedTokens.get(1))) {
				 result = integerValues.get(comparedTokens.get(1)) ==  Integer.parseInt(comparedTokens.get(0));
			}else if(integerValues.containsKey(comparedTokens.get(0)) && integerValues.containsKey(comparedTokens.get(1))) {
				 result = integerValues.get(comparedTokens.get(0)) ==  integerValues.get(comparedTokens.get(1));
			}else if(!integerValues.containsKey(comparedTokens.get(0)) && !integerValues.containsKey(comparedTokens.get(1))) {
				 result = Integer.parseInt(comparedTokens.get(0)) ==  Integer.parseInt(comparedTokens.get(1));
			}
			System.out.println("Compared: " + comparedTokens + " " + BooleanOperation + ": " + result);
			
		}else throw new Exception ("Unrecognized Boolean Operator <" + BooleanOperation+ ">");
		
		
	}

	private static String evaluateLoopExpression(ArrayList<String> line) throws Exception{
		String givenLoopSyntax = "for";
		int position = 1;
		
		while (position < line.size()) {
			if(line.get(position).equals("int")) {
				givenLoopSyntax += " int";
				break;
			}
			givenLoopSyntax += " " + line.get(position);
			position++;
		}
		if (!allowedLoopSyntax.contains(givenLoopSyntax)) throw new Exception ("ERROR: Invalid start of a for loop <" + givenLoopSyntax + ">");
		ArrayList<String> declarationSyntax = new ArrayList<>();
		while (position < line.size()) {
			if(line.get(position).equals(";")) {
				declarationSyntax.add(";");
				position++;
				break;
			}
			declarationSyntax.add(line.get(position));
			position ++;
		}
		char declareToken = checkDeclareSyntax(declarationSyntax);
		//get our variable name since that is mandatory for everything that is parse able next
		String definedVariableName = declarationSyntax.get(1);
		String getValue = declarationSyntax.get(3);
		
		//boolean expression
		if(!line.get(position).equals(definedVariableName)) throw new Exception ("ERROR: Unexpected token <" + line.get(position) + ">");
		ArrayList<String> ComparisionSyntax = new ArrayList<>();
		ComparisionSyntax.add(definedVariableName);
		position++;
		String BinaryOperator = "";
		while (position < line.size()) {
			if (line.get(position).equals(";")) {
				position ++;
				break;
			}else if (!line.get(position).matches("^[a-zA-Z0-9]*$")) {
				BinaryOperator += line.get(position); 
			}else {
				ComparisionSyntax.add(line.get(position));
			}
			position++;
		}
		evaluateBooleanExpression(BinaryOperator, ComparisionSyntax);
		String comparedValue = ComparisionSyntax.get(1);
		
		if(!line.get(line.size()-1).equals("{")) throw new Exception ("ERROR: Unexpected loop start token <" + line.get(line.size()-1) +">");
		
		if(!line.get(position).equals(definedVariableName)) throw new Exception("ERROR: Unexpected variable increment <" + line.get(position)+ ">");
		position++;
		
		String incrementOrDecrement = "";
		while(position < line.size()) {
			if (line.get(position).equals(")")) {
				position++;
				break;
			}
			incrementOrDecrement += line.get(position);
			position++;
		}
		
		if (!incrementOrDecrement.equals("++") && !incrementOrDecrement.equals("--") ) throw new Exception ("ERROR: Illegal incremental operator <" + incrementOrDecrement+">" );
		if(position != line.size() - 1) throw new Exception("ERROR: Unreachable declaration statements <" + line +">");
		System.out.println("Loop is being declared");
		
		return getValue +","+ comparedValue;
	}
}
