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
			String res = a + " : .word " + b;
			data.add(res);
		}
		else {
			String res = a + " : .asciiz, " + b;
			data.add(res);
		}
	}
	public String declareadd(String c,String a,String b) {	
		if (isParsable(a) && isParsable(b)) {
			String res="li $t0"+a+" \n li $t1"+b+" \n add $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}else if (isParsable(b)) {
			String res="lw $t0"+a+" \n li $t1"+b+" \n add $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}else if (isParsable(a)) {
			String res="li $t0"+a+" \n lw $t1"+b+" \n add $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}else {
			String res="lw $t0"+a+" \n lw $t1"+b+" \n add $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}
	}

	public String declaresub(String c,String a,String b) {
		if (isParsable(a) && isParsable(b)) {
			String res="li $t0"+a+" \n li $t1"+b+" \n add $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}else if (isParsable(b)) {
			String res="lw $t0"+a+" \n li $t1"+b+" \n add $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}else if (isParsable(a)) {
			String res="li $t0"+a+" \n lw $t1"+b+" \n add $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}else {
			String res="lw $t0"+a+" \n lw $t1"+b+" \n add $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}
	}
	public String declarediv(String c,String a,String b) {
		if (isParsable(a) && isParsable(b)) {
			String res="li $t0"+a+" \n li $t1"+b+" \n div $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}else if (isParsable(b)) {
			String res="lw $t0"+a+" \n li $t1"+b+" \n div $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}else if (isParsable(a)) {
			String res="li $t0"+a+" \n lw $t1"+b+" \n div $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}else {
			String res="lw $t0"+a+" \n lw $t1"+b+" \n div $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}
	}
	public String declaremult(String c,String a,String b) {
		if (isParsable(a) && isParsable(b)) {
			String res="li $t0"+a+" \n li $t1"+b+" \n mult $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}else if (isParsable(b)) {
			String res="lw $t0"+a+" \n li $t1"+b+" \n mult $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}else if (isParsable(a)) {
			String res="li $t0"+a+" \n lw $t1"+b+" \n mult $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}else {
			String res="lw $t0"+a+" \n lw $t1"+b+" \n mult $t2, $t1, $t0 \n sw $t2, "+c;
			return(res);
		}
	}
	public String declaremod(String c,String a,String b) {
		if (isParsable(a) && isParsable(b)) {
			String res="li $t0"+a+" \n li $t1"+b+" \n div $t1, $t0 \n mfh $t2 \n sw $t2, "+c;
			return(res);
		}else if (isParsable(b)) {
			String res="lw $t0"+a+" \n li $t1"+b+" \n div $t1, $t0 \n mfh $t2\n sw $t2, "+c;
			return(res);
		}else if (isParsable(a)) {
			String res="li $t0"+a+" \n lw $t1"+b+" \n div $t1, $t0 \n mfh $t2\n sw $t2, "+c;
			return(res);
		}else {
			String res="lw $t0"+a+" \n lw $t1"+b+" \n div $t1, $t0 \n mfh $t2 \n sw $t2, "+c;
			return(res);
		}
	}
	public void declareOperation(String c, String a, String b, String operator) {
		String resd=c+": .word ";
		data.add(resd);
		String rest;
		if (operator.equals("+")) {
			rest=declareadd(c,a,b);
		}else if (operator.equals("-")) {
			rest=declaresub(c,a,b);
		}else if (operator=="/") {
			rest=declarediv(c,a,b);
		}else if (operator.equals("*")) {
			rest=declaremult(c,a,b);
		}else if (operator.equals("%")) {
			rest=declaremod(c,a,b);
		}else {
			rest= "no such operator";
		}
		text.add(rest);
	}
	public void printOperation(String a,String b, String operator) {
		String rest;
		if (operator=="+") {
			rest=printadd(a,b);
		}else if (operator=="-") {
			rest=printsub(a,b);
		}else if (operator=="/") {
			rest=printdiv(a,b);
		}else if (operator=="*") {
			rest=printmult(a,b);
		}else if (operator=="%") {
			rest=printmod(a,b);
		}else {
			rest= "no such operator";
		}
		text.add(rest);
	}

	public String printadd(String a, String b) {
		if(isParsable(a)&& isParsable(b)) {
			String res="li $t0, a \n li $t1, b \n li $v0, 1 \n add $a0, $t0, $t1 \n syscall";
			return (res);
		}
		else if (isParsable(a)) {
			String res="li $t0, a \n lw $t1, b \n li $v0, 1 \n add $a0, $t0, $t1 \n syscall";
			return (res);
		}
		else if (isParsable(b)) {
			String res="lw $t0, a \n li $t1, b \n li $v0, 1 \n add $a0, $t0, $t1 \n syscall";
			return (res);
		}
		
		else {
			String res="lw $t0, "+ a + " \n lw $t1, "+b+" \n li $v0, 1 \n add $a0, $t0, $t1 \n syscall";
			return (res);
		}
	}
	public String printsub(String a, String b) {
		if(isParsable(a)&& isParsable(b)) {
			String res="li $t0, a \n li $t1, b \n li $v0, 1 \n sub $a0, $t0, $t1 \n syscall";
			return (res);
		}
		else if (isParsable(a)) {
			String res="li $t0, a \n lw $t1, b \n li $v0, 1 \n sub $a0, $t0, $t1 \n syscall";
			return (res);
		}
		else if (isParsable(b)) {
			String res="lw $t0, a \n li $t1, b \n li $v0, 1 \n sub $a0, $t0, $t1 \n syscall";
			return (res);
		}
		
		else {
		String res="lw $t0, "+ a + " \n lw $t1, "+b+" \n li $v0, 1 \n sub $a0, $t0, $t1 \n syscall";
		return (res);
		}
	}public String printdiv(String a, String b) {
		if(isParsable(a)&& isParsable(b)) {
			String res="li $t0, a \n li $t1, b \n li $v0, 1 \n div $a0, $t0, $t1 \n syscall";
			return (res);
		}
		else if (isParsable(a)) {
			String res="li $t0, a \n lw $t1, b \n li $v0, 1 \n div $a0, $t0, $t1 \n syscall";
			return (res);
		}
		else if (isParsable(b)) {
			String res="lw $t0, a \n li $t1, b \n li $v0, 1 \n div $a0, $t0, $t1 \n syscall";
			return (res);
		}
		
		else {
		String res="lw $t0, "+ a + " \n lw $t1, "+b+" \n li $v0, 1 \n div $a0, $t0, $t1 \n syscall";
		return (res);
		}
	}
	public String printmult(String a, String b) {
		if(isParsable(a)&& isParsable(b)) {
			String res="li $t0, a \n li $t1, b \n li $v0, 1 \n mult $a0, $t0, $t1 \n syscall";
			return (res);
		}
		else if (isParsable(a)) {
			String res="li $t0, a \n lw $t1, b \n li $v0, 1 \n mult $a0, $t0, $t1 \n syscall";
			return (res);
		}
		else if (isParsable(b)) {
			String res="lw $t0, a \n li $t1, b \n li $v0, 1 \n mult $a0, $t0, $t1 \n syscall";
			return (res);
		}
		
		else {
		String res="lw $t0, "+ a + " \n lw $t1, "+b+" \n li $v0, 1 \n mult $a0, $t0, $t1 \n syscall";
		return (res);
		}
	}
	public String printmod(String a, String b) {
		if(isParsable(a)&& isParsable(b)) {
			String res="li $t0, a \n li $t1, b \n div $t0, $t1 \n li $v0, 1 \n mfh $a0 \n syscall";
			return (res);
		}
		else if (isParsable(a)) {
			String res="li $t0, a \n lw $t1, b \ndiv $t0, $t1 \n li $v0, 1 \n mfh $a0 \n syscall";
			return (res);
		}
		else if (isParsable(b)) {
			String res="lw $t0, a \n li $t1, b \n div $t0, $t1 \n li $v0, 1 \n mfh $a0 \n syscall";
			return (res);
		}
		
		else {
		String res="lw $t0, "+ a + " \n lw $t1, "+b+" \n div $t0, $t1 \n li $v0, 1 \n mfh $a0 \n syscall";
		return (res);
		}
	}

	public void printimmediate(String a) {
		if(isParsable(a)) {
			String res=String.format("li $v0, 1 \n li $a0, %s \n syscall", a);
			text.add(res);
		}
		else {
		String resd=".data \n stringName : .asciiz "+a;
		String rest="li $v0, 4 \n la $a0, StringName \n syscall";
		text.add(rest);
		data.add(resd);
		}
	}
	public void printint(String a) {
		String res=String.format("li $v0, 1 \n li $a0, %s \n syscall", a);
		text.add(res);
	}
	public void printstr(String a) {
		String res=String.format("li $v0, 4 \n la $a0, %s \n syscall", a);
		text.add(res);
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
		}else if (operator=="%") {
			rest=moduler(c,a,b);
		}else {
			rest= "no such operator";
		}
		text.add(rest);
	}
	public String adder(String c, String a, String b ) {
		if(isParsable(a)&& isParsable(b)){
			String res= "li $t0, "+a+ " \n li $t1, "+b+" \n add $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);	
		}else if(isParsable(a)){
			String res= "li $t0, "+a+ " \n lw $t1, "+b+" \n add $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);	
		}else if(isParsable(b)){
			String res= "lw $t0, "+a+ " \n li $t1, "+b+" \n add $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);	
		}
		else {
			String res= "lw $t0, "+a+ " \n lw $t1, "+b+" \n add $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);
			}
	}
	public String subtracter(String c, String a, String b ) {
		if(isParsable(a)&& isParsable(b)){
			String res= "li $t0, "+a+ " \n li $t1, "+b+" \n sub $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);	
		}else if(isParsable(a)){
			String res= "li $t0, "+a+ " \n lw $t1, "+b+" \n sub $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);	
		}else if(isParsable(b)){
			String res= "lw $t0, "+a+ " \n li $t1, "+b+" \n sub $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);	
		}
		else {
			String res= "lw $t0, "+a+ " \n lw $t1, "+b+" \n sub $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);
			}
	}
	public String divider(String c, String a, String b ) {
		if(isParsable(a)&& isParsable(b)){
			String res= "li $t0, "+a+ " \n li $t1, "+b+" \n div $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);	
		}else if(isParsable(a)){
			String res= "li $t0, "+a+ " \n lw $t1, "+b+" \n div $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);	
		}else if(isParsable(b)){
			String res= "lw $t0, "+a+ " \n li $t1, "+b+" \n div $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);	
		}
		else {
			String res= "lw $t0, "+a+ " \n lw $t1, "+b+" \n div $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);
			}
	}
	public String multiplier(String c, String a, String b ) {
		if(isParsable(a)&& isParsable(b)){
			String res= "li $t0, "+a+ " \n li $t1, "+b+" \n mult $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);	
		}else if(isParsable(a)){
			String res= "li $t0, "+a+ " \n lw $t1, "+b+" \n mult $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);	
		}else if(isParsable(b)){
			String res= "lw $t0, "+a+ " \n li $t1, "+b+" \n mult $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);	
		}
		else {
			String res= "lw $t0, "+a+ " \n lw $t1, "+b+" \n mult $t2, $t0, $t1 \n sw $t2, "+c;
			return (res);
			}
	}
	public String moduler(String c, String a, String b ) {
		if(isParsable(a)&& isParsable(b)){
			String res= "li $t0, "+a+ " \n li $t1, "+b+" \n div $t0, $t1 \n mfh $t2 \n sw $t2, "+c;
			return (res);	
		}else if(isParsable(a)){
			String res= "li $t0, "+a+ " \n lw $t1, "+b+" \n div $t0, $t1 \n mfh $t2 \n sw $t2, "+c;
			return (res);	
		}else if(isParsable(b)){
			String res= "lw $t0, "+a+ " \n li $t1, "+b+" \n div $t0, $t1 \n mfh $t2 \n sw $t2, "+c;
			return (res);	
		}
		else {
			String res= "lw $t0, "+a+ " \n lw $t1, "+b+" \n div $t0, $t1 \n mfh $t2 \n sw $t2, "+c;
			return (res);
			}
	}
	public boolean isParsable(String a) {
	
		        try{
		        	Integer.parseInt(a);
		        	return true;
		        }catch(NumberFormatException e) {
		        	return false;
		        }
		    

	}

	public String MipResult(String input) throws Exception {
		//This is where you return a string and it's written on the converter
		Parser.parseAndConvert(input);
		if (!Parser.error.equals("")) return Parser.error;
//		Map<String, String[]> map = parse.createMap();
		ArrayList<String[]> file = Parser.fileParsed;
		for(int i = 0; i<file.size(); i++) {
			String[] line = file.get(i);
			if(line.length==5) {
				declarer(line[1],line[3]);
			}else {
				declareOperation(line[1], line[3], line[5], line[4]);
			}
		}
		String abebe=".data \n";
		for(int i=0; i<data.size();i++) {
			abebe=abebe+data.get(i);
			abebe+= "\n";
		}
		abebe=abebe+" \n .text \n";
		for(int i=0; i<text.size();i++) {
			abebe=abebe+text.get(i);
		}
		return abebe;
	}
	
}
