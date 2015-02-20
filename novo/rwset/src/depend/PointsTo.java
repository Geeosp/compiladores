package depend;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import com.ibm.wala.analysis.pointers.HeapGraph;
import com.ibm.wala.classLoader.IField;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.CallGraphBuilderCancelException;
import com.ibm.wala.ipa.callgraph.impl.Everywhere;
import com.ibm.wala.ipa.callgraph.propagation.InstanceFieldKey;
import com.ibm.wala.ipa.callgraph.propagation.InstanceKey;
import com.ibm.wala.ipa.callgraph.propagation.LocalPointerKey;
import com.ibm.wala.ipa.callgraph.propagation.PointerAnalysis;
import com.ibm.wala.ipa.callgraph.propagation.PointerKey;
import com.ibm.wala.ipa.cha.ClassHierarchyException;
import com.ibm.wala.ssa.IR;

import depend.util.CallGraphGenerator;
import depend.util.Util;

public class PointsTo {

  static Arquivo arq = new Arquivo("dumb.in", "out.txt"); 


  public static PointerKey getPointerKey(CallGraphGenerator cgg,
      MethodDependencyAnalysis mda, String variable, String method) throws CallGraphBuilderCancelException {

    // pa = Pointer Analysis
    mda.pa = cgg.getPointerAnalysis();
    Iterator<PointerKey> it = mda.pa.getHeapModel().iteratePointerKeys();
    HeapGraph hgraph = mda.pa.getHeapGraph();

    while (it.hasNext()) {
      PointerKey pkey = it.next();

      if (pkey instanceof InstanceFieldKey) {

        // pointer to a field associated with a set of instances.
        // Note that a single field can have multiple PointerKeys.
        InstanceFieldKey ifk = (InstanceFieldKey) pkey;

        // filter relevant fields
        if (ifk.getField().toString().contains(Util.APP_PREFIX)) {
          IField ifield = ifk.getField();
          
        } else {
          continue;
        }
      } else if (pkey instanceof LocalPointerKey) {
        // pointer to a local
        LocalPointerKey lpk = (LocalPointerKey) pkey;
        IMethod lpkMethod = lpk.getNode().getMethod();

        // filter relevant fields
        // More info:
        // http://wala.sourceforge.net/wiki/index.php/UserGuide:IR#Value_Numbering
        if (lpkMethod.toString().contains(Util.APP_PREFIX)) {
          IR ir = mda.cache.getIRFactory().makeIR(lpkMethod,
              Everywhere.EVERYWHERE, mda.options.getSSAOptions());

          // tinha um codigo comentado aqui. apaguei.

          String[] names = ir.getLocalNames(ir.getInstructions().length - 1,
              lpk.getValueNumber());
          
           arq.println("Analyzing local variable " + Arrays.toString(names) +
           " in method " + lpkMethod);
          
           //retornando aqui o pkey que contém a variável e o método especificado no teste
          if(Arrays.toString(names).contains(variable) && lpkMethod.toString().contains(method)){
            return pkey;
          }
          
        } else {
          continue;
        }
      } else {
        continue;
      }
    }
    return null;
  }

  // checa as dependências associadas

  public static boolean checkDeps(MethodDependencyAnalysis mda, PointerKey pkey, String[] depends) throws CallGraphBuilderCancelException, ClassHierarchyException, IOException {
    PointerAnalysis pa = mda.getCallGraphGenerator().getPointerAnalysis();; 
    Iterator<PointerKey> it = mda.pa.getHeapModel().iteratePointerKeys();
    HeapGraph hgraph = mda.pa.getHeapGraph();
    Iterator<Object> pointedInstances = hgraph.getSuccNodes(pkey);
    
    //check vai conferir se as dependências foram encontradas
    boolean[] check = new boolean[depends.length];
    for (int i = 0; i < check.length; i++) {
      check[i] = false;
    }
    
    while (pointedInstances.hasNext()) {
      InstanceKey ikey = (InstanceKey) pointedInstances.next();
      Iterator<Object> possibleAlias = hgraph.getPredNodes(ikey);

      while (possibleAlias.hasNext()) {
        PointerKey aliasPKey = (PointerKey) possibleAlias.next();
        if (!aliasPKey.equals(pkey)) {
          if (aliasPKey instanceof InstanceFieldKey) {
            InstanceFieldKey aliasIFK = (InstanceFieldKey) aliasPKey;
            IField aliasIField = aliasIFK.getField();
            
            //checando se tem algum alias aqui pra checar que foi encontrado
            for (int i = 0; i < depends.length; i++) {
              if(!check[i]){
                if(depends[i].equals(aliasIField.toString())){
                  System.out.println("Prog" + aliasIField.toString() + "É IGUAL???"+  depends[i]);
                  check[i] = true;
                }
              }
              
            }
            // arq.println(" > possible alias: field " + aliasIField);
            
          } else if (aliasPKey instanceof LocalPointerKey) {
            LocalPointerKey aliasLPK = (LocalPointerKey) aliasPKey;
            IMethod lpkMethod = aliasLPK.getNode().getMethod();
            IR ir = mda.cache.getIRFactory().makeIR(lpkMethod,
                Everywhere.EVERYWHERE, mda.options.getSSAOptions());
                String[] names = ir.getLocalNames(ir.getInstructions().length - 1,
                aliasLPK.getValueNumber());
            
                //checando se o método encontrado é o correto
                for (int i = 0; i < depends.length; i++) {
                  if(!check[i]){
                    if(depends[i].equals(Arrays.toString(names) +  " in method " + lpkMethod)){
                      System.out.println(lpkMethod + "É IGUAL???"+  depends[i]);
                      check[i] = true;
                    }
                  }
                }
                
             arq.println(" > possible alias: local " + Arrays.toString(names) + " in method " + lpkMethod);
          } else {
            //System.out.println(" > unhandled pointer type: "  + aliasPKey.getClass());
            // arq.println(" > unhandled pointer type: " +
            // aliasPKey.getClass());
          }
        }
      }
    }
    //verificando se todas as dependências fazem parte e retornando a asserção
    boolean boo = true;
    for (int i = 0; i < check.length; i++) {
      boo = boo&&check[i];
    }
    return boo;
  }
}
