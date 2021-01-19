package interpreter;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import cop5556fa19.Token;
import cop5556fa19.AST.ASTVisitor;
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
import interpreter.built_ins.print;
import interpreter.built_ins.println;
import interpreter.built_ins.toNumber;
//import interpreter.ASTVisitorAdapter.StaticSemanticException;

public abstract class ASTVisitorAdapter implements ASTVisitor {
	
	private boolean terminate=false;
	private boolean loopBreakFlag = false;
	
	@SuppressWarnings("serial")
	public static class StaticSemanticException extends Exception{
		
			public StaticSemanticException(Token first, String msg) {
				super(first.line + ":" + first.pos + " " + msg);
			}
		}
	
	
	@SuppressWarnings("serial")
	public
	static class TypeException extends Exception{

		public TypeException(String msg) {
			super(msg);
		}
		
		public TypeException(Token first, String msg) {
			super(first.line + ":" + first.pos + " " + msg);
		}
		
	}
	
	public abstract List<LuaValue> load(Reader r) throws Exception;

	@Override
	public Object visitExpNil(ExpNil expNil, Object arg) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpBin(ExpBinary expBin, Object arg) throws Exception {
		Exp e0 = expBin.e0;
		Token.Kind op = expBin.op;
		Exp e1 = expBin.e1;
		
		switch(op) {
		case OP_PLUS:{
			LuaValue lhs = (LuaValue) e0.visit(this, arg);
			LuaValue rhs = (LuaValue) e1.visit(this, arg);
			  
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				int r = ((LuaInt) lhs).v + ((LuaInt) rhs).v;
				return new LuaInt(r);							  
			}else if(lhs instanceof LuaString && rhs instanceof LuaString) {
				int a = Integer.parseInt(((LuaString) lhs).value);
				int b = Integer.parseInt(((LuaString) rhs).value);
				return new LuaInt(a+b);
			}else if(lhs instanceof LuaInt && rhs instanceof LuaString) {
				int a = ((LuaInt) lhs).v;
				int b = Integer.parseInt(((LuaString) rhs).value);
				return new LuaInt(a+b);
			}else if(lhs instanceof LuaString && rhs instanceof LuaInt) {
				int a = Integer.parseInt(((LuaString) lhs).value);
				int b = ((LuaInt) rhs).v;
				return new LuaInt(a+b);
			}else {
				throw new UnsupportedOperationException();
			}
		}
  
		case OP_MINUS:{
			LuaValue lhs = (LuaValue) e0.visit(this, arg);
			LuaValue rhs = (LuaValue) e1.visit(this, arg);
			
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				int r = ((LuaInt) lhs).v - ((LuaInt) rhs).v;
				return new LuaInt(r);							  
			}else if(lhs instanceof LuaString && rhs instanceof LuaString) {
				int a = Integer.parseInt(((LuaString) lhs).value);
				int b = Integer.parseInt(((LuaString) rhs).value);
				return new LuaInt(a-b);
			}else if(lhs instanceof LuaInt && rhs instanceof LuaString) {
				int a = ((LuaInt) lhs).v;
				int b = Integer.parseInt(((LuaString) rhs).value);
				return new LuaInt(a-b);
			}else if(lhs instanceof LuaString && rhs instanceof LuaInt) {
				int a = Integer.parseInt(((LuaString) lhs).value);
				int b = ((LuaInt) rhs).v;
				return new LuaInt(a-b);
			}else {
				throw new UnsupportedOperationException();
			}
		}
		
		case OP_TIMES:{
			LuaValue lhs = (LuaValue) e0.visit(this, arg);
			LuaValue rhs = (LuaValue) e1.visit(this, arg);
			
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				int r = ((LuaInt) lhs).v*((LuaInt) rhs).v;
				return new LuaInt(r);							  
			}else if(lhs instanceof LuaString && rhs instanceof LuaString) {
				int a = Integer.parseInt(((LuaString) lhs).value);
				int b = Integer.parseInt(((LuaString) rhs).value);
				return new LuaInt(a*b);
			}else if(lhs instanceof LuaInt && rhs instanceof LuaString) {
				int a = ((LuaInt) lhs).v;
				int b = Integer.parseInt(((LuaString) rhs).value);
				return new LuaInt(a*b);
			}else if(lhs instanceof LuaString && rhs instanceof LuaInt) {
				int a = Integer.parseInt(((LuaString) lhs).value);
				int b = ((LuaInt) rhs).v;
				return new LuaInt(a*b);
			}else {
				throw new UnsupportedOperationException();
			}
		}
		
		case OP_DIV:{
			LuaValue lhs = (LuaValue) e0.visit(this, arg);
			LuaValue rhs = (LuaValue) e1.visit(this, arg);
			
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				int r = ((LuaInt) lhs).v/((LuaInt) rhs).v;
				return new LuaInt(r);							  
			}else if(lhs instanceof LuaString && rhs instanceof LuaString) {
				int a = Integer.parseInt(((LuaString) lhs).value);
				int b = Integer.parseInt(((LuaString) rhs).value);
				return new LuaInt(a/b);
			}else if(lhs instanceof LuaInt && rhs instanceof LuaString) {
				int a = ((LuaInt) lhs).v;
				int b = Integer.parseInt(((LuaString) rhs).value);
				return new LuaInt(a/b);
			}else if(lhs instanceof LuaString && rhs instanceof LuaInt) {
				int a = Integer.parseInt(((LuaString) lhs).value);
				int b = ((LuaInt) rhs).v;
				return new LuaInt(a/b);
			}else {
				throw new UnsupportedOperationException();
			}
		}
		
		case OP_MOD:{
			LuaValue lhs = (LuaValue) e0.visit(this, arg);
			LuaValue rhs = (LuaValue) e1.visit(this, arg);
			
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				int r = ((LuaInt) lhs).v%((LuaInt) rhs).v;
				return new LuaInt(r);							  
			}else if(lhs instanceof LuaString && rhs instanceof LuaString) {
				int a = Integer.parseInt(((LuaString) lhs).value);
				int b = Integer.parseInt(((LuaString) rhs).value);
				return new LuaInt(a%b);
			}else if(lhs instanceof LuaInt && rhs instanceof LuaString) {
				int a = ((LuaInt) lhs).v;
				int b = Integer.parseInt(((LuaString) rhs).value);
				return new LuaInt(a%b);
			}else if(lhs instanceof LuaString && rhs instanceof LuaInt) {
				int a = Integer.parseInt(((LuaString) lhs).value);
				int b = ((LuaInt) rhs).v;
				return new LuaInt(a%b);
			}else {
				throw new UnsupportedOperationException();
			}
		}
		
		case OP_POW:{
			LuaValue lhs = (LuaValue) e0.visit(this, arg);
			LuaValue rhs = (LuaValue) e1.visit(this, arg);
			
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				int r = ((LuaInt) lhs).v^((LuaInt) rhs).v;
				return new LuaInt(r);							  
			}else if(lhs instanceof LuaString && rhs instanceof LuaString) {
				int a = Integer.parseInt(((LuaString) lhs).value);
				int b = Integer.parseInt(((LuaString) rhs).value);
				return new LuaInt(a^b);
			}else if(lhs instanceof LuaInt && rhs instanceof LuaString) {
				int a = ((LuaInt) lhs).v;
				int b = Integer.parseInt(((LuaString) rhs).value);
				return new LuaInt(a^b);
			}else if(lhs instanceof LuaString && rhs instanceof LuaInt) {
				int a = Integer.parseInt(((LuaString) lhs).value);
				int b = ((LuaInt) rhs).v;
				return new LuaInt(a^b);
			}else {
				throw new UnsupportedOperationException();
			}
		}
		  	
		case OP_DIVDIV:{
			LuaValue lhs = (LuaValue) e0.visit(this, arg);
			LuaValue rhs = (LuaValue) e1.visit(this, arg);
			
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				int a = ((LuaInt) lhs).v;
				int b = ((LuaInt) rhs).v;
				return new LuaInt(Math.floorDiv(a, b));							  
			}else if(lhs instanceof LuaString && rhs instanceof LuaString) {
				int a = Integer.parseInt(((LuaString) lhs).value);
				int b = Integer.parseInt(((LuaString) rhs).value);
				return new LuaInt(Math.floorDiv(a, b));
			}else if(lhs instanceof LuaInt && rhs instanceof LuaString) {
				int a = ((LuaInt) lhs).v;
				int b = Integer.parseInt(((LuaString) rhs).value);
				return new LuaInt(Math.floorDiv(a, b));
			}else if(lhs instanceof LuaString && rhs instanceof LuaInt) {
				int a = Integer.parseInt(((LuaString) lhs).value);
				int b = ((LuaInt) rhs).v;
				return new LuaInt(Math.floorDiv(a, b));
			}else {
				throw new UnsupportedOperationException();
			}
		}
		
		case REL_LT:{
			LuaValue lhs = (LuaValue) e0.visit(this, arg);
			LuaValue rhs = (LuaValue) e1.visit(this, arg);
			
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				boolean x = ((LuaInt) lhs).v<((LuaInt) rhs).v;
				return new LuaBoolean(x);							  
			}else if(lhs instanceof LuaString && rhs instanceof LuaString) {
				int x = ((LuaString) lhs).value.compareTo(((LuaString) rhs).value);
				if(x<0) {
					return new LuaBoolean(true);
				}else {
					return new LuaBoolean(false);
				}
			}else {
				return new LuaBoolean(false);
			}
		}
		
		case REL_LE:{
			LuaValue lhs = (LuaValue) e0.visit(this, arg);
			LuaValue rhs = (LuaValue) e1.visit(this, arg);
			
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				boolean x = ((LuaInt) lhs).v<=((LuaInt) rhs).v;
				return new LuaBoolean(x);							  
			}else if(lhs instanceof LuaString && rhs instanceof LuaString) {
				int x = ((LuaString) lhs).value.compareTo(((LuaString) rhs).value);
				if(x<=0) {
					return new LuaBoolean(true);
				}else {
					return new LuaBoolean(false);
				}
			}else {
				return new LuaBoolean(false);
			}
		}
		
		case REL_GT:{
			LuaValue lhs = (LuaValue) e0.visit(this, arg);
			LuaValue rhs = (LuaValue) e1.visit(this, arg);
			
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				boolean x = ((LuaInt) lhs).v>((LuaInt) rhs).v;
				return new LuaBoolean(x);							  
			}else if(lhs instanceof LuaString && rhs instanceof LuaString) {
				int x = ((LuaString) lhs).value.compareTo(((LuaString) rhs).value);
				if(x>0) {
					return new LuaBoolean(true);
				}else {
					return new LuaBoolean(false);
				}
			}else {
				return new LuaBoolean(false);
			}
		}
		
		case REL_GE:{
			LuaValue lhs = (LuaValue) e0.visit(this, arg);
			LuaValue rhs = (LuaValue) e1.visit(this, arg);
			
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				boolean x = ((LuaInt) lhs).v>=((LuaInt) rhs).v;
				return new LuaBoolean(x);							  
			}else if(lhs instanceof LuaString && rhs instanceof LuaString) {
				int x = ((LuaString) lhs).value.compareTo(((LuaString) rhs).value);
				if(x>=0) {
					return new LuaBoolean(true);
				}else {
					return new LuaBoolean(false);
				}
			}else {
				return new LuaBoolean(false);
			}
		}
		
		case REL_EQEQ:{
			LuaValue lhs = (LuaValue) e0.visit(this, arg);
			LuaValue rhs = (LuaValue) e1.visit(this, arg);
			
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				boolean x = ((LuaInt) lhs).v==((LuaInt) rhs).v;
				return new LuaBoolean(x);							  
			}else if(lhs instanceof LuaString && rhs instanceof LuaString) {
				return ((LuaString) lhs).value.equals(((LuaString) rhs).value);
			}else {
				return new LuaBoolean(false);
			}
		}
		
		case REL_NOTEQ:{
			LuaValue lhs = (LuaValue) e0.visit(this, arg);
			LuaValue rhs = (LuaValue) e1.visit(this, arg);
			
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				boolean x = ((LuaInt) lhs).v!=((LuaInt) rhs).v;
				return new LuaBoolean(x);							  
			}else if(lhs instanceof LuaString && rhs instanceof LuaString) {
				return !((LuaString) lhs).value.equals(((LuaString) rhs).value);
			}else {
				return new LuaBoolean(false);
			}
		}
		
		case KW_and:{
			LuaValue lhs = (LuaValue) e0.visit(this, arg);
			LuaValue rhs = (LuaValue) e1.visit(this, arg);
			
			if(lhs instanceof LuaNil) {
				return lhs;
			}else if(lhs instanceof LuaBoolean) {
				if(((LuaBoolean) lhs).value==false){
					return lhs;
				}else {
					return rhs;
				}
			}else {
				return rhs;
			}
		}
		
		case KW_or:{
			LuaValue lhs = (LuaValue) e0.visit(this, arg);
			LuaValue rhs = (LuaValue) e1.visit(this, arg);
			
			if(lhs instanceof LuaNil) {
				return rhs;
			}else if(lhs instanceof LuaBoolean) {
				if(((LuaBoolean) lhs).value==false){
					return rhs;
				}else {
					return lhs;
				}
			}else {
				return lhs;
			}
		}
		
		case BIT_AMP:{
			LuaValue lhs = (LuaValue) e0.visit(this, arg);
			LuaValue rhs = (LuaValue) e1.visit(this, arg);
			
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				int a = ((LuaInt) lhs).v;
				int b = ((LuaInt) rhs).v;
				return new LuaInt(a & b);							  
			}else if(lhs instanceof LuaInt && rhs instanceof LuaString) {
				int a = ((LuaInt) lhs).v;
				int b = Integer.parseInt(((LuaString) rhs).value);
				return new LuaInt(a & b);
			}else if(lhs instanceof LuaString && rhs instanceof LuaInt) {
				int a = Integer.parseInt(((LuaString) lhs).value);
				int b = ((LuaInt) rhs).v;
				return new LuaInt(a & b);
			}else if(lhs instanceof LuaString && rhs instanceof LuaString) {
				int a = Integer.parseInt(((LuaString) lhs).value);
				int b = Integer.parseInt(((LuaString) rhs).value);
				return new LuaInt(a & b);
			}else {
				throw new UnsupportedOperationException();
			}
		}
		
		case BIT_OR:{
			LuaValue lhs = (LuaValue) e0.visit(this, arg);
			LuaValue rhs = (LuaValue) e1.visit(this, arg);
			
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				int a = ((LuaInt) lhs).v;
				int b = ((LuaInt) rhs).v;
				return new LuaInt(a | b);							  
			}else if(lhs instanceof LuaInt && rhs instanceof LuaString) {
				int a = ((LuaInt) lhs).v;
				int b = Integer.parseInt(((LuaString) rhs).value);
				return new LuaInt(a | b);
			}else if(lhs instanceof LuaString && rhs instanceof LuaInt) {
				int a = Integer.parseInt(((LuaString) lhs).value);
				int b = ((LuaInt) rhs).v;
				return new LuaInt(a | b);
			}else if(lhs instanceof LuaString && rhs instanceof LuaString) {
				int a = Integer.parseInt(((LuaString) lhs).value);
				int b = Integer.parseInt(((LuaString) rhs).value);
				return new LuaInt(a | b);
			}else {
				throw new UnsupportedOperationException();
			}
		}
		
		case BIT_XOR:{
			LuaValue lhs = (LuaValue) e0.visit(this, arg);
			LuaValue rhs = (LuaValue) e1.visit(this, arg);
			
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				int a = ((LuaInt) lhs).v;
				int b = ((LuaInt) rhs).v;
				return new LuaInt(a^b);							  
			}else if(lhs instanceof LuaInt && rhs instanceof LuaString) {
				int a = ((LuaInt) lhs).v;
				int b = Integer.parseInt(((LuaString) rhs).value);
				return new LuaInt(a ^ b);
			}else if(lhs instanceof LuaString && rhs instanceof LuaInt) {
				int a = Integer.parseInt(((LuaString) lhs).value);
				int b = ((LuaInt) rhs).v;
				return new LuaInt(a ^ b);
			}else if(lhs instanceof LuaString && rhs instanceof LuaString) {
				int a = Integer.parseInt(((LuaString) lhs).value);
				int b = Integer.parseInt(((LuaString) rhs).value);
				return new LuaInt(a ^ b);
			}else {
				throw new UnsupportedOperationException();
			}
		}
		
		case BIT_SHIFTL:{
			LuaValue lhs = (LuaValue) e0.visit(this, arg);
			LuaValue rhs = (LuaValue) e1.visit(this, arg);
			
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				int a = ((LuaInt) lhs).v;
				int b = ((LuaInt) rhs).v;
				return new LuaInt(a<<b);							  
			}else if(lhs instanceof LuaInt && rhs instanceof LuaString) {
				int a = ((LuaInt) lhs).v;
				int b = Integer.parseInt(((LuaString) rhs).value);
				return new LuaInt(a<<b);
			}else if(lhs instanceof LuaString && rhs instanceof LuaInt) {
				int a = Integer.parseInt(((LuaString) lhs).value);
				int b = ((LuaInt) rhs).v;
				return new LuaInt(a<<b);
			}else if(lhs instanceof LuaString && rhs instanceof LuaString) {
				int a = Integer.parseInt(((LuaString) lhs).value);
				int b = Integer.parseInt(((LuaString) rhs).value);
				return new LuaInt(a<<b);
			}else {
				throw new UnsupportedOperationException();
			}
		}
		
		case BIT_SHIFTR:{
			LuaValue lhs = (LuaValue) e0.visit(this, arg);
			LuaValue rhs = (LuaValue) e1.visit(this, arg);
			
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				int a = ((LuaInt) lhs).v;
				int b = ((LuaInt) rhs).v;
				return new LuaInt(a>>b);							  
			}else if(lhs instanceof LuaInt && rhs instanceof LuaString) {
				int a = ((LuaInt) lhs).v;
				int b = Integer.parseInt(((LuaString) rhs).value);
				return new LuaInt(a>>b);
			}else if(lhs instanceof LuaString && rhs instanceof LuaInt) {
				int a = Integer.parseInt(((LuaString) lhs).value);
				int b = ((LuaInt) rhs).v;
				return new LuaInt(a>>b);
			}else if(lhs instanceof LuaString && rhs instanceof LuaString) {
				int a = Integer.parseInt(((LuaString) lhs).value);
				int b = Integer.parseInt(((LuaString) rhs).value);
				return new LuaInt(a>>b);
			}else {
				throw new UnsupportedOperationException();
			}
		}
		
		case DOTDOT:{
			LuaValue lhs = (LuaValue) e0.visit(this, arg);
			LuaValue rhs = (LuaValue) e1.visit(this, arg);
			
			if(lhs instanceof LuaInt && rhs instanceof LuaInt) {
				String a = Integer.toString(((LuaInt) lhs).v);
				String b = Integer.toString(((LuaInt) rhs).v);
				return new LuaString(a+b);							  
			}else if(lhs instanceof LuaString && rhs instanceof LuaString) {
				String a = ((LuaString) lhs).value;
				String b = ((LuaString) rhs).value;
				return new LuaString(a+b);							  
			}else if(lhs instanceof LuaString && rhs instanceof LuaInt) {
				String a = ((LuaString) lhs).value;
				String b = Integer.toString(((LuaInt) rhs).v);
				return new LuaString(a+b);
			}else if(lhs instanceof LuaInt && rhs instanceof LuaString) {
				String a = Integer.toString(((LuaInt) lhs).v);
				String b = ((LuaString) rhs).value;
				return new LuaString(a+b);
			}else {
				throw new UnsupportedOperationException();
			}
		}
						
		}
		
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitUnExp(ExpUnary unExp, Object arg) throws Exception {
		Exp e = unExp.e;
		Token.Kind op = unExp.op;
		
		switch(op) {
		case OP_HASH:{
			LuaValue lv = (LuaValue) e.visit(this, arg);
			
			if(lv instanceof LuaString) {
				int s = ((LuaString) lv).value.length();
				return new LuaInt(s);
			}else {
				throw new UnsupportedOperationException();
			}
		}
		
		case KW_not:{
			LuaValue lv = (LuaValue) e.visit(this, arg);
			
			if(lv instanceof LuaBoolean) {
				Boolean x = ((LuaBoolean) lv).value;
				return new LuaBoolean(!x);
			}else if(lv instanceof LuaNil) {
				return new LuaBoolean(true);
			}else {
				return new LuaBoolean(true);
			}
		
		}
		
		case OP_MINUS:{
			LuaValue lv = (LuaValue) e.visit(this, arg);
			
			if(lv instanceof LuaInt) {
				int i = ((LuaInt) lv).v;
				return new LuaInt(-i);
			}else if(lv instanceof LuaString) {
				int i = Integer.parseInt(((LuaString) lv).value);
				return new LuaInt(-i);
			}
		}
		
		case BIT_XOR:{
			LuaValue lv = (LuaValue) e.visit(this, arg);
			
			if(lv instanceof LuaInt) {
				int value = ((LuaInt) lv).v;
				return new LuaInt(~value);
			}else if(lv instanceof LuaString) {
				int value = Integer.parseInt(((LuaString) lv).value);
				return new LuaInt(~value);
			}else {
				throw new UnsupportedOperationException();
			}
			
		}
		}
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpInt(ExpInt expInt, Object arg) {
		return new LuaInt(expInt.v);
	}

	@Override
	public Object visitExpString(ExpString expString, Object arg) {
		return new LuaString(expString.v);
		//throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpTable(ExpTable expTableConstr, Object arg) throws Exception {
		LuaTable lt = new LuaTable();
		List<Field> fieldList = expTableConstr.fields;
		
		for(Field f : fieldList) {
			if(f instanceof FieldExpKey) {
				Exp key = ((FieldExpKey) f).key;
				Exp value = ((FieldExpKey) f).value;
				
				if(key instanceof ExpName) {
					LuaValue keyLuaValue = (LuaValue) key.visit(this, arg);
					LuaValue valueLuaValue = (LuaValue) value.visit(this, arg);
					
					if(keyLuaValue instanceof LuaNil) {
						lt.put(new LuaString(((ExpName) key).name), valueLuaValue);
					}else {
						lt.put(keyLuaValue, valueLuaValue);
					}
					
				}else {
					LuaValue keyLuaValue = (LuaValue) key.visit(this, arg);
					LuaValue valueLuaValue = (LuaValue) value.visit(this, arg);
					
					lt.put(keyLuaValue, valueLuaValue);
				}
				
			}else if(f instanceof FieldNameKey) {
				String key = ((FieldNameKey) f).name.name;
				Exp value = ((FieldNameKey) f).exp;
				
				LuaValue keyLuaValue = ((LuaTable) arg).get(new LuaString(key));
				LuaValue valueLuaValue = (LuaValue) value.visit(this, arg);
				
				if(keyLuaValue instanceof LuaNil) {
					lt.put(new LuaString(key), valueLuaValue);
				}else {
					lt.put(keyLuaValue, valueLuaValue);
				}
			}else {
				Exp implicitExp = ((FieldImplicitKey) f).exp;
				LuaValue implicitLuaValue = (LuaValue) implicitExp.visit(this, arg);
				
				if(implicitLuaValue instanceof LuaNil) {
					String key = ((ExpName) implicitExp).name;
					lt.putImplicit(new LuaString(key));
				}else if(implicitLuaValue==null) {
					
				}else {
					lt.putImplicit(implicitLuaValue);
				}
			
			}
		}
		
		return lt;
	}

	@Override
	public Object visitExpList(ExpList expList, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitParList(ParList parList, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitFunDef(ExpFunction funcDec, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitName(Name name, Object arg) {
		throw new UnsupportedOperationException();
	}
	
	private Object visitBlock(Block block,Object arg,int index) throws Exception{
		List<Stat> statList = block.stats;
		List<LuaValue> bv = new ArrayList<>();
		
		
		for(int i=index;i<statList.size();i++){
			if(!this.terminate) {
				if(statList.get(i) instanceof StatGoto) {
					if(((StatGoto) statList.get(i)).label!=null) {
						StatLabel sl =((StatGoto) statList.get(i)).label; 
						if(sl.enclosingBlock.equals(block)) {
							i = sl.index+1;
						}else {
							Block jumpBlock = sl.enclosingBlock;
							List<Stat> jumpBlockStats = jumpBlock.stats;
							int index2 = jumpBlockStats.indexOf(sl);
							return visitBlock(jumpBlock,arg,index2+1);						
						}
					}else {
						throw new interpreter.StaticSemanticException(((StatGoto) statList.get(i)).firstToken,"Static Semantic Exception");
					}
				}
				if(statList.get(i) instanceof StatBreak) {
					this.loopBreakFlag=true;
					break;
				}
				List<LuaValue> sv = (List<LuaValue>) statList.get(i).visit(this, arg);
				bv.addAll(sv);
				if(statList.get(i) instanceof RetStat) {
					this.terminate=true;
					break;
				}				
			}else {
				break;
			}
		}
		
		
		return bv;
	}

	@Override
	public Object visitBlock(Block block, Object arg) throws Exception {

			List<Stat> statList = block.stats;
			List<LuaValue> bv = new ArrayList<>();
			
			for(int i=0;i<statList.size();i++){
				if(!this.terminate) {
					if(statList.get(i) instanceof StatGoto) {
						if(((StatGoto) statList.get(i)).label!=null) {
							StatLabel sl = ((StatGoto) statList.get(i)).label; 
							if(sl.enclosingBlock.equals(block)) {
								i = sl.index+1;
							}else {
								Block jumpBlock = sl.enclosingBlock;
								List<Stat> jumpBlockStats = jumpBlock.stats;
								int index = jumpBlockStats.indexOf(sl);
								return visitBlock(jumpBlock,arg,index+1);
							}
						}else {
							throw new interpreter.StaticSemanticException(((StatGoto) statList.get(i)).firstToken,"Static Semantic Exception");
							
						}
					}
					
					if(statList.get(i) instanceof StatBreak) {
						this.loopBreakFlag=true;
						break;
					}
					
					List<LuaValue> sv = (List<LuaValue>) statList.get(i).visit(this, arg);
					bv.addAll(sv);
					
					if(statList.get(i) instanceof RetStat) {
						this.terminate=true;
						break;
					}
					
				}else {
					break;
				}
			}
			
			
			return bv;
			
	}

	@Override
	public Object visitStatBreak(StatBreak statBreak, Object arg, Object arg2) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatBreak(StatBreak statBreak, Object arg) throws Exception {
		return new ArrayList<LuaValue>();
	}

	@Override
	public Object visitStatGoto(StatGoto statGoto, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatDo(StatDo statDo, Object arg) throws Exception {
		Block b = statDo.b;
		return b.visit(this, arg);
	}

	@Override
	public Object visitStatWhile(StatWhile statWhile, Object arg) throws Exception {
		List<LuaValue> lvList = new ArrayList<>();
		Exp e = statWhile.e;
		Block b = statWhile.b;
		List<Stat> stats = b.stats;
		boolean x = false;
		
		while(((LuaBoolean) e.visit(this, arg)).value) {
			
			for(Stat s : stats) {
				if(!this.loopBreakFlag) {
					List<LuaValue> l = (List<LuaValue>) s.visit(this, arg);
					lvList.addAll(l);
				}else {
					x=true;
					break;
				}
			}
			if(x) {
				break;
			}
		}
		
		this.loopBreakFlag=false;
		return lvList;
	}

	@Override
	public Object visitStatRepeat(StatRepeat statRepeat, Object arg) throws Exception {
		List<LuaValue> lvList = new ArrayList<>();
		Exp e = statRepeat.e;
		Block b = statRepeat.b;
		
		List<Stat> stats = b.stats;
		boolean x = false;
		
		do {
			for(Stat s : stats) {
				if(!this.loopBreakFlag) {
					List<LuaValue> l = (List<LuaValue>) s.visit(this, arg);
					lvList.addAll(l);
				}else {
					x=true;
					break;
				}
			}
			if(x) {
				break;
			}
		}while(((LuaBoolean) e.visit(this, arg)).value);
		
		this.loopBreakFlag=false;
		return lvList;
	}
	
	@Override
	public Object visitStatIf(StatIf statIf, Object arg) throws Exception{
		List<LuaValue> l = new ArrayList<>();
		List<Exp> expList = statIf.es;
		List<Block> blockList = statIf.bs;
		boolean ifFlag = false;
		
		int i=0;
		while(i<expList.size()) {
			if(!ifFlag) {
				LuaValue luv = (LuaValue) expList.get(i).visit(this, arg);
				if(luv instanceof LuaInt) {
					List<LuaValue> lv = (List<LuaValue>) blockList.get(i).visit(this, arg);
					l.addAll(lv);
					ifFlag = true;
				}else if(luv instanceof LuaBoolean) {
					if(((LuaBoolean) luv).value){
						List<LuaValue> lv = (List<LuaValue>) blockList.get(i).visit(this, arg);
						l.addAll(lv);
						ifFlag = true;
					}
				}
			}else {
				break;
			}
			i++;
		}
		
		if(!ifFlag && i<blockList.size()) {
			List<LuaValue> lv = (List<LuaValue>) blockList.get(i).visit(this, arg);
			l.addAll(lv);
		}
		
		return l;
	}

	@Override
	public Object visitStatFor(StatFor statFor1, Object arg) throws Exception {
		List<LuaValue> l = new ArrayList<>();
		Exp init = statFor1.ebeg;
		Exp inc = statFor1.einc;
		Exp end = statFor1.eend;
		
		LuaValue initLua = (LuaValue) init.visit(this, arg);
		LuaValue incLua = (LuaValue) inc.visit(this, arg);
		LuaValue endLua = (LuaValue) end.visit(this, arg);
		
		Block b = statFor1.g;
		
		if(initLua instanceof LuaInt && incLua instanceof LuaInt && endLua instanceof LuaInt) {
			for(int i=((LuaInt) initLua).v;i<((LuaInt) endLua).v;i=i+((LuaInt) incLua).v) {
				List<LuaValue> ll = (List<LuaValue>) b.visit(this, arg);
				l.addAll(l);
			}
		}
		
		return l;
	}

	@Override
	public Object visitStatForEach(StatForEach statForEach, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitFuncName(FuncName funcName, Object arg) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatFunction(StatFunction statFunction, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatLocalFunc(StatLocalFunc statLocalFunc, Object arg) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatLocalAssign(StatLocalAssign statLocalAssign, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitRetStat(RetStat retStat, Object arg) throws Exception {
		List<LuaValue> rv = new ArrayList<>();
		List<Exp> exps = retStat.el;
		for(Exp e :exps) {
				LuaValue key = (LuaValue) e.visit(this, arg);
				rv.add(key);

		}
		return rv;
	}

	@Override
	public Object visitChunk(Chunk chunk, Object arg) throws Exception {
		List<LuaValue> cv = new ArrayList<>();
		Block b = chunk.block;
		List<LuaValue> bv = (List<LuaValue>) b.visit(this, arg);
		cv.addAll(bv);
		if(cv.size()==0) {
			return null;
		}
		return cv;
	}

	@Override
	public Object visitFieldExpKey(FieldExpKey fieldExpKey, Object object) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitFieldNameKey(FieldNameKey fieldNameKey, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Object visitFieldImplicitKey(FieldImplicitKey fieldImplicitKey, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpTrue(ExpTrue expTrue, Object arg) {
		return new LuaBoolean(true);
	}

	@Override
	public Object visitExpFalse(ExpFalse expFalse, Object arg) {
		return new LuaBoolean(false);
	}

	@Override
	public Object visitFuncBody(FuncBody funcBody, Object arg) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpVarArgs(ExpVarArgs expVarArgs, Object arg) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitStatAssign(StatAssign statAssign, Object arg) throws Exception {
		List<LuaValue> sav = new ArrayList<>();
		List<Exp> varList = statAssign.varList;
		List<Exp> expList = statAssign.expList;
		
		if(varList.size()!=expList.size()) {
			throw new UnsupportedOperationException();
		}
		
		for(int i=0;i<varList.size();i++) {
			Exp lhs = varList.get(i);
			Exp rhs = expList.get(i);
			
			if(lhs instanceof ExpName) {
				LuaString e = new LuaString(((ExpName) lhs).name);
				
				LuaValue v = (LuaValue) expList.get(i).visit(this, arg);
				
				((LuaTable) arg).put(e, v);
			}else if(lhs instanceof ExpTableLookup) {
				Exp tableName = ((ExpTableLookup) lhs).table;
				Exp keyName = ((ExpTableLookup) lhs).key;
				
				LuaValue tableLuaValue = (LuaValue) tableName.visit(this, arg);
				LuaValue keyLuaValue = (LuaValue) keyName.visit(this, arg);
				
				if(tableLuaValue instanceof LuaTable) {
					if(!(keyLuaValue instanceof LuaNil)) {
						((LuaTable) tableLuaValue).put(keyLuaValue, (LuaValue) rhs.visit(this, arg));
					}else {
						((LuaTable) tableLuaValue).put(new LuaString(((ExpName) keyName).name), (LuaValue) rhs.visit(this, arg));
					}				
				}else {
					throw new UnsupportedOperationException();
				}
				
				((LuaTable) arg).put(new LuaString(((ExpName) tableName).name), tableLuaValue);
			}
			
			
		}
		return sav;
	}

	@Override
	public Object visitExpTableLookup(ExpTableLookup expTableLookup, Object arg) throws Exception {
		
		Exp table = expTableLookup.table;
		Exp key = expTableLookup.key;
		
		LuaValue lookupTable = (LuaValue) table.visit(this, arg);
		LuaValue lookupKey = (LuaValue) key.visit(this, arg);
		
		if(!(lookupTable instanceof LuaNil)) {
			if(!(lookupKey instanceof LuaNil)) {
				return ((LuaTable) lookupTable).get(lookupKey);
			}else {
				return ((LuaTable) lookupTable).get(new LuaString(((ExpName) key).name));
			}
		}
		
		return null; 
	}

	@Override
	public Object visitExpFunctionCall(ExpFunctionCall expFunctionCall, Object arg) throws Exception {
		Exp funcName = expFunctionCall.f;
		List<Exp> argList = expFunctionCall.args;
		List<LuaValue> luaValueList = new ArrayList<>();
		
		for(Exp e : argList) {
			luaValueList.add((LuaValue) e.visit(this, arg));
		}
		
		if(((ExpName) funcName).name.equals("print")){
			print p = new print();
			p.call(luaValueList);
			return null;
		}else if(((ExpName) funcName).name.equals("println")){
			println p = new println();
			p.call(luaValueList);
			return null;
		}else if(((ExpName) funcName).name.equals("toNumber")){
			toNumber p = new toNumber();
			List<LuaValue> luaValues  = p.call(luaValueList);
			return luaValues.get(0);
		}
		
		return null;
	}

	@Override
	public Object visitLabel(StatLabel statLabel, Object ar) {
		return new ArrayList<LuaValue>();
	}

	@Override
	public Object visitFieldList(FieldList fieldList, Object arg) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object visitExpName(ExpName expName, Object arg) {
		LuaValue lv = ((LuaTable) arg).get(new LuaString(expName.name));
		return lv;
	}



}
