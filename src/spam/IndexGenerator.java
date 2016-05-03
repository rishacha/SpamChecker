package spam;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Jasmin2332 on 4/23/2016.
 */
class IndexGenerator {
    void generateIndex(String path) {
        try {
            File[] spam = new File(path+"\\spam").listFiles(File::isFile);
            File[] ham = new File(path+"\\ham").listFiles(File::isFile);
            File indexSpam = new File(path+"\\indexSpam.txt");
            File indexHam = new File(path+"\\indexHam.txt");
            System.out.println("indexer");
            List<String> fileNames = new ArrayList<>();

            Comparator<String> cmp = new Comparator<String>() {
                public int compare(String o1, String o2) {
                    return new Integer(o1.substring(o1.startsWith("s") ? 12:11)).compareTo(new Integer(o2.substring(o2.startsWith("s") ? 12:11)));
                }
            };

            PrintWriter mWriter = new PrintWriter(indexSpam);

            for (File file : spam) {
                fileNames.add(file.getName());
            }

            Collections.sort(fileNames, cmp);
            fileNames.forEach(mWriter::println);
            fileNames.clear();

            mWriter = new PrintWriter(indexSpam);
            for (File file : ham) {
                fileNames.add(file.getName());
            }

            Collections.sort(fileNames, cmp);
            fileNames.forEach(mWriter::println);
            fileNames.clear();

            mWriter.close();
    }catch (Exception e){
            e.printStackTrace();
        }
    }
}
