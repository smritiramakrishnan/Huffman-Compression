import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class HuffmanCompressor
{
    public static void compress(String fileName) throws FileNotFoundException
    {
        int[] freq = new int[256];
        Scanner fN = null;
        try {
            fN = new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            System.out.println("File(" + fileName + ") not found :(");
        }
        fN.useDelimiter("");
        while(fN.hasNext())
        {
            char temp = (fN.next()).charAt(0);
            freq[(int)temp] += 1;
        }
        HuffmanTree huffTree = new HuffmanTree(freq);
        String newFile = fileName.substring(0, fileName.indexOf(".")) + ".code";
        huffTree.write(newFile);
        huffTree.encode(new BitOutputStream(fileName.substring(0, fileName.indexOf(".")) + ".short"), fileName);

    }

    public static void expand(String codeFile, String fileName) throws FileNotFoundException
    {
        HuffmanTree huffTree = new HuffmanTree(codeFile);
        huffTree.decode(new BitInputStream(fileName.substring(0, fileName.indexOf(".")) + ".short"), fileName.substring(0, fileName.indexOf(".")) + ".new");
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        HuffmanCompressor.compress("short.txt");
        HuffmanCompressor.expand("short.code", "short.new");
    }
}
