package org.cmayes.hartree.parser;

import static org.kohsuke.args4j.ExampleMode.ALL;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.Lexer;
import org.antlr.runtime.Token;
import org.cmayes.hartree.parser.gaussian.antlr.CalcResultParser;
import org.cmayes.hartree.parser.gaussian.antlr.Gaussian09Lexer;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmayes.common.exception.EnvironmentException;

/**
 * Debugging tool that prints out the token stream generated by the lexer
 * (Gaussian09Lexer by default). Users may make subclasses that override
 * getLexer to use different lexers.
 * 
 * @author cmayes
 */
public class AntlrTokenStreamPrinter {
    /** Logger. */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(AntlrTokenStreamPrinter.class);
    /** The file to tokenize. */
    private File file;

    /**
     * Creates an instance of the lexer. Users may override this method to use a
     * different lexer.
     * 
     * @return The lexer to use for token extraction.
     */
    protected Lexer createLexer() {
        return new Gaussian09Lexer();
    }

    /**
     * Creates an instance of the lexer. Users may override this method to use a
     * different lexer.
     * 
     * @return The lexer to use for token extraction.
     */
    protected String[] getTokenNames() {
        return CalcResultParser.tokenNames;
    }

    /**
     * Sets the file if it is readable.
     * 
     * @param theFile
     *            The file to read.
     * @throws IllegalArgumentException
     *             If the file is not readable.
     */
    @Option(aliases = { "-f" }, name = "--file", usage = "The file to process", metaVar = "TOKENFILE", required = true)
    public void setFile(final File theFile) {
        if (theFile.canRead()) {
            this.file = theFile;
        } else {
            throw new IllegalArgumentException(String.format(
                    "File %s is not readable", theFile.getAbsolutePath()));
        }
    }

    /**
     * TODO: Make abstract main?
     * 
     * @param args
     *            The CLI arguments.
     */
    public static void main(final String... args) {
        try {
            new AntlrTokenStreamPrinter().doMain(args);
        } catch (final CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println(String.format(
                    "java %s [options...] arguments...",
                    AntlrTokenStreamPrinter.class.getName()));
            e.getParser().printUsage(System.err);
            System.err.println();
            System.err.println(String.format("Example: %s %s",
                    AntlrTokenStreamPrinter.class.getName(), e.getParser()
                            .printExample(ALL)));
            System.exit(1);
        } catch (final Exception e) {
            LOGGER.error("Top-level exception caught", e);
            System.err.println(e.getMessage());
            System.exit(2);
        }
    }

    /**
     * Performs argument processing and command dispatching.
     * 
     * @param args
     *            The arguments to process.
     * @throws CmdLineException
     *             When there are problems processing the command line.
     */
    public void doMain(final String... args) throws CmdLineException {
        final CmdLineParser parser = new CmdLineParser(this);

        parser.parseArgument(args);
        final Lexer lexer = createLexer();
        try {
            lexer.setCharStream(new ANTLRReaderStream(new FileReader(file)));
        } catch (final FileNotFoundException e) {
            throw new IllegalArgumentException(String.format(
                    "File %s not found", file.getAbsolutePath()), e);
        } catch (final IOException e) {
            throw new EnvironmentException(String.format(
                    "Problems reading file %s", file.getAbsolutePath()), e);
        }
        final CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();
        for (Object o : tokens.getTokens()) {
            final Token token = (Token) o;
            if (!(Gaussian09Lexer.WS == token.getType())) {
                System.out.println("=======================================\n"
                        + "EmbeddedCode = " + token.getText());
                if (token.getType() > 0) {
                    System.out.println("\nType: "
                            + getTokenNames()[token.getType()]);
                } else {
                    System.out.println("EOF");
                }
            }
        }
    }
}
