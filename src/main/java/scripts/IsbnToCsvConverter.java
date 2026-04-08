package scripts;

import java.io.*;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class IsbnToCsvConverter {

    private static final String EDITIONS_FILE = "C:/Users/ljone/Downloads/ol_dump_editions.txt";
    private static final String AUTHORS_FILE = "C:/Users/ljone/Downloads/ol_dump_authors.txt";
    private static final String OUTPUT_DIR = "csv_output/";

    private static final int STEP = 1000;
    private static final int LIMIT = 10000;

    private static final Set<String> neededAuthorKeys = new HashSet<>();
    private static final Set<String> publisherSet = new HashSet<>();
    private static final Set<String> addedAuthorsNamesSet = new HashSet<>();

    private static final Map<String, String> authorMap = new HashMap<>();

    private static final List<String[]> publishers = new ArrayList<>();
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
            //System.out.printf("a");
            while ((line = br.readLine()) != null && processedCount < LIMIT) {
                lineNumber++;
                if (lineNumber % STEP != 0) continue;
                //System.out.printf("b");
                try {
                    //System.out.printf("c");
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

                    // --- Year ---
                    int year = parseYear(json.optString("publish_date", "2000"));

                    // --- Publisher ---
                    String publisherName = "Unknown Publisher";
                    if (json.has("publishers")) {
                        JSONArray pubs = json.getJSONArray("publishers");
                        if (pubs.length() > 0) publisherName = pubs.getString(0);
                    }

                    if (publisherSet.add(publisherName.toLowerCase())) {
                        publishers.add(new String[]{
                                publisherName,
                                "Unknown",
                                "contact@" + publisherName.toLowerCase().replaceAll("[^a-z]", "") + ".com"
                        });
                    }

                    // --- Genre ---
                    String genre = "General";
                    if (json.has("subjects")) {
                        genre = json.getJSONArray("subjects").optString(0, "General");
                    }

                    books.add(new String[]{title, isbn, String.valueOf(year), publisherName, genre});

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
                            authors.add(new String[]{firstName, lastName, "Unknown"});
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

        writeCsv("publishers.csv", new String[]{"publisher_name", "country", "contact_email"}, publishers);
        writeCsv("authors.csv", new String[]{"first_name", "last_name", "nationality"}, authors);
        writeCsv("books.csv", new String[]{"title", "ISBN", "publication_year", "publisher_name", "genre"}, books);
        writeCsv("books_authors.csv", new String[]{"book_ISBN", "author_fullname"}, booksAuthorsFinal);
    }

    // ========================= POMOCNICZE =========================
    private static int parseYear(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return 2000;
        String digits = dateStr.replaceAll("[^0-9]", "");
        if (digits.length() >= 4) {
            try {
                return Integer.parseInt(digits.substring(0, 4));
            } catch (NumberFormatException e) { return 2000; }
        }
        return 2000;
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
}