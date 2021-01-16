/* 
* To change this template, choose Tools | Templates 
* and open the template in the editor. 
package wpbc; 
import java.io.File; 
import java.io.FileInputStream; 
import java.io.FileOutputStream; 
import java.io.BufferedReader; 
import java.io.FileReader; 
import java.awt.BorderLayout; 
import weka.core.Instances; 
import weka.classifiers.trees.RandomTree; 
import weka.classifiers.trees.REPTree; 
import weka.gui.treevisualizer.PlaceNode2; 
import weka.gui.treevisualizer.TreeVisualizer; 
/** 
* 
* @author seabirds 
*/ 
public class Classification 
{ 
MainFrame mf; 
PreprocessFrame pf; 
Classification(MainFrame me,PreprocessFrame pe) 
{ 
mf=me; 
pf=pe; 
}
25 
public void getData1() 
{ 
try 
{ 
String ar="@relation wp\n"; 
for(int i=0;i<mf.colName.length-1;i++) { 
ar=ar+"@attribute "+mf.colName[i]+" numeric\n"; } 
ar=ar+"@attribute class {"; 
for(int i=0;i<mf.cls.size();i++) 
ar=ar+mf.cls.get(i).toString()+","; 
ar=ar.substring(0, ar.lastIndexOf(","))+"}\n"; System.out.println(ar); 
ar=ar+"@data\n"; 
for(int i=0;i<mf.data.length;i++) { 
String g1=""; 
for(int j=0;j<mf.data[0].length;j++) { 
g1=g1+mf.data[i][j].trim()+"\t"; 
} 
ar=ar+g1+"\n"; 
} 
File fe=new File("wp.arff"); 
FileOutputStream fos=new FileOutputStream(fe); fos.write(ar.trim().getBytes()); 
fos.close(); 
} 
catch(Exception e) 
{ 
e.printStackTrace(); 
} 
} 
public void classify2()
26 
{ 
try 
{ 
String ar="@relation wp\n"; 
for(int i=0;i<pf.featName.size();i++) 
{ 
ar=ar+"@attribute "+pf.featName.get(i).toString()+" numeric \n"; } 
ar=ar+"@attribute class {"; 
for(int i=0;i<mf.cls.size();i++) 
ar=ar+mf.cls.get(i).toString()+","; 
ar=ar.substring(0, ar.lastIndexOf(","))+"}\n"; 
System.out.println(ar); 
ar=ar+"@data\n"; 
for(int i=0;i<mf.data.length;i++) 
{ 
String g1=""; 
for(int j=0;j<pf.featNo.size();j++) 
{ 
int k=Integer.parseInt(pf.featNo.get(j).toString()); 
g1=g1+mf.data[i][k].trim()+"\t"; 
} 
ar=ar+g1+mf.data[i][mf.data[0].length-1]+"\n"; 
} 
File fe=new File("wp.arff"); 
FileOutputStream fos=new FileOutputStream(fe); 
fos.write(ar.trim().getBytes()); 
fos.close(); 
REPTree tree=new REPTree(); 
Instances data = new Instances(new BufferedReader(new FileReader("wp.arff"))); data.setClassIndex(data.numAttributes() - 1); 
tree.buildClassifier(data); 
final javax.swing.JFrame jf = new javax.swing.JFrame("Decision Tree "); jf.setSize(500,400);
27 
jf.getContentPane().setLayout(new BorderLayout()); 
TreeVisualizer tv = new TreeVisualizer(null,tree.graph(), new PlaceNode2()); jf.getContentPane().add(tv, BorderLayout.CENTER); 
jf.addWindowListener(new java.awt.event.WindowAdapter() { 
public void windowClosing(java.awt.event.WindowEvent e) { 
jf.dispose(); 
} 
}); 
jf.setVisible(true); 
tv.fitToScreen(); 
} 
catch(Exception e) 
{ 
e.printStackTrace(); 
} 
} 
public void classify1() 
{ 
try 
{ 
String ar="@relation wp\n"; 
for(int i=0;i<pf.featName.size();i++) 
{ 
ar=ar+"@attribute "+pf.featName.get(i).toString()+" numeric \n"; } 
ar=ar+"@attribute class {"; 
for(int i=0;i<mf.cls.size();i++) 
ar=ar+mf.cls.get(i).toString()+","; 
ar=ar.substring(0, ar.lastIndexOf(","))+"}\n"; 
System.out.println(ar); 
ar=ar+"@data\n"; 
for(int i=0;i<mf.data.length;i++)
28 
{ 
String g1=""; 
for(int j=0;j<pf.featNo.size();j++) 
{ 
int k=Integer.parseInt(pf.featNo.get(j).toString()); 
g1=g1+mf.data[i][k].trim()+"\t"; 
} 
ar=ar+g1+mf.data[i][mf.data[0].length-1]+"\n"; 
} 
File fe=new File("wp1.arff"); 
FileOutputStream fos=new FileOutputStream(fe); 
fos.write(ar.trim().getBytes()); 
fos.close(); 
RandomTree rt=new RandomTree(); 
Instances data = new Instances(new BufferedReader(new FileReader("wp1.arff"))); data.setClassIndex(data.numAttributes() - 1); 
rt.buildClassifier(data); 
final javax.swing.JFrame jf = new javax.swing.JFrame("Random Tree with Fisher Filtering");jf.setSize(500,400); 
jf.getContentPane().setLayout(new BorderLayout()); 
TreeVisualizer tv = new TreeVisualizer(null,rt.graph(), new PlaceNode2()); jf.getContentPane().add(tv, BorderLayout.CENTER); 
jf.addWindowListener(new java.awt.event.WindowAdapter() { 
public void windowClosing(java.awt.event.WindowEvent e) 
{ 
jf.dispose(); 
} 
}); 
jf.setVisible(true); 
tv.fitToScreen(); 
} 
catch(Exception e) 
e.printStackTrace(); 
}
