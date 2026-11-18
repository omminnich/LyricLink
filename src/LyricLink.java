import java.util.Scanner;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

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
            
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        

        






    }

}