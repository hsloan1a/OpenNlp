package com.mybuild.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mybuild.model.NLPCatagory;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

@RestController
public class NLPController {

    private static final String template = "%s";
    private final AtomicLong counter = new AtomicLong();
    DoccatModel model;
    
    @Autowired
    private ResourceLoader resourceLoader;

    public NLPController() {
    	InputStream dataIn = null;
		try {
			//dataIn = combineFilesForNewInputStream();
			dataIn = new ClassPathResource("tweets.txt").getInputStream();
			//dataIn = new ClassPathResource("combined.txt").getInputStream();
			ObjectStream lineStream = new PlainTextByLineStream(dataIn, "UTF-8");
			ObjectStream sampleStream = new DocumentSampleStream(lineStream);
			// Specifies the minimum number of times a feature must be seen
			int cutoff = 2;
			int trainingIterations = 1000;
			model = DocumentCategorizerME.train("en", sampleStream, cutoff,
					trainingIterations);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (dataIn != null) {
				try {
					dataIn.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
    
    private InputStream combineFilesForNewInputStream() throws IOException {
    	//get first file dataset
    	InputStream dataIn = new ClassPathResource("datasetSentences.txt").getInputStream();
    	//get sencond file sentiment
    	InputStream sentIn = new ClassPathResource("sentiment_labels.txt").getInputStream();
    	//create buffer for return
    	StringBuilder stringBuilder = new StringBuilder();
    	String line;
    	//need two arrays to load files into......
    	List <String> mySentString = new ArrayList<String>();
    	mySentString = getStringsFileInputStream(sentIn);
    	List <String> myDataString = new ArrayList<String>();
    	myDataString = getStringsFileInputStream(dataIn);
    	String[] sentSplit;
    	String[] dataSplit;

    	for (String myString : mySentString){
    		sentSplit = null;
            sentSplit = myString.split("\\|", 2);
//        	System.out.println(sentSplit.length);
//        	System.out.println(sentSplit[0]);
//        	System.out.println(myString);
    		for (String myData : myDataString){
    			dataSplit = null;
                dataSplit = myData.split("\t");
            	System.out.println(sentSplit[0] + ":" + dataSplit[0]);
                if ((Integer.parseInt(sentSplit[0])+1) == Integer.parseInt(dataSplit[0])){
                	System.out.println(sentSplit[1] + " " + dataSplit[1]);
                	stringBuilder.append(sentSplit[1] + " " + dataSplit[1]);
                	stringBuilder.append("\n");
                	break;
                }
                else if ((Integer.parseInt(sentSplit[0])+1) < Integer.parseInt(dataSplit[0])){
                	System.out.println(sentSplit[0] + ":less:" + dataSplit[0]);
                	
                }
                	
    			
    		}
    	}
    	
    	File file = new File("combined.txt");
    	BufferedWriter writer = null;
    	try {
    	    writer = new BufferedWriter(new FileWriter(file));
    	    writer.write(stringBuilder.toString());
    	} finally {
    	    if (writer != null) writer.close();
    	}
    	
		return new ByteArrayInputStream(stringBuilder.toString().getBytes(StandardCharsets.UTF_8));
	}

	private List<String> getStringsFileInputStream(InputStream inputStream) throws IOException {
    	BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( inputStream ) );
    	List <String> mySplitString = new ArrayList<String>();
    	
    	String line;
    	while( (line = bufferedReader.readLine()) != null )
        {
    		mySplitString.add(line);
        }
    	return mySplitString;
	}

	@RequestMapping("/testSentence")
    public NLPCatagory nlpCatagory(@RequestParam(value="sentence", defaultValue="") String sentence) {
    	DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);
		double[] outcomes = myCategorizer.categorize(sentence);
		String category = myCategorizer.getBestCategory(outcomes);
		
		System.out.println(category);
		if (category.equalsIgnoreCase("1")) {
			System.out.println("The sentence is positive :) ");
		} else {
			System.out.println("The sentence is negative :( ");
		}
        return new NLPCatagory(counter.incrementAndGet(),
                String.format(template, sentence), category);
    }
}
