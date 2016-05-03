package spam.svd;

import spam.NB.topSorter;
import spam.SpamCheck;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Jasmin2332 on 4/23/2016.
 */
class Tester {
    public static void main(String[] args) {
        SpamCheck spamCheck;
        try {
            File file = new File("C:\\Users\\Jasmin2332\\Downloads\\MC\\trec07p\\trec07p\\trec07p\\data\\spam");
            File[] files = file.listFiles(File::isFile);
            Arrays.sort(files, Comparator.naturalOrder());
            int spam=0;
            int ham=0;
            int err=0;
            PrintWriter mWriter = new PrintWriter("C:\\Users\\Jasmin2332\\Downloads\\MC\\trec07p\\trec07p\\trec07p\\data\\NBBest.txt");
            for(int i=7000; i < 15000; i+=1000) {
                topSorter.main(i);
                spamCheck = new SpamCheck(new File("C:\\Users\\Jasmin2332\\Downloads\\MC\\trec07p\\trec07p\\trec07p\\data"));
                spamCheck.load();
                for (int i1 = 0, filesLength = 2000; i1 < filesLength; i1++) {
                    File file1 = files[i1];
                    spamCheck.setQueryFile(file1);
                    spamCheck.setMode(true);
                    spamCheck.run();
                    String result = spamCheck.getResult();
                    switch (result) {
                        case "Spam":
                            spam++;
                            break;
                        case "Ham":
                            ham++;
                            break;
                        default:
                            err++;
                            break;
                    }
                }
                System.out.println("Spam"+spam);
                System.out.println("Ham"+ham);
                mWriter.write(spam + ":"+ ham + ":" +err);
                spam=0;
                ham=0;
            }
            mWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
