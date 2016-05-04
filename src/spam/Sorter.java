package spam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Created by Jasmin2332 on 4/17/2016.
 */
public class Sorter {

    public Sorter(String path) throws IOException {
        File mFolder = new File(path);
        if(mFolder.isDirectory()) {
            File index = new File(path+"\\index.txt");
            if(index.isFile()) {
                System.out.println(new File(path + "\\ham").mkdirs());
                System.out.println(new File(path + "\\spam").mkdirs());
                BufferedReader mReader = new BufferedReader(new FileReader(index));
                String read = mReader.readLine();
                while (read!=null) {
                    String[] parts = read.split(" ");
                    if(parts.length==2) {
                        File file = new File(mFolder, parts[1]);
                        if(file.exists()) {
                            if(parts[0].equals("ham")||parts[0].equals("spam")) {
                                Path dest = new File(mFolder, parts[0]+"\\"+parts[1]).toPath();
                                Path source= new File(path, parts[1]).toPath();
                                System.out.println(source+" "+dest);
                                Files.move(source, dest, REPLACE_EXISTING );
                            }
                        }
                    }
                    read = mReader.readLine();
                }
                mReader.close();
            }
        }
    }
}
