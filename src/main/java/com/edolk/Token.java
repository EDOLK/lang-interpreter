package com.edolk;

public class Token {
    final TokenType type;
    final String lexeme;
    final String file;
    final Object literal;
    final int line;

    public Token(TokenType type, String lexeme, String file, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.file = file;
        this.literal = literal;
        this.line = line;
    }

    public String toString(){
        return type + " " + lexeme + " " + literal;
    }

}
