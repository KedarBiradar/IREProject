import org.jsoup.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.jsoup.select.NodeVisitor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Vector;

public class NewParser
{
	static String headingName=new String();
	static ArrayList<tuple> tagList=new ArrayList<>();
	
	public static void main(String [] args)
	{
		Document doc=null;
		try
		{
			
				String filePath=args[0]; 	 
				final String sourceDir=filePath.substring(0,filePath.lastIndexOf("/")+1);
				extractLinks(filePath);
				File sourceFile= new File("tempFile");				 
				//File sourceFile= new File(filePath);
				doc=Jsoup.parse(sourceFile,"UTF-8");
				 //final HashMap<Vector<String>,String>tableMap=new HashMap<>();
				
				for (Element element : doc.select("*")) 
				{

			        if (!element.hasText() &&( element.tagName().equalsIgnoreCase("b") || (element.isBlock() && element.childNodeSize()==0)))
			        {
			            element.remove();
			        }
			    }
				 final BufferedWriter tableFile=new BufferedWriter(new FileWriter("TableFile"));
				 doc.traverse(new NodeVisitor() 
				 {
					 	
					 	int index=0,count=0;
					 	int [] tagIndexArray={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
					 	Vector<String> imageMap=new Vector<>();
					 	Vector<String> table;
						StringBuilder row;
						boolean boldTagAsHeading=false;
						
					 	public void head(Node node, int depth) 
					    {
					 		if(node.toString().toUpperCase().contains("ABSTRACT") && node.nodeName().equalsIgnoreCase("B"))
					 		{
					 			//Abstract found in <B> tag. Possibly heading tag are enclosed in <B> tag.
					 			boldTagAsHeading=true;
					 		}
					 		if(boldTagAsHeading)
					 		{
					 			if(node.nodeName().equalsIgnoreCase("B") || node.nodeName().matches("h[1-6]") || node.nodeName().matches("H[1-6]") || node.nodeName().equalsIgnoreCase("STRONG"))
						        {	
					 				if(tagList.size()==1)
						        	{
						        		//tagNumber=1;
						        		index=0;
						        		tagIndexArray[index]=1;
						        	}
						        	else if(tagList.size()>1)
						        	{
						        		tuple prevTag=tagList.get(tagList.size()-1);
						        		String prevTagName=prevTag.NodeName;
						        		String currentTagName=node.nodeName();
						        		if(prevTagName.compareToIgnoreCase(currentTagName)==0)
						        		{
						        			tagIndexArray[index]++;
						        		}
						        		else if(prevTagName.charAt(0)==currentTagName.charAt(0) && currentTagName.length()>1 && prevTagName.length()>1 && prevTagName.charAt(1) > currentTagName.charAt(1))
						        		{
						        			tagIndexArray[index]=0;
						        			index-=count;
						        			count=0;
						        			tagIndexArray[index]++;
						        		}
						        		else if(prevTagName.charAt(0)==currentTagName.charAt(0) && currentTagName.length()>1 && prevTagName.length()>1 && prevTagName.charAt(1) < currentTagName.charAt(1))
						        		{
						        			index++;
						        			count++;
						        			tagIndexArray[index]++;
						        		}
						        		
						        	}
					 				NewParser.headingName=node.toString().replaceAll("\\<[^>]*>","");
						        	tuple currentTag=new tuple(); 
						        	currentTag.tagName=node.toString();
						        	currentTag.text="";
						        	currentTag.NodeName=node.nodeName();
						        	currentTag.tagNo="";
						        	for(int l=0;l<=index;l++)
						        	{
						        		if(l==0)
						        			currentTag.tagNo+=""+(tagIndexArray[l]-1);
						        		else
						        			currentTag.tagNo+="."+tagIndexArray[l];
						        	}
						        	tagList.add(currentTag);
						        	//NewParser.headingName=currentTag.tagNo+node.toString().replaceAll("\\<[^>]*>","");
						        }
					 		}
					 		else
					 		{
						        if(node.nodeName().matches("h[1-6]") || node.nodeName().matches("H[1-6]") || node.nodeName().equalsIgnoreCase("STRONG"))
						        {		
						        	
						        	if(tagList.size()==1)
						        	{
						        		index=0;
						        		tagIndexArray[index]=1;
						        	}
						        	else if(tagList.size()>1)
						        	{
						        		tuple prevTag=tagList.get(tagList.size()-1);
						        		String prevTagName=prevTag.NodeName;
						        		String currentTagName=node.nodeName();
						        		if(prevTagName.compareToIgnoreCase(currentTagName)==0)
						        		{
						        			tagIndexArray[index]++;
						        		}
						        		else if(prevTagName.charAt(0)==currentTagName.charAt(0) && currentTagName.length()>1 && prevTagName.length()>1 && prevTagName.charAt(1) > currentTagName.charAt(1))
						        		{
						        			tagIndexArray[index]=0;
						        			/*while(count>0)
						        			{	
						        				tagIndexArray[index]=0;
						        				index--;
						        				count--;
						        			}
						        			*/
						        			index-=count;
						        			count=0;
						        			tagIndexArray[index]++;
						        			
						        		}
						        		else if(prevTagName.charAt(0)==currentTagName.charAt(0) && currentTagName.length()>1 && prevTagName.length()>1 && prevTagName.charAt(1) < currentTagName.charAt(1))
						        		{
						        			index++;
						        			count++;
						        			tagIndexArray[index]++;
						        		}
						        		
						        	}
						        	
						        	NewParser.headingName=node.toString().replaceAll("\\<[^>]*>","");
						        	tuple currentTag=new tuple(); 
						        	currentTag.tagName=node.toString();
						        	currentTag.text="";
						        	currentTag.NodeName=node.nodeName();
						        	currentTag.tagNo="";
						        	for(int l=0;l<=index;l++)
						        	{
						        		if(l==0)
						        			currentTag.tagNo+=""+(tagIndexArray[l]-1);
						        		else
						        			currentTag.tagNo+="."+tagIndexArray[l];
						        	}
						        	tagList.add(currentTag);
						        	//NewParser.headingName=currentTag.tagNo+node.toString().replaceAll("\\<[^>]*>","");
						        }	
					 		}
					 		

					 		
					        //System.out.println(node.nodeName());
					        
					        if(node.childNodeSize()==0)
					        {
					        	if(node.nodeName().equalsIgnoreCase("img"))
					        	{
					 				Attributes attr=node.attributes();
					 				
					 				if(!(NewParser.headingName.equalsIgnoreCase("")))
					 				{
					 					imageMap.add("source="+sourceDir+attr.get("src")+" ALT Text="+attr.get("alt")+" Section Heading="+NewParser.headingName);
					 				}	
					        	}
					        }
					        else if(node.nodeName().equalsIgnoreCase("img"))
				        	{
				 				Attributes attr=node.attributes();
				 				
				 				if(!(NewParser.headingName.equalsIgnoreCase("")))
				 				{
				 					imageMap.add("source="+sourceDir+attr.get("src")+" ALT Text="+attr.get("alt")+" Section Heading="+NewParser.headingName);
				 				}	
				        	}
					        else if(node.nodeName().equalsIgnoreCase("table"))
			        		{
			        			table=new Vector<>();
			        		}
					        else if(node.nodeName().equalsIgnoreCase("tr"))
					        {
					        	row=new StringBuilder(); 	
					        }
					        else if(node.nodeName().equalsIgnoreCase("td"))
				        	{
				        		row.append("\""+node.toString()+"\",");
				        	}
					    }
					    public void tail(Node node, int depth) 
					    {
					       if(node.nodeName().equalsIgnoreCase("tr"))
					    	{
					    		table.add(row.toString());
					    		row.setLength(0);
					    	}
					       else if(node.nodeName().equalsIgnoreCase("table"))
					    	{
					    	   try
					    	   {	
					    		   if(!(NewParser.headingName.equalsIgnoreCase("")))
					    		   {	   
						    	   	tableFile.write("section_name::"+NewParser.headingName+"\nTable::");
						    	   	for(int k=0;k< table.size();k++)
						    	   	{
						    	   		//System.out.println(table.elementAt(k));
						    	   		tableFile.write(table.elementAt(k).replaceAll("\\<[^>]*>","")+"$:");
						    	   	}
						    	   	tableFile.write("\n");
						    	   	tableFile.write("section_name");
						    	   	tableFile.flush();
					    		   }
						    		table.clear();
					    	   }
					    	   catch(Exception e)
					    	   {
					    		   e.printStackTrace();
					    	   }
					    	   	
					    	}
					    	if(node.nodeName().equalsIgnoreCase("html"))
					    	{	
					    		BufferedWriter writer = null;
					    		
						    	
						    		try
						    		{
						    			writer=new BufferedWriter(new FileWriter("Images"));
						    		}
						    		catch(Exception e)
						    		{
						    			e.printStackTrace();
						    		}
						    		
						    		if(imageMap.size()>0)
									 {	
						    	
								    	for(int i=0;i< imageMap.size();i++)
								    		{
								    			String text1=imageMap.elementAt(i).replaceAll("&quot;", "\"");
								    			String text5=text1.replaceAll("&gt;", ">").replaceAll("&amp;", "&").replaceAll("&lt;","<").replaceAll("&nbsp;"," ").replaceAll(".gif",".png").replaceAll("_","");
								    			
								    			try 
								    			{
													writer.write(text5+"\n");
												} catch (IOException e) 
												{
													e.printStackTrace();
												}
								    		}
								    	try 
								    	{
								    		writer.close();
										}
								    	catch (IOException e) 
										{
								    		e.printStackTrace();
										}
								}
						    	
					    	}	
					    }
					});
				 
				 int j=0;
				 String text=doc.toString();
				 String delim="!@#$";
				 BufferedWriter writer=new BufferedWriter(new FileWriter("IntermediateFile"));
				 for(j=0;j<tagList.size()-1;j++)
				 {
					 tuple currentTagTuple=tagList.get(j);
					 tuple nextTagTuple=tagList.get(j+1);
					 String currentHeadTag=currentTagTuple.tagName.replaceAll("\n", "");
					 String nextHeadTag=nextTagTuple.tagName.replaceAll("\n", "");
					 int startIndex,endIndex;
					 if(text.contains(currentHeadTag) && text.contains(nextHeadTag))
					 {		 
							 startIndex=text.indexOf(currentHeadTag);
							 endIndex=text.indexOf(nextHeadTag);
					 }
					 else
					 {
						 continue;
					 }
					 
					 if(j == 0)
					 {
						 writer.write("<label-1>"+currentHeadTag.replaceAll("\\<[^>]*>","")+delim+"</label>\n");
					 }
					 else
					 {	 
						 String temp1=text.substring(startIndex,endIndex);
						 String temp2=temp1.replace(currentHeadTag, currentHeadTag+delim);
						 
						 if(currentHeadTag.replaceAll("\\<[^>]*>","").equalsIgnoreCase("references"))
						 {
							 currentTagTuple.text=temp2.replaceAll("[\\t\\n\\r]+","\n").replaceAll("\\<[^>]*>","").replaceAll("&quot;", "\"").replaceAll("&gt;", ">").replaceAll("&amp;", "&").replaceAll("&lt;","<").replaceAll("&nbsp;"," ");
						 }
						else
						{	 
						 	currentTagTuple.text=temp2.replaceAll("[\\t\\n\\r]+"," ").replaceAll("\\<[^>]*>","").replaceAll("&quot;", "\"").replaceAll("&gt;", ">").replaceAll("&amp;", "&").replaceAll("&lt;","<").replaceAll("&nbsp;"," ");
						} 
						 writer.write("<label"+currentTagTuple.tagNo+"> "+currentTagTuple.text+"</label>"+"\n");
						 text=text.substring(endIndex);
						 //System.out.println("<"+currentTagTuple.index+">"+currentTagTuple.text+"</"+currentTagTuple.index+">");
					 }
				}
				 if(j>0)
				 {	 
						 tuple currentTagTuple=tagList.get(j);
						 String currentHeadTag=currentTagTuple.tagName;
						 int startIndex,endIndex;
						 if(text.contains(currentHeadTag))
						{		 
								 startIndex=text.indexOf(currentHeadTag);
								 endIndex=text.length();
								 String temp1=text.substring(startIndex,endIndex);
								 String temp2=temp1.replace(currentHeadTag, currentHeadTag+delim);
								 //currentTagTuple.text=temp2.replaceAll("[\\t\\n\\r]+"," ").replaceAll("\\<[^>]*>","").replaceAll("&quot;", "\"").replaceAll("&gt;", ">").replaceAll("&amp;", "&").replaceAll("&lt;","<").replaceAll("&nbsp;", " ");
								 if(currentHeadTag.replaceAll("\\<[^>]*>","").equalsIgnoreCase("references"))
								 {
									 currentTagTuple.text=temp2.replaceAll("[\\t\\n\\r]+","\n").replaceAll("\\<[^>]*>","").replaceAll("&quot;", "\"").replaceAll("&gt;", ">").replaceAll("&amp;", "&").replaceAll("&lt;","<").replaceAll("&nbsp;"," ");
								 }
								else
								{	 
								 	currentTagTuple.text=temp2.replaceAll("[\\t\\n\\r]+"," ").replaceAll("\\<[^>]*>","").replaceAll("&quot;", "\"").replaceAll("&gt;", ">").replaceAll("&amp;", "&").replaceAll("&lt;","<").replaceAll("&nbsp;"," ");
								} 
								 
								 writer.write("<label"+currentTagTuple.tagNo+"> "+currentTagTuple.text+"</label>"+"\n");
						}
				 }
				 writer.close();
				 tableFile.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/******************************************kedar code*************************************************************/
	
		
	public static void extractLinks(String filePath)
	{
		Document document=null;
		String parentDir=filePath.substring(0,filePath.lastIndexOf("/")+1);
		String srcPath=filePath.substring(filePath.lastIndexOf("/")+1);
		//System.out.println(parentDir);
		try
		{
			File sourceFile= new File(filePath);				 
			 document=Jsoup.parse(sourceFile,"UTF-8");
			 /*
			 document.traverse(new NodeVisitor() {
				    public void head(Node node, int depth) {
				        System.out.println("Entering tag: " + node.nodeName());
				    }
				    public void tail(Node node, int depth) {
				        System.out.println("Exiting tag: " + node.nodeName());
				    }
				});
			 */
			 String mainText=document.toString();
			Elements links=document.select("a");
			String link;
			for(Element e: links)
			{
				
				if(e.hasAttr("HREF"))
				{	
					String destPath=e.attr("HREF");
					if(!(destPath.contains("http")) && !(destPath.contains("/")) && (destPath.endsWith(".htm") || destPath.endsWith(".html") )&& srcPath.compareTo(destPath) !=0 )
					{
						//extract file and add text
						link=e.toString();
						int startIndex=-1,endIndex=-1;
						String text = new String(Files.readAllBytes(Paths.get(parentDir+destPath)), StandardCharsets.UTF_8);
						
						startIndex=text.indexOf("<body>");
						if(startIndex==-1)
							startIndex=text.indexOf("<BODY>");
						
						endIndex=text.indexOf("</body>");
						if(endIndex==-1)
							endIndex=text.indexOf("</BODY>");
						
						if(startIndex != -1 && endIndex != -1)
						{
							mainText=mainText.replace(link, text.substring(startIndex+6,endIndex));
							//System.out.println(mainText);
						}
					}
					
				}
				else if(e.hasAttr("href"))
				{	
					String destPath=e.attr("href");
					if(!(destPath.contains("http")) && !(destPath.contains("/")) &&  (destPath.endsWith(".htm") || destPath.endsWith(".html")) && srcPath.compareTo(destPath) !=0 )
					{	
						//extract file and add text
						
						link=e.toString();
						String text = new String(Files.readAllBytes(Paths.get(parentDir+destPath)), StandardCharsets.UTF_8);
						
						int startIndex=-1,endIndex=-1;
						startIndex=text.indexOf("<body>");
						if(startIndex==-1)
							startIndex=text.indexOf("<BODY>");
						
						endIndex=text.indexOf("</body>");
						if(endIndex==-1)
							endIndex=text.indexOf("</BODY>");
						
						if(startIndex != -1 && endIndex != -1)
						{
							mainText=mainText.replace(link, text.substring(startIndex+6,endIndex));
						}
					}	
						
				}		
			}
			
			BufferedWriter tempFile=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("tempFile"), "UTF-8"));
			tempFile.write(mainText);
			tempFile.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	
	/***********************************Rajbhan code*******************************************************************/
	/*
	static void combinepage(String mainPagename) throws IOException 
	{
		 Document doc=null;
		 int index=mainPagename.lastIndexOf("\\");
		 String pagename = mainPagename.substring(index+1);
		 String pathname=mainPagename.substring(0, index+1);
		 File sourceFile= new File(mainPagename);
		 doc=Jsoup.parse(sourceFile,"UTF-8");	
		 Set<String> set = new HashSet<>();
		 Elements link = doc.select("a");
		 for(Element e:link)
		 {
			 
			 //System.out.println(e.toString());
			 String linkHref = e.attr("href");
			 	if(linkHref.contains("@")||linkHref.contains("/")||linkHref.contains("http"))
			 	{
			 		
			 	}
			 	else
			 	{
			      // System.out.println(linkHref);
			       set.add(linkHref);
			 	}
			 
		 }
		 
		 	set.remove(pagename) ;
		
		    String filetext=doc.toString();
		    BufferedWriter writer=new BufferedWriter(new FileWriter(pagename));
		    writer.write(filetext);
			Iterator<String> it=set.iterator();
			while(it.hasNext())
			{
				String tempPagename=pathname+it.next();
				File tempsourceFile= new File(tempPagename);
				doc=Jsoup.parse(tempsourceFile,"UTF-8");
				filetext=doc.toString();
				//filetext.replaceAll(regex, replacement)
				writer.write(filetext);
				//System.out.println(filetext);
				
			}
			
			writer.close();
		
		 
	  
	  List<Element> children=doc.children();
		Element body=doc.body();
		List<Node> list=body.childNodes();
		String text=doc.toString();
		int startIndex=text.indexOf("hr");
		 int endIndex=text.indexOf("hr");
	   for(Node child : list)
		{
			String nodeName=child.nodeName();
			System.out.println(nodeName);
			System.out.println(child.toString());
			if(nodeName==("hr"))
			{
				System.out.println(child.toString());
			}
		}
	

		
		
	}*/
}

class tuple
{
	String tagName;
	String text;
	String NodeName;
	String tagNo;
	public String getText() 
	{
		return text;
	}
	public void setText(String text) 
	{
		this.text=text;
	}
	public String getTagName() 
	{
		return tagName;
	}
	public void setTagName(String tagName) 
	{
		this.tagName = tagName;
	}
	
}
