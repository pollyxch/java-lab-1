import java.util.Scanner;

class Complex_Num {
    private double re;
    private double im;

    public Complex_Num() {
        this.re =0;
        this.im =0;
    }
    public Complex_Num(double re, double im) {
        this.re = re;
        this.im = im;
    }

    //геттеры
    public double get_re() {
        return re;
    }
    public double get_im() {
        return im;
    }

    //сложение
    public Complex_Num add(Complex_Num other) {
        return new Complex_Num(this.re + other.re, this.im + other.im);
    }
    //вычитание
    public Complex_Num sub(Complex_Num other) {
        return new Complex_Num(this.re - other.re, this.im - other.im);
    }
    //умножение
    public Complex_Num mult(Complex_Num other){
        double new_re = this.re * other.re - this.im * other.im;
        double new_im = this.re * other.im + this.im * other.re;
        return new Complex_Num(new_re, new_im);
    }
    //деление
    public Complex_Num div(Complex_Num other) {
        double denom = other.re * other.re + other.im * other.im;
        if (denom == 0) {
            throw new ArithmeticException("деление на ноль!");
        }
        double new_re = (this.re * other.re + this.im * other.im) / denom;
        double new_im = (this.im * other.re - this.re * other.im) / denom;
        return new Complex_Num(new_re, new_im);
    }
    //модуль
    public double abs() {
        return Math.sqrt(re *re + im * im);
    }
    //Строковое представление
    @Override
    public String to_string() {
        if (im >= 0) {
            return String.format("%.2f+%.2fi", re, im);
        }
        else {
            return String.format("%.2f%.2fi", re, im);
        }
    }
}

class Matrix {
    private Complex_Num[][] data;
    private int rows;
    private int cols;

    public Matrix(int rows, int cols) {
        if (rows <=0 || cols <=0 || rows > 25 || cols > 25) {
            throw new IllegalArgumentException("размер должен быть от 1x1 до 25x25");
        }
        this.rows = rows;
        this.cols = cols;
        this.data = new Complex_Num[rows][cols];
        for (int i = 0; i< rows; i++) {
            for (int j = 0; j<cols; j++) {
                data[i][j] = new Complex_Num();
            }
        }
    }

    //геттеры
    public int get_rows() {
        return rows;
    }
    public int get_cols() {
        return cols;
    }
}