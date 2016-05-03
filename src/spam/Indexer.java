package spam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Jasmin2332 on 4/19/2016.
 */
class Indexer {
    public Indexer(String path) throws FileNotFoundException {
        File[] spam = new File(path+"\\spam").listFiles(File::isFile);
        File[] ham = new File(path+"\\ham").listFiles(File::isFile);
        File index = new File(path+"\\index.txt");
        System.out.println("indexer");
        List<String> fileNames = new ArrayList<>();

        Comparator<String> cmp = new Comparator<String>() {
            public int compare(String o1, String o2) {
                return new Integer(o1.substring(o1.startsWith("s") ? 12:11)).compareTo(new Integer(o2.substring(o2.startsWith("s") ? 12:11)));
            }
        };

        PrintWriter mWriter = new PrintWriter(index);

        for (File file : spam) {
            fileNames.add("spam "+file.getName());
        }
        for (File file : ham) {
            fileNames.add("ham "+file.getName());
        }
        Collections.sort(fileNames, cmp);
        fileNames.forEach(mWriter::println);
        mWriter.close();
    }
}
