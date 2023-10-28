package main;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class MipsResult {
	String texter=".text \n";
	
	ArrayList<String> data = new ArrayList<String>();
	ArrayList<String> text = new ArrayList<String>();
	public Parser parse;
	public MipsResult() {
		parse = new Parser();
	}
	public void declarer(String a,String b) {
		if(isParsable(b)) {
			String res = "\n" + a + " : .word  " + b ;
			data.add(res);
		}
		else {
			String res = "\n" + a + " : .asciiz \" " + b +"\"\n";
			data.add(res);
		}
	}
	public String declareadd(String c,String a,String b) {	
		if (isParsable(a) && isParsable(b)) {
			String res="\nli $t0, "+a+" \n li $t1, "+b+" \n add $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}else if (isParsable(b)) {
			String res="\nlw $t0, " +a+" \n li $t1, "+b+" \n add $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}else if (isParsable(a)) {
			String res="\nli $t0, "+a+" \n lw $t1, "+b+" \n add $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}else {
			String res="\nlw $t0, "+a+" \n lw $t1, "+b+" \n add $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}
	}

	public String declaresub(String c,String a,String b) {
		if (isParsable(a) && isParsable(b)) {
			String res="\nli $t0"+a+" \n li $t1"+b+" \n sub $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}else if (isParsable(b)) {
			String res="\nlw $t0"+a+" \n li $t1"+b+" \n sub $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}else if (isParsable(a)) {
			String res="\nli $t0"+a+" \n lw $t1"+b+" \n sub $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}else {
			String res="\nlw $t0"+a+" \n lw $t1"+b+" \n sub $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}
	}
	public String declarediv(String c,String a,String b) {
		if (isParsable(a) && isParsable(b)) {
			String res="\nli $t0"+a+" \n li $t1"+b+" \n div $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}else if (isParsable(b)) {
			String res="\nlw $t0"+a+" \n li $t1"+b+" \n div $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}else if (isParsable(a)) {
			String res="\nli $t0"+a+" \n lw $t1"+b+" \n div $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}else {
			String res="\nlw $t0"+a+" \n lw $t1"+b+" \n div $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}
	}
	public String declaremult(String c,String a,String b) {
		if (isParsable(a) && isParsable(b)) {
			String res="\nli $t0"+a+" \n li $t1"+b+" \n mult $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}else if (isParsable(b)) {
			String res="\nlw $t0"+a+" \n li $t1"+b+" \n mult $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}else if (isParsable(a)) {
			String res="\nli $t0"+a+" \n lw $t1"+b+" \n mult $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}else {
			String res="\nlw $t0"+a+" \n lw $t1"+b+" \n mult $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}
	}
	public String declaremod(String c,String a,String b) {
		if (isParsable(a) && isParsable(b)) {
			String res="\nli $t0"+a+" \n li $t1"+b+" \n div $t1, $t0 \n mfh $t2 \n sw $t2, "+c;
			return(res);
		}else if (isParsable(b)) {
			String res="\nlw $t0"+a+" \n li $t1"+b+" \n div $t1, $t0 \n mfh $t2\n sw $t2, "+c;
			return(res);
		}else if (isParsable(a)) {
			String res="\nli $t0"+a+" \n lw $t1"+b+" \n div $t1, $t0 \n mfh $t2\n sw $t2, "+c;
			return(res);
		}else {
			String res="\nlw $t0"+a+" \n lw $t1"+b+" \n div $t1, $t0 \n mfh $t2 \n sw $t2, "+c;
			return(res);
		}
	}
	public void declareOperation(String c, String a, String b, String operator) {
		String resd="\n" + c + ": .word ";
		data.add(resd);
		String rest;
		if (operator.equals("+")) {
			rest=declareadd(c,a,b);
		}else if (operator.equals("-")) {
			rest=declaresub(c,a,b);
		}else if (operator.equals("/"))  {
			rest=declarediv(c,a,b);
		}else if (operator.equals("*"))  {
			rest=declaremult(c,a,b);
		}else   {
			rest=declaremod(c,a,b);
		}
		text.add(rest);
	}
	public void printOperation(String a,String b, String operator) {
		String rest;
		if (operator.equals("+")){
			rest=printadd(a,b);
		}else if (operator.equals("-")){
			rest=printsub(a,b);
		}else if (operator.equals("/")) {
			rest=printdiv(a,b);
		}else if (operator.equals("*")) {
			rest=printmult(a,b);
		}else{
			rest=printmod(a,b);
		}
		text.add(rest);
	}

	public String printadd(String a, String b) {
		if(isParsable(a)&& isParsable(b)) {
			String res="\nli $t0, "+a+" \n li $t1, "+b+" \n li $v0, 1 \n add $a0, $t0, $t1 \n syscall";
			return (res);
		}
		else if (isParsable(a)) {
			String res="\nli $t0, "+a+" \n lw $t1, "+b+" \n li $v0, 1 \n add $a0, $t0, $t1 \n syscall";
			return (res);
		}
		else if (isParsable(b)) {
			String res="\nlw $t0, "+a+" \n li $t1, "+b+" \n li $v0, 1 \n add $a0, $t0, $t1 \n syscall";
			return (res);
		}
		
		else {
			String res="\nlw $t0, "+ a + " \n lw $t1, "+b+" \n li $v0, 1 \n add $a0, $t0, $t1 \n syscall";
			return (res);
		}
	}
	public String printsub(String a, String b) {
		if(isParsable(a)&& isParsable(b)) {
			String res="\nli $t0, "+a+" \n li $t1, "+b+" \n li $v0, 1 \n sub $a0, $t0, $t1 \n syscall";
			return (res);
		}
		else if (isParsable(a)) {
			String res="\nli $t0, "+a+" \n lw $t1, "+b+ "\n li $v0, 1 \n sub $a0, $t0, $t1 \n syscall";
			return (res);
		}
		else if (isParsable(b)) {
			String res="\nlw $t0, "+a+" \n li $t1, "+b+" \n li $v0, 1 \n sub $a0, $t0, $t1 \n syscall";
			return (res);
		}
		
		else {
			String res="\nlw $t0, "+ a + " \n lw $t1, "+b+" \n li $v0, 1 \n sub $a0, $t0, $t1 \n syscall";
			return (res);
		}
	}public String printdiv(String a, String b) {
		if(isParsable(a)&& isParsable(b)) {
			String res="\nli $t0, "+a+" \n li $t1, "+b+"\n li $v0, 1 \n div $a0, $t0, $t1 \n syscall";
			return (res);
		}
		else if (isParsable(a)) {
			String res="\nli $t0, "+a+" \n lw $t1, "+b+"\n li $v0, 1 \n div $a0, $t0, $t1 \n syscall";
			return (res);
		}
		else if (isParsable(b)) {
			String res="\nlw $t0, "+a+" \n li $t1, "+b+"\n li $v0, 1 \n div $a0, $t0, $t1 \n syscall";
			return (res);
		}
		
		else {
		String res="\nlw $t0, "+ a + " \n lw $t1, "+b+" \n li $v0, 1 \n div $a0, $t0, $t1 \n syscall";
		return (res);
		}
	}
	public String printmult(String a, String b) {
		if(isParsable(a)&& isParsable(b)) {
			String res="\nli $t0, "+ a + " \n li $t1, "+b+" \n li $v0, 1 \n mult $a0, $t0, $t1 \n syscall";
			return (res);
		}
		else if (isParsable(a)) {
			String res="\nli $t0, "+ a + " \n lw $t1, "+b+" \n li $v0, 1 \n mult $a0, $t0, $t1 \n syscall";
			return (res);
		}
		else if (isParsable(b)) {
			String res="\nlw $t0, "+ a + " \n li $t1, "+b+" \n li $v0, 1 \n mult $a0, $t0, $t1 \n syscall";
			return (res);
		}
		
		else {
		String res="\nlw $t0, "+ a + " \n lw $t1, "+b+" \n li $v0, 1 \n mult $a0, $t0, $t1 \n syscall";
		return (res);
		}
	}
	public String printmod(String a, String b) {
		if(isParsable(a)&& isParsable(b)) {
			String res="\nlw $t0, "+ a + " \n lw $t1, "+b+" \n div $t0, $t1 \n li $v0, 1 \n mfh $a0 \n syscall";
			return (res);
		}
		else if (isParsable(a)) {
			String res="\nlw $t0, "+ a + " \n lw $t1, "+b+" \n div $t0, $t1 \n li $v0, 1 \n mfh $a0 \n syscall";
			return (res);
		}
		else if (isParsable(b)) {
			String res="\nlw $t0, "+ a + " \n lw $t1, "+b+" \n div $t0, $t1 \n li $v0, 1 \n mfh $a0 \n syscall";
			return (res);
		}
		
		else {
		String res="\nlw $t0, "+ a + " \n lw $t1, "+b+" \n div $t0, $t1 \n li $v0, 1 \n mfh $a0 \n syscall";
		return (res);
		}
	}

	public void printint(String a) {
			String res=String.format("\nli $v0, 1 \n li $a0, %s \n syscall", a);
			text.add(res);
	}
	public void printstr(String a){
		String resd="\n stringname : .asciiz\""+a+"\"";
		String rest="\nli $v0, 4 \n la $a0, stringname \n syscall";
		text.add(rest);
		data.add(resd);
	}

	public void print(String a) {
		if(Parser.integerValues.containsKey(a)) {
			String res=String.format("\nli $v0, 1 \n lw $a0, %s \n syscall", a);
			text.add(res);	
		}else {	
		String res=String.format("\nli $v0, 4 \n la $a0, %s \n syscall", a);
			text.add(res);
		}
		
	}
	public void operator(String c, String a, String b, String operator) {
		String rest;
		if (operator=="+") {
			rest=adder(c,a,b);
		}else if (operator=="-") {
			rest=subtracter(c,a,b);
		}else if (operator=="/") {
			rest=divider(c,a,b);
		}else if (operator=="*") {
			rest=multiplier(c,a,b);
		}else  {
			rest=moduler(c,a,b);
		}
		text.add(rest);
	}
	public String adder(String c, String a, String b ) {
		if(isParsable(a)&& isParsable(b)){
			String res= "\nli $t0, "+a+ " \n li $t1, "+b+" \n add $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);	
		}else if(isParsable(a)){
			String res= "\nli $t0, "+a+ " \n lw $t1, "+b+" \n add $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);	
		}else if(isParsable(b)){
			String res= "\nlw $t0, "+a+ " \n li $t1, "+b+" \n add $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);	
		}
		else {
			String res= "\nlw $t0, "+a+ " \n lw $t1, "+b+" \n add $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);
			}
	}
	public String subtracter(String c, String a, String b ) {
		if(isParsable(a)&& isParsable(b)){
			String res= "\nli $t0, "+a+ " \n li $t1, "+b+" \n sub $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);	
		}else if(isParsable(a)){
			String res= "\nli $t0, "+a+ " \n lw $t1, "+b+" \n sub $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);	
		}else if(isParsable(b)){
			String res= "\nlw $t0, "+a+ " \n li $t1, "+b+" \n sub $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);	
		}
		else {
			String res= "\nlw $t0, "+a+ " \n lw $t1, "+b+" \n sub $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);
			}
	}
	public String divider(String c, String a, String b ) {
		if(isParsable(a)&& isParsable(b)){
			String res= "\nli $t0, "+a+ " \n li $t1, "+b+" \n div $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);	
		}else if(isParsable(a)){
			String res= "\nli $t0, "+a+ " \n lw $t1, "+b+" \n div $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);	
		}else if(isParsable(b)){
			String res= "\nlw $t0, "+a+ " \n li $t1, "+b+" \n div $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);	
		}
		else {
			String res= "\nlw $t0, "+a+ " \n lw $t1, "+b+" \n div $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);
			}
	}
	public String multiplier(String c, String a, String b ) {
		if(isParsable(a)&& isParsable(b)){
			String res= "\nli $t0, "+a+ " \n li $t1, "+b+" \n mult $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);	
		}else if(isParsable(a)){
			String res= "\nli $t0, "+a+ " \n lw $t1, "+b+" \n mult $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);	
		}else if(isParsable(b)){
			String res= "\nlw $t0, "+a+ " \n li $t1, "+b+" \n mult $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);	
		}
		else {
			String res= "\nlw $t0, "+a+ " \n lw $t1, "+b+" \n mult $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);
			}
	}
	public String moduler(String c, String a, String b ) {
		if(isParsable(a)&& isParsable(b)){
			String res= "\nli $t0, "+a+ " \n li $t1, "+b+" \n div $t0, $t1 \n mfh $t2 \n sw $t2, "+c;
			return (res);	
		}else if(isParsable(a)){
			String res= "\nli $t0, "+a+ " \n lw $t1, "+b+" \n div $t0, $t1 \n mfh $t2 \n sw $t2, "+c;
			return (res);	
		}else if(isParsable(b)){
			String res= "\nlw $t0, "+a+ " \n li $t1, "+b+" \n div $t0, $t1 \n mfh $t2 \n sw $t2, "+c;
			return (res);	
		}
		else {
			String res= "\nlw $t0, "+a+ " \n lw $t1, "+b+" \n div $t0, $t1 \n mfh $t2 \n sw $t2, "+c;
			return (res);
			}
	}

	public static boolean isParsable(String str) {
	    return str.matches("-?\\d+");
	}

	boolean cond=false;
	boolean loop=false;
	boolean func=false;
		
	public void conditional(String a, String operator, String b) {
		String res ;
		String res2;
		cond=true;
		if(isParsable(a)&& isParsable(b)){
			res2="\n li $s0, "+a+"\n li $s1, "+b;
		}else if(isParsable(a)){
			res2="\n li $s0, "+a+"\n lw $s1, "+b;	
		}else if(isParsable(b)){
			res2="\n lw $s0, "+a+"\n li $s1, "+b;
		}
		else {
			res2="\n lw $s0, "+a+"\n lw $s1, "+b;
			}
		
		if(operator.equals("==")) {
			res="\nbne $s0, $s1, ender ";
		}else if(operator.equals(">")) {
			res="\nble $s0, $s1, ender \n";
		}else if(operator.equals("<")) {
			res="\nbge $s0, $s1, ender \n";
		}else if(operator.equals(">=")) {
			res="\nblt $s0, $s1, ender \n";
		}else if(operator.equals("<=")) {
			res="\nbgt $s0, $s1, ender \n";
		}else {
			res="\nbeq $s0, $s1, ender \n";
		}
		res2=res2+res;
		text.add(res2);
	}
	public void function(String a) {
		func=true;
		String res="\n j endof \n "+a+":";
		text.add(res);
	}
	
	public void ender() throws Exception{
		String res = "";
		if(cond) {
			res = "\n ender: ";
			cond=false;
		}else if(loop) {
			res="\n addi $s3, $s3, 1\nj loop \n end: ";
			loop=false;
		}
		else if(func){	
			res="\n jr $ra \n endof:";
			func=false;
		}else {
			throw new Exception("ERROR: Unexcpected closing brace ");
		}
		text.add(res);
		
	}
	public void loop(String initial,String last) {
		loop=true;
		String res ="\nloop:\n";
		String res2;
		String res3 = "\n beq $s3, $s4, end";
		if(isParsable(last)) {
			res2="\nli $s3, "+initial+"\n li $s4, "+last;
		}else {
			res2="\nli $s3, "+initial+"\n lw $s4, "+last;
		}
		
		text.add(res2 + res + res3);
	}
	
	public void functionCall(String a) {
		String res="jal "+a;
		text.add(res);
	}
	public void mapper(String[] line) throws Exception {
		if(line[line.length-1].equals("declarer")){
			if(line[3].charAt(0)!='"') {
			declarer(line[1], line[3]);// int a=5/int a="hello"/int a=b
			}else {
				declarer(line[1], line[4]);
			}
		}else if(line[line.length - 1].equals("declareOperation")){
			ophandler(line);// int a=5+/-/*/6 also for variable 
		}else if(line[line.length-1].equals("print")) {
			printhandler(line);// anything to be printed
		}else if(line[line.length-1].equals("function")) {
			function(line[line.length - 2]);// public void a() where a is the function name 
		}else if(line[line.length-1].equals("conditional")) {
			conditional(line[2],line[3],line[4]);// if(a==b){ for all comparisons
		}else if(line[line.length-1].equals("loop")) {
			loop(line[5],line[9]);//for(int i=5; i<a;i++){ a can be either number or variable
		}else if(line[line.length-1].equals("functionCall")) {//CHECK-----------------
			functionCall(line[0]);
		}else {
			ender();// whenever there is }
		}
	}

	public void printhandler(String[] line) {
		//System.out.printLn("5")
		//System.out.printLn(5)
		
		 if (line[2].charAt(0)=='"') {
				printstr(line[3]);
			}
		 else if(line.length>=7) {
			printOperation(line[2],line[4],line[3]);
		}else if(isParsable(line[2])) {
			printint(line[2]);
		}else {
			print(line[2]);
		}
	}
	public void ophandler(String[] line) {
		if(line.length==8) {
			//int b = 9 + a
			declareOperation(line[1],line[3],line[5],line[4]);
		}else {
			operator(line[0],line[2],line[4],line[3]);
		}
		
	}
	
	public String MipResult(String input) throws Exception {
		//This is where you return a string and it's written on the converter
		Parser.parseAndConvert(input);
		if (!Parser.error.equals("")) return Parser.error;
		ArrayList<String[]> file = Parser.fileParsed;
		for(int i = 0; i<file.size(); i++) {
			String[] line = file.get(i);
			mapper(line);
		}
		if(cond||func||loop) {
			throw new Exception("ERROR: Defined Statement not closed ");
		}
		String MipsCode=".data \n";
		for(int i=0; i<data.size();i++) {
			MipsCode=MipsCode+data.get(i);
		}
		MipsCode=MipsCode+" \n .text \n.globl main\nmain:";
		for(int i=0; i<text.size();i++) {
			MipsCode=MipsCode+text.get(i);
		}
		String halt = "\n li $v0, 10 \n syscall \n ";
		return MipsCode + halt;
	}
}
