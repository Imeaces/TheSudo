package io.github.silvigarabis.thesudo;

import java.util.regex.*;
import java.util.*;

public class CommandUtil {
    public static class ArgumentsParseResult {
        public boolean hasError = false;
        public ArgumentsParseErrorType errorType = ArgumentsParseErrorType.NONE;
        public List<String> args = null;
        public String rawCommand = null;
        public int incompleteEscapeIndex = -1;
        public int unterminatedQuoteStartIndex = -1;
        public static enum ArgumentsParseErrorType {
            NONE, UNTERMINATED_QUOTE, UNTERMINATED_SINGLE_QUOTE, INCOMPLETE_ESCAPE;
        }
        public ArgumentsParseResult(String rawCommand, List<String> args, int dquotedStartIndex, int squotedStartIndex, int escapeIndex){
            if (escapeIndex != -1){
                incompleteEscapeIndex = escapeIndex;
                errorType = ArgumentsParseErrorType.INCOMPLETE_ESCAPE;
            }
            if (squotedStartIndex != -1){
                unterminatedQuoteStartIndex = squotedStartIndex;
                errorType = ArgumentsParseErrorType.UNTERMINATED_SINGLE_QUOTE;
            } else if (dquotedStartIndex != -1){
                unterminatedQuoteStartIndex = dquotedStartIndex;
                errorType = ArgumentsParseErrorType.UNTERMINATED_QUOTE;
            }
            if (errorType != ArgumentsParseErrorType.NONE){
                hasError = true;
            }
            this.args = Collections.unmodifiableList(args);
            this.rawCommand = rawCommand;
        }
    }

    /**
     * 解析命令参数。
     */
    public static ArgumentsParseResult parseArguments(String command){
        List<String> args = new ArrayList<>();
        List<String> chars = unicodeSplit(command);

        Pattern matchSpace = Pattern.compile("\\s+");

        int i = 0;
        String dquotedChars = "";
        String squotedChars = "";
        int dquotedStartIndex = -1;
        int squotedStartIndex = -1;
        int escapeIndex = -1;
        while (i < chars.size()){
            while (i < chars.size() && matchSpace.matcher(chars.get(i)).matches()){
                i += 1;
            }
            String carg = null;
            boolean quoted = false;
            boolean squoted = false;
            boolean dquoted = false;
            while ( // an complex condition: chars still available, and no quote or empty char
                i < chars.size()
                && (
                    quoted || squoted || dquoted
                    || (! matchSpace.matcher(chars.get(i)).matches())
                )
            ){
                boolean valid = false;
                String character = chars.get(i);

                if (quoted){
                    valid = true;
                    quoted = false;
                    escapeIndex = -1;
                } else if (character.equals("\\") && !squoted){
                    quoted = true;
                    escapeIndex = i;
                } else if (character.equals("\"") && !squoted){
                    dquoted = !dquoted;
                    dquotedStartIndex = dquoted ? i : -1;
                } else if (character.equals("'") && !dquoted){
                    squoted = !squoted;
                    squotedStartIndex = squoted ? i : -1;
                } else {
                    valid = true;
                }
                
                if (valid){
                    if (carg == null && !(squoted || dquoted)){
                       carg = "";
                    }

                    if (squoted){
                        squotedChars += character;
                    } else if (dquoted){
                        dquotedChars += character;
                    } else {
                        carg += chars.get(i);
                    }
                    
                    if (!squoted && squotedChars.length() > 0){
                        carg += squotedChars;
                        squotedChars = "";
                    } else if (!dquoted && dquotedChars.length() > 0){
                        carg += dquotedChars;
                        dquotedChars = "";
                    }
                }
                
                i += 1;
            }
            
            if (carg != null){
                args.add(carg);
            }
        }

        return new ArgumentsParseResult(command, args, squotedStartIndex, dquotedStartIndex, escapeIndex);
    }

    /**
     * 简单的解析命令参数，而不处理错误。
     */
    public static List<String> parseArgumentsSimply(String command){
        List<String> args = new ArrayList<>();
        List<String> chars = unicodeSplit(command);

        Pattern matchSpace = Pattern.compile("\\s+");

        int i = 0;
        while (i < chars.size()){
            while (i < chars.size() && matchSpace.matcher(chars.get(i)).matches()){
                i += 1;
            }
            String carg = null;
            boolean qouted = false;
            boolean sqouted = false;
            boolean dqouted = false;
            while ( // an complex condition: chars still available, and no quote or empty char
                i < chars.size()
                && (
                    qouted || sqouted || dqouted
                    || (! matchSpace.matcher(chars.get(i)).matches())
                )
            ){
                boolean valid = false;

                if (qouted){
                    valid = true;
                    qouted = false;
                } else if (chars.get(i).equals("\\") && !sqouted){
                    qouted = true;
                } else if (chars.get(i).equals("\"") && !sqouted){
                    dqouted = !dqouted;
                } else if (chars.get(i).equals("'") && !dqouted){
                    sqouted = !sqouted;
                } else {
                    valid = true;
                }
                
                if (valid){
                    if (carg == null){
                       carg = "";
                    }
                    carg += chars.get(i);
                }
                
                if (carg == null && (qouted || sqouted || dqouted)){
                   carg = "";
                }
                
                i += 1;
            }
            
            if (carg != null){
                args.add(carg);
            }
        }

        return args;
    }

    public static List<String> unicodeSplit(String text){
        List<String> unicodeChars = new ArrayList<>();

        for (int i = 0; i < text.length(); i++){
            int codePoint = text.codePointAt(i);
            if (codePoint > 0xffff){
                i += 1;
            }
            
            unicodeChars.add(Character.toString(codePoint));
        }
        
        return unicodeChars;
    }
}
