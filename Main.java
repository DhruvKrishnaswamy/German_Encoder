package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.ArrayList;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Dhruv Krishnaswamy
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine mach = readConfig();

        boolean verify = false;

        while (_input.hasNextLine()) {

            String slot = _input.nextLine();

            if (Pattern.matches("\\s*\\*.+", slot)) {

                setUp(mach, slot);

                verify = true;
            } else {
                if (verify) {
                    printMessageLine(mach.convert(slot));
                } else {
                    throw new EnigmaException("File is not right");
                }


            }
        }
    }

    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {

            String letters = _config.next();
            int numrotors = _config.nextInt();

            int npawls = _config.nextInt();
            ArrayList rots = new ArrayList<Rotor>();

            _alphabet = new Alphabet(letters);


            while (_config.hasNext()) {
                rots.add(readRotor());
            }
            return new Machine(_alphabet, numrotors, npawls, rots);

        } catch (NoSuchElementException excp) {

            throw new EnigmaException("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            permus = "";
            String vals = "";
            String rotname = _config.next();
            String temp = (_config.next()).toUpperCase();

            while (_config.hasNext("\\(.+\\)")) {
                vals = vals + _config.next();
            }
            if (!_config.hasNext()) {
                vals = vals.concat(temp + " ");
            }
            Permutation rotorp = new Permutation(vals, _alphabet);

            if (temp.charAt(0) == 'M') {
                return new MovingRotor(rotname, rotorp, temp.substring(1));
            } else if (temp.charAt(0) == 'N') {
                return new FixedRotor(rotname, rotorp);
            } else if (temp.charAt(0) == 'R') {
                return new Reflector(rotname, rotorp);
            } else {
                throw new EnigmaException("bruhhh stop ");
            }
        } catch (NoSuchElementException excp) {
            throw new EnigmaException("bad rotor description");
        }
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {

        String pbperms = "";

        String[] strs = settings.split("\\s");
        if (strs[0].equals("*")) {
            String[] rotlist = new String[M.numRotors()];

            System.arraycopy(strs, 1, rotlist, 0, M.numRotors());
            M.insertRotors(rotlist);
            M.setRotors(strs[M.numRotors() + 1]);
            int x = M.numRotors() + 2;
            while (x < strs.length) {
                if (Pattern.matches("\\(.+\\)", strs[x])) {
                    pbperms = pbperms + strs[x];
                    x = x + 1;
                }
                Permutation plugp = new Permutation(pbperms, _alphabet);

                M.setPlugboard(plugp);
            }

        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        String subzz;
        int x = 0;
        while (x + 5 < msg.length()) {
            subzz = msg.substring(x, x + 5);
            System.out.printf("%s ", subzz);
            x = x + 5;
        }
        System.out.printf("%s\n", msg.substring(x, msg.length()));
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
    /** An ArrayList containing all rotors that can be used. */
    private ArrayList<Rotor> _allTheRotors = new ArrayList<>();

    /** This is a string w/ diff cycles. */
    private String permus;

    /** Name of the current rotor. */
    private String namerot;

    /** Type && notches of the present rotor. */
    private String notches;

    /** Worked on this class with students from the project party. */
}

