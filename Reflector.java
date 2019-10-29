package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a reflector in the enigma.
 *  @author Dhruv Krishnaswamy
 */
class Reflector extends FixedRotor {

    /** A non-moving rotor named NAME whose permutation at the 0 setting
     * is PERM. */
    Reflector(String name, Permutation perm) {
        super(name, perm);
        if (!perm.derangement()) {
            throw new EnigmaException("This value maps to itself");
        }
    }
    /** A method to check whether a rotor is reflecting.
     * @return Boolean values whether a rotor is reflecting
     */

    @Override
    boolean reflecting() {
        return true;
    }
    @Override
    void set(int posn) {
        if (posn != 0) {
            throw new EnigmaException("reflector has only one position");
        }
    }

    @Override
    void set(char pos) {
        if (alphabet().toInt(pos) != 0) {
            throw new EnigmaException("reflector has only one position");
        }
    }

}
