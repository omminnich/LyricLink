import java.util.Scanner;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;

public class LyricLink {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter song title: ");
        String songTitle = sc.nextLine();

        if (songTitle.isBlank()) {
            System.out.println("Error: song title cannot be empty.");
            return;
        }
        
        System.out.println("Enter artist name: ");
        String artistName = sc.nextLine();

        if (artistName.isBlank()) {
            System.out.println("Error: artist name cannot be empty.");
            return;
        }

        System.out.println("Searching for \"" + songTitle + "\" by " + artistName + "...");

        String token = System.getenv("GENIUS_ACCESS_TOKEN");
        if (token == null || token.isBlank()) {
            System.out.println("Error: need Genius API token");
            return;
        }
        String query = songTitle + " " + artistName;
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

        try {

            HttpClient hc = HttpClient.newHttpClient();
            HttpRequest hreq = HttpRequest.newBuilder()
                .uri(URI.create("https://api.genius.com/search?q=" + encodedQuery))
                .header("Authorization", "Bearer " + token).build();
            HttpResponse<String> hres = hc.send(hreq, HttpResponse.BodyHandlers.ofString());
            if (hres.statusCode() != 200) {
                System.out.println("Error: Genius API request failed. HTTP Status: " + hres.statusCode());
                return;
            }

            JsonObject root = JsonParser.parseString(hres.body()).getAsJsonObject();
            JsonObject response = root.getAsJsonObject("response");
            JsonArray hits = response.getAsJsonArray("hits");

            if (hits.size() > 0) {

                boolean foundMatch = false;

                for (int i = 0; i < hits.size(); i++) {

                    JsonObject currentHit = hits.get(i).getAsJsonObject();
                    JsonObject songResult = currentHit.getAsJsonObject("result");

                    String resultTitle = songResult.get("title").getAsString();
                    String resultArtist = songResult.get("artist_names").getAsString();
                    String resultUrl = songResult.get("url").getAsString();

                    if (songTitle.equalsIgnoreCase(resultTitle) && resultArtist.toLowerCase().contains(artistName.toLowerCase())) {
                        foundMatch = true;
                        System.out.println("Song: " + resultTitle);
                        System.out.println("Artist: " + resultArtist);
                        System.out.println("Genius Lyrics: " + resultUrl);
                        break;
                    }

                }

                if (!foundMatch) {
                    System.out.println("No songs found.");
                }

            } else {
                System.out.println("No songs found.");
            }
            
        } catch (IOException | InterruptedException e) {
            System.out.println("Error: Unable to connect to Genius."); 
        }

        

        






    }

}