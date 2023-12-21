import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;




public class S28430Vorobiov42c
{
	// this version generates the file with info about clients only during the first run,
	// and later operates on the same file, so that during each run the results of the program will be the same.

	private static final int CLIENTS_NUM = 500;

	private final static List<String> lines;

	static
	{
		if (!new File("data.txt").exists()) // for operating on the same file 
			createAndFillFile();

		try(Stream<String> streamLines = Files.lines(Path.of("data.txt")))
		{
			lines = streamLines.toList();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);

		System.out.println("Wybierz komende lub wpisz \"0\" zeby skonczyc program:");
		System.out.println("1 \t-> Lista klientow, ktorzy gadali najwiecej czasu jako dzwoniacy i ten czas.");
		System.out.println("2 \t-> Lista klientow, ktorzy gadali najwiecej czasu jako odbiorcy i ten czas.");
		System.out.println("3 \t-> Lista klientow, ktorzy dzwonili do najwiekszej liczby innych klientow.");
		System.out.println("4 \t-> Lista klientow, ktorzy odebrali rozmowy od najwiekszej liczby klientow.");
		System.out.println("5 \t-> Lista klientow, ktorzy dzwonili najczesciej.");
		System.out.println("6 \t-> Lista klientow, ktorzy odebrali najwieksza liczbe rozmow.");
		System.out.println("7 \t-> Lista klientow, ktorzy dzwonili najrzadziej.");
		System.out.println("8 \t-> Lista klientow, ktorzy odebrali najmniejsza liczbe rozmow.");
		System.out.println("9 \t-> Informacja o kliencie.");

		int action = in.nextInt();

		if (action > 0 && action < 9)
			System.out.println("Wybierz liczbe klientow(MAX -> " + S28430Vorobiov42c.CLIENTS_NUM + "):");
		else if (action == 9)
			System.out.println("Wybierz numer klienta, ktory Ciebie interesuje(MAX -> " + S28430Vorobiov42c.CLIENTS_NUM +"):");
		else if (action == 0)
			System.exit(0);
		else
			throw new IllegalArgumentException();

		int num = in.nextInt();

		if (num < 0 || num > S28430Vorobiov42c.CLIENTS_NUM)
			throw new IllegalArgumentException();

		switch(action)
		{
			case 1 ->
			{
				System.out.println("\nKlienci, ktorzy gadali najdluzej jako dzwoniacy.");
				System.out.println("Format: Klient -> liczba sekund\n");
				S28430Vorobiov42c.printResult(S28430Vorobiov42c.getClientsWhoTalkedLongestAsCallers(num),
						"sekund");
			}

			case 2 ->
			{
				System.out.println("\nKlienci, ktorzy gadali najdluzej jako odbiorcy.");
				System.out.println("Format: Klient -> liczba sekund\n");
				S28430Vorobiov42c.printResult(S28430Vorobiov42c.getClientsWhoTalkedLongestAsReceivers(num),
						"sekund");
			}

			case 3 ->
			{
				System.out.println("\nKlienci, ktorzy dzownili do najwiekszej liczby innych klientow.");
				System.out.println("Format: Klient -> liczba klientow, do ktorych zadzwonil\n");
				S28430Vorobiov42c.printResult(S28430Vorobiov42c.getClientsWhoCalledMostClients(num), "innych klientow");
			}

			case 4 ->
			{
				System.out.println("\nKlienci, ktorzy odebrali rozmowy od najwiekszej liczby innych klientow.");
				System.out.println("Format: Klient -> liczba klientow, do ktorych zadzwonil\n");
				S28430Vorobiov42c.printResult(S28430Vorobiov42c.
						getClientsWhoReceivedMostCallsFromMostClients(num), "innych klientow");
			}

			case 5 ->
			{
				System.out.println("\nKlienci, ktorzy dzownili najczesciej.");
				System.out.println("Format: Klient -> ile razy dzwonil\n");
				S28430Vorobiov42c.printResult(S28430Vorobiov42c.getClientsWhoCalledMostTimes(num), "razy");
			}

			case 6 -> {
				System.out.println("\nKlienci, ktorzy odebrali najwieksza liczbe rozmow.");
				System.out.println("Format: Klient -> ile razy odebral\n");
				S28430Vorobiov42c.printResult(S28430Vorobiov42c.getClientsWhoReceivedMostCalls(num), "razy");
			}

			case 7 ->
			{
				System.out.println("\nKlienci, ktorzy dzwonili najrzadziej.");
				System.out.println("Format: Klient -> ile razy zadzwonil\n");
				S28430Vorobiov42c.printResult(S28430Vorobiov42c.getClientsWhoCalledLeastTimes(num), "razy");
			}

			case 8 ->
			{
				System.out.println("\nKlienci, ktorzy odebrali najmniejsza liczbe rozmow.");
				System.out.println("Format: Klient -> liczba odebranych rozmow\n");
				S28430Vorobiov42c.printResult(S28430Vorobiov42c.getClientsWhoReceivedLeastCalls(num), "rozmow");
			}

			case 9 ->
			{
				System.out.println("\nInformacja o kliencie " + num + "\n");
				S28430Vorobiov42c.printInfoAbout(num);
			}

			default -> throw new IllegalArgumentException();
		}
		System.out.println("\n==============================\n");
		main(args);
	}


	private static void printResult(List<Map.Entry<Integer,Integer>> list, String message)
	{
		list.forEach(entry ->
				System.out.printf("Klient %d -> %d %s\n", entry.getKey(), entry.getValue(), message));
	}

	private static void createAndFillFile()
	{
		try(BufferedWriter bw = new BufferedWriter(new FileWriter("data.txt")))
		{
			Random generator = new Random();

			int caller, receiver;
			for (int i = 0; i < 200_000; i++)
			{
				caller = generator.nextInt(1,CLIENTS_NUM + 1);
				do
					receiver = generator.nextInt(1,CLIENTS_NUM + 1);

				while (caller == receiver);

				int secs = generator.nextInt(1, 600);
				bw.write(caller+" ");
				bw.write(receiver+" ");
				bw.write(secs+"\n");
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(1);
		}
	}

	private static List<Map.Entry<Integer, Integer>> getClientsWhoTalkedLongestAsCallers(int n)
	{
		return
				lines
						.stream()
						.map(line -> line.split(" "))
						.collect(
								Collectors
										.toMap(line->Integer.parseInt(line[0]),
												line->Integer.parseInt(line[2]), Integer::sum)).entrySet()
						.stream().sorted(
								Comparator.comparingInt((Map.Entry<Integer,Integer> entry)->entry.getValue()).reversed())
						.limit(n)
						.toList();
	}

	private static List<Map.Entry<Integer, Integer>> getClientsWhoTalkedLongestAsReceivers(int n)
	{
		return
				lines
						.stream()
						.map(line -> line.split(" "))
						.collect(
								Collectors.
										toMap(line -> Integer.parseInt(line[1]), line -> Integer.parseInt(line[2]), Integer::sum)).
						entrySet().stream().sorted(
								Comparator.comparingInt((Map.Entry<Integer, Integer> entry)
										->entry.getValue()).reversed()).
						limit(n).
						toList();
	}

	private static List<Map.Entry<Integer, Integer>> getClientsWhoCalledMostClients(int n)
	{
		return
				lines
						.stream()
						.map(line -> line.split(" "))
						.collect(Collectors.toMap(line->Integer.parseInt(line[0]),
								line->Set.of(Integer.parseInt(line[1])), (a, b)->{
									Set<Integer> union = new HashSet<>(a);
									union.addAll(b);
									return union;
								}))
						.entrySet()
						.stream()
						.sorted(Comparator.comparingInt((Map.Entry<Integer, Set<Integer>>entry) ->
								entry.getValue().size()).reversed())
						.limit(n)
						.map(entry -> Map.entry(entry.getKey(), entry.getValue().size()))
						.toList();
	}

	private static List<Map.Entry<Integer, Integer>> getClientsWhoReceivedMostCallsFromMostClients(int n)
	{
		return
				lines
						.stream()
						.map(s->s.split(" "))
						.map(line->new int[]{Integer.parseInt(line[0]), Integer.parseInt(line[1])})
						.collect(Collectors.toMap(line->line[1],line->Set.of(line[0]), (a, b)->{
							Set<Integer> union = new HashSet<>(a);
							union.addAll(b);
							return union;
						}))
						.entrySet()
						.stream()
						.sorted(Comparator.comparingInt((Map.Entry<Integer, Set<Integer>> entry)->
								entry.getValue().size()).reversed())
						.limit(n)
						.map(entry -> Map.entry(entry.getKey(), entry.getValue().size()))
						.toList();
	}

	private static List<Map.Entry<Integer, Integer>> getClientsWhoCalledMostTimes(int n)
	{
		return
				lines
						.stream()
						.map(line -> line.split(" "))
						.collect(Collectors.toMap(line->Integer.parseInt(line[0]), line->1, Integer::sum))
						.entrySet()
						.stream()
						.sorted(Comparator.comparingInt((Map.Entry<Integer, Integer> entry)
								-> entry.getValue()).reversed())
						.limit(n)
						.toList();
	}

	private static List<Map.Entry<Integer, Integer>> getClientsWhoReceivedMostCalls(int n)
	{
		return
				lines
						.stream()
						.map(line -> line.split(" "))
						.collect(Collectors.toMap(line->Integer.parseInt(line[1]), line->1, Integer::sum))
						.entrySet()
						.stream()
						.sorted(Comparator.comparingInt((Map.Entry<Integer, Integer> entry)
								-> entry.getValue()).reversed())
						.limit(n)
						.toList();
	}


	private static List<Map.Entry<Integer, Integer>> getClientsWhoCalledLeastTimes(int n)
	{
		return
				lines
						.stream()
						.map(line->line.split(" "))
						.collect(Collectors.toMap(line->Integer.parseInt(line[0]), line->1, Integer::sum))
						.entrySet()
						.stream()
						.sorted(Comparator.comparingInt(Map.Entry::getValue))
						.limit(n)
						.toList();
	}


	private static List<Map.Entry<Integer, Integer>> getClientsWhoReceivedLeastCalls(int n)
	{
		return
				lines
						.stream()
						.map(line->line.split(" "))
						.collect(Collectors.toMap(line->Integer.parseInt(line[1]), line->1, Integer::sum))
						.entrySet()
						.stream()
						.sorted(Comparator.comparingInt(Map.Entry::getValue))
						.limit(n)
						.toList();
	}

	private static void printInfoAbout(int k)
	{
		//ile razy dzwonil
		int timesCalled =
				lines
						.stream()
						.map(line -> line.split(" "))
						.filter(line -> Integer.parseInt(line[0]) == k)
						.map(line -> 1)
						.reduce(0, Integer::sum);
		System.out.println("Zadzwonil " + timesCalled + " razy");

		int timesReceived =
				lines
						.stream()
						.map(line->line.split(" "))
						.filter(line -> Integer.parseInt(line[1]) == k)
						.map(line -> 1)
						.reduce(0, Integer::sum);
		System.out.println("Do niego dzowniono " + timesReceived + " razy");

		int secondsTalkedAsCaller =
				lines
						.stream()
						.map(line -> line.split(" "))
						.filter(line -> Integer.parseInt(line[0]) == k)
						.map(line -> Integer.parseInt(line[2]))
						.reduce(0, Integer::sum);
		System.out.println("Gadal jako dzwoniacy " + secondsTalkedAsCaller + " sekund");

		int secondsTalkedAsReceiver =
				lines
						.stream()
						.map(line -> line.split(" "))
						.filter(line -> Integer.parseInt(line[1]) == k)
						.map(line -> Integer.parseInt(line[2]))
						.reduce(0, Integer::sum);
		System.out.println("Gadal jako odbiorca " + secondsTalkedAsReceiver + " sekund");
	}
}
