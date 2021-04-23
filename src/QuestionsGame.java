/**
 * This class represents a game of 20 Questions. It keeps track of a binary tree whose nodes represent questions and
 * answers. (Every node’s data is a string representing the text of the question or answer.) The leaves of the tree
 * represent possible answers (guesses) that the computer might make. All the other nodes represent questions that the
 * computer will ask to narrow the possibilities. The left branch indicates the next question the computer asks if the
 * answer is yes, and the right branch is the next question if the answer is no. Note that even though the name of the
 * game is “20 questions”, the computer will not be limited to only twenty; the tree may have a larger height.
 */

import java.io.*;
import java.util.*;

public class QuestionsGame {
    private QuestionNode root;

    // This constructor should initialize a new QuestionsGame object with a single leaf node representing the object object.
    // You may assume the String is not null. To get QuestionMain to use this constructor, you should supply an empty file
    // or one that doesn't exist.
    public QuestionsGame(String object) {
        root = new QuestionNode(object);
    }

    // This constructor should initialize a new QuestionsGame object by reading from the provided Scanner containing a
    // tree of questions in standard format. You may assume the Scanner is not null and is attached to a legal, existing
    // file in standard format. Make sure to read entire lines of input using calls on Scanner's nextLine.
    public QuestionsGame(Scanner input) {
        root = readFile(input);
    }

    private QuestionNode readFile (Scanner input) {
        if (input.nextLine().equals("A:")) {
            return new QuestionNode(input.nextLine());
        } else {
            return new QuestionNode(input.nextLine(), readFile(input), readFile(input));
        }
    }

    // This method should store the current questions tree to an output file represented by the given PrintStream.
    // This method can be used to later play another game with the computer using questions from this one. You should
    // throw an IllegalArgumentException if the PrintStream is null.
    public void saveQuestions(PrintStream output) {
        if (output == null) {
            throw new IllegalArgumentException();
        }
        saveQuestionsHelper(output, root);
    }

    private void saveQuestionsHelper(PrintStream output, QuestionNode current) {
        if (current.right == null) {
            output.println("A:");
            output.println(current.data);
        } else {
            output.println("Q:");
            output.println(current.data);
            saveQuestionsHelper(output, current.left);
            saveQuestionsHelper(output, current.right);
        }
    }

    // This method should use the current question tree to play one complete guessing game with the user, asking yes/no
    // questions until reaching an answer object to guess. A game begins with the root node of the tree and ends upon
    // reaching an answer leaf node.
    // If the computer wins the game, this method should print a message saying so.
    // Other, this method should ask the user for the following:
    // what object they were thinking of
    // a question to distinguish that object from the player's guess, and
    // whether the player's object is the yes or no answer for that question.
    public void play(Scanner input) {
       root = playHelper(root, input);
    }

    private QuestionNode playHelper(QuestionNode current, Scanner input) {
        if (current.right == null) {
            System.out.println("I guess that your object is " + current.data + "!");
            System.out.print("Am I right? (y/n)? ");
            if (input.nextLine().trim().toLowerCase().startsWith("y")) {
                System.out.println("Awesome! I win!");
            } else {
                System.out.println("Boo! I Lose.  Please help me get better!");
                System.out.print("What is your object? ");
                String userAnswer = input.nextLine();
                System.out.println("Please give me a yes/no question that distinguishes between " + userAnswer + " and " + current.data + ".");
                String userQuestion = input.nextLine();
                System.out.print ("Is the answer \"yes\" for "+ userAnswer + "? (y/n)? ");
                if (input.nextLine().trim().toLowerCase().startsWith("y")) {
                    current = new QuestionNode(userQuestion, new QuestionNode(userAnswer), current);
                } else {
                    current = new QuestionNode(userQuestion, current, new QuestionNode(userAnswer));
                }
            }
        } else if (current.right != null){
            System.out.print(current.data + " (y/n)? ");
            if (input.nextLine().trim().toLowerCase().startsWith("y")) {
                current.left = playHelper(current.left, input);
            } else {
                current.right = playHelper(current.right, input);
            }
        }
        return current;
    }

    private static class QuestionNode {
        public String data;
        public QuestionNode left;
        public QuestionNode right;

        public QuestionNode(String text) {
            this(text, null, null);
        }

        public QuestionNode(String textSet, QuestionNode yes, QuestionNode no) {
            data = textSet;
            left = yes;
            right = no;
        }
    }
}