if [ $# -eq 1 ] 
then	
	dirName=`dirname $1`
	#echo $dirName
	for f in "$dirName"/*.gif;
	do
		#echo $f
		convert "$f" "${f/%gif/png}"
	done 2> /dev/null
	for file in "$dirName"/*.png; do
		cp "$file" "${file/_/}"
	done 2> /dev/null

	for file in "$dirName"/*.jpg; do
		cp "$file" "${file/_/}"
	done 2> /dev/null

	for file in "$dirName"/*.jpeg; do
		cp "$file" "${file/_/}"
	done 2> /dev/null
	
	javac -cp "./jsoup-1.7.3.jar" -d . NewParser.java
	java -cp ".:./jsoup-1.7.3.jar" NewParser "$1" 
	g++ tfidf_cossim.cpp -w
	fileName="IntermediateFile"
	./a.out $fileName > output.txt
	cp template.tex myfile.tex
	python parse.py
	pdflatex myfile.tex

else	
	echo "Usage bash run.sh <HTML File Path>"
	exit
fi
