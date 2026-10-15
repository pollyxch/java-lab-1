import java.util.Scanner;

//класс комплексного числа
class Complex_Num {
    private final double re;
    private final double im;

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
    public Complex_Num minus(Complex_Num other) {
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

    @Override
    public String toString() {
        if (im >= 0) {
            return String.format("%.2f+%.2fi", re, im);
        } else {
            return String.format("%.2f%.2fi", re, im);
        }
    }
}

//класс матрицы
class Compl_Matrix {
    private final Complex_Num[][] values;
    private final int rows;
    private final int cols;

    public Compl_Matrix(int rows, int cols) {
        if (rows <=0 || cols <=0 || rows > 25 || cols > 25) {
            throw new IllegalArgumentException("размер должен быть от 1x1 до 25x25");
        }
        this.rows = rows;
        this.cols = cols;
        this.values = new Complex_Num[rows][cols];
        for (int i = 0; i< rows; i++) {
            for (int j = 0; j<cols; j++) {
                values[i][j] = new Complex_Num();
            }
        }
    }

    //геттеры-сеттеры
    public int get_rows() {
        return rows;
    }
    public int get_cols() {
        return cols;
    }
    public Complex_Num get_values(int i, int j) {
        return values[i][j];
    }
    public void set_values(int i, int j, Complex_Num value) {
        values[i][j] = value;
    }

    //сложение самих матриц
    public Compl_Matrix add(Compl_Matrix other) {
        if (this.cols != other.cols || this.rows != other.rows) {
            throw new IllegalArgumentException("матрицы должны быть одинаковые по размеру");
        }
        Compl_Matrix result = new Compl_Matrix(rows, cols);
        for (int i = 0; i< rows; i++) {
            for (int j = 0; j< cols; j++) {
                result.set_values(i, j, this.values[i][j].add(other.values[i][j]));
            }
        }
        return result;
    }

    //метод для транспонирования
    public Compl_Matrix transpon() {
        Compl_Matrix result = new Compl_Matrix(cols, rows);
        for (int i = 0; i< rows; i++) {
            for (int j = 0; j< cols; j++) {
                result.set_values(j, i, this.values[i][j]);
            }
        }
        return result;
    }

    //умножение
    public Compl_Matrix mult(Compl_Matrix other) {
        if (this.cols != other.rows) {
            throw new IllegalArgumentException("число столбцов 1 должно равняться числу строк 2");
        }
        Compl_Matrix result = new Compl_Matrix(this.rows, other.cols);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                Complex_Num s = new Complex_Num();
                for (int a = 0; a < this.cols; a++) {
                    s = s.add(this.values[i][a].mult(other.values[a][j]));
                }
                result.set_values(i, j, s);
            }
        }
        return result;
    }

    /* метод для определителя
    по Гауссу */

    public Complex_Num det() {
        if (rows != cols) {
            throw new IllegalArgumentException("определитель можно считать только для квадратной матрицы");
        }
        int b = rows;
        Complex_Num[][] t = new Complex_Num[b][b];
        for (int i = 0; i < b; i++) {
            for (int j = 0; j < b; j++) {
                t[i][j] = new Complex_Num(values[i][j].get_re(), values[i][j].get_im());
            }
        }
        Complex_Num determ = new Complex_Num(1, 0);
        for (int i = 0; i < b; i++) {
            int st = i;
            while (st < b && t[st][i].abs() <1e-12) {
                st++;
            }
            if (st != i) {
                Complex_Num[] temp = t[i];
                t[i] = t[st];
                t[st] = temp;
                determ = determ.mult(new Complex_Num(-1, 0));
            }
            if (st == b) {
                return new Complex_Num(0, 0);
            }

            determ = determ.mult(t[i][i]);
            for (int j = i + 1; j < b; j++) {
                Complex_Num fact = t[j][i].div(t[i][i]);
                for (int a = i; a < b; a++) {
                    t[j][a] = t[j][a].minus(fact.mult(t[i][a]));
                }
            }
        }
        return determ;
    }

    //для вычисления обратной
    public Compl_Matrix inv() {
        if (rows != cols) {
            throw new IllegalArgumentException("можно только для квадратной");
        }
        int c = rows;
        Complex_Num[][] t = new Complex_Num[c][2 * c];
        for (int i = 0; i < c; i++) {
            for (int j = 0; j < c; j++) {
                t[i][j] = new Complex_Num(values[i][j].get_re(), values[i][j].get_im());
            }
            for (int j = 0; j < c; j++) {
                t[i][c + j] = new Complex_Num(i == j ? 1 : 0, 0);
            }
        }
        for (int i = 0; i < c; i++) {
            int st = i;
            while (st < c && t[st][i].abs() < 1e-12) {
                st++;
            }
            if (st != i) {
                Complex_Num[] temp = t[i];
                t[i] = t[st];
                t[st] = temp;
            }
            if (st == c) {
                throw new ArithmeticException("матрица вырождена :( обратную нельзя сделать, не существует");
            }
            Complex_Num divisor = t[i][i];
            for (int j = 0; j < 2 * c; j++) {
                t[i][j] = t[i][j].div(divisor);
            }
            for (int j = 0; j < c; j++) {
                if (j != i) {
                    Complex_Num fact = t[j][i];
                    for (int d = 0; d < 2 * c; d++) {
                        t[j][d] = t[j][d].minus(fact.mult(t[i][d]));
                    }
                }
            }
        }
        Compl_Matrix result = new Compl_Matrix(c, c);
        for (int i = 0; i < c; i++) {
            for (int j = 0; j < c; j++) {
                result.set_values(i, j, t[i][c + j]);
            }
        }
        return result;
    }

    //метод для деления матриц через A/B = A*B^-1
    public Compl_Matrix div(Compl_Matrix other) {
        if (other.rows != other.cols || this.cols != other.rows) {
            throw new IllegalArgumentException("иди учи линал для деления число столбцов 1 должно быть равно числу строк 2, и 2-я должна быть квадратной");
        }
        Compl_Matrix inv = other.inv();
        return this.mult(inv);
    }

    //вывод
    public void print_matrix() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(values[i][j] + "    ");
            }
            System.out.println();
        }
    }
}

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static Compl_Matrix last_result = null;
    public static void main(String[] arg) {
        System.out.println("ограничние размера матрицы - 25x25 ");

        while (true) {
            System.out.println("\nнапишите номер операции - 1: сложение\n2: умножение\n3: деление\n4: транспонирование\n5: вычисление определителя\n6: выйти");
            System.out.print(">>> ");

            int var = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (var) {
                    case 1:
                        add_matrix();
                        break;
                    case 2:
                        mult_matrix();
                        break;
                    case 3:
                        div_matrix();
                        break;
                    case 4:
                        transp_matrix();
                        break;
                    case 5:
                        det_matrix();
                        break;
                    case 6:
                        System.out.println("выход, пока");
                        return;
                    default:
                        System.out.println("выберите нормально (пожалуйста)");
                }
            } catch (Exception e) {
                System.out.println("что-то не работает: " + e.getMessage());
            }
        }
    }

    //метод для того чтобы использовать последний результат и делать с ним что-то
    static Compl_Matrix get_matrix(String name) {
        if (last_result != null) {
            String answ;
            while (true) {
                System.out.println("вы хотите использовать последний результат как " + name + "? да/нет");
                answ = scanner.nextLine().trim().toLowerCase();
                if (answ.isEmpty()) continue;
                break;
            }
            if (answ.equals("да")) {
                return last_result;
            }

        }
        return input(name);
    }

    //ввод матрицы
    static Compl_Matrix input(String name) {
        System.out.println("число строк матрицы " + name);
        int rows = scanner.nextInt();
        scanner.nextLine();
        System.out.println("число столбцов матрицы " + name);
        int cols = scanner.nextInt();
        scanner.nextLine();

        Compl_Matrix matrix = new Compl_Matrix(rows, cols);
        System.out.println("введите элементы матрицы, действительную и мнимую части через пробел ");
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print("эл-т " + i + j + " ");
                double re = scanner.nextDouble();
                double im = scanner.nextDouble();
                matrix.set_values(i, j, new Complex_Num(re, im));
            }
        }
        return matrix;
    }
    //это для свитча
    static void mult_matrix() {
        Compl_Matrix a = get_matrix("A");
        Compl_Matrix b = get_matrix("B");
        Compl_Matrix result = a.mult(b);
        last_result = result;
        System.out.println("Результат: ");
        result.print_matrix();
    }
    static void add_matrix() {
        Compl_Matrix a = get_matrix("A");
        Compl_Matrix b = get_matrix("B");
        Compl_Matrix result = a.add(b);
        last_result = result;
        System.out.println("Результат: ");
        result.print_matrix();
    }
    static void div_matrix() {
        Compl_Matrix a = get_matrix("A");
        Compl_Matrix b = get_matrix("B");
        Compl_Matrix result = a.div(b);
        last_result = result;
        System.out.println("Результат: ");
        result.print_matrix();
    }
    static void transp_matrix() {
        Compl_Matrix a = get_matrix("A");
        Compl_Matrix result = a.transpon();
        last_result = result;
        System.out.println("Результат: ");
        result.print_matrix();
    }
    static void det_matrix() {
        Compl_Matrix a = get_matrix("A");
        Complex_Num result = a.det();
        System.out.println("Результат: " + result);
    }

}