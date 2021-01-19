/* *
 * Developed  for the class project in COP5556 Programming Language Principles 
 * at the University of Florida, Fall 2019.
 * 
 * This software is solely for the educational benefit of students 
 * enrolled in the course during the Fall 2019 semester.  
 * 
 * This software, and any software derived from it,  may not be shared with others or posted to public web sites or repositories,
 * either during the course or afterwards.
 * 
 *  @Beverly A. Sanders, 2019
 */

package cop5556fa19;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import cop5556fa19.Scanner.LexicalException;

import static cop5556fa19.Token.Kind.*;

class ScannerTest {
	
	//I like this to make it easy to print objects and turn this output on and off
	static boolean doPrint = true;
	private void show(Object input) {
		if (doPrint) {
			System.out.println(input.toString());
		}
	}
	
	

	 /**
	  * Example showing how to get input from a Java string literal.
	  * 
	  * In this case, the string is empty.  The only Token that should be returned is an EOF Token.  
	  * 
	  * This test case passes with the provided skeleton, and should also pass in your final implementation.
	  * Note that calling getNext again after having reached the end of the input should just return another EOF Token.
	  * 
	  */
	@Test 
	void test0() throws Exception {
		Reader r = new StringReader("");
		Scanner s = new Scanner(r);
		Token t;
		show(t= s.getNext()); 
		assertEquals(EOF, t.kind);
		show(t= s.getNext());
		assertEquals(EOF, t.kind);
	}

	/**
	 * Example showing how to create a test case to ensure that an exception is thrown when illegal input is given.
	 * 
	 * This "@" character is illegal in the final scanner (except as part of a String literal or comment). So this
	 * test should remain valid in your complete Scanner.
	 */
	@Test
	void test1() throws Exception {
		Reader r = new StringReader("@");
		Scanner s = new Scanner(r);
        assertThrows(LexicalException.class, ()->{
		   s.getNext();
        });
	}
	
	/**
	 * Example showing how to read the input from a file.  Otherwise it is the same as test1.
	 *
	 */
	@Test
	void test2() throws Exception {
		String file = "./src/testInputFiles/test2.input"; 
		Reader r = new BufferedReader(new FileReader(file));
		Scanner s = new Scanner(r);
        assertThrows(LexicalException.class, ()->{
		   s.getNext();
        });
	}
	

	
	/**
	 * Another example.  This test case will fail with the provided code, but should pass in your completed Scanner.
	 * @throws Exception
	 */
	@Test
	void test3() throws Exception {
		Reader r = new StringReader(",,::==");
		Scanner s = new Scanner(r);
		Token t;
		show(t= s.getNext());
		assertEquals(t.kind,COMMA);
		assertEquals(t.text,",");
		show(t = s.getNext());
		assertEquals(t.kind,COMMA);
		assertEquals(t.text,",");
		
		show(t = s.getNext());
		assertEquals(t.kind,COLONCOLON);
		assertEquals(t.text,"::");
		
		show(t = s.getNext());
		assertEquals(t.kind,REL_EQEQ);
		assertEquals(t.text,"==");
		
		show(t= s.getNext());
		assertEquals(t.kind, EOF);
		assertEquals(t.text, "EOF");
	}
	
	@Test
	void test4() throws Exception{
		Reader r = new StringReader("if  (a==b){");
		Scanner s = new Scanner(r);
		Token t;
		show(t=s.getNext());
		assertEquals(t.kind,KW_if);
		assertEquals(t.text, "if");
		
		show(t=s.getNext());
		assertEquals(t.kind,LPAREN);
		assertEquals(t.text, "(");
		
		show(t=s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text, "a");
		
		show(t=s.getNext());
		assertEquals(t.kind,REL_EQEQ);
		assertEquals(t.text, "==");
		
		show(t=s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text, "b");
		
		show(t=s.getNext());
		assertEquals(t.kind,RPAREN);
		assertEquals(t.text, ")");
		
		show(t=s.getNext());
		assertEquals(t.kind,LCURLY);
		assertEquals(t.text, "{");
		
		show(t=s.getNext());
		assertEquals(t.kind,EOF);
		assertEquals(t.text, "EOF");
	}
	
	@Test
	void test5() throws Exception {
		Reader r = new StringReader("\"abc\\a\"");
		Scanner s = new Scanner(r);
		Token t;
		show(t=s.getNext());
		assertEquals(t.kind,STRINGLIT);
		assertEquals(t.text,"\"abc\"");
		
		show(t=s.getNext());
		assertEquals(t.kind,EOF);
		assertEquals(t.text, "EOF");
	}
	
	@Test
	void test6() throws Exception{
		Reader r = new StringReader("and+xyz");
		Scanner s = new Scanner(r);
		Token t;
		show(t=s.getNext());
		assertEquals(t.kind,KW_and);
		assertEquals(t.text,"and");
		
		show(t=s.getNext());
		assertEquals(t.kind,OP_PLUS);
		assertEquals(t.text,"+");
		
		show(t=s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"xyz");
		
		show(t=s.getNext());
		assertEquals(t.kind,EOF);
		assertEquals(t.text,"EOF");
		
	}
	
	@Test
	void test7() throws Exception {
		Reader r = new StringReader("==+=++");
		Scanner s = new Scanner(r);
		Token t;
		show(t=s.getNext());
		assertEquals(t.kind,REL_EQEQ);
		assertEquals(t.text,"==");
		
		show(t=s.getNext());
		assertEquals(t.kind,OP_PLUS);
		assertEquals(t.text,"+");
		
		show(t=s.getNext());
		assertEquals(t.kind,ASSIGN);
		assertEquals(t.text,"=");
		
		show(t=s.getNext());
		assertEquals(t.kind,OP_PLUS);
		assertEquals(t.text,"+");
		
		show(t=s.getNext());
		assertEquals(t.kind,OP_PLUS);
		assertEquals(t.text,"+");
		
		show(t=s.getNext());
		assertEquals(t.kind,EOF);
		assertEquals(t.text,"EOF");
	}
	
	@Test
	void test8() throws Exception {
		Reader r = new StringReader("");
		Scanner s = new Scanner(r);
		Token t;
		show(t=s.getNext());
		assertEquals(t.kind,EOF);
		assertEquals(t.text,"EOF");
	}
	
	@Test
	void test9() throws Exception{
		Reader r = new StringReader("if (a==b) {\n\treturn \"Hello World!\"\n}");
		Scanner s = new Scanner(r);
		Token t;
		show(t=s.getNext());
		assertEquals(t.kind,KW_if);
		assertEquals(t.text,"if");
		
		show(t=s.getNext());
		assertEquals(t.kind,LPAREN);
		assertEquals(t.text,"(");
		
		show(t=s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"a");
		
		show(t=s.getNext());
		assertEquals(t.kind,REL_EQEQ);
		assertEquals(t.text,"==");
		
		show(t=s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"b");
		
		show(t=s.getNext());
		assertEquals(t.kind,RPAREN);
		assertEquals(t.text,")");
		
		show(t=s.getNext());
		assertEquals(t.kind,LCURLY);
		assertEquals(t.text,"{");
		
		show(t=s.getNext());
		assertEquals(t.kind,KW_return);
		assertEquals(t.text,"return");
		
		show(t=s.getNext());
		assertEquals(t.kind,STRINGLIT);
		assertEquals(t.text,"\"Hello World!\"");
		
		show(t=s.getNext());
		assertEquals(t.kind,RCURLY);
		assertEquals(t.text,"}");
		
		show(t=s.getNext());
		assertEquals(t.kind,EOF);
		assertEquals(t.text,"EOF");

	}
	
	@Test
	void test10() throws Exception{
		Reader r = new StringReader("--abce wjnsiow osnwnw\na");
		Scanner s = new Scanner(r);
		Token t;
		show(t=s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"a");

	}
	
	@Test
	void test11() throws Exception{
		Reader r = new StringReader("++===...--abc");
		Scanner s = new Scanner(r);
		Token t;
		show(t=s.getNext());
		assertEquals(t.kind,OP_PLUS);
		assertEquals(t.text,"+");
		
		show(t=s.getNext());
		assertEquals(t.kind,OP_PLUS);
		assertEquals(t.text,"+");
		
		show(t=s.getNext());
		assertEquals(t.kind,REL_EQEQ);
		assertEquals(t.text,"==");
		
		show(t=s.getNext());
		assertEquals(t.kind,ASSIGN);
		assertEquals(t.text,"=");
		
		show(t=s.getNext());
		assertEquals(t.kind,DOTDOTDOT);
		assertEquals(t.text,"...");
		
		show(t=s.getNext());
		assertEquals(t.kind,EOF);
		assertEquals(t.text,"EOF");
	}
	
	@Test
	void test12() throws Exception {
		Reader r = new StringReader("\"abc\"+\"xyz\"");
		Scanner s = new Scanner(r);
		Token t;
		show(t=s.getNext());
		assertEquals(t.kind,STRINGLIT);
		assertEquals(t.text,"\"abc\"");
		
		show(t=s.getNext());
		assertEquals(t.kind,OP_PLUS);
		assertEquals(t.text,"+");
		
		show(t=s.getNext());
		assertEquals(t.kind,STRINGLIT);
		assertEquals(t.text,"\"xyz\"");
		
		show(t=s.getNext());
		assertEquals(t.kind,EOF);
		assertEquals(t.text,"EOF");
	}
	
	@Test
	void test13() throws Exception{
		Reader r = new StringReader("3407187470374023072385730792375029370239570923750923750923750973250975029372039723075092375092375");
		Scanner s = new Scanner(r);
        assertThrows(LexicalException.class, ()->{
		   s.getNext();
        });
	}
	
	@Test
	void test14() throws Exception {
		Reader r = new StringReader("if (first_name == \"Sid\" and age==12)");
		Scanner s = new Scanner(r);
		Token t;
		show(t=s.getNext());
		assertEquals(t.kind,KW_if);
		assertEquals(t.text,"if");
		
		show(t=s.getNext());
		assertEquals(t.kind,LPAREN);
		assertEquals(t.text,"(");
		
		show(t=s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"first_name");
		
		show(t=s.getNext());
		assertEquals(t.kind,REL_EQEQ);
		assertEquals(t.text,"==");
		
		show(t=s.getNext());
		assertEquals(t.kind,STRINGLIT);
		assertEquals(t.text,"\"Sid\"");
		
		show(t=s.getNext());
		assertEquals(t.kind,KW_and);
		assertEquals(t.text,"and");
		
		show(t=s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"age");
		
		show(t=s.getNext());
		assertEquals(t.kind,REL_EQEQ);
		assertEquals(t.text,"==");
		
		show(t=s.getNext());
		assertEquals(t.kind,INTLIT);
		assertEquals(t.text,"12");
		
		show(t=s.getNext());
		assertEquals(t.kind,RPAREN);
		assertEquals(t.text,")");
		
		show(t=s.getNext());
		assertEquals(t.kind,EOF);
		assertEquals(t.text,"EOF");
	}
	
	@Test
	void test15() throws Exception{
		Reader r = new StringReader("123\"abc");
		Scanner s = new Scanner(r);
		Token t;
		show(t=s.getNext());
		assertEquals(t.kind,INTLIT);
        assertThrows(LexicalException.class, ()->{
		   s.getNext();
        });
	}
	
	@Test
	void test16() throws Exception{
		Reader r = new StringReader("\"abc\n\ndef\"");
		Scanner s = new Scanner(r);
		Token t;
		show(t=s.getNext());
		assertEquals(t.kind,STRINGLIT);
		assertEquals(t.text,"\"abcdef\"");
		
		show(t=s.getNext());
		assertEquals(t.kind,EOF);
		assertEquals(t.text,"EOF");
	}
	
	@Test
	void test17() throws Exception{
		Reader r = new StringReader("and\tabc def\toops");
		Scanner s = new Scanner(r);
		Token t;
		show(t=s.getNext());
		assertEquals(t.kind,KW_and);
		assertEquals(t.text,"and");
		
		show(t=s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"abc");
		
		show(t=s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"def");
		
		show(t=s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"oops");
		
		show(t=s.getNext());
		assertEquals(t.kind,EOF);
		assertEquals(t.text,"EOF");
	}

	@Test
	void test18() throws Exception{
		String file = "./src/testInputFiles/test3.input"; 
		Reader r = new BufferedReader(new FileReader(file));
		Scanner s = new Scanner(r);
		Token t;
		show(t=s.getNext());
		assertEquals(t.kind,KW_if);
		assertEquals(t.text,"if");
		
		show(t=s.getNext());
		assertEquals(t.kind,LPAREN);
		assertEquals(t.text,"(");
		
		show(t=s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"a");
		
		show(t=s.getNext());
		assertEquals(t.kind,REL_EQEQ);
		assertEquals(t.text,"==");
		
		show(t=s.getNext());
		assertEquals(t.kind,INTLIT);
		assertEquals(t.text,"1");
		
		show(t=s.getNext());
		assertEquals(t.kind,KW_or);
		assertEquals(t.text,"or");
		
		show(t=s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"a");
		
		show(t=s.getNext());
		assertEquals(t.kind,REL_EQEQ);
		assertEquals(t.text,"==");
		
		show(t=s.getNext());
		assertEquals(t.kind,INTLIT);
		assertEquals(t.text,"2");
		
		show(t=s.getNext());
		assertEquals(t.kind,RPAREN);
		assertEquals(t.text,")");
		
		show(t=s.getNext());
		assertEquals(t.kind,LCURLY);
		assertEquals(t.text,"{");
		
		show(t=s.getNext());
		assertEquals(t.kind,KW_return);
		assertEquals(t.text,"return");
		
		show(t=s.getNext());
		assertEquals(t.kind,STRINGLIT);
		assertEquals(t.text,"\"Sid\"");
		
		show(t=s.getNext());
		assertEquals(t.kind,RCURLY);
		assertEquals(t.text,"}");
		
		show(t=s.getNext());
		assertEquals(t.kind,KW_elseif);
		assertEquals(t.text,"elseif");
		
		show(t=s.getNext());
		assertEquals(t.kind,LPAREN);
		assertEquals(t.text,"(");
		
		show(t=s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"a");
		
		show(t=s.getNext());
		assertEquals(t.kind,REL_EQEQ);
		assertEquals(t.text,"==");
		
		show(t=s.getNext());
		assertEquals(t.kind,INTLIT);
		assertEquals(t.text,"3");
		
		show(t=s.getNext());
		assertEquals(t.kind,RPAREN);
		assertEquals(t.text,")");
		
		show(t=s.getNext());
		assertEquals(t.kind,LCURLY);
		assertEquals(t.text,"{");
		
		show(t=s.getNext());
		assertEquals(t.kind,KW_return);
		assertEquals(t.text,"return");
		
		show(t=s.getNext());
		assertEquals(t.kind,STRINGLIT);
		assertEquals(t.text,"\"John\"");
		
		show(t=s.getNext());
		assertEquals(t.kind,RCURLY);
		assertEquals(t.text,"}");
		
		show(t=s.getNext());
		assertEquals(t.kind,KW_else);
		assertEquals(t.text,"else");
		
		show(t=s.getNext());
		assertEquals(t.kind,LCURLY);
		assertEquals(t.text,"{");
		
		show(t=s.getNext());
		assertEquals(t.kind,KW_return);
		assertEquals(t.text,"return");
		
		show(t=s.getNext());
		assertEquals(t.kind,STRINGLIT);
		assertEquals(t.text,"\"Tom\"");
		
		show(t=s.getNext());
		assertEquals(t.kind,RCURLY);
		assertEquals(t.text,"}");
		
		show(t=s.getNext());
		assertEquals(t.kind,EOF);
		assertEquals(t.text,"EOF");
	}
	
	@Test
	void test19() throws Exception{
		String file = "./src/testInputFiles/test4.input"; 
		Reader r = new BufferedReader(new FileReader(file));
		Scanner s = new Scanner(r);
		Token t;
		
		show(t=s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"name");
		
		show(t=s.getNext());
		assertEquals(t.kind,ASSIGN);
		assertEquals(t.text,"=");
		
		show(t=s.getNext());
		assertEquals(t.kind,STRINGLIT);
		assertEquals(t.text,"\"Sid\"");
		
		show(t=s.getNext());
		assertEquals(t.kind,KW_function);
		assertEquals(t.text,"function");
		
		show(t=s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"getName");
		
		show(t=s.getNext());
		assertEquals(t.kind,LPAREN);
		assertEquals(t.text,"(");
		
		show(t=s.getNext());
		assertEquals(t.kind,RPAREN);
		assertEquals(t.text,")");
		
		show(t=s.getNext());
		assertEquals(t.kind,LCURLY);
		assertEquals(t.text,"{");
		
		show(t=s.getNext());
		assertEquals(t.kind,KW_return);
		assertEquals(t.text,"return");
		
		show(t=s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"name");
		
		show(t=s.getNext());
		assertEquals(t.kind,RCURLY);
		assertEquals(t.text,"}");
		
		show(t=s.getNext());
		assertEquals(t.kind,EOF);
		assertEquals(t.text,"EOF");
	}
	
	@Test
	void test20() throws Exception{
		String file = "./src/testInputFiles/test5.input"; 
		Reader r = new BufferedReader(new FileReader(file));
		Scanner s = new Scanner(r);
		Token t;
		show(t=s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"x");
		
		show(t=s.getNext());
		assertEquals(t.kind,ASSIGN);
		assertEquals(t.text,"=");
		
		show(t=s.getNext());
		assertEquals(t.kind,INTLIT);
		assertEquals(t.text,"10");
		
		show(t=s.getNext());
		assertEquals(t.kind,KW_local);
		assertEquals(t.text,"local");
		
		show(t=s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"i");
		
		show(t=s.getNext());
		assertEquals(t.kind,ASSIGN);
		assertEquals(t.text,"=");
		
		show(t=s.getNext());
		assertEquals(t.kind,INTLIT);
		assertEquals(t.text,"1");
		
		show(t=s.getNext());
		assertEquals(t.kind,KW_while);
		assertEquals(t.text,"while");
		
		show(t=s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"i");
		
		show(t=s.getNext());
		assertEquals(t.kind,REL_LE);
		assertEquals(t.text,"<=");
		
		show(t=s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"x");
		
		show(t=s.getNext());
		assertEquals(t.kind,KW_do);
		assertEquals(t.text,"do");
		
		show(t=s.getNext());
		assertEquals(t.kind,KW_local);
		assertEquals(t.text,"local");
		
		show(t=s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"x");
		
		show(t=s.getNext());
		assertEquals(t.kind,ASSIGN);
		assertEquals(t.text,"=");
		
		show(t=s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"i");
		
		show(t=s.getNext());
		assertEquals(t.kind,OP_TIMES);
		assertEquals(t.text,"*");
		
		show(t=s.getNext());
		assertEquals(t.kind,INTLIT);
		assertEquals(t.text,"2");
		
		show(t=s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"i");
		
		show(t=s.getNext());
		assertEquals(t.kind,ASSIGN);
		assertEquals(t.text,"=");
		
		show(t=s.getNext());
		assertEquals(t.kind,NAME);
		assertEquals(t.text,"i");
		
		show(t=s.getNext());
		assertEquals(t.kind,OP_PLUS);
		assertEquals(t.text,"+");
		
		show(t=s.getNext());
		assertEquals(t.kind,INTLIT);
		assertEquals(t.text,"1");
		
		show(t=s.getNext());
		assertEquals(t.kind,KW_end);
		assertEquals(t.text,"end");
		
		show(t=s.getNext());
		assertEquals(t.kind,EOF);
		assertEquals(t.text,"EOF");
	}
	
	@Test
	void test21() throws Exception{
		Reader r = new StringReader("\"abc\\a\"");
		Scanner s = new Scanner(r);
		Token t;
		show(t=s.getNext());
		assertEquals(t.kind,STRINGLIT);
		assertEquals(t.text,"\"abc\"");
	}
	
	@Test
	void test22() throws Exception{
		Reader r = new StringReader("\"\b\\b\f\\f\n\\n\r\\r\t\\t\"");
		Scanner s = new Scanner(r);
		Token t;
		show(t=s.getNext());
		while(!t.isKind(EOF)) {
			System.out.println(t);
			show(t=s.getNext());
		}
		//assertEquals(t.kind,STRINGLIT);
		//assertEquals(t.text,"\"abc\u0007\"");
	}

}
