package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Dhruv Krishnaswamy
 */
class MovingRotor extends Rotor {

    /**
     * A rotor named NAME whose permutation in its default setting is
     * PERM, and whose notches are at the positions indicated in NOTCHES.
     * The Rotor is initally in its 0 setting (first character of its
     * alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        this._notches = notches;
    }


    @Override
    boolean rotates() {
        return true;
    }

    @Override
    public void advance() {
        int s = setting();
        set(permutation().wrap(s + 1));

    }

    @Override
    boolean atNotch() {
        for (int x = 0; x < _notches.length(); x = x + 1) {
            if (alphabet().toInt(_notches.charAt(x)) == setting()) {
                return true;
            }
        }
        return false;
    }
    /** This is a private string with all the notches. */
    private String _notches;
}
