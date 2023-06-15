import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class HuffmanTree
{
    private Node tree;
    private HashMap<String, Character> map;
    private String pseudoEOF;

    public HuffmanTree(int[] counts)
    {
        pseudoEOF = "";
        map = new HashMap<String, Character>();
        Queue<Node> temp = new PriorityQueue<Node>();
        for(int i = 0; i < counts.length; i++)
        {
            temp.add(new Node(counts[i], (char)i));
        }
        temp.add(new Node(1, (char)256));
        while(temp.size() > 1)
        {
            Node x = temp.poll();
            Node y = temp.poll();
            Node z = new Node(x.freq + y.freq, null);
            z.left = x;
            z.right = y;
            temp.offer(z);
        }
        tree = temp.poll();
    }

    public HuffmanTree(String codeFile) throws FileNotFoundException
    {
        map = new HashMap<String, Character>();
        tree = new Node();
        Scanner file = new Scanner(new File(codeFile));
        Node iterate = tree;
        while(file.hasNext())
        {
            long character = file.nextLong();
            String ascii = file.next();
            map.put(ascii, (char)character);
            if(character == 256)
            {
                pseudoEOF = ascii;
            }
            for(int i = 0; i < ascii.length(); i++)
            {
                String s = ascii.substring(i, i+1);
                if(s.equals("0"))
                {
                    if(iterate.left == null)
                    {
                        iterate.left = new Node();
                    }
                    iterate = iterate.left;
                }
                if(s.equals("1"))
                {
                    if(iterate.right == null)
                    {
                        iterate.right = new Node();
                    }
                    iterate = iterate.right;
                }
            }
            iterate.letter = (char)character;
            iterate.freq = -1;
            iterate.left = iterate.right = null;
        }
    }

    public void write(String fileName) throws FileNotFoundException
    {
        PrintWriter diskFile = null;
        try {
            diskFile = new PrintWriter(new File(fileName));
        }
        catch (IOException io) {
            System.out.println("Could not create file: " + fileName);
        }
        makeMap(tree, "", diskFile);
        diskFile.close();
    }

    public void makeMap(Node n, String s, PrintWriter fN)
    {
        if(n == null)
        {
            return;
        }
        if(n.letter != null && n.freq != 0)
        {
            map.put(s, n.letter);
            fN.println((int)n.letter);
            fN.println(s);
            if(n.letter == (char)256)
            {
                pseudoEOF = s;
            }
        }
        else
        {
            makeMap(n.left, s + "0", fN);
            makeMap(n.right, s + "1", fN);
        }
    }

    public void encode(BitOutputStream out, String fileName)
    {
        Scanner fN = null;
        try {
            fN = new Scanner(new File(fileName));
        } catch (FileNotFoundException e) {
            System.out.println("File(" + fileName + ") not found :(");
        }
        fN.useDelimiter("");

        while(fN.hasNext())
        {
            char l = fN.next().charAt(0);
            String value = "";
            for(HashMap.Entry<String, Character> entry: map.entrySet())
            {
                if(entry.getValue() == l)
                {
                    value = entry.getKey();
                }
            }
            for(int i = 0; i < value.length(); i++)
            {
                if (value.substring(i, i + 1).equals("0"))
                {
                    out.writeBit(0);
                }
                else
                {
                    out.writeBit(1);
                }
            }
        }
        for(int i = 0; i < pseudoEOF.length(); i++)
        {
            if(pseudoEOF.substring(i, i+1).equals("0"))
            {
                out.writeBit(0);
            }
            else
            {
                out.writeBit(1);
            }
        }
    }

    public void decode(BitInputStream in, String outFile)
    {
        PrintWriter diskFile = null;
        try {
            diskFile = new PrintWriter(new File(outFile));
        }
        catch (IOException io) {
            System.out.println("Could not create file: " + outFile);
        }
        String s = "";
        while(true)
        {
            int temp = in.readBit();
            if(temp < 0)
            {
                break;
            }
            s = s + "" + temp;
            if(map.get(s) != null)
            {
                if(s.equals(pseudoEOF))
                {
                    diskFile.close();
                    return;
                }
                diskFile.print(map.get(s));
                s = "";
            }
        }
        diskFile.close();
    }


}
