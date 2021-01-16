/* 
* To change this template, choose Tools | Templates * and open the template in the editor. 
*/ 
package wpbc; 
import java.io.*; 
import java.util.*; 
import java.text.*; 
import weka.core.*; 
import weka.core.Instance;
30 
import weka.core.Instances; 
import weka.core.Attribute; 
import weka.classifiers.*; 
import weka.classifiers.Classifier; 
import weka.classifiers.trees.*; 
import weka.classifiers.trees.j48.*; 
import weka.classifiers.bayes.*; 
import weka.filters.unsupervised.attribute.StringToWordVector; /** 
* 
* @author seabirds 
*/ 
public class TestClassify 
{ 
private String[] inputText = null; 
private String[] inputClasses = null; 
private String classString = null; 
private Attribute classAttribute = null; 
private Attribute textAttribute = null; 
private FastVector attributeInfo = null; 
private Instances instances = null; 
private Classifier classifier = null; 
private Instances filteredData = null; 
private Evaluation evaluation = null; 
private Set modelWords = null; 
private String delimitersStringToWordVector = "\\s.,:'\\\"()?!"; TestClassify(String[] inputText, String[] inputClasses, FastVector attributeInfo, AttributetextAttribute, Attribute classAttribute, String classString { 
this.inputText = inputText; 
this.inputClasses = inputClasses; 
this.classString = classString; 
this.attributeInfo = attributeInfo; 
this.textAttribute = textAttribute;
31 
this.classAttribute = classAttribute; 
} 
public StringBuffer classify() { 
if (classString == null || "".equals(classString)) { 
return(new StringBuffer()); 
} 
return classify(classString); 
} 
public StringBuffer classify(String classString) { 
this.classString = classString; 
StringBuffer result = new StringBuffer(); 
instances = new Instances("data set", attributeInfo, 100); 
instances.setClass(classAttribute); 
try { 
instances = populateInstances(inputText, inputClasses, instances, classAttribute, textAttribute);result.append("DATA SET:\n" + instances + "\n"); 
filteredData = filterText(instances); 
modelWords = new HashSet(); 
Enumeration enumx = filteredData.enumerateAttributes(); 
while (enumx.hasMoreElements()) { 
Attribute att = (Attribute)enumx.nextElement(); 
String attName = att.name().toLowerCase(); 
modelWords.add(attName); 
} 
classifier = Classifier.forName(classString,null); 
classifier.buildClassifier(filteredData); 
evaluation = new Evaluation(filteredData); 
evaluation.evaluateModel(classifier, filteredData); 
result.append(printClassifierAndEvaluation(classifier, evaluation) + "\n"); int startIx = 0; 
result.append(checkCases(filteredData, classifier, classAttribute, inputText, "not test", startIx)+"\n"); 
} catch (Exception e) { 
e.printStackTrace(); 
result.append("\nException (sorry!):\n" + e.toString());
32 
} 
return result; 
} 
public StringBuffer classifyNewCases(String[] tests) { System.out.println("----- classify new case "+tests.length); 
StringBuffer result = new StringBuffer(); 
Instances testCases = new Instances(instances); 
testCases.setClass(classAttribute); 
String[] testsWithModelWords = new String[tests.length]; 
int gotModelWords = 0; 
for (int i = 0; i < tests.length; i++) { 
StringBuffer acceptedWordsThisLine = new StringBuffer(); String[] splittedText = tests[i].split("["+delimitersStringToWordVector+"]"); for (int wordIx = 0; wordIx < splittedText.length; wordIx++) { String sWord = splittedText[wordIx]; 
if (modelWords.contains((String)sWord)) { 
gotModelWords++; 
acceptedWordsThisLine.append(sWord + " "); 
} 
} 
testsWithModelWords[i] = acceptedWordsThisLine.toString(); } 
if (gotModelWords == 0) { 
result.append("\nWarning!\nThe text to classify didn't contain a single\nword fromthemodelledwords. This makes it hard for the classifier to\ndo something usefull.\nThe result maybeweird.\n\n"); 
} 
try { 
String[] tmpClassValues = new String[tests.length]; 
for (int i = 0; i < tmpClassValues.length; i++) { 
tmpClassValues[i] = "?"; 
} 
} 
}
33 
testCases = populateInstances(testsWithModelWords, tmpClassValues, testCases, classAttribute,textAttribute); 
System.out.println("---- testcase "+testCases.numInstances()); 
Instances filteredTests = filterText(testCases); 
int startIx = instances.numInstances(); 
System.out.println("--- classify new case"+startIx+" : "+filteredTests.numInstances()); result.append(checkCases(filteredTests, classifier, classAttribute, tests, "newcase", startIx) +"\n");} catch (Exception e) { 
e.printStackTrace(); 
result.append("\nException (sorry!):\n" + e.toString()); 
} 
return result; 
} 
public static Instances populateInstances(String[] theseInputTexts, String[] theseInputClasses, Instances theseInstances, Attribute classAttribute, Attribute textAttribute) { System.out.println(theseInputTexts.length); 
for (int i = 0; i < theseInputTexts.length; i++) 
{ 
Instance inst = new Instance(2); 
inst.setValue(textAttribute,theseInputTexts[i]); 
if (theseInputClasses != null && theseInputClasses.length > 0) { inst.setValue(classAttribute, theseInputClasses[i]); 
} 
theseInstances.add(inst); 
} 
System.out.println("populate instacnec "+theseInstances.numInstances()); return theseInstances; 
} 
public static StringBuffer checkCases(Instances theseInstances, Classifier thisClassifier, AttributethisClassAttribute, String[] texts, String testType, int startIx) { StringBuffer result = new StringBuffer(); 
try { 
result.append("\nCHECKING ALL THE INSTANCES:\n"); 
Enumeration enumClasses = thisClassAttribute.enumerateValues();
34 
result.append("Class values (in order): "); 
while (enumClasses.hasMoreElements()) 
{ 
String classStr = (String)enumClasses.nextElement(); 
result.append("'" + classStr + "' "); 
} 
result.append("\n"); 
System.out.println("------------check case "+startIx+" : "+theseInstances.numInstances()); for (int i = startIx; i < theseInstances.numInstances(); i++) { SparseInstance sparseInst = new SparseInstance(theseInstances.instance(i)); sparseInst.setDataset(theseInstances); 
result.append("\nTesting: '" + texts[i-startIx] + "'\n"); 
double correctValue = (double)sparseInst.classValue(); 
double predictedValue = thisClassifier.classifyInstance(sparseInst); String predictString = thisClassAttribute.value((int)predictedValue) + " (" + predictedValue+")";result.append("predicted: '" + predictString); 
if (!"newcase".equals(testType)) { 
String correctString = thisClassAttribute.value((int)correctValue) + " (" + correctValue+")"; String testString = ((predictedValue == correctValue) ? "OK!" : "NOT OK!") + "!"; result.append("' real class: '" + correctString + "' ==> " + testString); } 
result.append("\n"); 
result.append("\n"); 
} 
} catch (Exception e) { 
e.printStackTrace(); 
result.append("\nException (sorry!):\n" + e.toString()); 
} 
return result; 
} 
public static Instances filterText(Instances theseInstances) { System.out.println("---filertext "+theseInstances.numInstances()); 
StringToWordVector filter = null; 
int wordsToKeep = 1000;
35 
Instances filtered = null; 
try { 
filter = new StringToWordVector(wordsToKeep); 
filter.setOutputWordCounts(true); 
filter.setSelectedRange("1"); 
filter.setInputFormat(theseInstances); 
filtered = weka.filters.Filter.useFilter(theseInstances,filter); 
} catch (Exception e) { 
e.printStackTrace(); 
} 
return filtered; 
} 
public static StringBuffer printClassifierAndEvaluation(Classifier thisClassifier, EvaluationthisEvaluation) { 
StringBuffer result = new StringBuffer(); 
try { 
result.append("\n\nINFORMATION ABOUT THE CLASSIFIER AND EVALUATION:\n");result.append("\nclassifier.toString():\n" + thisClassifier.toString() + "\n"); result.append("\nevaluation.toSummaryString(title, false):\n" + thisEvaluation.toSummaryString("Summary",false) + "\n"); result.append("\nevaluation.toMatrixString():\n" + thisEvaluation.toMatrixString() +"\n"); result.append("\nevaluation.toClassDetailsString():\n" + thisEvaluation.toClassDetailsString("Details") + "\n"); 
result.append("\nevaluation.toCumulativeMarginDistribution:\n" + thisEvaluation.toCumulativeMarginDistributionString() + "\n"); } catch (Exception e) { 
e.printStackTrace(); 
result.append("\nException (sorry!):\n" + e.toString()); 
} 
return result; 
} 
public void setClassifierString(String classString) { 
this.classString = classString; 
} 
}
