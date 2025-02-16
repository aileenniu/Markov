import java.util.*;

public class HashMarkov implements MarkovInterface {

    protected String[] myWords;		// Training text split into array of words 
	protected Random myRandom;		// Random number generator
	protected int myOrder;			// Length of WordGrams used
	protected static String END_OF_TEXT = "*** ERROR ***"; 
    HashMap<WordGram, List<String>> myMap = new HashMap<>();

    /**
	 * Default constructor creates order 3 model
	 */
	//public HashMarkov() {
		//this(3);
	//}

    public HashMarkov(int order){
		myOrder = order;
		myRandom = new Random();
        myMap = new HashMap<WordGram, List<String>>();        
	} 
    /*
     * @Override
    public void setTraining(String text) {
        // TODO Auto-generated method stub
        myWords = text.split("\\s+");
        myMap.clear();
        WordGram myGram = new WordGram(myWords, 0, myOrder);
        for (int i = myOrder; i< myWords.length;i++) {
            if (!myMap.containsKey(myGram)) {
                myMap.put(myGram, new ArrayList<>());
            }
            myMap.get(myGram).add(myWords[i]);

            myGram = myGram.shiftAdd(myWords[i]);
        }
    }
     */
    @Override
    public void setTraining(String text) {
        myWords = text.split("\\s+");
        myMap.clear();
        WordGram wg = new WordGram(myWords,0,myOrder);
        for(int k = myOrder; k<myWords.length; k++){
            if(myMap.containsKey(wg)){
                myMap.get(wg).add(myWords[k]);
            }else{
                ArrayList<String> following = new ArrayList<>();
                following.add(myWords[k]);
                myMap.put(wg, following);
            }
            wg = wg.shiftAdd(myWords[k]);
        }
    }


    @Override
    public List<String> getFollows(WordGram wgram) {
        // TODO Auto-generated method stub

		// first WordGram is at the beginning of myWords
		//WordGram currentWG = new WordGram(myWords,0,wgram.length());
		if (myMap.containsKey(wgram)) {
            return myMap.get(wgram);
        }
        else {
            List<String> follows = new ArrayList<>();
            return follows;
        }
    }
			// shift word gram by one, add currentWord
			//currentWG = currentWG.shiftAdd(currentWord)}
    
    public String getRandomText(int length){
		ArrayList<String> randomWords = new ArrayList<>(length);
		int index = myRandom.nextInt(myWords.length - myOrder + 1);
		WordGram current = new WordGram(myWords,index,myOrder);
		randomWords.add(current.toString());
        //randomWords.addAll(getFollows(current));
        for(int k=0; k < length-myOrder; k += 1) {
			String nextWord = getNextWord(current);
            if (nextWord.equals(END_OF_TEXT)) {
				break;
			}
			randomWords.add(nextWord);
			current = current.shiftAdd(nextWord);
		}
		return String.join(" ", randomWords);
	}

    private String getNextWord(WordGram wgram) {
		List<String> follows = getFollows(wgram);
		if (follows.size() == 0) {
			return END_OF_TEXT;
		}
		else {
			int randomIndex = myRandom.nextInt(follows.size());
			return follows.get(randomIndex);
		}
	}

    @Override
    public int getOrder() {
        // TODO Auto-generated method stub
        return myOrder;
    }

    @Override
    public void setSeed(long seed) {
        // TODO Auto-generated method stub
        myRandom.setSeed(seed);
    }
    
}
