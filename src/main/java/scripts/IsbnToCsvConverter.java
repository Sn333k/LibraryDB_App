package scripts;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;

/*
URUCHOMIENIE SKRYPTU WYMAGA PLIKÓW POBRANYCH Z https://openlibrary.org/developers/dumps:
editions dump (~ 9.2G)
authors dump (~ 0.5G)
Takie parametry STEP i LIMIT powodują że do pliku books dostaną się 4 rekordy bez isbn. Wtedy najlepiej wejść przez intelij, widok tabeli i sortować po isbn
i usunąć błędne wiersze
 */

public class IsbnToCsvConverter {

    private static final String EDITIONS_FILE = "C:/Users/ljone/Downloads/ol_dump_editions.txt";
    private static final String AUTHORS_FILE = "C:/Users/ljone/Downloads/ol_dump_authors.txt";
    private static final String OUTPUT_DIR = "csv_output/";

    private static final int STEP = 1000;
    private static final int LIMIT = 10000;

    private static final Set<String> neededAuthorKeys = new HashSet<>();
    private static final Set<String> addedAuthorsNamesSet = new HashSet<>();

    private static final Map<String, String> authorMap = new HashMap<>();

    private static final List<String[]> authors = new ArrayList<>();
    private static final List<String[]> books = new ArrayList<>();
    private static final List<String[]> booksAuthorsRaw = new ArrayList<>(); // [ISBN, AuthorKey]

    public static void main(String[] args) {
        try {
            new File(OUTPUT_DIR).mkdirs();

            System.out.println("--- STEP 1: Processing Editions ---");
            processEditions();

            System.out.println("--- STEP 2: Loading Needed Authors Only ---");
            loadNeededAuthors();


            System.out.println("--- STEP 3: Exporting to CSV ---");
            exportAll();

            System.out.println("DONE! Files saved in: " + OUTPUT_DIR);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ========================= ETAP 1: EDYCJE (KSIĄŻKI) =========================
    private static void processEditions() throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(EDITIONS_FILE))) {
            String line;
            int lineNumber = 0;
            int processedCount = 0;
            Pattern pattern = Pattern.compile("\\d{4}");
            while ((line = br.readLine()) != null && processedCount < LIMIT) {
                lineNumber++;
                if (lineNumber % STEP != 0) continue;
                try {
                    String[] tsv = line.split("\t");
                    if (tsv.length < 5) continue;
                    String jsonPart = tsv[tsv.length - 1];
                    JSONObject json = new JSONObject(jsonPart);

                    // --- ISBN ---
                    String isbn = null;
                    if (json.has("isbn_13")) {
                        isbn = json.getJSONArray("isbn_13").optString(0);
                    } else if (json.has("isbn_10")) {
                        isbn = json.getJSONArray("isbn_10").optString(0);
                    }
                    if (isbn == null || isbn.isEmpty()) continue;

                    // --- Title ---
                    String title = json.optString("title", "Unknown Title");
                    if (title.length() >= 200) title=title.substring(0, 50);

                    // --- Year ---
                    int year = parseYear(json.optString("publish_date", "2000"));

                    // --- Genre ---
                    String genre = "General";
                    if (json.has("subjects")) {
                        genre = pickBestSubject(json.getJSONArray("subjects"));
                    }

                    books.add(new String[]{title, isbn, String.valueOf(year), genre});

                    // --- Collect Author Keys ---
                    if (json.has("authors")) {
                        JSONArray authorsArray = json.getJSONArray("authors");
                        for (int i = 0; i < authorsArray.length(); i++) {
                            JSONObject aObj = authorsArray.getJSONObject(i);
                            if (aObj.has("key")) {
                                String aKey = aObj.getString("key");
                                neededAuthorKeys.add(aKey);
                                booksAuthorsRaw.add(new String[]{isbn, aKey});
                            }
                        }
                    }

                    processedCount++;
                    if (processedCount % 100 == 0) System.out.println("Analyzed " + processedCount + " books...");

                } catch (Exception e) {
                    System.out.println("\nProblem z linią: " + line);
                    System.out.println("Błąd: " + e.getMessage());
                    break;
                }
            }
            System.out.println("Finished editions. Found " + books.size() + " books and " + neededAuthorKeys.size() + " unique authors to find.");
        }
    }

    // ========================= ETAP 2: AUTORZY (TYLKO POTRZEBNI) =========================
    private static void loadNeededAuthors() throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(AUTHORS_FILE))) {
            //System.out.printf("a");
            String line;
            int foundCount = 0;

            while ((line = br.readLine()) != null) {
                //System.out.printf("b");
                String[] parts = line.split("\t");
                if (parts.length < 5) continue;

                String keyInFile = parts[1]; // Druga kolumna to zazwyczaj klucz /authors/OL...

                if (neededAuthorKeys.contains(keyInFile)) {
                    //System.out.printf("c");
                    try {
                        String[] tsv = line.split("\t");
                        if (tsv.length < 5) continue;
                        String jsonPart = tsv[tsv.length - 1];
                        JSONObject json = new JSONObject(jsonPart);
                        String fullName = json.optString("name", "Unknown Author");

                        authorMap.put(keyInFile, fullName);

                        if (addedAuthorsNamesSet.add(fullName.toLowerCase())) {
                            String[] nameParts = fullName.split(" ", 2);
                            String firstName = nameParts[0];
                            String lastName = (nameParts.length > 1) ? nameParts[1] : "Unknown";
                            authors.add(new String[]{firstName, lastName});
                        }

                        foundCount++;
                        if (foundCount % 100 == 0) System.out.println("Found " + foundCount + " authors details...");
                    } catch (Exception ignored) {}
                }
            }
        }
    }

    // ========================= ETAP 3: EKSPORT =========================
    private static void exportAll() throws Exception {
        // Przygotuj listę powiązań (zamień klucze na nazwiska)
        List<String[]> booksAuthorsFinal = new ArrayList<>();
        for (String[] pair : booksAuthorsRaw) {
            String isbn = pair[0];
            String authorName = authorMap.getOrDefault(pair[1], "Unknown Author");
            booksAuthorsFinal.add(new String[]{isbn, authorName});
        }

        writeCsv("authors.csv", new String[]{"first_name", "last_name"}, authors);
        writeCsv("books.csv", new String[]{"title", "ISBN", "publication_year", "genre"}, books);
        writeCsv("books_authors.csv", new String[]{"book_ISBN", "author_fullname"}, booksAuthorsFinal);
    }

    // ========================= POMOCNICZE =========================
    private static int parseYear(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return 2000;
        Pattern pattern = Pattern.compile("\\d{4}");
        Matcher matcher = pattern.matcher(dateStr);
        String year = "0000";
        while (matcher.find()) {
            year = matcher.group();
        }
        try {
            return Integer.parseInt(year.substring(0, 4));
        } catch (NumberFormatException e) { return 2000; }
    }

    private static void writeCsv(String fileName, String[] headers, List<String[]> rows) throws Exception {
        try (PrintWriter pw = new PrintWriter(new File(OUTPUT_DIR + fileName))) {
            pw.println(String.join(",", headers));
            for (String[] row : rows) {
                for (int i = 0; i < row.length; i++) {
                    row[i] = row[i].replace(",", " ");
                }
                pw.println(String.join(",", row));
            }
        }
    }

    private static String pickBestSubject(JSONArray subjects) {
        String best = "General";
        int bestScore = Integer.MIN_VALUE;

        for (int i = 0; i < subjects.length(); i++) {
            String raw = subjects.optString(i, "").trim();
            if (raw.isEmpty()) continue;

            String candidate = raw.split("--")[0].trim();

            int score = 0;

            score -= candidate.length();

            if (!raw.contains("--")) score += 10;

            if (candidate.matches(".*\\d.*")) score -= 20;

            if (candidate.split(" ").length > 3) score -= 10;

            String lower = candidate.toLowerCase();
            if (lower.contains("united states") ||
                    lower.contains("england") ||
                    lower.contains("sweden") ||
                    lower.contains("france")) {
                score -= 15;
            }

            if (score > bestScore) {
                bestScore = score;
                best = candidate;
            }
        }
        if (best.length() >= 50) best=best.substring(0, 50);

        return best;
    }
}