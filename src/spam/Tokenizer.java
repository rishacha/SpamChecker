package spam;

import edu.ucla.sspace.text.IteratorFactory;

import java.io.File;
import java.util.*;

/**
 * Created by Jasmin2332 on 4/17/2016.
 */
class Tokenizer {
    private List<String> tokens;
    private List<String> stopWords;
    Tokenizer(File folder) {
        tokens = new ArrayList<>();
        stopWords = new ArrayList<>();
        Scanner reader = new Scanner(folder.getAbsolutePath()+"\\stopwords.txt");
        while(reader.hasNext()) {
            stopWords.add(reader.nextLine());
        }
    }
    String[] tokenize(String text, boolean words) {
        if(text.equals("")) return new String[] {""};
        if(words) {
            try {
                StringBuilder mToken = new StringBuilder();
                String[] lines = text.split("\n");
                for (String mLine : lines) {
                    char[] line = mLine.toCharArray();
                    for (int i = 0, lineLength = line.length; i < lineLength; i++) {
                        char next = line[i];
                        if (!Character.isAlphabetic(next)) {
                            if (mToken.length() == 1) mToken.setLength(0);
                            if (mToken.length() > 1) {
                                add(mToken.toString().toLowerCase());
                                mToken.setLength(0);
                            }
                        } else {
                            if (mToken.length() == 0) {
                                if (Character.isAlphabetic(next))
                                    mToken.append(next);
                            } else {
                                if (Character.isAlphabetic(next)) {
                                    if (Character.isAlphabetic(mToken.charAt(mToken.length() - 1))) {
                                        mToken.append(next);
                                    } else {
                                        if (mToken.length() == 1) mToken.setLength(0);
                                        if (mToken.length() > 0) {
                                            add(mToken.toString().toLowerCase());
                                            mToken.setLength(0);
                                        }
                                    }
                                } else {
                                    if (mToken.length() == 1) mToken.setLength(0);
                                    if (mToken.length() > 0) {
                                        add(mToken.toString().toLowerCase());
                                        mToken.setLength(0);
                                        i--;
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-3);
            }
        }
        else {
            Iterator<String> tokenize = IteratorFactory.tokenize(text);
            while (tokenize.hasNext()) add(tokenize.next());
        }
        if (tokens.isEmpty()) add("");
        String[] tokenize = tokens.toArray(new String[tokens.size()]);
        for (int i = 0; i < tokenize.length; i++) {
            tokenize[i] = tokenize[i].trim().toLowerCase();
        }
        Arrays.sort(tokenize, Comparator.naturalOrder());
        reset();
        return tokenize;
    }

    private void add(String token) {
        if(token.length()!=1) {
            if(!isStopWord(token))
                tokens.add(token);
        }
    }

    private boolean isStopWord(String token) {
        return stopWords.contains(token);
    }

    private void reset() {
        tokens.clear();
    }
}