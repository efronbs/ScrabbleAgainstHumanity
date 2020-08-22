package com.efronbs;

import com.efronbs.game.*;
import com.efronbs.view.simplewebserver.SimpleServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        Ruleset r = new SimpleRuleset();
        Player p1 = new Player("playerOne");
        Player p2 = new Player("playerTwo");
        GameManager manager = new GameManager(r, List.of(p1, p2), 9);

        serverMode(manager);
    }

    public static void serverMode(GameManager manager) {
        SimpleServer server = new SimpleServer(
                manager.getGameState(),
                manager.getBoard(),
                manager
        );
        try {
            server.start();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to start server. Exiting...");
        }

        // hang forever while server handles connections
        for (;;);
    }

    // ***********************************************
    // TODO: Externalize this code into a local client
    // ***********************************************
    public static void cliMode(GameManager manager) throws IOException {
        // Input management
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.print("cmd> ");
            String input = reader.readLine();

            switch (input) {
                // query who's turn it is
                case "turn?":
                    System.out.println(manager.getGameState().getCurrentTurnPlayer().getName() + "'s turn");
                    break;
                // displays the board state
                case "board":
                    System.out.println(manager.getBoard());
                    break;
                case "submit":
                    handleSubmission(reader, manager);
                    break;
                default:
                    System.out.println(input + "was not a valid command. Valid commands are \"turn?\", \"board\", " +
                            "and \"submit\"");
            }
        }
    }

    public static void handleSubmission(BufferedReader reader, GameManager manager) throws IOException {
        String word = "";
        while (word.isEmpty()) {
            System.out.print("word:" );
            word = reader.readLine().trim();
        }

        String directionAsString = "";
        while(!(Objects.equals("V", directionAsString) || Objects.equals("H", directionAsString))) {
            System.out.print("Direction (V or H): ");
            directionAsString = reader.readLine().trim();
        }
        Direction direction = Objects.equals(directionAsString, "V") ? Direction.VERTICAL : Direction.HORIZONTAL;

        int row = -1;
        while (row < 0) {
            System.out.print("row: ");
            try {
                row = Integer.parseInt(reader.readLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Could not parse input into number. Please try again");
            }

        }

        int column = -1;
        while (column < 0) {
            System.out.print("column: ");
            try {
                column = Integer.parseInt(reader.readLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Could not parse input into number. Please try again");
            }
        }

        boolean successfulSubmit = manager.submitWord(
                word,
                manager.getGameState().getCurrentTurnPlayer(),
                direction,
                row,
                column
        );

        if (!successfulSubmit) {
            System.out.println("Word was invalid, please try again");
        }
    }
}
