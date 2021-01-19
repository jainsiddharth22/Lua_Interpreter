package interpreter;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cop5556fa19.AST.ASTVisitor;
import cop5556fa19.AST.Block;
import cop5556fa19.AST.Chunk;
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
import interpreter.ASTVisitorAdapter.StaticSemanticException;

public class StaticAnalysis implements ASTVisitor{
	
	int counter=0;
	List<Integer> blockPattern = new ArrayList<>();
	//HashMap<StatGoto,StatLabel> gotoMap=new HashMap<>();
	//static HashMap<StatLabel,Integer> labelMap=new HashMap<>();
	//StaticAnalysisLabel sal;
	//ArrayList<StatLabelRep> statLabelList = StaticAnalysisLabel.statLabelList;
	ArrayList<StatLabelRep> statLabelList;
	
	public StaticAnalysis(ArrayList<StatLabelRep> statLabelList) {
		//this.sal = new StaticAnalysisLabel();
		this.statLabelList = new ArrayList<>();
		this.statLabelList.addAll(statLabelList);		
	}

	@Override
	public Object visitExpNil(ExpNil expNil, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitExpBin(ExpBinary expBin, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitUnExp(ExpUnary unExp, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitExpInt(ExpInt expInt, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitExpString(ExpString expString, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitExpTable(ExpTable expTableConstr, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitExpList(ExpList expList, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitParList(ParList parList, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitFunDef(ExpFunction funcDec, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitName(Name name, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitBlock(Block block, Object arg) throws Exception {
		counter++;
		blockPattern.add(counter);
		List<Stat> stats = block.stats;
		
		for(int i=0;i<stats.size();i++) {
			if(stats.get(i) instanceof StatDo || stats.get(i) instanceof StatWhile || stats.get(i) instanceof StatRepeat || stats.get(i) instanceof StatIf) {
				stats.get(i).visit(this, arg);
			}else if(stats.get(i) instanceof StatGoto) {
				Name name = ((StatGoto) stats.get(i)).name;
				//gotoMap.put((StatGoto) stats.get(i), null);
				
				List<StatLabelRep> filtered = new ArrayList<>();
				
				//System.out.println("anna"+statLabelList);
				
				for(StatLabelRep slr : statLabelList) {
					if(slr.statLabel.label.equals(((StatGoto) stats.get(i)).name)){
						List<Integer> gotoPattern = blockPattern;
						List<Integer> labelPattern = slr.blockPattern;
//						System.out.println("---");
//						System.out.println("***"+blockPattern);
//						System.out.println("***"+slr.blockPattern);
//						System.out.println("---");
						
						String gotoPatternString = cstring(gotoPattern);
						String labelPatternString = cstring(labelPattern);
						
						if(gotoPatternString.contains(labelPatternString) && labelPatternString!="") {
							filtered.add(slr);
						}
					}
				}
				
				StatLabelRep kam = null;
				
				if(!filtered.isEmpty()) {
					int gotonum = tonum(blockPattern);
					int min = Integer.MAX_VALUE;
					for(StatLabelRep slr : filtered) {
						int labelnum = tonum(slr.blockPattern);
						if(gotonum-labelnum<min) {
							min = gotonum-labelnum;
							kam = slr;
						}
					}
					((StatGoto) stats.get(i)).label=kam.statLabel;
					//gotoMap.put((StatGoto) stats.get(i), kam.statLabel);
				}else {
					((StatGoto) stats.get(i)).label=null;
					//gotoMap.put((StatGoto) stats.get(i), null);
				}
				
			}
		}
		
		
		blockPattern.remove(blockPattern.size()-1);
		//counter--;
		
		//System.out.println(StaticAnalysis.gotoMap);
		

		
		return null;
	}
	
	private int tonum(List<Integer> l) {
		String s = cstring(l);
		return Integer.parseInt(s);
	}
	
	private String cstring(List<Integer> l) {
		StringBuilder sb = new StringBuilder();
		for(Integer i : l) {
			sb.append(i);
		}
		return sb.toString();
	}

	@Override
	public Object visitStatBreak(StatBreak statBreak, Object arg, Object arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitStatBreak(StatBreak statBreak, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitStatGoto(StatGoto statGoto, Object arg) throws Exception {
		Name name = statGoto.name;
		return null;
	}

	@Override
	public Object visitStatDo(StatDo statDo, Object arg) throws Exception {
		Block b = statDo.b;
		b.visit(this, arg);
		return null;
	}

	@Override
	public Object visitStatWhile(StatWhile statWhile, Object arg) throws Exception {
		Block b = statWhile.b;
		b.visit(this, arg);
		return null;
	}

	@Override
	public Object visitStatRepeat(StatRepeat statRepeat, Object arg) throws Exception {
		Block b = statRepeat.b;
		b.visit(this, arg);
		return null;
	}

	@Override
	public Object visitStatIf(StatIf statIf, Object arg) throws Exception {
		List<Block> blockList = statIf.bs;
//		System.out.println("xxxx");
//		
//		for(Block b : blockList) {
//			System.out.println(b);
//		}
//		
//		System.out.println("xxxx");
		
//		for(Block b : blockList) {
//			System.out.println(b);
//			b.visit(this, arg);
//		}
		
		//System.out.println(blockList.size());
		
		for(int i=0;i<blockList.size();i++) {
			//System.out.println(blockList.get(i));
			blockList.get(i).visit(this, arg);
		}
		return null;
	}

	@Override
	public Object visitStatFor(StatFor statFor1, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitStatForEach(StatForEach statForEach, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitFuncName(FuncName funcName, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitStatFunction(StatFunction statFunction, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitStatLocalFunc(StatLocalFunc statLocalFunc, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitStatLocalAssign(StatLocalAssign statLocalAssign, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitRetStat(RetStat retStat, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitChunk(Chunk chunk, Object arg) throws Exception {
		Block b = chunk.block;
		b.visit(this, arg);
		//System.out.println("-------------------------------------");
		//System.out.println(StaticAnalysis.gotoMap);
//		for (StatGoto key : StaticAnalysis.gotoMap.keySet()) {
//		    if(StaticAnalysis.gotoMap.get(key)==null) {
//		    	throw new StaticSemanticException(key.firstToken,"Static Semantic Exception");
//		    }
//		}
		//System.out.println("cobb");
		return null;
	}

	@Override
	public Object visitFieldExpKey(FieldExpKey fieldExpKey, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitFieldNameKey(FieldNameKey fieldNameKey, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitFieldImplicitKey(FieldImplicitKey fieldImplicitKey, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitExpTrue(ExpTrue expTrue, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitExpFalse(ExpFalse expFalse, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitFuncBody(FuncBody funcBody, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitExpVarArgs(ExpVarArgs expVarArgs, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitStatAssign(StatAssign statAssign, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitExpTableLookup(ExpTableLookup expTableLookup, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitExpFunctionCall(ExpFunctionCall expFunctionCall, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitLabel(StatLabel statLabel, Object ar) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitFieldList(FieldList fieldList, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visitExpName(ExpName expName, Object arg) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}


	
}
