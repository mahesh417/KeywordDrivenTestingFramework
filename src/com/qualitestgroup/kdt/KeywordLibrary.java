package com.qualitestgroup.kdt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doclet;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.Tag;

public class KeywordLibrary extends Doclet{
	
	static StringBuffer sb = new StringBuffer();
	
	public static boolean start(RootDoc root) {		
        ClassDoc[] classes = root.classes();
        HashMap<String, LinkedList<ClassDoc>> apps = new HashMap<String, LinkedList<ClassDoc>>();
        for (int i = 0; i < classes.length; ++i) {
        	
            ClassDoc cls = classes[i];
            ClassDoc sup = cls;
            String name;
            do
            {
            	sup = sup.superclass();
            	name = sup.name();
            	if(name.equals("Keyword"))
            	{
            		String app = cls.name().split("\\.", 2)[0];
            		apps.get(app).add(cls);
            	} else if(name.equals("KeywordGroup"))
            	{
            		apps.put(cls.name(), new LinkedList<ClassDoc>());
            	}
            } while(!name.equals("Object"));           
            
        }     
        
        System.out.println("Saving in " + System.getProperty("user.dir"));
        try {
        	PrintWriter pw = new PrintWriter("./KeywordLibrary.html");
        	pw.print(createHTML(apps));        	
        	pw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        //System.out.println(sb.toString());
        return true;
        
    }
	
	private static void s(String s)
	{
		sb.append(s);
		sb.append("\n");
	}
	
	private static StringBuffer createHTML(HashMap<String, LinkedList<ClassDoc>> apps)
	{
		int i = 0;
		
        s("<html>");
        
        
        //Header Start*************************************************************************
        s("<head>");
        
        s("<title>Keyword Library</title>");
        
        //Hide Function
        s("<script>");
        
        s("function hide(id){");
        
        s("clearTables();");
        
        s("var tbody=document.getElementById(id);");
        
        s("if(tbody.style.display==''){" );
        
        s("tbody.style.display='none';}" );
        
        s("else{");
        
        s("tbody.style.display='';}}");
        s("function clearTables(){");
        
        s("var list=document.getElementsByTagName('table');");
        
        s("for(j=0;j<list.length;j++){" );
        
        s("if(list[j].style.display==''){");
        
        s("list[j].style.display='none';}}}");
        s("</script>" );
        
        //Table Zebra
        s("<style>");
        
        s("tr:nth-child(2n+1){background-color: #C2EBFF;}");                
        s("table{");
        s("border: 1px solid black;");
        s("width: 90%;");
        s("text-align: center;");
        s("}");
        s("td{");
        s("border: 1px solid black;");
        s("text-align: center;");
        s("}");
        s(".author{");
        s("font-style: italic;");
        s("}");
        s("</style>");
        
        s("</head>");
        
        //End of Header******************************************************************
        //Body Start*********************************************************************
        s("<body>");
        
        //App Name Div
        s("<div style='float: left; width: 10%;'>");
        
        for(Entry<String, LinkedList<ClassDoc>> app : apps.entrySet())
        {
        	s("<li><a id='AppLink' onclick='hide("+i+")' href='#'>"+app.getKey()+"</a></li>");
        	
        	i++;
        }
        i = 0;
        s("</div>");
        
        //Keyword Data Div
        s("<div style='float: right;width: 90%;'>");
        
        //Keyword Data Table Loop
        for(Entry<String, LinkedList<ClassDoc>> app : apps.entrySet())
        {
        	s("<table id="+i+" align='center' style='display: none;'>");
            
            s("<tr><th style='width: 15%'>Keyword</th><th style='width: 75%'>Doc</th></tr>");
            
        	for(ClassDoc doc : app.getValue())
        	{
        		String name = doc.name().split("\\.", 2)[1];
        		String comment = doc.commentText();
        		Tag[] authorArray = doc.tags("author");
        		String author = "";
        		if(authorArray.length > 0)
        		{
        			author = authorArray[0].text();
        			for(int a = 1; a < authorArray.length; a++)
        			{
        				author += ", " + authorArray[a].text();
        			}
        		}
        		s("<tr><td>"+name+"</td><td><div class='comment'>"+ comment +"</div>"
        				+ "<div class='author'>" + author +  "</div></td></tr>");
                
        	}
        	s("</table>");
        	i++;
        }
        //End Loop Close Div
        s("</div>");
        
        s("</body>");
        
        s("</html>");
        
        return sb;
	}
	
//	public static int optionLength(String option) {
//        return HtmlDoclet.optionLength(option);
//    }
//	public static boolean validOptions(String[][] options,
//            DocErrorReporter reporter) {
//		return HtmlDoclet.validOptions(options, reporter);
//	}
	
	/*public static LanguageVersion languageVersion() {
		return HtmlDoclet.languageVersion();
	}*/
	
}
