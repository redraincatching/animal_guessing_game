import javax.swing.*;
import java.io.*;

public class guessingApp {
    private BinaryTree<String> tree = new BinaryTree<>(); // has to be a proper binary tree
    private final String filepath = "C:\\Users\\eidhn\\eclipse-workspace\\guessing-game\\src\\folder\\";

    public guessingApp() {
        // this is bad encapsulation, but honestly it's fine, this only really does one thing so i'm just writing it functionally
    }

    public static void main(String[] args) {
        guessingApp app = new guessingApp();
        // start a tree
        app.initialise();   // this is just until loading is implemented
        Boolean running;
        do {
            running = app.run();
        } while (running);
    }

    public Boolean run() {
        // joptionpane with 4 custom options
        String[] options = {"play","load", "save", "quit"};
        int result = JOptionPane.showOptionDialog(null, "what would you like to do?", "Guessing Game", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
        switch ( result ) {
            case 0:
                play();
                break;
            case 1:
                loadTree();
                break;
            case 2:
                saveTree();
                break;
            default:
                return false;
        }
        return true;
    }

    // main functions
    public void play() {
        BinaryNode<String> curr = (BinaryNode<String>) tree.getRootNode();
        String message;
        String title = "Guessing Game";

        do {
            // if yes go left, if no go right
            message = curr.getData();
            if ( JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION ) {
                curr = (BinaryNode<String>) curr.getLeftChild();
            } else {
                curr = (BinaryNode<String>) curr.getRightChild();
            }
        } while (curr.hasLeftChild());  // proper binary tree so no need to check both

        // now that we're at a leaf, check if answer is correct or not
        // also regex just to make sure it's a + consonant or an + vowel
        message = "Is your animal a" + (curr.getData().matches("^[aeiouAEIOU].*") ? "n " : " ") + curr.getData() + "?";
        if (JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(null, "i win!");
        } else {
            addAnimal(curr);
        }
    }

    public void loadTree() {
        // overloaded methods :)
        String filename = JOptionPane.showInputDialog(null, "enter a filename");
        loadTree(filename);
    }

    public void loadTree(String filename) {
        // recover binary tree from text file

        File f = new File(filepath + filename + ".txt");
        if (f.exists()) {
            try {
                FileInputStream fIn = new FileInputStream(f);
                ObjectInputStream objIn = new ObjectInputStream(fIn);
                tree = (BinaryTree<String>) objIn.readObject();
                objIn.close();
                fIn.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "there is no file of that name that exists");
        }
    }

    public void saveTree() {
        // save binary tree to text file
        // any binary tree is recoverable from two traversals, however it's serialisable so that's what i'm using
        String filename = JOptionPane.showInputDialog(null, "enter a filename");
        try {
            FileOutputStream fOut = new FileOutputStream(filepath + filename + ".txt");
            ObjectOutputStream objOut = new ObjectOutputStream(fOut);
            objOut.writeObject(tree);
            objOut.close();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // helper functions
    public void initialise() {
        // load from the sample tree by default
        loadTree("tree");
    }

    public void addAnimal(BinaryNode<String> node) {
        String originAnimal = node.getData();   // puns > readability
        String newAnimal = JOptionPane.showInputDialog(null, "i don't know, what is the correct answer?");
        String question = JOptionPane.showInputDialog(null, "what distinguishes this animal from a" + (originAnimal.matches("^[aeiouAEIOU].*") ? "n " : " ") + originAnimal + "?");

        if (question != null && newAnimal != null) {
            node.setData(question);
            node.setLeftChild(new BinaryNode<>(newAnimal));
            node.setRightChild(new BinaryNode<>(originAnimal));
        }
    }
}
