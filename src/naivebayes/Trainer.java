package naivebayes;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jasmin2332 on 3/18/2016.
 */

class Trainer {
    private List<WordEntry> mData = new ArrayList<>();

    Trainer(List<WordEntry> trainedData) {
        mData.addAll(trainedData);
    }

    Trainer(File mTrainedFile) {
        try(BufferedReader mLineReader = new BufferedReader(new FileReader(mTrainedFile))) {
            String mLine = mLineReader.readLine();
            while(mLine!=null) {
                mData.add(new WordEntry(mLine.split(",")));
                mLine = mLineReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void addData(List<WordEntry> trainedData) {
        for (WordEntry aTrainedData : trainedData) {
            int index = mData.indexOf(aTrainedData);
            if (index != -1) {
                mData.get(index).upSpam(aTrainedData.getSpamCount());
                mData.get(index).upHam(aTrainedData.getHamCount());
            } else {
                mData.add(aTrainedData);
            }
        }
    }

    List<WordEntry> getData() {
        return mData;
    }

    void train(String[] words, boolean isSpam) {
        for (String word : words) {
            WordEntry mTemp = new WordEntry(word, 0, 0);
            int index = mData.indexOf(mTemp);
            if (index != -1) {
                if(isSpam)
                    mData.get(index).upSpam(1);
                else mData.get(index).upHam(1);
            } else {
                if(isSpam)
                    mTemp.upSpam(1);
                else mTemp.upHam(1);
                mData.add(mTemp);
            }
        }
        System.out.print(mData);
    }
}
