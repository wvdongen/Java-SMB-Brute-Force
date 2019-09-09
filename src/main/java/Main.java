
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;

import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.apache.commons.cli.*;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        CommandLine cmd = getParsedCommandLine(args);
        String domain = cmd.getOptionValue("domain");

        System.out.println("Start");
        ExecutorService pool = Executors.newFixedThreadPool(20);

        File hostsFile = new File("hosts.txt");
        LineIterator hostsIt = FileUtils.lineIterator(hostsFile, "UTF-8");
        try {
            while (hostsIt.hasNext()) {
                String host = hostsIt.nextLine();
                File userPassFile = new File("user_pass.txt");
                if (userPassFile.exists()) {
                    // Try all combinations in user_pass.txt (user;pass per line)
                    LineIterator userPassIt = FileUtils.lineIterator(userPassFile, "UTF-8");
                    try {
                        while (userPassIt.hasNext()) {
                            String[] userPass = userPassIt.nextLine().split(";");
                            pool.execute(new Brute(host, userPass[0], userPass[1], domain));
                        }
                    } finally {
                        userPassIt.close();
                    }

                } else {
                    // Single username and password test
                    pool.execute(new Brute(host, cmd.getOptionValue("username"), cmd.getOptionValue("password"), domain));
                }

            }
        } finally {
            hostsIt.close();
        }
        pool.shutdown();
        pool.awaitTermination(25, TimeUnit.SECONDS);
        System.out.println("End");
    }

    private static CommandLine getParsedCommandLine(String[] args) {
        Options options = new Options();

        Option username = new Option("u", "username", true, "Username to test");
        username.setRequired(false);
        options.addOption(username);

        Option password = new Option("p", "password", true, "Password to test");
        password.setRequired(false);
        options.addOption(password);

        Option domain = new Option("d", "domain", true, "domain");
        domain.setRequired(false);
        options.addOption(domain);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("smb", options);
            return null;
        }
        return cmd;
    }

}


