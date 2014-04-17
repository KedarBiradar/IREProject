import re
yo=open('Images','r')
imgs={}
yi=yo.readlines()
chj=re.compile('source=(.*?) ALT')
tt=re.compile('=(.*?)\n')
chet=re.compile('Heading=(.*?)\n')
imgreg= re.compile('ALT Text=(.*?)Section')
for line in yi:
#print line
	sew=chet.findall(line)
	rew=chj.findall(line)
	if(len(sew)>0):
#print sew[0]
		sew[0]=sew[0].lower()
		sew[0]=sew[0].strip()
#		rew[0]=rew[0].lower()
#		rew[0]=rew[0].strip()
#		sew[0]=re.sub('[0-9].[0-9]|[0-9]','',sew[0])
		sew[0].lstrip("0123456789. ")
		if(sew[0] in imgs):
			imgs[sew[0]].append(rew[0])
		else:
			imgs[sew[0]]=[rew[0]]
print imgs		

	
ta=open('TableFile','r')
#fara=open('TableFile','a')
#fara.write("section_name")
racha=ta.read()
print racha
chacha=re.compile('::((.|\n)*?)section_name')	
allthngs=chacha.findall(racha)
heads={}	
for ghi in allthngs:
	ghi=''.join(ghi)
	ttyl=ghi.split('\n')
	for xxx in range(1,len(ttyl)):
		if(len(ttyl[xxx])>0):
			ttyl[0]=ttyl[0].lower()
			ttyl[0]=ttyl[0].strip()
			if ttyl[0] in heads:
				heads[ttyl[0]].append(ttyl[xxx])
			else:
				heads[ttyl[0]]=[ttyl[xxx]]

print heads
moveit=re.compile(':(.*?)\$')				
likeit=re.compile('\"(.*?)\"')				
f = open('output.txt', 'r')
fa=open('output.txt','a')
fa.write("Heading")
dfgh=re.compile('Title::((.|\n)*?)Heading')
r = re.compile('g::((.|\n)*?)Headin')
str1=f.read()
#str1=re.sub(str1,"%","\\%")	
superss=dfgh.findall(str1)
print "PRINTING superss"	
print superss
sde=superss[0]
sde=''.join(sde)
sde=sde.strip()
print sde
m = r.findall(str1)
w = open('myfile.tex','a')
w.write('\\title[Short Title]{'+sde+'}\n')
w.write('\\begin{document}\n')
w.write('\\maketitle\n')
for jum in m:	
	jum=''.join(jum)
	ff=jum.split('\n')
	w.write("\\begin{frame}\n")
	count=0
	for chk in ff:
		chk=re.sub('&quot','\"',chk)
		chk=re.sub('&|\'','',chk)
		chk=re.sub('%','\\%',chk)
		chk=re.sub('#',' ',chk)
		if count==0:
			w.write("\\frametitle{"+chk+"}\n")
		else:	
			if(len(chk)!=0):
				w.write(chk+"\\\\")
		count=count+1
	w.write("\n\end{frame}\n")
	chk=ff[0]	
	toremove=chk
#	toremove=re.sub('[0-9].[0-9]|[0-9]','',toremove)	   
	
	toremove=toremove.lstrip("0123456789. ")
	if(toremove in imgs):
		print "Heres an image for " + toremove
		w.write("\\begin{frame}\n")
		w.write("\\frametitle{"+toremove+"}\n")
		for yop in imgs[toremove]:
			w.write("\\begin{figure}\includegraphics[width=0.2\linewidth]{"+yop+"}\end{figure}")
		w.write("\end{frame}\n")
	chk=ff[0]		  
	ffrt=chk
	ffrt=ffrt.lstrip("0123456789. ") 
	print ffrt
	if(ffrt in heads):
			w.write("\\begin{frame}\n")
			w.write("\\frametitle{"+ffrt+"}\n")
			print "HEADING IS"+chk
			for ink in heads[ffrt]:
				rows=moveit.findall(ink)		
				jingle=0
				w.write("\\begin{table}\\begin{tabular}{")
				for row in rows:
					qwerty=likeit.findall(row)
					cols=len(qwerty)
				for rty in range(cols):
					w.write("|l")
				w.write("|")	
				w.write("}\\toprule")	
				for row in rows:
					qwerty=likeit.findall(row)
					cols=len(qwerty)
					if(jingle==0):
						for ijk in range(cols):
							if ijk==0:
								w.write("\\textbf{"+qwerty[ijk]+"}")
							else:
								w.write("&\\textbf{"+qwerty[ijk]+"}")
						w.write("\\\\")			
						w.write("\midrule\n")		
					else:		
						for ijk in range(cols):
							if ijk==0:
								w.write(qwerty[ijk])
								
							else:
								w.write("&"+qwerty[ijk])
						w.write("\\\\")		
								
					print qwerty
					jingle=jingle+1
				w.write("\\bottomrule\end{tabular}\end{table}")		
				print "NEXT TABLE"	
			w.write("\n\end{frame}\n")
w.write("\end{document}\n")
