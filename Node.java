public class Node implements Comparable<Node>
{
    Node left;
    Node right;
    Integer freq; //frequency of a letter
    Character letter;

    public Node()
    {
        this(0, null);
    }

    public Node(int f, Character ch)
    {
        freq = f;
        letter = ch;
        left = null;
        right = null;
    }

    public String toString()
    {
        return freq + ": " + letter;
    }

    public int compareTo(Node other)
    {
        return (Integer)this.freq.compareTo((Integer)other.freq);
    }
}
