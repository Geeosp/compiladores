package rwsets.coffeemaker;

import japa.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import rwsets.Helper;
import rwsets.RWTest;

import com.ibm.wala.classLoader.IClass;
import com.ibm.wala.classLoader.IMethod;
import com.ibm.wala.ipa.callgraph.propagation.PointerKey;
import com.ibm.wala.shrikeCT.InvalidClassFileException;
import com.ibm.wala.util.CancelException;
import com.ibm.wala.util.WalaException;
import com.ibm.wala.util.io.CommandLine;
import com.ibm.wala.util.warnings.Warnings;

import depend.Main;
import depend.MethodDependencyAnalysis;
import depend.util.Util;
import depend.util.graph.SimpleGraph;

public class TestCoffeeMaker extends RWTest {
  
  @Before
  public void setup() {
    JAR_FILENAME = EXAMPLES_JAR + SEP + "coffee.jar";
  }
  
  //@Test
  public void findChocolate() throws IOException, WalaException, CancelException, ParseException, InvalidClassFileException {
    String compilationUnitName = EXAMPLES_SRC + SEP + "coffeemaker/CoffeeMaker.java";
    String targetLineInCompilationUnit = "if(addRecipe(newRecipe)) {";
    String expectedResultFile = TEST_DIR + SEP + "rwsets/coffeemaker/TestCoffeeMaker.test0.data";
    String prefix = "coffee";
    String jarFileName = JAR_FILENAME;
     
    Assert.assertTrue((new File(compilationUnitName)).exists());
    Assert.assertTrue((new File(jarFileName)).exists());

    String[] lineAndClass = depend.util.parser.Util.getLineAndWALAClassName(targetLineInCompilationUnit, compilationUnitName);
    
    SimpleGraph sg = depend.Main.analyze(jarFileName, prefix, Integer.parseInt(lineAndClass[0]), lineAndClass[1]);
    
    MethodDependencyAnalysis mda= Main.mda;
    PointerKey pk =  depend.PointsTo.getPointerKey(depend.MethodDependencyAnalysis.cgg, mda, "chocolateString", "< Application, Lcoffeemaker/Main, addInventory()V >");
    String[] depends =  {"[chocolateString] in method < Application, Lcoffeemaker/Main, editRecipe()V >"};
    Assert.assertTrue(depend.PointsTo.checkDeps(mda, pk, depends));
    }
  
  @Test
  public void test0() throws IOException, WalaException, CancelException, ParseException, InvalidClassFileException {
    String compilationUnitName = EXAMPLES_SRC + SEP + "coffeemaker/CoffeeMaker.java";
    String targetLineInCompilationUnit = "if(addRecipe(newRecipe)) {";
    String expectedResultFile = TEST_DIR + SEP + "rwsets/coffeemaker/TestCoffeeMaker.test0.data";
    String prefix = "coffee";
    String jarFileName = JAR_FILENAME;
     
    Assert.assertTrue((new File(compilationUnitName)).exists());
    Assert.assertTrue((new File(jarFileName)).exists());

    String[] lineAndClass = depend.util.parser.Util.getLineAndWALAClassName(targetLineInCompilationUnit, compilationUnitName);
    
    SimpleGraph sg = depend.Main.analyze(jarFileName, prefix, Integer.parseInt(lineAndClass[0]), lineAndClass[1]);
    
    MethodDependencyAnalysis mda= Main.mda;
    PointerKey pk =  depend.PointsTo.getPointerKey(depend.MethodDependencyAnalysis.cgg, mda, "coffeeString", "< Application, Lcoffeemaker/Main, addRecipe()V >");
    String[] depends =  {"[chocolateString] in method < Application, Lcoffeemaker/Main, editRecipe()V >"};
    Assert.assertTrue(depend.PointsTo.checkDeps(mda, pk, depends));
    }
  
//  Analyzing local variable [priceString] in method < Application, Lcoffeemaker/Main, addRecipe()V >
//
//  Analyzing local variable [coffeeString] in method < Application, Lcoffeemaker/Main, addRecipe()V >
//  
//  Analyzing local variable [milkString] in method < Application, Lcoffeemaker/Main, addRecipe()V >
//  
//  Analyzing local variable [sugarString] in method < Application, Lcoffeemaker/Main, addRecipe()V >
//  
//  Analyzing local variable [chocolateString] in method < Application, Lcoffeemaker/Main, addRecipe()V >
  
  
  //@Test
  public void testAnalysisWithLineContents() throws Exception {
    String strCompUnit = EXAMPLES_SRC + SEP + "coffeemaker/CoffeeMaker.java";
    String exclusionFile = RESOURCES_DIR + SEP + "ExclusionAllJava.txt";
    String exclusionFileForCallGraph = RESOURCES_DIR + SEP + "ExclusionForCallGraph.txt";
    String coffeejar = EXAMPLES_JAR + SEP + "coffee.jar";
    String targetClass = "Lcoffeemaker/CoffeeMaker";
    String targetMethod = "editRecipe(Lcoffeemaker/Recipe;Lcoffeemaker/Recipe;)Z";

    // checks
    Assert.assertTrue((new File(strCompUnit)).exists());
    Assert.assertTrue((new File(exclusionFile)).exists());
    Assert.assertTrue((new File(exclusionFileForCallGraph)).exists());
    Assert.assertTrue((new File(coffeejar)).exists());


    String[] args = new String[] { 
        "-appJar=" + coffeejar,
        "-printWalaWarnings=" + false, 
        "-exclusionFile=" + exclusionFile,
        "-exclusionFileForCallGraph=" + exclusionFileForCallGraph,
        "-dotPath=" + "/usr/bin/dot", 
        "-appPrefix=" + "coffee",
        "-listAppClasses=" + false, 
        "-listAllClasses=" + false,
        "-listAppMethods=" + false, 
        "-genCallGraph=" + false,
        "-measureTime=" + false, 
        "-reportType=" + "list",
        "-targetClass=" + targetClass, 
        "-targetMethod=" + targetMethod,
        "-targetLine=99"};
    
    // reading and saving command-line properties
    Properties p = CommandLine.parse(args);
    Util.setProperties(p);

    // clearing warnings from WALA
    Warnings.clear();

    MethodDependencyAnalysis mda = new MethodDependencyAnalysis(p);

    // find informed class    
    IClass clazz = depend.Main.findClass(mda);
    //  find informed method
    IMethod method = depend.Main.findMethod(clazz);
    SimpleGraph sg = depend.Main.run(mda, method);
    
    String expectedResultFile = TEST_DIR + SEP + "rwsets/coffeemaker/TestCoffeeMaker.testAnalysisWithLineContents.data";
    Assert.assertEquals(Helper.readFile(expectedResultFile), sg.toString());
  } 
} 