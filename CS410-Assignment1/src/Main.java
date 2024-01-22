import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


class DFA {
     int numberOfStates;
     int numberOfVariables;
     int numberOfFinalStates;
     ArrayList<String> states;
     ArrayList<String> finalStates;
     ArrayList<String> variables;
     ArrayList<String> transitionLines;

    public DFA(int numberOfStates, int numberOfVariables, int numberOfFinalStates, ArrayList<String> states, ArrayList<String> finalStates, ArrayList<String> variables, ArrayList<String> transitionLines) {
        this.numberOfStates = numberOfStates;
        this.numberOfVariables = numberOfVariables;
        this.numberOfFinalStates = numberOfFinalStates;
        this.states = states;
        this.finalStates = finalStates;
        this.variables = variables;
        this.transitionLines = transitionLines;
    }

    public void stringCheckList(ArrayList<String> inputList, PrintWriter outputWriter) {
        for (int i = 0; i < inputList.size(); i++) {
            String input = inputList.get(i);
            System.out.println("\nInput string: " + input);
            stringCheck(input, outputWriter);
        }
    }

    private void stringCheck(String input, PrintWriter outputWriter) {
        String currentState = states.get(0);

        System.out.print("States visited:");
        outputWriter.println("Input string: " + input);
        outputWriter.print("States visited:");

        for (int i = 0; i < input.length(); i++) {
            String symbol = String.valueOf(input.charAt(i));
            String transition = findTransition(currentState, symbol);
            if (transition == null) {
                System.out.println("\nRejected");
                outputWriter.println("\nRejected");
                return;
            }

            currentState = transition;
            System.out.print(" " + currentState);
            outputWriter.print(" " + currentState);
        }

        if (finalStates.contains(currentState)) {
            System.out.println("\nAccepted");
            outputWriter.println("\nAccepted");
        } else {
            System.out.println("\nRejected");
            outputWriter.println("\nRejected");
        }
    }

    private String findTransition(String state, String symbol) {
        for (int i = 0; i < transitionLines.size(); i++) {
            String transition = transitionLines.get(i);
            String[] partionedTransition = transition.split(" ");
            if (partionedTransition[0].equals(state) && partionedTransition[1].equals(symbol)) {
                return partionedTransition[2];
            }
        }
        return null;
    }
}

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter input file name: ");
        String filePath = scanner.nextLine();

        //String filePath = "input.txt";

        ArrayList<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }




        if (!lines.isEmpty()) {


            //FROM INPUT.TXT:
            //firstLine -> parseInt -> numberOfState
            //secondLine -> parseInt -> numberOfVariables
            //thirdLine -> parseInt -> numberOfGoalState
            //fourthLine -> ArrayList<String> statesList
            //fifthLine -> ArrayList<String> finalStateList
            //sixthLine -> ArrayList<String> variablesList
            // from 6 to transtionLineCount+6 -> ArrayList<String> transitionLines
            // from transtionLineCount+6 to end of the input file-> ArrayList<String> inputLines


            String firstLine = lines.get(0).trim();
            String secondLine = lines.get(1).trim();
            String thirdLine = lines.get(2).trim();
            String fourthLine = lines.get(3);
            String fifthLine = lines.get(4);
            String sixthLine = lines.get(5);

            try {
                int numberOfState = Integer.parseInt(firstLine);
                int numberOfVariables = Integer.parseInt(secondLine);
                int numberOfGoalState = Integer.parseInt(thirdLine);

                ArrayList<String> statesList = new ArrayList<>(Arrays.asList(fourthLine.split(" ")));
                ArrayList<String> finalStatesList = new ArrayList<>(Arrays.asList(fifthLine.split(" ")));
                ArrayList<String> variablesList = new ArrayList<>(Arrays.asList(sixthLine.split(" ")));

                int transitionLineCount = numberOfState * numberOfVariables;

                ArrayList<String> transitionLines = new ArrayList<>();
                for (int i = 6; i < 6 + transitionLineCount && i < lines.size(); i++) {
                    String transitionLine = lines.get(i).trim();
                    transitionLines.add(transitionLine);
                }

                System.out.print("Enter output file name: ");
                String outputFileName = scanner.nextLine();

            try (PrintWriter outputWriter = new PrintWriter(outputFileName)) {
                DFA dfa = new DFA(numberOfState, numberOfVariables, numberOfGoalState, statesList, finalStatesList, variablesList, transitionLines);
                ArrayList<String> inputLines = new ArrayList<>();
                int startLine = transitionLineCount + 6;
                for (int i = startLine; i < lines.size(); i++) {
                    String inputLine = lines.get(i).trim();
                    inputLines.add(inputLine);
                }

                dfa.stringCheckList(inputLines, outputWriter);

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

                System.out.println();


            } catch (NumberFormatException e) {
                System.err.println("error");
                e.printStackTrace();
            }
        }
    }
}
