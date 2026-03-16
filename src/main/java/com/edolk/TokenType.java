package com.edolk;

public enum TokenType {

    // Single-character tokens.
    LEFT_PAREN, RIGHT_PAREN, LEFT_BRACE, RIGHT_BRACE, LEFT_BRACKET, RIGHT_BRACKET,
    COMMA, DOT, MINUS, PLUS, SEMICOLON, SLASH, STAR, COLON,

    // One or two character tokens.
    BANG, BANG_EQUAL,
    EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,

    //Two character tokens.
    PLUS_EQUAL, MINUS_EQUAL,
    SLASH_EQUAL, STAR_EQUAL,

    // Three character tokens.
    ELLIPSES,

    // Literals.
    IDENTIFIER, STRING, NUMBER,

    // Keywords.
    AND, CLASS, ELSE, FALSE, FUN, FOR, IF, NIL, OR,
    RETURN, SUPER, THIS, TRUE, VAR, WHILE, SOURCE,

    EOF
}
