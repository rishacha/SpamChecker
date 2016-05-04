package spam;

import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.MatrixIO;
import edu.ucla.sspace.matrix.OnDiskMatrix;

import java.io.File;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by Jasmin2332 on 3/18/2016.
 */

public class Trainer {
    private File[] spamFiles;
    private File[] hamFiles;
    private String folder;
    private String[] words;
    private List<String> mUniqueWords = new ArrayList<>();

    public Trainer(File[] spamFiles, File[] hamFiles, String folder) {
        this.spamFiles = spamFiles;
        this.hamFiles = hamFiles;
        this.folder = folder;
    }

    public void train() {
        try {
            getUniqueWords(spamFiles);
            getUniqueWords(hamFiles);
            int mWordCount = mUniqueWords.size();
            Collections.sort(mUniqueWords, Comparator.naturalOrder());
            words = mUniqueWords.toArray(new String[mUniqueWords.size()]);
            int totalFiles = spamFiles.length + hamFiles.length;
            String[] senders = new String[totalFiles];
            String[] subjects = new String[totalFiles];
            mUniqueWords.clear();
            mUniqueWords = null;
            System.gc();
            System.out.println(mWordCount);
            Matrix termDocMatrix = new OnDiskMatrix(mWordCount, totalFiles);
            int rows = termDocMatrix.rows();
            int columns = termDocMatrix.columns();
            for (int i = 0; i < rows; i++) {
                for (int i1 = 0; i1 < columns; i1++) {
                    termDocMatrix.set(i, i1, 0.0d);
                }
            }

            MailReader mMailReader = new MailReader(new File(folder));
            MailData mailData;
            int k=0;
            for (int i = 0, spamFilesLength = spamFiles.length; i < spamFilesLength; i++) {
                File mFile = spamFiles[i];
                try {
                    mailData = mMailReader.read(mFile);
                    senders[k] = mailData.getSender();
                    subjects[k++] = mailData.getSubject();
                    String[] mWords = mailData.getTokens();
                    for (String mWord : mWords) {
                        int index = findPos(mWord);
                        termDocMatrix.set(index, i, Double.sum(termDocMatrix.get(index, i), 1.0d));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            int spamFileLength = spamFiles.length;
            for (int i = spamFileLength; i < totalFiles; i++) {
                File mFile = hamFiles[i-spamFileLength];
                try {
                    mailData = mMailReader.read(mFile);
                    senders[k] = mailData.getSender();
                    subjects[k++] = mailData.getSubject();
                    String[] mWords = mailData.getTokens();
                    for (String mWord : mWords) {
                        int index = findPos(mWord);
                        termDocMatrix.set(index, i, Double.sum(termDocMatrix.get(index, i), 1.0d));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
            }

            File mFile = new File("C:\\Users\\Jasmin2332\\Downloads\\MC\\trec07p\\trec07p\\trec07p\\data\\words.txt");
            PrintWriter writer = new PrintWriter(mFile);
            for (String word : words) {
                writer.print(word + " ");
            }
            writer.println();
            writer.println(spamFileLength);
            writer.close();
            writer = new PrintWriter(new File("C:\\Users\\Jasmin2332\\Downloads\\MC\\trec07p\\trec07p\\trec07p\\data\\wordCount.txt"));
            writer.println(words.length+" "+ 2);
            int j;
            int docNum=0;
            File tfExtras = new File("C:\\Users\\Jasmin2332\\Downloads\\MC\\trec07p\\trec07p\\trec07p\\data\\tfdfCount.txt");
            PrintWriter writerExtra = new PrintWriter(tfExtras);
            for (int i = 0; i < rows; i++) {
                double total = 0.0;
                for (j = 0; j < spamFiles.length; j++) {
                    double v = termDocMatrix.get(i, j);
                    if(v>0.0d) {
                        docNum++;
                        total++;
                    }
                }
                writer.print(total + " ");
                total = 0.0;
                for (; j < totalFiles; j++) {
                    double v = termDocMatrix.get(i, j);
                    if(v>0.0d) {
                        docNum++;
                        total++;
                    }
                }
                writerExtra.print(docNum+ " ");
                docNum = 0;
                writer.println(total);
            }
            writerExtra.close();
            writer.close();
            writer = new PrintWriter(new File("C:\\Users\\Jasmin2332\\Downloads\\MC\\trec07p\\trec07p\\trec07p\\data\\extras.txt"));
            for (String sender : senders) {
                if(sender.equals(""))
                    writer.print("Empty"+"\t");
                else
                writer.print(sender + "\t");
            }
            writer.print("\n");
            for (String subject : subjects) {
                if(subject.equals(""))
                    writer.print("Empty"+"\t");
                else

                    writer.print(subject.trim() + "\t");
            }
            writer.close();
            Scanner tfReader = new Scanner(tfExtras);

            File matrixOut = new File("C:\\Users\\Jasmin2332\\Downloads\\MC\\trec07p\\trec07p\\trec07p\\data\\matrix.txt");
            for (int i = 0; i < termDocMatrix.rows(); i++) {
                int idf = tfReader.nextInt();
                for (int l = 0; l < termDocMatrix.columns(); l++) {
                 double temp = termDocMatrix.get(i, l);
                    if(temp>0.0) {
                        termDocMatrix.set(i, l, (1 + Math.log(temp))*Math.log(totalFiles/idf) );
                    }
                }
            }
            tfReader.close();
            MatrixIO.writeMatrix(termDocMatrix, matrixOut , MatrixIO.Format.SVDLIBC_SPARSE_TEXT);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private int findPos(String mWord) {
        return Arrays.binarySearch(words, mWord, Comparator.naturalOrder());
    }

    private void getUniqueWords(File[] files) {
        MailReader mMailReader = new MailReader(new File(folder));
        for (File file : files) {
            try {
                String[] tokens = mMailReader.read(file).getTokens();
                for (String token : tokens) {
                    if (!mUniqueWords.contains(token)) mUniqueWords.add(token);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-2);
            }
        }
    }
}
