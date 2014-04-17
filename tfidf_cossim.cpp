#include<iostream>
#include<map>
#include<vector>
#include<math.h>
#include<fstream>
#include<stdlib.h>
#include<cstring>
#include<sstream>
#include<algorithm>
using namespace std;
class TFIDF {
private:
	map<string,int> doc_map;
	int words_count;
public:
	TFIDF(map<string,int> mymap,int count)
	{
		doc_map = mymap;
		words_count=count;
	}
	double calculate_TF(map<string,int> local_map, string word)
	{
		double count=0;
		for(map<string,int>::iterator it=local_map.begin() ; it!=local_map.end() ; ++it)
			count += it->second;
		return (double)local_map[word]/count;
	/*	int count=0;
		for(vector<string>::iterator it=vec.begin() ; it!= vec.end() ; ++it)
		{
			if((*it)==word)
				count++;
		}
		if(type=="TF")
			return count/vec.size();
		else if (type=="IDF")
			return log(vec.size()/count);
*/
	}
	double calculate_IDF(string word)
	{
		return log((double)words_count/(double)doc_map[word]);
	}
};

class Cosine_Similarity {
private:
	map<string,int> ref_map;
public:
	Cosine_Similarity(vector<string> text)
	{
		for(vector<string>::iterator it=text.begin() ; it!=text.end() ; ++it)
			ref_map[*it]++;
	}

	double calculate(vector<string> sentense)
	{
		map<string,int> s_map,ref_map_temp;
		ref_map_temp = ref_map;
		for(vector<string>::iterator it=sentense.begin() ; it!=sentense.end() ; ++it)
			s_map[*it]++;

		for(map<string,int>::iterator it=ref_map_temp.begin() ; it!=ref_map_temp.end() ; ++it)
		{
			if(s_map[it->first]==0)
			{
				s_map[it->first]=0;
			}
		}
		for(map<string,int>::iterator it=s_map.begin() ; it!=s_map.end() ; ++it)
		{
			if(ref_map_temp[it->first]==0)
				ref_map_temp[it->first]=0;
		}
		//cout << "ref_map size::" << ref_map_temp.size() << " " << s_map.size() << endl;

		double sum=0,sq_sum_ref=0,sq_sum_s=0;
	//	cout << "string ref_map s_map\n";
		for(map<string,int>::iterator it=ref_map_temp.begin() ; it!=ref_map_temp.end() ; ++it)
		{
	//		cout << it->first << " " << ref_map_temp[it->first] << " " << s_map[it->first] << endl;
			sum += ref_map_temp[it->first]*s_map[it->first];
			sq_sum_ref += ref_map_temp[it->first]*ref_map_temp[it->first];
			sq_sum_s += s_map[it->first]*s_map[it->first];
		}
	//	cout << "sum::" << sum << " " << sq_sum_ref << " " << sq_sum_s << endl;
		double mod_sum_ref = sqrt(sq_sum_ref);
		double mod_sum_s = sqrt(sq_sum_s);
		return sum/(mod_sum_ref*mod_sum_s);
	}
};
string open_file(char* filename)
{
	string line,text="";
	ifstream myfile(filename);
	if(myfile.is_open())
	{
		while(getline(myfile,line))
		{
			text += line;
		}
		//cout << text;
		myfile.close();
	}
	else
		cout << "Error in opening the file/ or file doesn't exist\n";
	return text;
}

void lower(char* result)
{
	for(int i=0 ; result[i]!='\0';++i)
		result[i] = tolower(result[i]);
}

map<string,int> stop_words;
void define_stopWords()
{
	string line;
	ifstream wordsfile("stopwords.txt");
	while(getline(wordsfile,line))
		stop_words[line] = 1;
}

bool check_stop_word(char* result)
{
	string s = result;
	if(stop_words[s]==1)
		return 1;
	else
		return 0;
}

vector<string> parse(string text)
{
	string s;
	vector<string> v;
	char temp[1000];
	int c=0;
	for(int i=0 ; text[i]!='\0';++i)
	{
		if((text[i]<'A' || text[i]>'Z') && (text[i]<'a' || text[i]>'z'))
		{
			temp[c] = '\0';
			lower(temp);
			if(check_stop_word(temp)==0)
			{
				string s = (string)temp;
				v.push_back(s);
			}
			c=0;
			continue;
		}
		temp[c++] = text[i];
	}
	return v;
	/*
	vector<string> v;
	string word;
	char* result;
	char deli[100] = "\ ,!+-!@#$%^&*()[]{}=|':;<>,.?/~`_1234567890";
	char* str = (char*)text.c_str();
	result = strtok(str,deli);
	while(result!=NULL)
	{
		lower(result);
		if(check_stop_word(result)==0)
		{
			word = (string)result;
			v.push_back(word + " ");
		}
		result = strtok(NULL,deli);
	}
	return v;*/
}
vector<string> parse_as_sentences(string text)
{
	vector<string> v;
	char* result;
	string line;
	char* str = (char*)text.c_str();
	result = strtok(str,".");
	while(result!=NULL)
	{
		line = (string)result;
		line = line + " ";
		v.push_back(line);
		result = strtok(NULL,".");
	}
	return v;
}
vector< pair< pair<string,string>, string> > call_this_function(string filename)
{
	vector< pair< pair<string,string>, string> > mymap;
	std::ifstream in(filename.c_str());

	std::stringstream buffer;
	buffer << in.rdbuf();
	std::string contents(buffer.str());
//	cout << contents<<endl;
	int l=(int)contents.size();
	//cout << l<<endl;
	char label[100000],heading[100000],text[1000000];
	int j=0,i=0;
	while(i<=l-1)
	{
		if(contents[i]=='<')
		{
			if(contents[i+1]!='/' && contents[i+1]=='l' && contents[i+2]=='a' && contents[i+3]=='b' && contents[i+4]=='e' && contents[i+5]=='l')
			{
				i++;
				j=0;
				while(contents[i] !='>')
				{
					label[j++]=contents[i];
					label[j]='\0';
					i++;
				}
				i++;
				j=0;
				
				while(1)
				{
					while(contents[i]!='!')
					{
						heading[j++]=contents[i];
						heading[j]='\0';
						i++;
					}
					if(i+3 <= l-1 && contents[i+1]=='@' && contents[i+2]=='#' && contents[i+3]=='$')
					{
						i=i+3;
						break;
					}
					else
					{					
						continue;
					}
				}
				i++;
				j=0;
				while(contents[i]!='<')
				{
					text[j++]=contents[i];
					text[j]='\0';
					i++;
				}
				while(contents[i] !='>')
					i++;
				string lbl(label),hding(heading),txt(text);
	//			cout << lbl <<endl;
	//			cout << hding << endl;
	//			cout << txt << endl;
				mymap.push_back(make_pair(make_pair(lbl, hding),txt));
//				lbl.clear();
//				hding.clear();
//				txt.clear();
			}
			
		}
		i++;
//		cout <<"l= "<<l<< "i= "<<i<< endl;
	}
	return mymap;
}
bool mycomp (const pair<int , pair<string,double> >  &a , const pair<int , pair<string,double> > &b)
{
	if(a.second.second < b.second.second) return 0;
	else return 1;
}
bool mycomp2 (const pair<int , pair<string,double> >  &a , const pair<int , pair<string,double> > &b)
{
	if(a.first < b.first) return 1;
	else return 0;
}
vector<string> getIntro(vector<string> abs_vec , vector<string> intro_sent_vec,int N)
{
	Cosine_Similarity *obj = new Cosine_Similarity(abs_vec);
	map<double,string> cos_map;
	pair<int , pair<string,double> > mypair[100000];
	int count=0;
	for(vector<string>::iterator it=intro_sent_vec.begin() ; it!=intro_sent_vec.end() ; ++it)
	{
		if(it->length() < 20)
			continue;
		double cosvalue;
		pair<string,double> p =  make_pair(*it,0);
		string temp = *it;
	///	cout << *it << endl;
		vector<string> s = parse(temp);
		if(!s.empty())
		{
			cosvalue = obj->calculate(s);
			p.second = cosvalue;
		}
	//	cout << p.second << " " << p.first << endl;
		pair<int , pair<string,double> > p2 = make_pair(count,p);
		mypair[count++] = p2;
		//	cout << "\n\n\n\n";
	}
	sort(mypair,mypair+count,mycomp);
//	for(int i=0 ; i < count ; ++i)
//		cout << mypair[i].second << " " << mypair[i].first << endl;
	vector<string> v;
	if(N>count)
		N = count;
	
	sort(mypair,mypair+N,mycomp2);
	for(int i=0 ; i < N ; ++i)
	{
		v.push_back(mypair[i].second.first);
		//cout << i+1 <<". " << mypair[i].first << "("<<mypair[i].second <<")" << endl;
	}
	return v;
}
vector<string> getTopNSentencesTFTDF(string text,map<string,int> doc_words,int word_count,int N)
{
	vector<string> v;
	map<string,int> mymap;
	vector<string> model_vec = parse(text);
	for(vector<string>::iterator it=model_vec.begin() ; it!=model_vec.end() ; ++it)
	{
		mymap[*it]++;
	}
	vector<string> model_sentences_vec = parse_as_sentences(text);
	TFIDF* obj = new TFIDF(doc_words,word_count);
	int s = model_sentences_vec.size();
//	pair<string,double> mypairs[100000];
	pair<int , pair<string,double> > mypairs[100000];
	int c=0;
	for(vector<string>::iterator it=model_sentences_vec.begin() ; it!=model_sentences_vec.end() ; ++it)
	{
		if(it->length() < 20)
			continue;
		double TF_IDF;
		double sum =0 ;
		vector<string> words = parse(*it);
		for(vector<string>::iterator it2=words.begin() ; it2!=words.end() ; ++it2)
		{
			double TF = obj->calculate_TF(mymap,*it2);
		//	cout << "TF " << TF << endl;
			double IDF = obj->calculate_IDF(*it2);
		//	cout << "IDF " << IDF << endl;
			TF_IDF = TF*IDF;
			sum += TF_IDF;
		}
		if(!words.empty())
		{
		//	cout << "SUM:" << sum << endl;
		//	cout << "words.size():" << words.size() << endl;
			double sentence_TFIDF = sum/words.size();
		//	cout << "sentence TFIDF::" << sentence_TFIDF << endl;
			pair<string,double> p = make_pair(*it,sentence_TFIDF);
			pair<int, pair<string,double> > p2 = make_pair(c,p);
			mypairs[c++] = p2;
		}
	}

	sort(mypairs,mypairs+c,mycomp);
	if(N > c)
		N = c;

	sort(mypairs,mypairs+N,mycomp2);
	for(int i=0 ; i < N ; ++i)
	{
		v.push_back(mypairs[i].second.first);
	//	cout << mypairs[i].second << " " << mypairs[i].first << endl;
	}
	return v;

}
bool check_str(string s)
{
	bool flag=0;
	for(int i=0 ; s[i]!='\0';++i)
	{
		if(s[i]>'a' && s[i]<'z')
		{
			flag=1;
			break;
		}
	}
	return flag;
}
vector<string> getTopN(string text,int N)
{
	vector<string> v;
	char str[10000];
	int c=0,count=0;
	for(int i=0 ; text[i]!='\0';++i)
	{
		if(count==N)
			break;
		if(text[i]=='\n')
		{
			str[c]='\0';
			c=0;
			string s = (string)str;
			if(check_str(s)==0)
				continue;
			else
			{
				v.push_back(s);
				count++;
				continue;
			}
		}
		str[c++] = text[i];
	}
	return v;
}

//not using this as of now
/*void refine_string(string s)
{
	int k=1;
	for(int i=0 ; s[i]!='0'; ++i)
	{
		if(s[i]==" " && k==1)
		{
			k=2;
			continue;
		}
		cout << s[i];
	}
}*/
int main(int argc, char *argv[])
{
	define_stopWords();
	string filename = (string)argv[1];

	vector<string> abs_vec;
	vector<string> intro_vec;
	vector<string> conclu_vec;
	map<string,int> doc_words;
//	cout << "Before \n";
	vector< pair< pair<string,string>, string> > entire_doc = call_this_function(filename);
//	cout << "After call_this_function()\n";
//	for(vector< pair< pair<string,string>, string> >::iterator it=entire_doc.begin() ; it!=entire_doc.end() ; ++it)
//	{
//		cout << it->first.first << endl << it->first.second << endl << it->second << endl << endl;
//	}
//	cout << "After printing the call this function\n";
	//for the entire doc , make a bag of words for TF-IDF
	
	int word_count=0;
	for(vector< pair< pair<string,string>, string> >::iterator it=entire_doc.begin() ; it!=entire_doc.end() ; ++it)
	{
		vector<string> v = parse(it->second);
		for(vector<string>::iterator it=v.begin() ; it!=v.end() ; ++it)
		{
			doc_words[*it]++;
			word_count++;
		}
	}
//	for(map<string,int>::iterator it=doc_words.begin() ; it!=doc_words.end() ; ++it)
//		cout << it->first << " " << it->second << endl;
	for(vector< pair< pair<string,string>, string> >::iterator it=entire_doc.begin() ; it!=entire_doc.end() ; ++it)
	{
	//	cout << it->first.first << it->first.second << endl;
		char* str = (char*)it->first.second.c_str();
		lower(str);
		string s = (string)str;
		if(it->first.first.find("label")!=string::npos)
		{
			string label_no = it->first.first.substr(5);
			if(label_no == "-1")
			{
				cout << "Title::" << s << endl << endl;
				continue;
			}

		//	cout << "                   "<<it->first.first.substr(5) << endl;
			//assuming that abstract and keywords will always come before intro and etc. 
			if(s.find("abstract")!=string::npos)
			{
				abs_vec = parse(it->second);
			}
			else if(s.find("keywords")!=string::npos)
			{
				vector<string> temp = parse(it->second);
				for(vector<string>::iterator it=temp.begin() ; it!=temp.end() ; ++it)
				{
					abs_vec.push_back(*it);
				}
			}
			else if(s.find("introduction")!=string::npos || s.find("motivation")!=string::npos||s.find("background")!=string::npos||s.find("problem statement")!=string::npos)
			{
				cout << "Heading::"<<label_no<< s << endl;
			//	cout << "Introduction\n";
				intro_vec = parse_as_sentences(it->second);
				vector<string> v = getIntro(abs_vec,intro_vec,5);
				int i=1;
				for(vector<string>::iterator it=v.begin() ; it!=v.end() ; ++it)
				{
					cout << i++ << " " << *it << endl;
				}
				cout << endl << endl;
			}
			else if(s.find("conclusion")!=string::npos || s.find("future work")!=string::npos)
			{
				//NOTE:: Do you thing we need to summerize this in a different way??
				//as of now using TF_IDF for this as well.
			//	conclu_vec = parse_as_sentences(it->second);
				cout << "Heading::" << label_no<<s << endl;
				vector<string> v = getTopNSentencesTFTDF(it->second,doc_words,word_count,5);
				int i=1;
				for(vector<string>::iterator it=v.begin() ; it!=v.end() ; ++it)
				{
					cout << i++ << " " << *it << endl;
				}
				cout << endl << endl;
			}
			else if(s.find("reference")!=string::npos)
			{
				cout << "Heading::" << label_no <<s << endl;
			//	cout << it->second;
				vector<string> v = getTopN(it->second,5);
				int c=1;
				for(vector<string>::iterator it=v.begin() ; it!=v.end() ; ++it,c++)
					cout << c << " "<<*it << endl;
				cout << endl;
			}
			else
			{
				cout << "Heading::" << label_no <<s << endl;
			//	cout << it->second << endl;
				vector<string> v = getTopNSentencesTFTDF(it->second,doc_words,word_count,5);
				int i=1;
				for(vector<string>::iterator it=v.begin() ; it!=v.end() ; ++it)
				{
					cout << i++ << " " << *it << endl;
				}
				cout << endl << endl;
			}
		}
	}
	return 0;
}
