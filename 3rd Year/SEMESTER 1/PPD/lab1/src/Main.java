import java.util.Arrays;
import java.util.Random;


public class Main {

    static class MyThread extends Thread {
        private int id;
        private int[] a;
        private int[] b;
        private int[] c;
        private int NoThreads;
        private int VEC_SIZE;
        public MyThread(int id, int[] a, int[] b, int[] c, int NoThreads, int VEC_SIZE) {
            this.id = id;
            this.a = a;
            this.b = b;
            this.c = c;
            this.NoThreads = NoThreads;
            this.VEC_SIZE = VEC_SIZE;
        }

        public void run() {
            for(int i=id; i<VEC_SIZE; i+=NoThreads) {
                c[i] =(int) Math.sqrt(a[i] * a[i] * a[i] * a[i] * a[i] + b[i] * b[i] * b[i] * b[i] * b[i]);
            }
        }

    }
    static class MyThread2 extends Thread {
        private int start;
        private int end;
        private int[] a;
        private int[] b;
        private int[] c;

        public MyThread2(int start, int end, int[] a, int[] b, int[] c) {
            this.start = start;
            this.end = end;
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public void run() {
            for(int i=start; i<end; i++) {
                c[i] =(int) Math.sqrt(a[i] * a[i] * a[i] * a[i] * a[i] + b[i] * b[i] * b[i] * b[i] * b[i]);
            }
        }

    }


    public static void main(String[] args) {
        int VEC_SIZE = 100000000^10000;
        int NO_THREADS = 8;

        int[] a = generateArray(VEC_SIZE, 10);
        int[] b = generateArray(VEC_SIZE, 10);
        int[] c_seq = new int[VEC_SIZE];

        long startTime = System.currentTimeMillis();

        //secvential
        for(int i=0; i<VEC_SIZE; i++) {
            c_seq[i] =(int) Math.sqrt(a[i] * a[i] * a[i] * a[i] * a[i] + b[i] * b[i] * b[i] * b[i] * b[i]);

        }




        long endTime = System.currentTimeMillis();
        if(VEC_SIZE <= 10){
            System.out.println(Arrays.toString(a));
            System.out.println(Arrays.toString(b));
            System.out.println(Arrays.toString(c_seq));
        }


        System.out.println("Sequential time: " + (endTime - startTime) + " ms");


        //paralel

        long startTime2 = System.currentTimeMillis();

        int[] c_par = new int[VEC_SIZE];
        MyThread[] threads = new MyThread[NO_THREADS];

        for (int i=0; i<NO_THREADS;i++){
            MyThread t = new MyThread(i, a, b, c_par, NO_THREADS, VEC_SIZE);
            threads[i] = t;
            t.start();

        }
        for (int i=0; i<NO_THREADS;i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        long endTime2 = System.currentTimeMillis();
        if(VEC_SIZE <= 10){
            System.out.println(Arrays.toString(c_par));
        }


        System.out.println("Threads time: " + (endTime2 - startTime2) + " ms");




        long startTime3 = System.currentTimeMillis();

        int[] c_par2 = new int[VEC_SIZE];
        MyThread2[] threads2 = new MyThread2[NO_THREADS];
        int start = 0;
        int end = VEC_SIZE / NO_THREADS;
        int rest = VEC_SIZE % NO_THREADS;
        for (int i=0; i<NO_THREADS;i++){
            if(rest> 0){
                end++;
                rest--;
            }
            MyThread2 t = new MyThread2(start, end, a, b, c_par2);
            threads2[i] = t;
            t.start();
            start = end;
            end += VEC_SIZE / NO_THREADS;

        }
        for (int i=0; i<NO_THREADS;i++) {
            try {
                threads2[i].join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        long endTime3 = System.currentTimeMillis();
        if(VEC_SIZE <= 10){
            System.out.println(Arrays.toString(c_par2));
        }


        System.out.println("Threads time2: " + (endTime3 - startTime3) + " ms");
    }

    private static int[] generateArray(int size, int upperBound) {
        int[] vec =  new int[size];
        Random rand = new Random();

        for (int i = 0; i < size; i++) {
            vec[i] = rand.nextInt(upperBound);
        }
        return vec;
    }

}