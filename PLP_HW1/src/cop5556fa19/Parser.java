/**
 * Developed  for the class project in COP5556 Programming Language Principles 
 * at the University of Florida, Fall 2019.
 * 
 * This software is solely for the educational benefit of students 
 * enrolled in the course during the Fall 2019 semester.  
 * 
 * This software, and any software derived from it,  may not be shared with others or posted to public web sites,
 * either during the course or afterwards.
 * 
 *  @Beverly A. Sanders, 2019
 */

package cop5556fa19;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import cop5556fa19.AST.Block;
import cop5556fa19.AST.Chunk;
import cop5556fa19.AST.Exp;
import cop5556fa19.AST.ExpBinary;
import cop5556fa19.AST.ExpFalse;
import cop5556fa19.AST.ExpFunction;
import cop5556fa19.AST.ExpFunctionCall;
import cop5556fa19.AST.ExpInt;
import cop5556fa19.AST.ExpList;
import cop5556fa19.AST.ExpName;
import cop5556fa19.AST.ExpNil;
import cop5556fa19.AST.ExpString;
import cop5556fa19.AST.ExpTable;
import cop5556fa19.AST.ExpTableLookup;
import cop5556fa19.AST.ExpTrue;
import cop5556fa19.AST.ExpUnary;
import cop5556fa19.AST.ExpVarArgs;
import cop5556fa19.AST.Field;
import cop5556fa19.AST.FieldExpKey;
import cop5556fa19.AST.FieldImplicitKey;
import cop5556fa19.AST.FieldList;
import cop5556fa19.AST.FieldNameKey;
import cop5556fa19.AST.FuncBody;
import cop5556fa19.AST.FuncName;
import cop5556fa19.AST.Name;
import cop5556fa19.AST.ParList;
import cop5556fa19.AST.RetStat;
import cop5556fa19.AST.Stat;
import cop5556fa19.AST.StatAssign;
import cop5556fa19.AST.StatBreak;
import cop5556fa19.AST.StatDo;
import cop5556fa19.AST.StatFor;
import cop5556fa19.AST.StatForEach;
import cop5556fa19.AST.StatFunction;
import cop5556fa19.AST.StatGoto;
import cop5556fa19.AST.StatIf;
import cop5556fa19.AST.StatLabel;
import cop5556fa19.AST.StatLocalAssign;
import cop5556fa19.AST.StatLocalFunc;
import cop5556fa19.AST.StatRepeat;
import cop5556fa19.AST.StatWhile;
import cop5556fa19.Token.Kind;
import static cop5556fa19.Token.Kind.*;

public class Parser {
	
	private int counter;
	private List<Stat> stats;
	
	@SuppressWarnings("serial")
	class SyntaxException extends Exception {
		Token t;
		
		public SyntaxException(Token t, String message) {
			super(t.line + ":" + t.pos + " " + message);
		}
	}
	
	final Scanner scanner;
	Token t;  //invariant:  this is the next token


	public Parser(Scanner s) throws Exception {
		this.scanner = s;
		t = scanner.getNext(); //establish invariant
	}


	Exp exp() throws Exception {
		Token first = t;
		Exp e0 = null;
		Exp e1 = null;
		e0 = term1();
		while (isKind(KW_or)) {
			Token op = t;
			consume();
			e1 = term1();
			e0 = new ExpBinary(first, e0, op, e1);
		}
		return e0;
	}
	
	Exp term1() throws Exception{
		Token first = t;
		Exp e0 = null;
		Exp e1 = null;
		e0 = term2();
		while (isKind(KW_and)) { 
			Token op = t;
			consume();
			e1 = term2();
			e0 = new ExpBinary(first,e0,op,e1);
		}
		return e0;
	}

	
	Exp term2() throws Exception{
		Token first = t;
		Exp e0 = null;
		Exp e1 = null;
		e0 = term3();
		while (isKind(REL_LE) || isKind(REL_GE) || t.isKind(REL_LT) || t.isKind(REL_GT) || t.isKind(REL_EQEQ) || t.isKind(REL_NOTEQ)) { 
			Token op = t;
			consume();
			e1 = term3();
			e0 = new ExpBinary(first,e0,op,e1);
		}
		return e0;
	}
	
	Exp term3() throws Exception{
		Token first = t;
		Exp e0 = null;
		Exp e1 = null;
		e0 = term4();
		while (t.isKind(BIT_OR)) { 
			Token op = t;
			consume();
			e1 = term4();
			e0 = new ExpBinary(first,e0,op,e1);
		}
		return e0;
	}
	
	Exp term4() throws Exception{
		Token first = t;
		Exp e0 = null;
		Exp e1 = null;
		e0 = term5();
		while (t.isKind(BIT_XOR)) { 
			Token op = t;
			consume();
			e1 = term5();
			e0 = new ExpBinary(first,e0,op,e1);
		}
		return e0;
	}
	
	Exp term5() throws Exception{
		Token first = t;
		Exp e0 = null;
		Exp e1 = null;
		e0 = term6();
		while (t.isKind(BIT_AMP)) { 
			Token op = t;
			consume();
			e1 = term6();
			e0 = new ExpBinary(first,e0,op,e1);
		}
		return e0;
	}
	
	Exp term6() throws Exception{
		Token first = t;
		Exp e0 = null;
		Exp e1 = null;
		e0 = term7();
		while (t.isKind(BIT_SHIFTL) || t.isKind(BIT_SHIFTR)) { 
			Token op = t;
			consume();
			e1 = term7();
			e0 = new ExpBinary(first,e0,op,e1);
		}
		return e0;
	}
	
	Exp term7() throws Exception{
		Token first = t;
		Exp e0 = null;
		Exp e1 = null;
		e0 = term8();
		while (t.isKind(DOTDOT)) { 
			Token op = t;
			consume();
			e1 = term7();
			e0 = new ExpBinary(first,e0,op,e1);
		}
		return e0;
	}
	
	Exp term8() throws Exception{
		Token first = t;
		Exp e0 = null;
		Exp e1 = null;
		e0 = term9();
		while (t.isKind(OP_PLUS) || t.isKind(OP_MINUS)) { 
			Token op = t;
			consume();
			e1 = term9();
			e0 = new ExpBinary(first,e0,op,e1);
		}
		return e0;
	}
	
	Exp term9() throws Exception{
		Token first = t;
		Exp e0 = null;
		Exp e1 = null;
		e0 = term10();
		while (t.isKind(OP_TIMES) || t.isKind(OP_DIV) || t.isKind(OP_DIVDIV) || t.isKind(OP_MOD)) { 
			Token op = t;
			consume();
			e1 = term10();
			e0 = new ExpBinary(first,e0,op,e1);
		}
		return e0;
	}
	
	Exp term10() throws Exception{
		Token first = t;
		Exp e0 = null;
		Exp e1 = null;
		
		if(t.isKind(KW_not) || t.isKind(OP_HASH) || t.isKind(OP_MINUS) || t.isKind(BIT_XOR)) {
			Token op = t;
			consume();
			e1 = exp();
			e0 = new ExpUnary(first,op.kind,e1);
		}else {
			e0=term11();
		}
		
		return e0;
	}
	
	Exp term11() throws Exception{
		Token first = t;
		Exp e0 = null;
		Exp e1 = null;
		e0 = factor();
		while (t.isKind(OP_POW)) { 
			Token op = t;
			consume();
			e1 = term11();
			e0 = new ExpBinary(first,e0,op,e1);
		}
		return e0;
	}
	
	Exp factor() throws Exception{
		Kind kind = t.kind;
		Exp e0 = null;
		switch (kind) {
		case KW_nil: {			
			e0 = new ExpNil(t);
			consume();
		}
			break;
		case KW_false: {			
			e0 = new ExpFalse(t);
			consume();
		}
			break;
		case KW_true: {			
			e0 = new ExpTrue(t);
			consume();
		}
			break;
		case INTLIT: {
			e0 = new ExpInt(t);
			consume();
		}
			break;
		case STRINGLIT: {			
			e0 = new ExpString(t);
			consume();
		}
			break;
		case DOTDOTDOT: {
			e0 = new ExpVarArgs(t);
			consume();
		}
		break;
		case KW_function:{
			
			Token fstFunction = t;
			consume();
			
			FuncBody funcBody = funcBody();
			e0 = new ExpFunction(fstFunction,funcBody);
		}
		break;
		
		case NAME:{
			Token fname = t;
			//System.out.println("1:"+t);
			Exp e = new ExpName(fname);
			//System.out.println("2:"+t);
			consume();
			//System.out.println("3:"+t);
			
			e0 = prefixTail(e);
			//System.out.println(e0);
			
		}
		break;
		
		case LCURLY:{
			
			e0 = expTable();
			
		}
		break;
		
		case LPAREN: {
			consume();
			Exp e=exp();
			match(RPAREN);
			
			e0 = prefixTail(e);

		}
			break;
		default:
			//you will want to provide a more useful error message
			//throw new SyntaxException("illegal factor "+t.getLinePos()+" "+t.getText());
			throw new SyntaxException(t,"Illegal factor Position: " + t.pos + " Line: " + t.line);
		}		
		return e0;
	}


private Exp andExp() throws Exception{
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("andExp");  //I find this is a more useful placeholder than returning null.
	}

public Chunk parse() throws Exception {
	Chunk chunk = chunk();
	if(!t.isKind(EOF)) {
		throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
	}
	
	return chunk;
}

private ExpTable expTable() throws Exception {
	Token fstTable = t;
	
	if(t.isKind(LCURLY)) {
		consume();
		if(t.isKind(RCURLY)) {
			consume();
			return new ExpTable(fstTable,new ArrayList<Field>());
		}else {
			List<Field> fieldList = fieldList();
			//System.out.println(fieldList);
			//System.out.println(t);
			
			if(t.isKind(RCURLY)) {
				consume();
				return new ExpTable(fstTable,fieldList);
			}else {
				throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
			}
		}
	}else {
		throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
	}
}

private List<Field> fieldList() throws Exception{
	List<Field> flist = new ArrayList<>();
	Field f1 = field();
	
	flist.add(f1);
	//System.out.println("x:"+flist);
	while(t.isKind(COMMA) || t.isKind(SEMI)) {
		consume();
		f1 = field();
		flist.add(f1);
	}
	
	return flist;
}

private Field field() throws Exception {
	Token f = t;
	
	if(t.isKind(LSQUARE)) {
		consume();
		Exp e1 = exp();
		
		if(t.isKind(RSQUARE)) {
			consume();
			
			if(t.isKind(ASSIGN)) {
				consume();
				Exp e2 = exp();
				return new FieldExpKey(f,e1,e2);
			}else {
				throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
			}
		}else {
			throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
		}
	}else if(t.isKind(NAME)) {
		Name name = new Name(t,t.text);
		Exp en = exp();
		
		if(en instanceof ExpName) {
			if(t.isKind(ASSIGN)) {
				consume();
				Exp e1 = exp();
				return new FieldNameKey(f,name,e1);
			}else {
				return new FieldImplicitKey(f,en);
			}
		}else if(en instanceof ExpFunctionCall || en instanceof ExpTableLookup){
			return new FieldImplicitKey(f,en);
		}else {
			throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
		}
	}else {
		return new FieldImplicitKey(f,exp());
	}
}

private List<Exp> varList() throws Exception{
	List<Exp> varList = new ArrayList<>();
	
	Exp var = var();
	varList.add(var);
	
	while(t.isKind(COMMA)) {
		consume();
		var = var();
		varList.add(var);
	}
	
	return varList;
}

private Exp var() throws Exception {
	Exp e = prefixExp();
	
	if(e instanceof ExpFunctionCall) {
		throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
	}else {
		return e;
	}
	
}

private Exp prefixExp() throws Exception {
	
	if(t.isKind(NAME)) {
		ExpName ename = new ExpName(t);
		consume();
		
		Exp pe = prefixTail(ename);
		return pe;
	}else if(t.isKind(LPAREN)) {
		consume();
		Exp e = exp();
		
		if(t.isKind(RPAREN)) {
			consume();
			return prefixTail(e);
		}else {
			throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
		}
	}else {
		throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
	}
}

private Stat stat1() throws Exception {
	this.counter++;
	Token f = t;
	
	if(t.isKind(SEMI)) {
		consume();
	}
	
	if(t.isKind(NAME) || t.isKind(LPAREN)) {
		List<Exp> varList = varList();
		if(t.isKind(ASSIGN)) {
			consume();
			List<Exp> expList = expList();
			return new StatAssign(f,varList,expList);
		}else {
			throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
		}
	}else if(t.isKind(COLONCOLON)) {
		consume();
		if(t.isKind(NAME)) {
			Name name = new Name(t,t.text);
			consume();
			if(t.isKind(COLONCOLON)) {
				consume();
				return new StatLabel(f,name,null,counter);
			}else {
				throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
			}
		}else {
			throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
		}
	}else if(t.isKind(KW_break)) {
		consume();
		return new StatBreak(f);
	}else if(t.isKind(KW_goto)) {
		consume();
		if(t.isKind(NAME)) {
			Name name = new Name(t,t.text);
			consume();
			return new StatGoto(f,name);
		}else {
			throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
		}
	}else if(t.isKind(KW_do)) {
		consume();
		Block b = block();
		if(t.isKind(KW_end)) {
			consume();
			return new StatDo(f,b);
		}else {
			throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
		}
	}else if(t.isKind(KW_while)) {
		consume();
		Exp e = exp();
		if(t.isKind(KW_do)) {
			consume();
			Block b = block();
			if(t.isKind(KW_end)) {
				consume();
				return new StatWhile(f,e,b);
			}else {
				throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
			}
		}else {
			throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
		}
	}else if(t.isKind(KW_repeat)) {
		consume();
		Block b = block();
		if(t.isKind(KW_until)) {
			consume();
			Exp e = exp();		
			return new StatRepeat(f,b,e);
		}else {
			throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
		}
	}else if(t.isKind(KW_if)) {
		consume();
		List<Exp> expList = new ArrayList<>();
		List<Block> blockList = new ArrayList<>();
		
		Exp e = exp();
		expList.add(e);
		
		if(t.isKind(KW_then)) {
			consume();
			Block b = block();
			blockList.add(b);
			
			while(t.isKind(KW_elseif)) {
				consume();
				Exp e1 = exp();
				expList.add(e1);
				if(t.isKind(KW_then)) {
					consume();
					Block b1 = block();
					blockList.add(b1);
				}else {
					throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
				}
			}
			
			if(t.isKind(KW_else)) {
				consume();
				Block b2 = block();
				blockList.add(b2);
			}
			
			if(t.isKind(KW_end)) {
				consume();
			}else {
				throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
			}
			
			return new StatIf(f,expList,blockList);
			
		}else {
			throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
		}
	}else if(t.isKind(KW_for)) {
		consume();
		
		ExpName fname;
		
		if(t.isKind(NAME)) {
			ExpName ename = new ExpName(t);
			fname = ename;
			consume();
			if(t.isKind(ASSIGN)) {
				consume();
				Exp e = exp();
				if(t.isKind(COMMA)) {
					consume();
					Exp e1 = exp();
					Exp e2 = null;
					
					if(t.isKind(COMMA)) {
						consume();
						
						e2 = exp();
						
					}
					
					if(t.isKind(KW_do)) {
						consume();
						
						Block b = block();
						if(t.isKind(KW_end)) {
							consume();	
							
							return new StatFor(f,ename,e,e1,e2,b);
						}else {
							throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
						}
					}else {
						throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
					}
				}else {
					throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
				}
			}else if(t.isKind(COMMA)) {
				consume();
				List<ExpName> nameList = new ArrayList<>();
				nameList.add(fname);
				
				List<ExpName> nameList1 = nameList();
				nameList.addAll(nameList1);
				
				if(t.isKind(KW_in)) {
					consume();
					List<Exp> expList = expList();
					if(t.isKind(KW_do)) {
						consume();
						Block b = block();
						if(t.isKind(KW_end)) {
							consume();
							return new StatForEach(f,nameList,expList,b);
						}else {
							throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
						}
					}else {
						throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
					}
				}else {
					throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
				}
				
			}else {
				throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
			}
		}else {
			throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
		}
	}else if(t.isKind(KW_function)) {
		consume();
		FuncName funcName = funcName();
		
		FuncBody funcBody = funcBody();
		
		return new StatFunction(f,funcName,funcBody);
	}else if(t.isKind(KW_local)) {
		consume();
		if(t.isKind(KW_function)) {
			consume();
			if(t.isKind(NAME)) {
				
				ExpName ename = new ExpName(t);
				List<ExpName> expNameList = new ArrayList<>();
				expNameList.add(ename);
				
				FuncName funcName = new FuncName(t,expNameList,null);
				consume();
				
				FuncBody funcBody = funcBody();
				return new StatLocalFunc(f,funcName,funcBody);
			}else {
				throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
			}
		}else if(t.isKind(NAME)){
			List<ExpName> expNameList = nameList();
			List<Exp> expList = new ArrayList<>();
			
			if(t.isKind(ASSIGN)) {
				consume();
				expList = expList();
			}
			
			return new StatLocalAssign(f,expNameList,expList);
		}else {
			throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
		}
	}else {
		return null;
	}
}

private RetStat retStat() throws Exception {
	Token f = t;
	
	if(t.isKind(KW_return)) {
		consume();
		List<Exp> expList = expList();
		if(t.isKind(SEMI)) {
			consume();
		}		
		return new RetStat(f,expList);
	}else {
		throw new SyntaxException(t,"Illegal factor Position: " + t.pos + " Line: " + t.line);
	}
	
}

private FuncBody funcBody() throws Exception {

	boolean hasVarArgs = false;
	ParList parList;
	if(t.isKind(LPAREN)) {
		Token f = t;
		consume();
		if(t.isKind(RPAREN)) {
			consume();
			Block b = block();
			if(t.isKind(KW_end)) {
				consume();
				return new FuncBody(f,null,b);
			}else {
				throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
			}
		}else if(t.isKind(NAME)){
			List<Name> nameList = new ArrayList<>();
			Token pname = t;
			Token fname = t;
			nameList.add(new Name(fname,t.text));
			consume();
			
			while(t.isKind(COMMA)) {
				consume();
				fname = t;
				
				if(t.isKind(NAME)) {
					nameList.add(new Name(fname,t.text));
					consume();
				}else if(t.isKind(DOTDOTDOT)){
					consume();
					hasVarArgs=true;
					break;
				}else {
					throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
				}
			}
			
			parList = new ParList(pname,nameList,hasVarArgs);
			
			if(t.isKind(RPAREN)) {
				consume();
				Block b = block();
				if(t.isKind(KW_end)) {
					consume();
					return new FuncBody(f,parList,b);
				}else {
					throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
				}
			}else {
				throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
			}

		}else if(t.isKind(DOTDOTDOT)) {
			Token d = t;
			parList = new ParList(d,new ArrayList<Name>(),true);
			consume();
			
			if(t.isKind(RPAREN)) {
				consume();
				Block b = block();
				if(t.isKind(KW_end)) {
					consume();
					return new FuncBody(f,parList,b);
				}else {
					throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
				}
			}else {
				throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
			}
		}else {
			throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
		}
		
		
	}else {
		throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
	}
}

private FuncName funcName() throws Exception {
	Token f = t;
	List<ExpName> enameList = new ArrayList<>();
	ExpName afterColonName = null;
	
	
	if(t.isKind(NAME)) {
		enameList.add(new ExpName(t));
		consume();
		
		while(t.isKind(DOT)) {
			consume();
			if(t.isKind(NAME)) {
				enameList.add(new ExpName(t));
				consume();
			}else {
				throw new SyntaxException(t,"Illegal factor Position: " + t.pos + " Line: " + t.line);
			}
		}	
		
		if(t.isKind(DOT)) {
			consume();
			if(t.isKind(NAME)) {
				afterColonName = new ExpName(t);
				consume();
			}else {
				throw new SyntaxException(t,"Illegal factor Position: " + t.pos + " Line: " + t.line);
			}
		}
		
		return new FuncName(f,enameList,afterColonName);
	}else {
		throw new SyntaxException(t,"Illegal factor Position: " + t.pos + " Line: " + t.line);
	}
}

private Chunk chunk() throws Exception {
	Token f = t;
	return new Chunk(f,block());
}

private List<ExpName> nameList() throws Exception{

	List<ExpName> nameList = new ArrayList<>();
	
	if(t.isKind(NAME)) {
		nameList.add(new ExpName(t));
	}
	
	consume();
	while(t.isKind(COMMA)) {
		consume();
		if(t.isKind(NAME)) {
			nameList.add(new ExpName(t));
			consume();
		}
	}
	
	return nameList;
}



	private Exp prefixTail(Exp e) throws Exception {
		Token f = t;
		//System.out.println(",,,"+t);
		
		if(t.isKind(LSQUARE)) {
			consume();
			Exp exp1 = exp();
			if(t.isKind(RSQUARE)) {
				consume();
				
				Exp exp3 = new ExpTableLookup(f,e,exp1);

				return prefixTail(exp3);

			}else{
				throw new SyntaxException(t,"Illegal factor Position: " + t.pos + " Line: " + t.line);
			}
		}else if(t.isKind(DOT)) {
			consume();
			if(t.isKind(NAME)) {
				Exp exp1 = new ExpString(t);
				
				Exp exp2 = new ExpTableLookup(f,e,exp1);
				consume();
				return prefixTail(exp2); 
			}else {
				throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
			}
		}else if(t.isKind(LPAREN) || t.isKind(LCURLY) || t.isKind(STRINGLIT)) {
			
			List<Exp> argsList = args();
			Exp exp1 = new ExpFunctionCall(f,e,argsList);
			
			return prefixTail(exp1);
		}else if(t.isKind(COLON)) {
			consume();
			if(t.isKind(NAME)) {
				Exp exp1 = new ExpString(t);
				consume();
				List<Exp> argsList = args();
				Exp exp3 = new ExpTableLookup(f,e,exp1);
				
				Exp exp4 = new ExpFunctionCall(f,exp3,argsList);
				return prefixTail(exp4);
			}else {
				throw new SyntaxException(t,"at " + t.pos + " Line: " + t.line);
			}
		}else {
			return e; 
		}
		
	}


//	private Block block() throws Exception {
//		this.counter=0;
//		
//		Token f = t;
//		List<Stat> statList = new ArrayList<>();
//		Stat stat = stat1();
//		
//		while(stat!=null && !t.isKind(KW_return)) {
//
//			statList.add(stat);
//			stat = stat1();
//		}
//		
//		if(t.isKind(KW_return)) {
//			statList.add(retStat());
//		}
//		
//		return new Block(f,statList);  //this is OK for Assignment 2
//	}
	
	private Block block() throws Exception {
		Token f = t;
		Stat stat = stat1();
		this.counter=0;
		
		List<Stat> statList = new ArrayList<>();
		while(stat!=null) {

			statList.add(stat);
			stat = stat1();
		}
		
		if(t.isKind(KW_return)) {
			statList.add(retStat());
		}
		
		for(int i=0;i<statList.size();i++) {
			if(statList.get(i) instanceof StatLabel) {
				StatLabel sl = (StatLabel) statList.get(i);
				Token ft = sl.firstToken;
				Name name = sl.label;
				Block b = new Block(f,statList);
				int index = sl.getIndex();
				
				statList.set(i, new StatLabel(ft,name,b,index));
			}
		}
		
		return new Block(f,statList);
	}
	
	List<Exp> args() throws Exception {

		List<Exp> list = new ArrayList<>();
		
		if(t.isKind(LPAREN)) {
			consume();
			if(t.isKind(RPAREN)) {
				consume();
				return list;
			}else {
				List<Exp> e = expList();
				if(t.isKind(RPAREN)) {
					consume();
					return e;
				}else {
					throw new SyntaxException(t,"Position: "+ t.pos + " Line: " + t.line);
				}
			}
		}else if(t.isKind(LCURLY)) {
			list.add(expTable());
			return list;
		}else if(t.isKind(STRINGLIT)){
			consume();
			list.add(new ExpString(t));
			return list;
		}else {
			throw new SyntaxException(t,"Position: "+ t.pos + " Line: " + t.line);
		}
		
	}
	
	private List<Exp> expList() throws Exception {
		
		List<Exp> list = new ArrayList<>();
		
		Exp e = exp();
		list.add(e);
		
		while(t.isKind(COMMA)) {
			consume();
			e=exp();
			list.add(e);
		}
		
		return list;
		
	}


	protected boolean isKind(Kind kind) {
		return t.kind == kind;
	}

	protected boolean isKind(Kind... kinds) {
		for (Kind k : kinds) {
			if (k == t.kind)
				return true;
		}
		return false;
	}

	/**
	 * @param kind
	 * @return
	 * @throws Exception
	 */
	Token match(Kind kind) throws Exception {
		Token tmp = t;
		if (isKind(kind)) {
			consume();
			return tmp;
		}
		error(kind);
		return null; // unreachable
	}

	/**
	 * @param kind
	 * @return
	 * @throws Exception
	 */
	Token match(Kind... kinds) throws Exception {
		Token tmp = t;
		if (isKind(kinds)) {
			consume();
			return tmp;
		}
		StringBuilder sb = new StringBuilder();
		for (Kind kind1 : kinds) {
			sb.append(kind1).append(kind1).append(" ");
		}
		error(kinds);
		return null; // unreachable
	}

	Token consume() throws Exception {
		Token tmp = t;
		//System.out.println("."+t);
        t = scanner.getNext();
        //System.out.println(".."+t);
		return tmp;
	}
	
	void error(Kind... expectedKinds) throws SyntaxException {
		String kinds = Arrays.toString(expectedKinds);
		String message;
		if (expectedKinds.length == 1) {
			message = "Expected " + kinds + " at " + t.line + ":" + t.pos;
		} else {
			message = "Expected one of" + kinds + " at " + t.line + ":" + t.pos;
		}
		throw new SyntaxException(t, message);
	}

	void error(Token t, String m) throws SyntaxException {
		String message = m + " at " + t.line + ":" + t.pos;
		throw new SyntaxException(t, message);
	}
	


}
