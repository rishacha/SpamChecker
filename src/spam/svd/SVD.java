package spam.svd;

import edu.ucla.sspace.matrix.Matrices;
import edu.ucla.sspace.matrix.Matrix;
import edu.ucla.sspace.matrix.MatrixIO;
import edu.ucla.sspace.matrix.OnDiskMatrix;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Jasmin2332 on 4/23/2016.
 */

class SVD {
    static Matrix x = null;
    public static void main(String[] args) {
        trunSVD();
    }
    @SuppressWarnings("deprecation")
    static void trunSVD() {
        try {
            File u = new File("C:\\Users\\Jasmin2332\\Downloads\\MC\\trec07p\\trec07p\\trec07p\\data\\matrixU");
            File vt = new File("C:\\Users\\Jasmin2332\\Downloads\\MC\\trec07p\\trec07p\\trec07p\\data\\matrix-Vt");
            Matrix U = MatrixIO.readMatrix(u, MatrixIO.Format.SVDLIBC_DENSE_TEXT, Matrix.Type.DENSE_ON_DISK);
            System.out.println("u");
            Matrix Vt = MatrixIO.readMatrix(vt, MatrixIO.Format.SVDLIBC_DENSE_TEXT, Matrix.Type.DENSE_ON_DISK);
            System.out.println("vt");
            Scanner mReader = new Scanner(new File("C:\\Users\\Jasmin2332\\Downloads\\MC\\trec07p\\trec07p\\trec07p\\data\\matrix-S"));
            Matrix S = new OnDiskMatrix(U.columns(), mReader.nextInt());
            for(int i = 0; i < S.rows(); i++) {
                for(int j = 0; j < S.columns(); j++) {
                    if(i==j) S.set(i,j,mReader.nextDouble());
                    else S.set(i,j,0);
                }
            }
            System.out.println("s");
            x = Matrices.multiply(Matrices.multiply(U, S), Vt);
            System.out.println("m");
            x = Matrices.transpose(x);
            System.out.println("t");
            File sqSumOut = new File("C:\\Users\\Jasmin2332\\Downloads\\MC\\trec07p\\trec07p\\trec07p\\data\\elementSqSums.txt");
            File readAble = new File("C:\\Users\\Jasmin2332\\Downloads\\MC\\trec07p\\trec07p\\trec07p\\data\\elementread.txt");
            ObjectOutputStream mWriter = new ObjectOutputStream(new FileOutputStream(sqSumOut));
            PrintWriter mWriterText = new PrintWriter(readAble);
            double magnitude = 0.0d;
            double[] rowVector = new double[x.columns()];
            for (int i = 0; i < x.rows(); i++) {
                System.out.println(i);
                for (int j = 0; j < x.columns(); j++) {
                    magnitude += Math.pow(x.get(i, j), 2);
                }
                magnitude = Math.sqrt(magnitude);
                for (int j = 0; j < x.columns(); j++) {
                    rowVector[j] = (x.get(i,j))/(magnitude);
                }
                magnitude = 0.0d;
                mWriterText.println(Arrays.toString(rowVector));
                mWriter.writeObject(rowVector);
            }
            mWriterText.close();
            mWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
