package spam.NB;

import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.MatrixIO;
import edu.ucla.sspace.matrix.OnDiskMatrix;
import javafx.util.Pair;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Jasmin2332 on 5/3/2016.
 */
public class topSorter {
    private Matrix countVector;
    private Matrix out;

    public static void main(String[] args) {
        main(11000);
    }
    public static void main(int args) {
        new topSorter().train(11000);
    }

    private void train(int arg) {
        try {
            int k=arg;
            countVector = MatrixIO.readMatrix(new File("C:\\Users\\Jasmin2332\\Downloads\\MC\\trec07p\\trec07p\\trec07p\\data" + "\\wordCount.txt"), MatrixIO.Format.CLUTO_DENSE, Matrix.Type.DENSE_ON_DISK);
            out = new OnDiskMatrix(countVector.rows(), countVector.columns());
            for (int i = 0; i < countVector.rows(); i++) {
                out.set(i, 0, (countVector.get(i,0))/(countVector.get(i,0)+countVector.get(i,1)));
                out.set(i, 1, (countVector.get(i,1))/(countVector.get(i,0)+countVector.get(i,1)));
            }

            /*
            for (int i = 0; i < countVector.rows(); i++) {
                if(countVector.get(i, 0) > 1.0 || countVector.get(i, 1) > 1.0) {
                    k++;
                }
            }
            */
            Comparator<Pair<Integer, Double>> comp = (o1, o2) -> o1.getValue().compareTo(o2.getValue());
            List<Pair<Integer, Double>> topk = new ArrayList<>();
            for (int i = 0; i < countVector.rows(); i++) {
                if(topk.size()<k) {
                    topk.add(new Pair<>(i, out.get(i, 0)));
                }
                else {
                    Collections.sort(topk, Collections.reverseOrder(comp));
                    topk.remove(0);
                    topk.add(new Pair<>(i, out.get(i, 0)));
                }
            }
            Collections.sort(topk, comp);
            Set<Integer> index = topk.stream().map(Pair::getKey).collect(Collectors.toSet());
            topk.clear();
            for (int i = 0; i < countVector.rows(); i++) {
                if(topk.size()<k) {
                    topk.add(new Pair<>(i, out.get(i, 1)));
                }
                else {
                    Collections.sort(topk, Collections.reverseOrder(comp));
                    topk.remove(0);
                    topk.add(new Pair<>(i, out.get(i, 1)));
                }
            }
            Collections.sort(topk, comp);
            index.addAll(topk.stream().map(Pair::getKey).collect(Collectors.toList()));
            for (int i = 0; i < out.rows(); i++) {
                for (int i1 = 0; i1 < out.columns(); i1++) {
                    out.set(i, i1, 0.0);
                }
            }
            index.forEach(integer -> {
                out.set(integer, 0, countVector.get(integer, 0));
                out.set(integer, 1, countVector.get(integer, 1));
            });
            MatrixIO.writeMatrix(out, new File("C:\\Users\\Jasmin2332\\Downloads\\MC\\trec07p\\trec07p\\trec07p\\data" + "\\wordCountTOP.txt"), MatrixIO.Format.CLUTO_DENSE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
