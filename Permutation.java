package enigma;


import java.util.ArrayList;
import java.util.HashMap;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Dhruv Krishnaswamy
 */
class Permutation {
    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;

        newlist = new ArrayList<>();
        for (int x = 0; x < _alphabet.size(); x = x + 1) {
            newlist.add(x);
        }
        _newHash = new HashMap();
        _reversedHashMap = new HashMap();

        cycles = cycles.replaceAll("[ )(]", " ");
        String[] cyclear;
        cyclear = cycles.split("  ");
        _cyclear = cyclear;
        for (String c : _cyclear) {
            addCycle(c);

        }
        for (char i : _newHash.keySet()) {
            _reversedHashMap.put(_newHash.get(i), i);
        }

        for (Character name: _newHash.keySet()) {
            String key = name.toString();
            String value = _newHash.get(name).toString();
            System.out.println(key + " " + value);
        }

    }


    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        for (int i = 0; i < cycle.length(); i = i + 1) {

            if (i == cycle.length() - 1) {
                _newHash.put(cycle.charAt(i),  cycle.charAt(0));
            } else {
                _newHash.put(cycle.charAt(i), cycle.charAt(wrap(i + 1)));
            }
        }
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r = r + size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {

        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        if (p < 0) {
            throw new EnigmaException("Illegal Argument ");
        } else {
            char letter = _newHash.get(_alphabet.toChar(wrap(p)));
            return _alphabet.toInt(letter);
        }
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        if (c < 0) {
            throw new EnigmaException("Illegal Argument ");
        } else {
            char revletter = _reversedHashMap.get(_alphabet.toChar(wrap(c)));
            return _alphabet.toInt(revletter);
        }

    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        return _newHash.get(p);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        return _reversedHashMap.get(c);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        boolean b = true;
        for (Character name : _newHash.keySet()) {
            String key = name.toString();
            String value = _newHash.get(name).toString();

            if (key.equals(value)) {
                return false;
            }
        }
        return b;
    }



    /** Alphabet of this permutation. */
    private Alphabet _alphabet;
    /** Cycles of the letters. */
    private String _cycles;
    /** Arraylist containing perms. */
    private ArrayList<Integer> newlist;
    /** Hashmap containing perms. */
    private HashMap<Character, Character> _newHash;
    /** This has the perms. */
    private String [] _cyclear;
    /** Reversed Hashmap containing perms. */
    private HashMap<Character, Character> _reversedHashMap;


}

