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
        
        System.out.println("Enter artist name: ");
        String artistName = sc.nextLine();

        System.out.println("Searching for \"" + songTitle + "\" by " + artistName + "...");

        String token = System.getenv("GENIUS_ACCESS_TOKEN");
        String query = songTitle + " " + artistName;
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

        try {

            HttpClient hc = HttpClient.newHttpClient();
            HttpRequest hreq = HttpRequest.newBuilder()
                .uri(URI.create("https://api.genius.com/search?q=" + encodedQuery))
                .header("Authorization", "Bearer " + token).build();
            HttpResponse<String> hres = hc.send(hreq, HttpResponse.BodyHandlers.ofString());

            System.out.println("HTTP Status: " + hres.statusCode());
            System.out.println(hres.body());

            JsonObject root = JsonParser.parseString(hres.body()).getAsJsonObject();
            JsonObject response = root.getAsJsonObject("response");
            JsonArray hits = response.getAsJsonArray("hits");
            if (hits.size() > 0) {
                JsonObject firstHit = hits.get(0).getAsJsonObject();
                JsonObject songResult = firstHit.getAsJsonObject("result");

                String resultTitle = songResult.get("title").getAsString();
                String resultArtist = songResult.get("artist_names").getAsString();
                String resultUrl = songResult.get("url").getAsString();

                System.out.println(resultTitle);
                System.out.println(resultArtist);
                System.out.println(resultUrl);
            } else {
                System.out.println("No songs found.");
            }
            
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        

        






    }

}