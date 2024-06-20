import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

class WordDatabaseApp {

    static class WordEntry {
        private String word;
        private int pageNumber;
        private int wordCount;

        public WordEntry() {
        }

        public WordEntry(String word, int pageNumber, int wordCount) {
            this.word = word;
            this.pageNumber = pageNumber;
            this.wordCount = wordCount;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public int getWordCount() {
            return wordCount;
        }

        public void setWordCount(int wordCount) {
            this.wordCount = wordCount;
        }

        @Override
        public String toString() {
            return String.format("%-15s %-10d %-15d", word, pageNumber, wordCount);
        }

        public static String getTableHeader() {
            return String.format("%-15s %-10s %-15s", "Слово", "Номер сторінки", "Кількість слів");
        }
    }

    static class WordDatabase {
        private List<WordEntry> wordEntries;

        public WordDatabase() {
            wordEntries = new ArrayList<>();
        }

        public void addWordEntry(WordEntry wordEntry) {
            wordEntries.add(wordEntry);
            saveToFile();
        }

        public List<WordEntry> getWordEntries() {
            return wordEntries;
        }

        public void editWordEntry(String word, WordEntry newWordEntry) {
            for (int i = 0; i < wordEntries.size(); i++) {
                if (wordEntries.get(i).getWord().equalsIgnoreCase(word)) {
                    wordEntries.set(i, newWordEntry);
                    saveToFile();
                    return;
                }
            }
            System.out.println("Запис з таким словом не знайдено.");
        }

        public void deleteWordEntry(String word) {
            for (int i = 0; i < wordEntries.size(); i++) {
                if (wordEntries.get(i).getWord().equalsIgnoreCase(word)) {
                    wordEntries.remove(i);
                    saveToFile();
                    return;
                }
            }
            System.out.println("Запис з таким словом не знайдено.");
        }

        public void displayWordEntries() {
            System.out.println(WordEntry.getTableHeader());
            System.out.println("--------------- ---------- ---------------");
            for (WordEntry wordEntry : wordEntries) {
                System.out.println(wordEntry);
            }
        }

        public WordEntry searchWordEntryByWord(String word) {
            for (WordEntry wordEntry : wordEntries) {
                if (wordEntry.getWord().equalsIgnoreCase(word)) {
                    return wordEntry;
                }
            }
            return null;
        }

        public void sortWordEntriesByPageNumber() {
            Collections.sort(wordEntries, Comparator.comparing(WordEntry::getPageNumber));
        }

        private void saveToFile() {
            try (PrintWriter writer = new PrintWriter(new FileWriter("wordEntries.txt"))) {
                for (WordEntry wordEntry : wordEntries) {
                    writer.println(wordEntry.getWord() + "," + wordEntry.getPageNumber() + "," + wordEntry.getWordCount());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void loadFromFile() {
            try (BufferedReader reader = new BufferedReader(new FileReader("wordEntries.txt"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    String word = parts[0];
                    int pageNumber = Integer.parseInt(parts[1]);
                    int wordCount = Integer.parseInt(parts[2]);
                    WordEntry wordEntry = new WordEntry(word, pageNumber, wordCount);
                    wordEntries.add(wordEntry);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        WordDatabase wordDatabase = new WordDatabase();
        wordDatabase.loadFromFile();

        while (true) {
            System.out.println("\nМеню:");
            System.out.println("1. Додати запис");
            System.out.println("2. Редагувати запис");
            System.out.println("3. Видалити запис");
            System.out.println("4. Показати всі записи");
            System.out.println("5. Пошук запису за словом");
            System.out.println("6. Сортування записів за номером сторінки");
            System.out.println("7. Вихід");
            System.out.print("Оберіть опцію: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // consume newline

            switch (choice) {
                case 1:
                    addWordEntry(scanner, wordDatabase);
                    break;
                case 2:
                    editWordEntry(scanner, wordDatabase);
                    break;
                case 3:
                    deleteWordEntry(scanner, wordDatabase);
                    break;
                case 4:
                    wordDatabase.displayWordEntries();
                    break;
                case 5:
                    searchWordEntry(scanner, wordDatabase);
                    break;
                case 6:
                    wordDatabase.sortWordEntriesByPageNumber();
                    wordDatabase.displayWordEntries();
                    break;
                case 7:
                    System.out.println("Завершення програми.");
                    return;
                default:
                    System.out.println("Невірний вибір.");
            }
        }
    }

    private static void addWordEntry(Scanner scanner, WordDatabase wordDatabase) {
        System.out.print("Слово: ");
        String word = scanner.nextLine();

        System.out.print("Номер сторінки: ");
        int pageNumber = scanner.nextInt();

        System.out.print("Кількість слів на сторінці: ");
        int wordCount = scanner.nextInt();
        scanner.nextLine();  // consume newline

        WordEntry wordEntry = new WordEntry(word, pageNumber, wordCount);
        wordDatabase.addWordEntry(wordEntry);
        System.out.println("Запис успішно додано.");
    }

    private static void editWordEntry(Scanner scanner, WordDatabase wordDatabase) {
        System.out.print("Введіть слово запису, який потрібно редагувати: ");
        String word = scanner.nextLine();

        WordEntry existingWordEntry = wordDatabase.searchWordEntryByWord(word);
        if (existingWordEntry != null) {
            System.out.print("Нова номер сторінки: ");
            int newPageNumber = scanner.nextInt();

            System.out.print("Нова кількість слів на сторінці: ");
            int newWordCount = scanner.nextInt();
            scanner.nextLine();  // consume newline

            WordEntry newWordEntry = new WordEntry(word, newPageNumber, newWordCount);
            wordDatabase.editWordEntry(word, newWordEntry);
            System.out.println("Запис успішно відредаговано.");
        } else {
            System.out.println("Запис з таким словом не знайдено.");
        }
    }

    private static void deleteWordEntry(Scanner scanner, WordDatabase wordDatabase) {
        System.out.print("Введіть слово запису, який потрібно видалити: ");
        String word = scanner.nextLine();

        wordDatabase.deleteWordEntry(word);
        System.out.println("Запис успішно видалено.");
    }

    private static void searchWordEntry(Scanner scanner, WordDatabase wordDatabase) {
        System.out.print("Введіть слово для пошуку: ");
        String word = scanner.nextLine();

        WordEntry foundWordEntry = wordDatabase.searchWordEntryByWord(word);
        if (foundWordEntry != null) {
            System.out.println("Знайдено запис: " + foundWordEntry);
        } else {
            System.out.println("Запис з таким словом не знайдено.");
        }
    }
}
