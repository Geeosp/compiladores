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
  
  @Test
  public void findChocolate() throws IOException, WalaException, CancelException, ParseException, InvalidClassFileException {
    String compilationUnitName = EXAMPLES_SRC + SEP + "coffeemaker/CoffeeMaker.java";
    String targetLineInCompilationUnit = "if(addRecipe(newRecipe)) {";
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
  public void usingTypoChocolatetringTest() throws IOException, WalaException, CancelException, ParseException, InvalidClassFileException {
    String compilationUnitName = EXAMPLES_SRC + SEP + "coffeemaker/CoffeeMaker.java";
    String targetLineInCompilationUnit = "if(addRecipe(newRecipe)) {";
    String prefix = "coffee";
    String jarFileName = JAR_FILENAME;
     
    Assert.assertTrue((new File(compilationUnitName)).exists());
    Assert.assertTrue((new File(jarFileName)).exists());

    String[] lineAndClass = depend.util.parser.Util.getLineAndWALAClassName(targetLineInCompilationUnit, compilationUnitName);
    
    SimpleGraph sg = depend.Main.analyze(jarFileName, prefix, Integer.parseInt(lineAndClass[0]), lineAndClass[1]);
    
    MethodDependencyAnalysis mda= Main.mda;
    PointerKey pk =  depend.PointsTo.getPointerKey(depend.MethodDependencyAnalysis.cgg, mda, "chocolateString", "< Application, Lcoffeemaker/Main, addInventory()V >");
    String[] depends =  {"[chocolatetring] in method < Application, Lcoffeemaker/Main, editRecipe()V >"};
    Assert.assertFalse(depend.PointsTo.checkDeps(mda, pk, depends));
    }
  
  
  //usamos um typo no depends "chocolatetring" pra dar erro e conferirmos o resultado 
  @Test
  public void recipeDeleteDependencyTest() throws IOException, WalaException, CancelException, ParseException, InvalidClassFileException {
    String compilationUnitName = EXAMPLES_SRC + SEP + "coffeemaker/CoffeeMaker.java";
    String targetLineInCompilationUnit = "r.getAmtSugar())";
    String prefix = "coffee";
    String jarFileName = JAR_FILENAME;
     
    Assert.assertTrue((new File(compilationUnitName)).exists());
    Assert.assertTrue((new File(jarFileName)).exists());

    String[] lineAndClass = depend.util.parser.Util.getLineAndWALAClassName(targetLineInCompilationUnit, compilationUnitName);
    
    SimpleGraph sg = depend.Main.analyze(jarFileName, prefix, Integer.parseInt(lineAndClass[0]), lineAndClass[1]);
    
    MethodDependencyAnalysis mda= Main.mda;
    PointerKey pk =  depend.PointsTo.getPointerKey(depend.MethodDependencyAnalysis.cgg, mda, "recipeToPurchaseString", "< Application, Lcoffeemaker/Main, makeCoffee()V >");
    String[] depends =  {"[recipeToDeleteString] in method < Application, Lcoffeemaker/Main, deleteRecipe()V >"};
    Assert.assertTrue(depend.PointsTo.checkDeps(mda, pk, depends));
    }

//  Analyzing local variable [r] in method < Application, Lcoffeemaker/Recipe, equals(Lcoffeemaker/Recipe;)Z >

  @Test
  public void milkStringDependencyTest() throws IOException, WalaException, CancelException, ParseException, InvalidClassFileException {
    String compilationUnitName = EXAMPLES_SRC + SEP + "coffeemaker/CoffeeMaker.java";
    String targetLineInCompilationUnit = "if(addRecipe(newRecipe)) {";
    String prefix = "coffee";
    String jarFileName = JAR_FILENAME;
     
    Assert.assertTrue((new File(compilationUnitName)).exists());
    Assert.assertTrue((new File(jarFileName)).exists());

    String[] lineAndClass = depend.util.parser.Util.getLineAndWALAClassName(targetLineInCompilationUnit, compilationUnitName);
    
    SimpleGraph sg = depend.Main.analyze(jarFileName, prefix, Integer.parseInt(lineAndClass[0]), lineAndClass[1]);
    
    MethodDependencyAnalysis mda= Main.mda;
    PointerKey pk =  depend.PointsTo.getPointerKey(depend.MethodDependencyAnalysis.cgg, mda, "chocolateString", "< Application, Lcoffeemaker/Main, addInventory()V >");
    String[] depends =  {"[milkString] in method < Application, Lcoffeemaker/Main, editRecipe()V >"};
    Assert.assertTrue(depend.PointsTo.checkDeps(mda, pk, depends));
    }
  
  @Test
  public void priceTagDependencyTest() throws IOException, WalaException, CancelException, ParseException, InvalidClassFileException {
    String compilationUnitName = EXAMPLES_SRC + SEP + "coffeemaker/CoffeeMaker.java";
    String targetLineInCompilationUnit = "if(addRecipe(newRecipe)) {";
    String prefix = "coffee";
    String jarFileName = JAR_FILENAME;
     
    Assert.assertTrue((new File(compilationUnitName)).exists());
    Assert.assertTrue((new File(jarFileName)).exists());

    String[] lineAndClass = depend.util.parser.Util.getLineAndWALAClassName(targetLineInCompilationUnit, compilationUnitName);
    
    SimpleGraph sg = depend.Main.analyze(jarFileName, prefix, Integer.parseInt(lineAndClass[0]), lineAndClass[1]);
    
    MethodDependencyAnalysis mda= Main.mda;
    PointerKey pk =  depend.PointsTo.getPointerKey(depend.MethodDependencyAnalysis.cgg, mda, "chocolateString", "< Application, Lcoffeemaker/Main, addInventory()V >");
    String[] depends =  {"[priceString] in method < Application, Lcoffeemaker/Main, addRecipe()V >"};
    Assert.assertTrue(depend.PointsTo.checkDeps(mda, pk, depends));
    }
  
  @Test
  public void recipeToEditDependencyTest() throws IOException, WalaException, CancelException, ParseException, InvalidClassFileException {
    String compilationUnitName = EXAMPLES_SRC + SEP + "coffeemaker/CoffeeMaker.java";
    String targetLineInCompilationUnit = "if(addRecipe(newRecipe)) {";
    String prefix = "coffee";
    String jarFileName = JAR_FILENAME;
     
    Assert.assertTrue((new File(compilationUnitName)).exists());
    Assert.assertTrue((new File(jarFileName)).exists());

    String[] lineAndClass = depend.util.parser.Util.getLineAndWALAClassName(targetLineInCompilationUnit, compilationUnitName);
    
    SimpleGraph sg = depend.Main.analyze(jarFileName, prefix, Integer.parseInt(lineAndClass[0]), lineAndClass[1]);
    
    MethodDependencyAnalysis mda= Main.mda;
    PointerKey pk =  depend.PointsTo.getPointerKey(depend.MethodDependencyAnalysis.cgg, mda, "chocolateString", "< Application, Lcoffeemaker/Main, addInventory()V >");
    String[] depends =  {"[recipeToEditString] in method < Application, Lcoffeemaker/Main, editRecipe()V >"};
    Assert.assertTrue(depend.PointsTo.checkDeps(mda, pk, depends));
    }
  
  
  @Test
  public void priceStringDependencyTest() throws IOException, WalaException, CancelException, ParseException, InvalidClassFileException {
    String compilationUnitName = EXAMPLES_SRC + SEP + "coffeemaker/CoffeeMaker.java";
    String targetLineInCompilationUnit = "if(addRecipe(newRecipe)) {";
    String prefix = "coffee";
    String jarFileName = JAR_FILENAME;
     
    Assert.assertTrue((new File(compilationUnitName)).exists());
    Assert.assertTrue((new File(jarFileName)).exists());

    String[] lineAndClass = depend.util.parser.Util.getLineAndWALAClassName(targetLineInCompilationUnit, compilationUnitName);
    
    SimpleGraph sg = depend.Main.analyze(jarFileName, prefix, Integer.parseInt(lineAndClass[0]), lineAndClass[1]);
    
    MethodDependencyAnalysis mda= Main.mda;
    PointerKey pk =  depend.PointsTo.getPointerKey(depend.MethodDependencyAnalysis.cgg, mda, "priceString", "< Application, Lcoffeemaker/Main, addRecipe()V >");
    String[] depends =  {"[milkString] in method < Application, Lcoffeemaker/Main, addRecipe()V >"};
    Assert.assertTrue(depend.PointsTo.checkDeps(mda, pk, depends));
    }
  
  @Test
  public void recipeToPurchaseDependencyTest() throws IOException, WalaException, CancelException, ParseException, InvalidClassFileException {
    String compilationUnitName = EXAMPLES_SRC + SEP + "coffeemaker/CoffeeMaker.java";
    String targetLineInCompilationUnit = "if(addRecipe(newRecipe)) {";
    String prefix = "coffee";
    String jarFileName = JAR_FILENAME;
     
    Assert.assertTrue((new File(compilationUnitName)).exists());
    Assert.assertTrue((new File(jarFileName)).exists());

    String[] lineAndClass = depend.util.parser.Util.getLineAndWALAClassName(targetLineInCompilationUnit, compilationUnitName);
    
    SimpleGraph sg = depend.Main.analyze(jarFileName, prefix, Integer.parseInt(lineAndClass[0]), lineAndClass[1]);
    
    MethodDependencyAnalysis mda= Main.mda;
    PointerKey pk =  depend.PointsTo.getPointerKey(depend.MethodDependencyAnalysis.cgg, mda, "priceString", "< Application, Lcoffeemaker/Main, addRecipe()V >");
    String[] depends =  {"[recipeToPurchaseString] in method < Application, Lcoffeemaker/Main, makeCoffee()V >", "[milkString] in method < Application, Lcoffeemaker/Main, addRecipe()V >"};
    Assert.assertTrue(depend.PointsTo.checkDeps(mda, pk, depends));
    }
   
  @Test
  public void toGetErrorDependencyTest() throws IOException, WalaException, CancelException, ParseException, InvalidClassFileException {
    String compilationUnitName = EXAMPLES_SRC + SEP + "coffeemaker/CoffeeMaker.java";
    String targetLineInCompilationUnit = "if(addRecipe(newRecipe)) {";
    String prefix = "coffee";
    String jarFileName = JAR_FILENAME;
     
    Assert.assertTrue((new File(compilationUnitName)).exists());
    Assert.assertTrue((new File(jarFileName)).exists());

    String[] lineAndClass = depend.util.parser.Util.getLineAndWALAClassName(targetLineInCompilationUnit, compilationUnitName);
    
    SimpleGraph sg = depend.Main.analyze(jarFileName, prefix, Integer.parseInt(lineAndClass[0]), lineAndClass[1]);
    
    MethodDependencyAnalysis mda= Main.mda;
    PointerKey pk =  depend.PointsTo.getPointerKey(depend.MethodDependencyAnalysis.cgg, mda, "priceString", "< Application, Lcoffeemaker/Main, addRecipe()V >");
    String[] depends =  {"[oldRecipe] in method < Application, Lcoffeemaker/Main, editRecipe()V >"};
    Assert.assertFalse(depend.PointsTo.checkDeps(mda, pk, depends));
  }
} 