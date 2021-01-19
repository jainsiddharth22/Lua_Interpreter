

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


import static cop5556fa19.Token.Kind.*;


import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import cop5556fa19.Token.Kind;

public class Scanner {
	
	Reader r;
	int currPos;
	int currLine;
	int ch;
	int pos;
	int line;
	Map<String, Kind> keyWords;
	
	public enum State {
		START,
		HAVE_EQ,
		HAVE_SLASH,
		HAVE_XOR,
		HAVE_REL_LT,
		HAVE_REL_GT,
		HAVE_COLON,
		HAVE_DOT,
		HAVE_DOUBLE_DOT,
		IN_NUMLIT,
		IN_IDENT,
		STRING_LIT_DOUBLE,
		STRING_LIT_SINGLE,
		POTENTIAL_COMMENT,
		COMMENT,
		CUSTOM_ESCAPE_SEQUENCE,
		R_LINE_TERMINATOR
		}


	@SuppressWarnings("serial")
	public static class LexicalException extends Exception {	
		public LexicalException(String arg0) {
			super(arg0);
		}
	}
	
	public Scanner(Reader r) throws IOException {
		this.r = r;
		getChar();
		this.currPos = 0;
		this.currLine = 0;
		this.pos = 0;
		this.line=0;
		this.keyWords = new HashMap<>();
		keyWords.put("and", Kind.valueOf("KW_and"));
		keyWords.put("break", Kind.valueOf("KW_break"));
		keyWords.put("do", Kind.valueOf("KW_do"));
		keyWords.put("else", Kind.valueOf("KW_else"));
		keyWords.put("elseif", Kind.valueOf("KW_elseif"));
		keyWords.put("end", Kind.valueOf("KW_end"));
		keyWords.put("false", Kind.valueOf("KW_false"));
		keyWords.put("for", Kind.valueOf("KW_for"));
		keyWords.put("function", Kind.valueOf("KW_function"));
		keyWords.put("if", Kind.valueOf("KW_if"));
		keyWords.put("in", Kind.valueOf("KW_in"));
		keyWords.put("local", Kind.valueOf("KW_local"));
		keyWords.put("nil", Kind.valueOf("KW_nil"));
		keyWords.put("not", Kind.valueOf("KW_not"));
		keyWords.put("or", Kind.valueOf("KW_or"));
		keyWords.put("repeat", Kind.valueOf("KW_repeat"));
		keyWords.put("return", Kind.valueOf("KW_return"));
		keyWords.put("then", Kind.valueOf("KW_then"));
		keyWords.put("true", Kind.valueOf("KW_true"));
		keyWords.put("until", Kind.valueOf("KW_until"));
		keyWords.put("while", Kind.valueOf("KW_while"));
		keyWords.put("goto", Kind.valueOf("KW_goto"));
	}
	
	void getChar() throws IOException {
		  ch = r.read();	
	}


	public Token getNext() throws Exception {
		
			Token t = null;
			StringBuilder sb = new StringBuilder();
			boolean string_double_flag = false;
			boolean string_single_flag = false;
			State state = State.START;
			
			while(t==null) {
				switch(state) {
					case START: {
				
									switch(ch) {
										
										case '+': {
											t = new Token(OP_PLUS,"+",currPos,currLine);
											currPos++;
											pos++;
											getChar();
										}
										break;
										
										case '-': {
											state = State.POTENTIAL_COMMENT;
											sb = new StringBuilder();
											sb.append((char) ch);
											pos++;
											getChar();
										}
										break;
										
										case '*': {
											t = new Token(OP_TIMES,"*",currPos,currLine);
											currPos++;
											pos++;
											getChar();
										}
										break;
										
										case '%': {
											t = new Token(OP_MOD,"%",currPos,currLine);
											currPos++;
											pos++;
											getChar();											
										}
										break;
										
										case '^': {
											t = new Token(OP_POW,"^",currPos,currLine);
											currPos++;
											pos++;
											getChar();
										}
										break;
										
										case '#': {
											t = new Token(OP_HASH,"#",currPos,currLine);
											currPos++;
											pos++;
											getChar();
										}
										break;
										
										case '&': {
											t = new Token(BIT_AMP,"&",currPos,currLine);
											currPos++;
											pos++;
											getChar();
										}
										break;
										
										case '|': {
											t = new Token(BIT_OR,"|",currPos,currLine);
											currPos++;
											pos++;
											getChar();
										}	
										break;
										
										case '(': {
											t = new Token(LPAREN,"(",currPos,currLine);
											currPos++;
											pos++;
											getChar();
										}
										break;
										
										case ')': {
											t = new Token(RPAREN,")",currPos,currLine);
											currPos++;
											pos++;
											getChar();
										}
										break;
										
										case '{': {
											t = new Token(LCURLY,"{",currPos,currLine);
											currPos++;
											pos++;
											getChar();
										}
										break;
										
										case '}': {
											t = new Token(RCURLY,"}",currPos,currLine);
											currPos++;
											pos++;
											getChar();
										}
										break;
										
										case '[': {
											t = new Token(LSQUARE,"[",currPos,currLine);
											currPos++;
											pos++;
											getChar();
										}
										break;
										
										case ']': {
											t = new Token(RSQUARE,"]",currPos,currLine);
											currPos++;
											pos++;
											getChar();
										}
										break;
										
										case ';': {
											t = new Token(SEMI,";",currPos,currLine);
											currPos++;
											pos++;
											getChar();
										}
										break;
										
										case ',': {
											t = new Token(COMMA,",",currPos,currLine);
											currPos++;
											pos++;
											getChar();
										}
										break;
										
										case ' ':
										case '\t':
										case '\f':
										case '\b': {
											currPos++;
											pos++;
											getChar();
										}
										break;
										
										case '\n': {
											currPos=0;
											pos=0;
											line++;
											currLine=line;
											getChar();
										}
										break;
										
										case '\r': {
											state = State.R_LINE_TERMINATOR;
											pos++;
											getChar();
										}
										break;
										
										//---
										
										case '=': {
											getChar();
											state = State.HAVE_EQ;
											pos++;
											
										}
										break;
										
										case '/': {
											state = State.HAVE_SLASH;
											getChar();
											pos++;
										}
										break;
										
										case '~': {
											state = State.HAVE_XOR;
											getChar();
											pos++;
										}
										break;
										
										case '<': {
											state = State.HAVE_REL_LT;
											getChar();
											pos++;
										}
										break;
										
										case '>': {
											state = State.HAVE_REL_GT;
											getChar();
											pos++;
										}
										break;
										
										case '.': {
											state = State.HAVE_DOT;
											getChar();
											pos++;
										}
										break;
										
										case ':': {
											state = State.HAVE_COLON;
											getChar();
											pos++;
										}
										break;

										case '0': {
											t = new Token(INTLIT,"0",currPos,currLine);
											currPos++;
											pos++;
											getChar();
										}
										break;
										
										case -1 : {
											if(string_double_flag==true || string_single_flag==true) {
												throw new LexicalException("String invalid. Missing \" at position: " + currPos + " at line: " + currLine);
											}else {
												t = new Token(EOF,"EOF",currPos,currLine);
											}
											
										}
										break;
										
										case '"':{
											state = State.STRING_LIT_DOUBLE;
											sb = new StringBuilder();
											sb.append((char) ch);
											pos++;
											getChar();
											string_double_flag = true;
										}
										break;
										
										case '\'':{
											state = State.STRING_LIT_SINGLE;
											sb = new StringBuilder();
											sb.append((char) ch);
											pos++;
											getChar();
											string_single_flag = true;
										}
										break;
										
										case '\\':{
											state = State.CUSTOM_ESCAPE_SEQUENCE;
											pos++;
											getChar();
										}
										break;
										
										default: {
											if(Character.isDigit(ch)) {
												state = State.IN_NUMLIT;
												sb = new StringBuilder();
												sb.append((char) ch);
												pos++;
												getChar();
											}else if(Character.isJavaIdentifierStart(ch)) {
												state = State.IN_IDENT;
												sb = new StringBuilder();
												sb.append((char)ch);
												pos++;
												getChar();
											}else {
												throw new LexicalException("Illegal character at position: " + currPos + " at line: " + currLine);
											}
										}
									}
								}
								break;
					
					case IN_NUMLIT: {
										if(Character.isDigit(ch)) {
											sb.append((char) ch);
											pos++;
											getChar();
										}else {
											try {
												Integer.parseInt(sb.toString());
												t = new Token(INTLIT,sb.toString(),currPos,currLine);
												currPos=pos;
											}catch(NumberFormatException e) {
												throw new LexicalException("Integer out of range at position: " + currPos + " at line: " + currLine);
											}
										}
									}
									break;
									
					case HAVE_EQ: {
									if(ch=='=') {
										t = new Token(REL_EQEQ,"==",currPos,currLine);
										pos++;
										currPos=pos;
										getChar();
									}else {
										t = new Token(ASSIGN,"=",currPos,currLine);
										currPos++;
									}
								  }
									break;
									
					case HAVE_SLASH: {
										if(ch=='/') {
											t = new Token(OP_DIVDIV,"//",currPos,currLine);
											pos++;
											currPos=pos;
											getChar();
										}else {
											t = new Token(OP_DIV,"/",currPos,currLine);
											currPos++;
										}
									}
									break;
									
					case HAVE_XOR: {
										if(ch=='=') {
											t = new Token(REL_NOTEQ,"~=",currPos,currLine);
											pos++;
											currPos=pos;
											getChar();
										}else {
											t = new Token(BIT_XOR,"~",currPos,currLine);
											currPos++;
										}
									}
									break;
									
					case HAVE_REL_LT: {
										if(ch=='<') {
											t = new Token(BIT_SHIFTL,"<<",currPos,currLine);
											pos++;
											currPos=pos;
											getChar();
										}else if(ch=='=') {
											t = new Token(REL_LE,"<=",currPos,currLine);
											pos++;
											currPos=pos;
											getChar();
										}else {
											t = new Token(REL_LT,"<",currPos,currLine);
											currPos++;
										}
									}
									break;
									
					case HAVE_REL_GT: {
										if(ch=='>') {
											t = new Token(BIT_SHIFTR,">>",currPos,currLine);
											pos++;
											currPos=pos;
											getChar();
										}else if(ch=='=') {
											t = new Token(REL_GE,">=",currPos,currLine);
											pos++;
											currPos=pos;
											getChar();
										}else {
											t = new Token(REL_GT,">",currPos,currLine);
											currPos++;
										}
									}
									break;
									
					case HAVE_COLON: {
										if(ch==':') {
											t = new Token(COLONCOLON,"::",currPos,currLine);
											pos++;
											currPos=pos;
											getChar();
										}else {
											t = new Token(COLON,":",currPos,currLine);
											currPos++;
										}
									}
									break;
									
					case HAVE_DOT: {
										if(ch=='.') {
											state = State.HAVE_DOUBLE_DOT;
											pos++;
											getChar();
										}else {
											t = new Token(DOT,".",currPos,currLine);
											currPos++;
										}
									}
									break;
									
					case HAVE_DOUBLE_DOT: {
										if(ch=='.') {
											t = new Token(DOTDOTDOT,"...",currPos,currLine);
											pos++;
											currPos=pos;
											getChar();
										}else {
											t = new Token(DOTDOT,"..",currPos,currLine);
											currPos=pos;
										}
									}
									break;
									
					case IN_IDENT: {
									if(Character.isJavaIdentifierPart(ch)) {
										sb.append((char)ch);
										pos++;
										getChar();
									}else {
										if(this.keyWords.get(sb.toString())!=null) {
											t = new Token(this.keyWords.get(sb.toString()),sb.toString(),currPos,currLine);
										}else {
											t = new Token(NAME,sb.toString(),currPos,currLine);
										}
										currPos=pos;
									}
								}
								break;
								
					case STRING_LIT_DOUBLE:{
									if(ch=='\"') {
										string_double_flag = false;
										sb.append((char) ch);
										System.out.println(sb.toString());
										t = new Token(STRINGLIT,sb.toString(),currPos,currLine);
										pos++;
										currPos=pos;
										currLine=line;
										getChar();
									}else if(ch=='\''|| ch==-1) {
										throw new LexicalException("Invalid String at position: " + currPos + " at line: " + currLine);
									}else if(ch=='\\'){
										getChar();
										System.out.println((char) ch);
										if(ch=='a') {
											sb.append('\u0007');
											pos++;
											getChar();
										}else if(ch=='v'){
											sb.append('\u2B7F');
											pos++;
											getChar();
										}else {	
											sb.append('\\');
											getChar();
										}
									}else if(ch=='\n'){
										pos=0;
										line++;
										sb.append((char) ch);
										getChar();
									}else if(ch=='\r') {
										sb.append((char) ch);
										getChar();
										if(ch=='\n') {
											sb.append((char) ch);
											getChar();
										}
										pos=0;
										line++;
										
									}else if(ch=='\f' || ch=='\t' || ch=='\b') {
										getChar();
									}else {
										System.out.println((char) ch);
										sb.append((char) ch);
										pos++;
										getChar();									
									}
								}
								break;
					
					case STRING_LIT_SINGLE: {
									if(ch=='\'') {
										string_single_flag = false;
										sb.append((char) ch);
										t = new Token(STRINGLIT,sb.toString(),currPos,currLine);
										pos++;
										currPos=pos;
										currLine=line;
										getChar();
									}else if(ch=='"'|| ch==-1) {	
										throw new LexicalException("Invalid String at position: " + currPos + " at line: " + currLine);
									}else if(ch=='\\'){
										getChar();
										if(ch=='a') {
											sb.append('\u0007');
											pos++;
											getChar();
										}else if(ch=='v') {
											sb.append('\u2B7F');
											pos++;
											getChar();
										}else {
											sb.append('\\');
											getChar();
										}
									}else if(ch=='\n'){
										pos=0;
										line++;
										sb.append((char) ch);
										getChar();
									}else if(ch=='\r') {
										sb.append((char) ch);
										getChar();
										if(ch=='\n') {
											sb.append((char) ch);
											getChar();
										}
										pos=0;
										line++;								
									}else if(ch=='\f' || ch=='\t' || ch=='\b') {
										getChar();
									}else {
										sb.append((char) ch);
										pos++;
										getChar();									
									}		
								}
								break;
								
					case CUSTOM_ESCAPE_SEQUENCE: {
									if(ch=='a' || ch=='v') {
										getChar();
										state = State.START;
									}else {
										throw new LexicalException("Illegal escape sequence at position: " + currPos + " at line: " + currLine);
									}
								}
								break;
								
					case R_LINE_TERMINATOR: {
									if(ch=='\n') {
										currPos=0;
										pos=0;
										line++;
										currLine=line;
										getChar();
									}else {
										currPos=0;
										pos=0;
										line++;
										currLine=line;
										
										state=State.START;	
									}							
								}
								break;
					
					case POTENTIAL_COMMENT: {
									if(ch=='-') {
										state = State.COMMENT;
										pos++;
										currPos=pos;
										getChar();
									}else {
										t = new Token(OP_MINUS,"-",currPos,currLine);
										currPos=pos;
									}
								}
								break;
							
					case COMMENT: {
									if(ch=='\n') {
										state = State.START;
										currLine++;
										currPos=0;
										pos=0;
										getChar();
									}else if(ch=='\r') {
										getChar();
										if(ch=='\n') {
											getChar();
										}
										state = State.START;
										currLine++;
										currPos=0;
										pos=0;
									}else if(ch==-1){
										t = new Token(EOF,"EOF",currPos,currLine);
									}else {
										currPos++;
										pos++;
										getChar();
									}
									
								}
								break;
								
					default: {
						throw new LexicalException("Illegal state at position: " + currPos + " at line: " + currLine);
					}
					
				}
			}
					
			return t;
		
	}


}
