import java.io.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static boolean continueQuiz()
    {
        boolean continueUserAnswer = false;
        System.out.println("Please pick true if you wish to continue, false otherwise");
        while (true) {
            try {
                Scanner continueQuestioner = new Scanner(System.in);
                continueUserAnswer = continueQuestioner.nextBoolean();
                break;
            } catch (InputMismatchException | NumberFormatException ex) {
                System.err.println("Invalid input, your answer must be 'false' or 'true'");
            }
        }
        return continueUserAnswer;
    }


    public static void showQuestions(String [] question)
    {
        boolean correct = false;
        int userAnswer = -1;
        int random = (int )(Math.random() * 4); // Generates random sequence
        System.out.println("Question:\n" + question[0] + "\n\nPossible answers:");

        for (int i=0; i<=3; i++)
        {
            System.out.println(i+1 + ".)   " + question[((random+i)%4)+1]);
        }
        while (!(userAnswer>=1 && userAnswer<=4)){
            System.out.println("Please pick your answer,   input: 1/2/3/4");
            while (true) {
                try {
                    Scanner answer = new Scanner(System.in);
                    userAnswer = answer.nextInt();
                    break;
                } catch (InputMismatchException | NumberFormatException ex) {
                    System.err.println("Invalid input, your answer must be integers between 1-4");
                }
            }
        }
        if ( ((random+userAnswer-1)%4)+1 == 1)
            correct = true;

        System.out.println("Your answer is: " + correct +
                "\nYour answer: "+question[((random+userAnswer-1)%4)+1] +
                "\nThe correct answer is: " + question[1]);
    }


    public static void main(String[] args) throws IOException {

        String [] question = new String[5];
        String strLine;
        int counter=-1;
        FileInputStream fstream = new FileInputStream("src\\main\\java\\exam.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        while ((strLine = br.readLine()) != null)
        {
            counter = 0;
            question[counter++] = strLine;
            while (counter<=4){
                strLine = br.readLine();
                if (strLine == null){break;}
                question[counter++] = strLine;
            }

            if (counter!=5){
                System.out.println("The file format is incorrect");
                System.out.println("First entry is: " + question[0]);
                System.out.println("Last entry is: " + question[counter-1]);
                System.out.println("We will not exit the program for you");
                fstream.close();
                System.exit(1);
            }

            showQuestions(question);

            if (!continueQuiz())
            {
                System.out.println("The user has decided to stop the questioner");
                System.out.println("We will not exit the program for you");
                fstream.close();
                System.exit(0);
            }
        }
        fstream.close();
        if (counter>=0) {System.out.println("Well done, you finished the questioner");}
        else {System.out.println("The questioner was empty, please fill the text file with questions");}
    }
}
