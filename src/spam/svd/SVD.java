package spam.svd;

import edu.ucla.sspace.matrix.*;

import java.io.File;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Jasmin2332 on 4/23/2016.
 */
class SVD {
    public static void main1(String[] args) {
        try {
            File u = new File("C:\\Users\\Jasmin2332\\Downloads\\MC\\trec07p\\trec07p\\trec07p\\data\\matrixU");
            File vt = new File("C:\\Users\\Jasmin2332\\Downloads\\MC\\trec07p\\trec07p\\trec07p\\data\\matrix-Vt");
            Matrix U = MatrixIO.readMatrix(u, MatrixIO.Format.SVDLIBC_DENSE_TEXT, Matrix.Type.DENSE_ON_DISK);
            System.out.println("u");
            Matrix Vt = MatrixIO.readMatrix(vt, MatrixIO.Format.SVDLIBC_DENSE_TEXT, Matrix.Type.DENSE_ON_DISK);
            System.out.println("vt");
            Scanner mReader = new Scanner(new File("C:\\Users\\Jasmin2332\\Downloads\\MC\\trec07p\\trec07p\\trec07p\\data\\matrix-S"));
            double[] diag = new double[mReader.nextInt()];
            int k = 0;
            while (mReader.hasNext()) {
                diag[k++]=mReader.nextDouble();
            }
            DiagonalMatrix S = new DiagonalMatrix(diag);
            System.out.println("s");
            U = Matrices.multiply(U,S);
            System.out.println("m");
            int u_rows =  U.rows();
            int vt_cols = Vt.columns();
            Matrix resultMatrix = new OnDiskMatrix(u_rows, vt_cols);
            double[] row = U.getRow(1);
            int row_length = row.length;
            double resultValue;
            for (int r = 0; r < u_rows; ++r) {
                System.out.println(r);
                row = U.getRow(r);
                for (int c = 0; c < vt_cols; ++c) {
                    resultValue = 0;
                    for (int i = 0; i < row_length; ++i)
                        resultValue += row[i] * Vt.get(i, c);
                    resultMatrix.set(r, c, resultValue);
                }
            }
            System.out.println("m1");
            MatrixIO.writeMatrix(resultMatrix, new File("C:\\Users\\Jasmin2332\\Downloads\\MC\\trec07p\\trec07p\\trec07p\\data\\matrixTrunc"), MatrixIO.Format.SVDLIBC_DENSE_BINARY);
            System.out.println("m2");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Matrix x = MatrixIO.readMatrix(new File("C:\\Users\\Jasmin2332\\Downloads\\MC\\trec07p\\trec07p\\trec07p\\data\\matrixTruncTransposed"), MatrixIO.Format.SVDLIBC_DENSE_BINARY, Matrix.Type.DENSE_ON_DISK);
            File sqSumOut = new File("C:\\Users\\Jasmin2332\\Downloads\\MC\\trec07p\\trec07p\\trec07p\\data\\elementSqSums.txt");
            File readAble = new File("C:\\Users\\Jasmin2332\\Downloads\\MC\\trec07p\\trec07p\\trec07p\\data\\elementread.txt");
            PrintWriter mWriter = new PrintWriter(sqSumOut);
            PrintWriter mWriterText = new PrintWriter(readAble);
            double magnitude = 0.0d;
            double[] rowVector = new double[x.columns()];
            int row = x.rows();
            int col = x.columns();
            for (int i = 0; i < row; ++i) {
                System.out.println(i);
                for (int j = 0; j < col; ++j) {
                    magnitude += Math.pow(x.get(i, j), 2);
                }
                magnitude = Math.sqrt(magnitude);
                for (int j = 0; j < col; j++) {
                    rowVector[j] = x.get(i,j);
                }
                mWriterText.println(magnitude);
                for (double v : rowVector) {
                    mWriter.print(v+" ");
                }
                mWriter.println();
                Arrays.fill(rowVector, 0.0);
                magnitude = 0.0d;
            }
            mWriterText.close();
            mWriter.close();
        }catch (Exception ignored){}
    }
}
