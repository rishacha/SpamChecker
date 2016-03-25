package naivebayes;

/**
 * Created by Jasmin2332 on 3/18/2016.
 */

class WordEntry {
    private String word;
    private int spamCount;
    private int hamCount;

    public WordEntry(String[] entry) {
        try {
            word = entry[0];
            spamCount = Integer.parseInt(entry[1]);
            hamCount = Integer.parseInt(entry[2]);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public WordEntry(String word, int spamCount, int hamCount) {
        try {
            this.word = word;
            this.spamCount = spamCount;
            this.hamCount = hamCount;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        return word+","+ spamCount +","+ hamCount;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof WordEntry)) return false;

        WordEntry that = (WordEntry) obj;

        return getWord().equalsIgnoreCase(that.getWord());
    }

    public void upSpam(int spamCount) {
        this.spamCount+=spamCount;
    }

    public void upHam(int hamCount) {
        this.hamCount+=hamCount;
    }

    public int getSpamCount() {
        return spamCount;
    }

    public int getHamCount() {
        return hamCount;
    }

    public float getSpamProb() {
        return 1.0f*getSpamCount()/(getSpamCount()+getHamCount());
    }

    public float getHamProb() {
        return 1.0f*getHamCount()/(getSpamCount()+getHamCount());
    }

    public String getWord() { return word; }
}