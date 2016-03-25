package naivebayes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by Jasmin2332 on 3/18/2016.
 */

public class SpamCheck {
    private List<WordEntry> mData = new ArrayList<>();

    public void trainManually(String text, boolean isSpam) {
        Trainer mTrainer = new Trainer(mData);
        text=text.replaceAll("\\n", " ");
        text=text.replaceAll("\\r", " ");
        Set<String> mQuery = new HashSet<>();
        String mTempSplit[] = text.split("( |!|\\[|,|\\?|\\.|_|'|@|\\+|\\])+");
        Collections.addAll(mQuery, mTempSplit);
        String mFinalQuery[] = new String[mQuery.size()];
        mFinalQuery = mQuery.toArray(mFinalQuery);
        System.out.print(Arrays.toString(mFinalQuery));
        mTrainer.train(mFinalQuery, isSpam);
        refresh(mTrainer);
        System.out.println(mData);
    }

    public void trainWithFile(File mDataFile) {
        Trainer mTrainer = new Trainer(mDataFile);
        mTrainer.addData(mData);
        refresh(mTrainer);
    }

    public String query(String query) {
        query=query.replaceAll("\\n", " ");
        query=query.replaceAll("\\r", " ");
        Set<String> mQuery = new HashSet<>();
        String mTempSplit[] = query.split("( |!|\\[|,|\\?|\\.|_|'|@|\\+|\\])+");
        Collections.addAll(mQuery, mTempSplit);
        String mFinalQuery[] = new String[mQuery.size()];
        mFinalQuery = mQuery.toArray(mFinalQuery);
        return query(mFinalQuery);
    }

    private String query(String[] query) {
        float spam=1.0f;
        float ham=1.0f;
        Set<String> mQuery = new HashSet<>();
        Collections.addAll(mQuery, query);
        query = new String[mQuery.size()];
        query = mQuery.toArray(query);

        for (String aQuery : query) {
            if(aQuery.length()>1) {
                // TODO : Update formula
                try {
                    spam *= mData.get(mData.indexOf(new WordEntry(aQuery, 0, 0))).getSpamProb();
                    ham *= mData.get(mData.indexOf(new WordEntry(aQuery, 0, 0))).getHamProb();
                }
                catch (Exception ignored) {}
            }
        }
        if(spam>ham) {
            return  "Spam. Oink Oink.";
        }
        else {
            return "Ham. Should I make a burger now?";
        }
    }

    private void refresh(Trainer mTrainer) {
        mData.clear();
        mData.addAll(mTrainer.getData());
    }

    public boolean isReady() {
        return !mData.isEmpty();
    }

    public boolean saveData(String text) {
        if(mData.isEmpty()) return false;
        try(PrintWriter mWriter = new PrintWriter(new File(text))) {
            System.out.print(mData);
            mData.forEach(mWriter::println);
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }
}
