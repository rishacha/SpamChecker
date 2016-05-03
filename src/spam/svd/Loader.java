package spam.svd;

import spam.Trainer;

import java.io.File;
import java.io.IOException;

class Loader {
//C:\Users\Jasmin2332\IdeaProjects\SideProject\trainingdata.txt

    public static void main(String[] args) {
        try {
            //new Sorter("C:\\Users\\Jasmin2332\\Downloads\\MC\\trec07p\\trec07p\\trec07p\\data");
            //new Indexer("C:\\Users\\Jasmin2332\\Downloads\\MC\\trec07p\\trec07p\\trec07p\\data");
            load("C:\\Users\\Jasmin2332\\Downloads\\MC\\trec07p\\trec07p\\trec07p\\data");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void load(String folderPath) throws IOException {
        System.out.println("load");
        File mSpamFolder = new File(folderPath+"\\spam");
        File mHamFolder = new File(folderPath+"\\ham");
        if(!mSpamFolder.isDirectory() || !mHamFolder.isDirectory()) return;
        System.out.println("start");
        File[] mSpamFiles = mSpamFolder.listFiles(File::isFile);
        System.out.println("start");
        File[] mHamFiles = mHamFolder.listFiles(File::isFile);
        System.out.println("start");
        System.out.println("Trainer");
        Trainer mTrainer = new Trainer(mSpamFiles, mHamFiles);
        mTrainer.train();
        mSpamFiles = null;
        mHamFiles = null;
        System.gc();
    }
}
