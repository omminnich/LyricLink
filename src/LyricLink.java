import java.util.Scanner;

public class LyricLink {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("Enter song title: ");
        String songTitle = sc.nextLine();
        
        System.out.println("Enter artist name: ");
        String artistName = sc.nextLine();

        System.out.println("Searching for \"" + songTitle + "\" by " + artistName + "...");


    }

}