package com.thibclnt.chessgame;


import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Integer.parseInt;

public class ChessConsole implements ChessGameInterface{

    private final static Scanner in = new Scanner(System.in);
    private final ChessGame game;
    private boolean running = false;

    private final static Map<Integer, String> intToLettersMap = Stream.of(
            new Object[][] {
                    { 1, "A" },
                    { 2, "B" },
                    { 3, "C" },
                    { 4, "D" },
                    { 5, "E" },
                    { 6, "F" },
                    { 7, "G" },
                    { 8, "H" },
                }).collect(Collectors.toMap(data -> (Integer) data[0], data -> (String) data[1]));
    private final static Map<String, Integer> lettersToIntMap = intToLettersMap.entrySet().stream()
            .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));

    public ChessConsole() {
        this.game = new ChessGame(this);
        this.loop();
    }

    @Override
    public ChessPiece.ChessPieceType askForPromotion(Pawn pawn) {
        // TODO
        return null;
    }

    @Override
    public void tellCheck() {
        // TODO
    }

    @Override
    public void init() {
        System.out.println("==============================");
        System.out.println("=== Welcome in CHESS GAME ====");
        System.out.println("==============================");
        System.out.println("\nIf you want to quit at one moment, just type 'QUIT'");
    }

    private void reset() {
        this.game.reset();
        this.game.start();
        this.loop();
    }

    private void stop() {
        this.game.stop();
        this.running = false;
        this.printSeparator();
        System.out.println("EXIT");
    }

    private void loop() {
        this.running = true;
        this.draw();

        while (running) {
            askForMove();
        }
    }

    @Override
    public void draw() {
        this.printSeparator();

        System.out.println("    A     B     C     D     E     F     G     H");
        System.out.println(" -------------------------------------------------");
        boolean white = true;

        for (int y=16 ; y > 0 ; y--) {
            if (y % 2 == 0) {
                System.out.print(y / 2 + "|");
            } else {
                System.out.print(" |");
            }

            for (int x=1 ; x < 9 ; x++) {
                String c = (white)? "█" : " ";
                ChessPiece p = this.game.getBoard().getPieceAt(new Pos(x, (y + 1) / 2));

                if (y % 2 == 0) {
                    System.out.print((p == null) ? c + c + c + c + c + "|" :
                            c + (p.getPlayer().getColor() == Player.COLOR.WHITE ? "wht" : "blk") + c + "|");
                } else {
                    System.out.print((p == null) ? c + c + c + c + c + "|" :
                            ( c + p.getType().getLetter() + c + "|"));
                }
                white = !white;
            }
            if (y % 2 == 1)
                white = !white;

            System.out.print((y % 2 == 0 ? y / 2 : "") + "\n");
        }
        System.out.println(" -------------------------------------------------");
        System.out.println("    A     B     C     D     E     F     G     H");
    }

    @Override
    public void askForMove() {
        String input = "";
        Pos pos = null;

        do {
            if (!input.isEmpty()) {
                System.out.println("Please enter a valid pos for a piece that is yours (format example : A4)");
            }

            System.out.println("\n" + this.game.getPlayerTurn().getName() + ", please chose a pos in the board (format example : A4)");
            input = in.nextLine().toUpperCase();

            try {
                if (input.length() == 2
                        && lettersToIntMap.containsKey(input.substring(0, 1))
                        && lettersToIntMap.containsValue(parseInt(input.substring(1)))) {
                    pos = new Pos(lettersToIntMap.get(input.substring(0, 1)), parseInt(input.substring(1)));
                }
            } catch (NumberFormatException ignored) {}

            if (input.equals("QUIT")) {
                this.stop();
                return;
            }

        } while (pos == null);

        this.game.posChosen(pos);
    }

    @Override
    public void declareWinner(Player player) {
        this.printSeparator();

        if (player.getColor() == Player.COLOR.WHITE) {
            System.out.println("██    ██ ██  ██████ ████████  ██████  ██████  ██    ██     ██     ██ ██   ██ ██ ████████ ███████");
            System.out.println("██    ██ ██ ██         ██    ██    ██ ██   ██  ██  ██      ██     ██ ██   ██ ██    ██    ██");
            System.out.println("██    ██ ██ ██         ██    ██    ██ ██████    ████       ██  █  ██ ███████ ██    ██    █████");
            System.out.println(" ██  ██  ██ ██         ██    ██    ██ ██   ██    ██        ██ ███ ██ ██   ██ ██    ██    ██");
            System.out.println("  ████   ██  ██████    ██     ██████  ██   ██    ██         ███ ███  ██   ██ ██    ██    ███████");
        } else {
            System.out.println("██    ██ ██  ██████ ████████  ██████  ██████  ██    ██     ██████  ██       █████   ██████ ██   ██ ");
            System.out.println("██    ██ ██ ██         ██    ██    ██ ██   ██  ██  ██      ██   ██ ██      ██   ██ ██      ██  ██  ");
            System.out.println("██    ██ ██ ██         ██    ██    ██ ██████    ████       ██████  ██      ███████ ██      █████   ");
            System.out.println(" ██  ██  ██ ██         ██    ██    ██ ██   ██    ██        ██   ██ ██      ██   ██ ██      ██  ██  ");
            System.out.println("  ████   ██  ██████    ██     ██████  ██   ██    ██        ██████  ███████ ██   ██  ██████ ██   ██");
        }
        askForContinue();
    }

    @Override
    public void declareNull() {
        this.printSeparator();

        System.out.println("███    ██ ██    ██ ██      ██     ");
        System.out.println("████   ██ ██    ██ ██      ██     ");
        System.out.println("██ ██  ██ ██    ██ ██      ██     ");
        System.out.println("██  ██ ██ ██    ██ ██      ██     ");
        System.out.println("██   ████  ██████  ███████ ███████");

        askForContinue();
    }

    private void askForContinue() {
        this.printSeparator();

        String input;
        while (true) {
            System.out.println("Do you want to continue ? (y: yes / n: no)");
            input = in.nextLine().toUpperCase();

            if (input.equals("Y")) {
                this.reset();
                return;
            } else if (input.equals("N") || input.equals("QUIT")) {
                this.stop();
                return;
            }

            System.out.println("\nPlease enter a valid answer :");
        }

    }

    @Override
    public void displayLegalMove(Set<Pos> legalMoves) {
        this.printSeparator();
        System.out.print("Legal Moves : {");
        for (Pos p : legalMoves) {
            System.out.print(intToLettersMap.get(p.getX()) + p.getY() + ",  ");
        }
        if (legalMoves.isEmpty()) {
            System.out.println("NULL");
        }
        System.out.print("}\n");

        askForMove2(legalMoves);
    }

    private void askForMove2(Set<Pos> legalMoves) {
        String input = "";
        Pos pos = null;

        do {
            if (!input.isEmpty()) {
                System.out.println("Please enter a valid move (same format as legal moves above) :");
            }

            System.out.println("\nPlease chose a move from the ones printed above or type CANCEL.");
            input = in.nextLine().toUpperCase();

            try {
                if (input.length() == 2
                        && lettersToIntMap.containsKey(input.substring(0, 1))
                        && lettersToIntMap.containsValue(parseInt(input.substring(1)))) {
                    pos = new Pos(lettersToIntMap.get(input.substring(0, 1)), parseInt(input.substring(1)));
                }
            } catch (NumberFormatException ignored) {}


        } while (!input.equals("QUIT") && !input.equals("CANCEL") && !legalMoves.contains(pos));

        if (input.equals("QUIT")) {
            this.stop();
        } else if (input.equals("CANCEL")) {
            askForMove();
        } else {
            this.game.confirmPos(pos);
        }
    }

    public ChessGame getGame() {
        return this.game;
    }

    private void printSeparator() {
        System.out.println("\n" + new String(new char[60]).replace("\0", "=") + "\n");
    }
}
