package enigma;

import java.util.ArrayList;
import java.util.Collection;

/** Class that represents a complete enigma machine.
 *  @author Dhruv Krishnaswamy
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {

        _alphabet = alpha;
        _pawlzz = pawls;
        _numrots = numRotors;
        _rotors = new Rotor[allRotors.size()];
        int a = 0;
        for (Rotor r: allRotors) {
            _rotors[a] = r;
            a = a + 1;
        }
    }
    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numrots;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawlzz;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        _rot = new ArrayList<>();

        ArrayList<Rotor> rotnames = new ArrayList<>();

        for (String str : rotors) {
            for (int i = 0; i < _rotors.length; i = i + 1)  {

                if (str.equals(_rotors[i].name())) {
                    _rotors[i].set(0);

                    _rot.add(_rotors[i]);

                    if (rotnames.contains(str)) {

                        throw new EnigmaException("rotors have the same name.");

                    } else {
                        rotnames.add(_rotors[i]);
                    }
                }
            }
        }
        if (rotnames.size() != rotors.length) {
            throw new EnigmaException(" Length of Lists is unequal broski ");
        }
    }

    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {

        for (int i = 0; i < numRotors() - 1; i = i + 1) {
            _rotors[i].set(setting.charAt(i));
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugb = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing

     *  the machine. */
    int convert(int c) {
        for (Rotor r : _rotors) {
            if (r.rotates() && r.atNotch()) {
                r.advance();
            }
        }
        c = c % _alphabet.size();

        if (_plugb != null) {
            c = _plugb.permute(c);
        }
        for (int i = _rot.size() - 1; i >= 0; i = i - 1) {
            Rotor forrot = _rot.get(i);
            c = forrot.convertForward(c);
        }
        for (int x = 1; 1 < _rot.size(); x = x + 1) {
            Rotor backrot = _rot.get(x);
            c = backrot.convertBackward(c);
        }
        if (_plugb != null) {
            c = _plugb.permute(c);
        }
        return c;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {

        String fin = "";
        int x = 0;
        while (x < msg.length()) {
            char z = _alphabet.toChar(convert(_alphabet.toInt(msg.charAt(x))));
            fin = fin + z;
            x  = x + 1;
        }

        return fin;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;


/**  Integer containing number of rotors. */
    private int _numrots;
    /** An arraylist of Rotors to insert & have names of rotors. */
    private ArrayList<Rotor> _rot;

    /**
     * Common rotor pawls.
     */
    private int _pawlzz;

    /**
     * Initial settings of the rotor.
     */

    private Permutation _plugb;


    /** This is the rotor array of all rotors. */
    private Rotor[] _rotors;

    /** Worked on this section with students from the project party. */
}
