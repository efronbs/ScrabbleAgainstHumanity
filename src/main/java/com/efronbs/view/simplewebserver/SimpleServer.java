package com.efronbs.view.simplewebserver;

import com.efronbs.game.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class SimpleServer {

    private static final Set<String> WORD_SUBMISSION_FIELDS = Set.of(
            "word",
            "player",
            "direction",
            "row",
            "column"
    );

    private final HttpServer server;
    private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
    private final ObjectMapper objectMapper;

    private final GameState state;
    private final GameBoard board;
    private final GameManager manager;

    // TODO should this be injected???
    public SimpleServer(
            GameState gameState,
            GameBoard board,
            GameManager manager
    ) {
        try {
            server = HttpServer.create(new InetSocketAddress("localhost", 8001), 0);
            server.createContext("/turn", new TurnHandler());
            server.createContext("/board", new BoardHandler());
            server.createContext("/submit", new SubmitHandler());
            server.setExecutor(executor);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create server", e);
        }
        this.objectMapper = new ObjectMapper();

        this.state = gameState;
        this.board = board;
        this.manager = manager;
    }

    public void start() throws IOException {
        server.start();
    }

    public void stop() {
        server.stop(10);
    }

    private class TurnHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                throw new IllegalArgumentException("turn request endpoint only handles GET requests");
            }

            String turnPlayer = SimpleServer.this.state.getCurrentTurnPlayer().getName();
            String response = "{ \"turn\": \"" + turnPlayer + "\" }";
            exchange.sendResponseHeaders(200, response.length());
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.flush();
            outputStream.close();
        }
    }

    private class BoardHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                throw new IllegalArgumentException("board state endpoint only handles GET requests");
            }

            String response = SimpleServer.this.board.toString();
            exchange.sendResponseHeaders(200, response.length());
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(response.getBytes());
            outputStream.flush();
            outputStream.close();
        }
    }

    private class SubmitHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                throw new IllegalArgumentException("submit endpoint only handles POST requests");
            }

            // The input data is just a flat set of values, no nested maps
            //noinspection unchecked
            Map<String, String> submission = (Map<String, String>) objectMapper.readValue(exchange.getRequestBody(), Map.class);
            if (!submission.keySet().containsAll(WORD_SUBMISSION_FIELDS)) {
                throw new IllegalArgumentException("Word submissions must be a post request whose body contains the " +
                        "fields: " + WORD_SUBMISSION_FIELDS.toString());
            }

            Player submissionPlayer = state.playerForName(submission.get("player"));
            if (Objects.isNull(submissionPlayer)) {
                throw new IllegalArgumentException("Requested player: " + submission.get("player") + " does not exist");
            }

            boolean success = SimpleServer.this.manager.submitWord(
                    submission.get("word"),
                    submissionPlayer,
                    Direction.valueOf(submission.get("direction").toUpperCase(Locale.ENGLISH)),
                    Integer.parseInt(submission.get("row")),
                    Integer.parseInt(submission.get("column"))
            );

            int responseCode = success ? 200 : 400;

            exchange.sendResponseHeaders(responseCode, 0);
            exchange.getResponseBody().close();
        }
    }
}
