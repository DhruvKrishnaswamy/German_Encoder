package enigma;

import java.util.ArrayList;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Dhruv Krishnaswamy
 */
class Alphabet {

    /** A private string containing different characters. */
    private String _cha;

    /** A private arraylist containing different characters. */
    private ArrayList<Character> _ch;

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    Alphabet(String chars) {
        _cha = chars;
        int len = chars.length();

        ArrayList ch = new ArrayList<Character>();
        _ch = ch;
        char[] characs = chars.toCharArray();

        for (char c : characs) {
            {
                int i = 0;
                while (i < _ch.size()) {
                    if (_ch.get(i) != c) {
                        i = i + 1;
                    } else {
                        throw new EnigmaException("The character exists");
                    }
                }
                _ch.add(c);
            }
        }
    }



    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {
        return _ch.size();
    }

    /** Returns true if preprocess(CH) is in this alphabet. */
    boolean contains(char ch) {
        for (char c : _ch) {
            if (c == ch) {
                return true;
            }
        }
        return false;
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        if (0 <= index && index < size()) {
            return _ch.get(index);
        } else {
            throw new EnigmaException("Invalid input");
        }
    }

    /** Returns the index of character preprocess(CH), which must be in
     *  the alphabet. This is the inverse of toChar(). */
    int toInt(char ch) {

        int val =  _ch.indexOf(ch);

        if (val == -1) {
            throw new EnigmaException("Character not found ");
        }
        return val;
    }
}


