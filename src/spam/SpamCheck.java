package spam;

import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.MatrixIO;
import javafx.util.Pair;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by Jasmin2332 on 4/20/2016.
 */
public class SpamCheck {

    private File folder;
    private Matrix countVector;
    private String[] words;
    private String[] sender;
    private String[] subject;
    private int spamHamDivider;
    private MailReader mailReader;
    private BufferedReader sqSumOut;
    private Scanner mags;
    private String folderPath;
    private Tokenizer mTokenizer;
    private int length;
    private File queryFile;
    private boolean mode;
    private String result;

    public SpamCheck(File folder) {
        this.folder = folder;
        folderPath = folder.getAbsolutePath();
    }

    public boolean load() {
        try {
            mTokenizer = new Tokenizer();
            mailReader = new MailReader();
            BufferedReader mReader = new BufferedReader(new FileReader(folderPath + "\\words.txt"));
            String file = mReader.readLine();
            words = file.split(" ");
            Arrays.sort(words, Comparator.naturalOrder());
            spamHamDivider = Integer.parseInt(mReader.readLine());
            mReader.close();
            sqSumOut = new BufferedReader(new FileReader(new File(folderPath + "\\elementSqSums.txt")));
            mags = new Scanner(new FileReader(new File(folderPath + "\\elementread.txt")));
            countVector = MatrixIO.readMatrix(new File(folderPath + "\\wordCount.txt"), MatrixIO.Format.CLUTO_DENSE, Matrix.Type.DENSE_ON_DISK);
            mReader = new BufferedReader(new FileReader(folder.getAbsolutePath() + "\\extras.txt"));
            sender = mReader.readLine().split("\t");
            subject = mReader.readLine().split("\t");
            mReader.close();
            length = (int) Math.sqrt(sender.length);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private int findPosWord(String word) {
        return Arrays.binarySearch(words, word, Comparator.naturalOrder());
    }

    private int findPosSubject(String word) {
        return Arrays.binarySearch(subject, word, Comparator.naturalOrder());
    }

    private int findPosSender(String word) {
        return Arrays.binarySearch(sender, word, Comparator.naturalOrder());
    }

    public String queryNB() {
        System.out.println(queryFile);
        double[] queryVector = new double[0];
        try {
            MailData mailData = mailReader.read(queryFile);
            int indexSender = findPosSender(mailData.getSender());
            if (indexSender > -1 && indexSender < spamHamDivider) {
                reset();
                return "Spam";
            }

            String[] subjectTokens = mTokenizer.tokenize(mailData.getSubject(), false);
            Arrays.sort(subjectTokens, Comparator.naturalOrder());

            HashSet<String> hashSet = new HashSet<>();
            List<String> list = new ArrayList<>();  // Contains the intersection

            double similarity = 0.0;
            int index = -1;
            double dist;
            int spamCount = 0;
            int hamCount = 0;

            PriorityQueue<Pair<Integer, Double>> topK = new PriorityQueue<>(new Comparator<Pair<Integer, Double>>() {
                @Override
                public int compare(Pair<Integer, Double> o1, Pair<Integer, Double> o2) {
                    return o1.getValue().compareTo(o2.getValue());
                }
            });


            for (int i = 0, subjectLength = subject.length; i < subjectLength; i++) {
                if (subject[i].equals("Empty")) continue;
                String[] array1 = mTokenizer.tokenize(subject[i], false);
                Collections.addAll(hashSet, array1);
                for (String subjectToken : subjectTokens) {
                    if (hashSet.contains(subjectToken)) {
                        list.add(subjectToken);
                    }
                }
                Collections.addAll(hashSet, subjectTokens);
                dist = list.size() / (hashSet.size());

                index = i;
                topK.add(new Pair<>(i, dist));
                System.out.println(Arrays.toString(array1));
                System.out.println(Arrays.toString(subjectTokens));
                System.out.println(list.size() + "/" + hashSet.size() + ":" + dist + ":" + similarity);
                hashSet.clear();
                list.clear();
            }

            double spamLength = 0.0;
            double hamLength = 0.0;
            if (index > -1) {
                int k = 1;
                for (Pair<Integer, Double> integerDoublePair : topK) {
                    if (k++ > length) break;
                    if (integerDoublePair.getKey() < spamHamDivider) {
                        spamCount++;
                        spamLength += integerDoublePair.getValue();
                    } else {
                        hamCount++;
                        hamLength += integerDoublePair.getValue();
                    }
                }
                if (spamCount * spamLength > hamCount * hamLength) {
                    reset();
                    return "Spam";
                }
            }
            topK.clear();

            String[] mailBody = mailData.getTokens();
            Arrays.sort(mailBody, Comparator.naturalOrder());
            queryVector = new double[words.length];
            Arrays.fill(queryVector, 0.0);
            for (String aMailBody : mailBody) {
                int indexWord = findPosWord(aMailBody);
                if (indexWord != -1) {
                    queryVector[indexWord]++;
                }
            }
            double spam = 1.0;
            double ham = 1.0;
            for (int i = 0; i < queryVector.length; i++) {
                if(queryVector[i]!=0.0) {
                    System.out.println("S: " + countVector.get(i, 0) / ((countVector.get(i, 0) + countVector.get(i, 1))));
                    System.out.println("H: " + countVector.get(i, 1) / ((countVector.get(i, 0) + countVector.get(i, 1))));
                    if(countVector.get(i, 0) != 0.0)
                        spam *= countVector.get(i, 0)/((countVector.get(i, 0)+countVector.get(i, 1)));
                    if(countVector.get(i, 1) != 0.0)
                        ham *= countVector.get(i, 1)/((countVector.get(i, 0)+countVector.get(i, 1)));
                }
            }
            if(spam > ham) return "Spam";
            System.out.println(spam);
            System.out.println(ham);
            //
        } catch (Exception e) {
            return "Error";
        }
        return "Ham";
    }

    public String querySVD() {
        System.out.println(queryFile);
        double[] vector = new double[0];
        double[] queryVector = new double[0];
        try {
            MailData mailData = mailReader.read(queryFile);
            int indexSender = findPosSender(mailData.getSender());
            if (indexSender > -1 && indexSender < spamHamDivider) {
                reset();
                return "Spam";
            }

            String[] subjectTokens = mTokenizer.tokenize(mailData.getSubject(), false);
            Arrays.sort(subjectTokens, Comparator.naturalOrder());

            HashSet<String> hashSet = new HashSet<>();
            List<String> list = new ArrayList<>();  // Contains the intersection

            double similarity = 0.0;
            int index = -1;
            double dist;
            int spamCount = 0;
            int hamCount = 0;

            PriorityQueue<Pair<Integer, Double>> topK = new PriorityQueue<>(new Comparator<Pair<Integer, Double>>() {
                @Override
                public int compare(Pair<Integer, Double> o1, Pair<Integer, Double> o2) {
                    return o1.getValue().compareTo(o2.getValue());
                }
            });


            for (int i = 0, subjectLength = subject.length; i < subjectLength; i++) {
                if (subject[i].equals("Empty")) continue;
                String[] array1 = mTokenizer.tokenize(subject[i], false);
                Collections.addAll(hashSet, array1);
                for (String subjectToken : subjectTokens) {
                    if (hashSet.contains(subjectToken)) {
                        list.add(subjectToken);
                    }
                }
                Collections.addAll(hashSet, subjectTokens);
                dist = list.size() / (hashSet.size());

                index = i;
                topK.add(new Pair<>(i, dist));
                System.out.println(Arrays.toString(array1));
                System.out.println(Arrays.toString(subjectTokens));
                System.out.println(list.size() + "/" + hashSet.size() + ":" + dist + ":" + similarity);
                hashSet.clear();
                list.clear();
            }
            System.out.println(index);
            double spamLength = 0.0;
            double hamLength = 0.0;
            if (index > -1) {
                int k = 1;
                for (Pair<Integer, Double> integerDoublePair : topK) {
                    if (k++ > length) break;
                    if (integerDoublePair.getKey() < spamHamDivider) {
                        spamCount++;
                        spamLength += integerDoublePair.getValue();
                    } else {
                        hamCount++;
                        hamLength += integerDoublePair.getValue();
                    }
                }
                System.out.println(spamCount * spamLength);
                System.out.println(hamCount * hamLength);
                if (spamCount * spamLength > hamCount * hamLength) {
                    reset();
                    return "Spam";
                }
            }
            topK.clear();
            String[] mailBody = mailData.getTokens();
            Arrays.sort(mailBody, Comparator.naturalOrder());
            queryVector = new double[words.length];
            Arrays.fill(queryVector, 0.0);
            double magnitude = 0;
            for (String aMailBody : mailBody) {
                int indexWord = findPosWord(aMailBody);
                if (indexWord != -1) {
                    queryVector[indexWord]++;
                }
            }
            for (double v : queryVector) {
                magnitude += v * v;
            }
            magnitude = Math.sqrt(magnitude);
            double res = 0.0;
            index = -1;
            for (int i = 0; i < 5000; i++) {
                String readLine[] = sqSumOut.readLine().split(" ");
                vector = new double[readLine.length];
                for (int i1 = 0, readLineLength = readLine.length; i1 < readLineLength; i1++) {
                    vector[i1] = Double.parseDouble(readLine[i1]);
                }
                for (int j = 0, queryVectorLength = queryVector.length; j < queryVectorLength; j++) {
                    res = res + queryVector[j] * vector[j];
                }
                res = res / (magnitude * mags.nextDouble());
                System.out.println(i + " : " + res);
                index = i;
                topK.add(new Pair<>(i, res));
                res = 0.0;
            }
            System.out.println(index);
            spamCount = 0;
            hamCount = 0;
            spamLength = 0;
            hamLength = 0;
            if (index > -1) {
                int k = 1;
                for (Pair<Integer, Double> integerDoublePair : topK) {
                    if (k++ > length) break;
                    if (integerDoublePair.getKey() < spamHamDivider) {
                        spamCount++;
                        spamLength += integerDoublePair.getValue();
                    } else {
                        hamCount++;
                        hamLength += integerDoublePair.getValue();
                    }
                }
                System.out.println(spamCount * spamLength);
                System.out.println(hamCount * hamLength);
                if (spamCount * spamLength > hamCount * hamLength) {
                    reset();
                    return "Spam";
                }
                topK.clear();
            }
        } catch (Exception e) {
            reset();
            System.out.println(queryVector.length);
            System.out.println(vector.length);

        }
        reset();
        return "Ham";
    }

    private void reset() {
        try {
            sqSumOut = new BufferedReader(new FileReader(new File(folderPath + "\\elementSqSums.txt")));
            mags = new Scanner(new FileReader(new File(folderPath + "\\elementread.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        if(mode) result = queryNB();
        else result = querySVD();
    }

    public void setQueryFile(File queryFile) {
        this.queryFile = queryFile;
    }

    public void setMode(boolean mode) {
        this.mode = mode;
    }

    public String getResult() {
        return result;
    }
}

