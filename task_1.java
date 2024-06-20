import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class StudentDatabase {
    private List<Student> students;

    public StudentDatabase() {
        students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void sortStudentsByRating() {
        students.sort(Comparator.comparingInt(Student::getRating).reversed());
    }

    public void displayStudents() {
        System.out.println(String.format("%-15s %-10s %-5s", "Прізвище", "No залікової книжки,", "рейтинг"));
        System.out.println("--------------------------------------");
        for (Student student : students) {
            System.out.println(student);
        }
    }

    public double calculateAverageRating() {
        return students.stream().mapToInt(Student::getRating).average().orElse(0);
    }

    public long countStudentsBelowAverage(double averageRating) {
        return students.stream().filter(student -> student.getRating() < averageRating).count();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        StudentDatabase database = new StudentDatabase();

        int n = 0;

        while (true) {
            System.out.println("введіть кількість учнів n= ");
            if (scanner.hasNextInt()) {
                n = scanner.nextInt();
                break;
            } else {
                System.out.println("Помилка: введене значення не є числом. Спробуйте ще раз.");
                scanner.next(); // Очищення неправильного вводу
            }
        }

        //Додавання  учнів
        for (int i = 0; i < n; i++) {
            try {
                String surname;
                while (true) {
                    System.out.print("Введіть прізвище: ");
                    surname = scanner.nextLine();
                    if (surname.matches("[a-zA-Zа-яА-Я]+")) {
                        break;
                    } else {
                        System.out.println("Прізвище повинно складатися тільки з букв. Будь ласка спробуйте ще раз.");
                    }
                }

                int recordBookNumber;
                while (true) {
                    System.out.print("Введіть номер залікової книги: ");
                    try {
                        recordBookNumber = Integer.parseInt(scanner.nextLine());
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Неправильні дані. Номер залікової книги має бути числом. Будь ласка спробуйте ще раз.");
                    }
                }

                int rating;
                while (true) {
                    System.out.print("Введіть оцінку (0-100): ");
                    try {
                        rating = Integer.parseInt(scanner.nextLine());
                        if (rating < 0 || rating > 100) {
                            throw new IllegalArgumentException("Рейтинг має бути від 0 до 100.");
                        }
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Неправильні дані. Рейтинг має бути числом. Будь ласка спробуйте ще раз.");
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }

                Student student = new Student(surname, recordBookNumber, rating);
                database.addStudent(student);
            } catch (Exception e) {
                System.out.println("Сталася неочікувана помилка. Будь ласка спробуйте ще раз.");
                i--; // to repeat this iteration
            }
        }

        // Сортування та відображення учнів
        database.sortStudentsByRating();
        database.displayStudents();

        // Обчислити середній рейтинг і підрахувати студентів нижче середнього
        double averageRating = database.calculateAverageRating();
        long belowAverageCount = database.countStudentsBelowAverage(averageRating);

        System.out.println("\nСередня оцінка: " + averageRating);
        System.out.println("Кількість студентів з рейтингом нижче середнього: " + belowAverageCount);
    }
}
