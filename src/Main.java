import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        String filename = "5777names.txt";   // change to other files if needed

        try
        {
            // Count number of lines
            Scanner counter = new Scanner(new File(filename));
            int N = 0;

            while (counter.hasNextLine())
            {
                counter.nextLine();
                N++;
            }
            counter.close();

            // Read names into array
            String[] names = new String[N];

            Scanner reader = new Scanner(new File(filename));
            int i = 0;

            while (reader.hasNextLine())
            {
                names[i] = reader.nextLine();
                i++;
            }
            reader.close();

            HashSimulator simulator = new HashSimulator();

            // Hash table sizes
            int[] sizes = {N, 2*N, 5*N, 10*N, 100*N};

            for (int size : sizes)
            {
                int[] results = simulator.runHashSimulation(names, size);

                System.out.println("HT size: " + size);

                System.out.println("H1 collisions: " + results[0] +
                        "   H1 probes: " + results[1]);

                System.out.println("H2 collisions: " + results[2] +
                        "   H2 probes: " + results[3]);

                System.out.println("H3 collisions: " + results[4] +
                        "   H3 probes: " + results[5]);

                System.out.println();
            }
        }
        catch (FileNotFoundException e)
        {
            System.out.println("File not found.");
        }
    }
}