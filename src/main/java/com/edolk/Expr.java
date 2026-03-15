package com.edolk;

import java.util.List;

abstract class Expr {
  interface Visitor<R> {
    R visitAssignExpr(Assign expr);
    R visitBinaryExpr(Binary expr);
    R visitCallExpr(Call expr);
    R visitGetExpr(Get expr);
    R visitGroupingExpr(Grouping expr);
    R visitLiteralExpr(Literal expr);
    R visitLogicalExpr(Logical expr);
    R visitSetExpr(Set expr);
    R visitThisExpr(This expr);
    R visitUnaryExpr(Unary expr);
    R visitVariableExpr(Variable expr);
    R visitFunctionLiteralExpr(FunctionLiteral expr);
    R visitArrayLiteralExpr(ArrayLiteral expr);
    R visitGetArrayExpr(GetArray expr);
    R visitSetArrayExpr(SetArray expr);
    R visitSliceArrayExpr(SliceArray expr);
  }
  static class Assign extends Expr {
    Assign(Token name, Expr value) {
      this.name = name;
      this.value = value;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitAssignExpr(this);
    }

    final Token name;
    final Expr value;
  }
  static class Binary extends Expr {
    Binary(Expr left, Token operator, Expr right) {
      this.left = left;
      this.operator = operator;
      this.right = right;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitBinaryExpr(this);
    }

    final Expr left;
    final Token operator;
    final Expr right;
  }
  static class Call extends Expr {
    Call(Expr callee, Token paren, List<Expr> arguments) {
      this.callee = callee;
      this.paren = paren;
      this.arguments = arguments;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitCallExpr(this);
    }

    final Expr callee;
    final Token paren;
    final List<Expr> arguments;
  }
  static class Get extends Expr {
    Get(Expr object, Token name) {
      this.object = object;
      this.name = name;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitGetExpr(this);
    }

    final Expr object;
    final Token name;
  }
  static class Grouping extends Expr {
    Grouping(Expr expression) {
      this.expression = expression;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitGroupingExpr(this);
    }

    final Expr expression;
  }
  static class Literal extends Expr {
    Literal(Object value) {
      this.value = value;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitLiteralExpr(this);
    }

    final Object value;
  }
  static class Logical extends Expr {
    Logical(Expr left, Token operator, Expr right) {
      this.left = left;
      this.operator = operator;
      this.right = right;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitLogicalExpr(this);
    }

    final Expr left;
    final Token operator;
    final Expr right;
  }
  static class Set extends Expr {
    Set(Expr object, Token name, Expr value) {
      this.object = object;
      this.name = name;
      this.value = value;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitSetExpr(this);
    }

    final Expr object;
    final Token name;
    final Expr value;
  }
  static class This extends Expr {
    This(Token keyword) {
      this.keyword = keyword;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitThisExpr(this);
    }

    final Token keyword;
  }
  static class Unary extends Expr {
    Unary(Token operator, Expr right) {
      this.operator = operator;
      this.right = right;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitUnaryExpr(this);
    }

    final Token operator;
    final Expr right;
  }
  static class Variable extends Expr {
    Variable(Token name) {
      this.name = name;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitVariableExpr(this);
    }

    final Token name;
  }
  static class FunctionLiteral extends Expr {
    FunctionLiteral(List<Token> params, List<Stmt> body) {
      this.params = params;
      this.body = body;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitFunctionLiteralExpr(this);
    }

    final List<Token> params;
    final List<Stmt> body;
  }
  static class ArrayLiteral extends Expr {
    ArrayLiteral(List<Expr> elements, Token rightBracket, Expr sizeExpr) {
      this.elements = elements;
      this.rightBracket = rightBracket;
      this.sizeExpr = sizeExpr;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitArrayLiteralExpr(this);
    }

    final List<Expr> elements;
    final Token rightBracket;
    final Expr sizeExpr;
  }
  static class GetArray extends Expr {
    GetArray(Expr array, Expr index, Token rightBracket) {
      this.array = array;
      this.index = index;
      this.rightBracket = rightBracket;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitGetArrayExpr(this);
    }

    final Expr array;
    final Expr index;
    final Token rightBracket;
  }
  static class SetArray extends Expr {
    SetArray(Expr array, Expr index, Token rightBracket, Expr value) {
      this.array = array;
      this.index = index;
      this.rightBracket = rightBracket;
      this.value = value;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitSetArrayExpr(this);
    }

    final Expr array;
    final Expr index;
    final Token rightBracket;
    final Expr value;
  }
  static class SliceArray extends Expr {
    SliceArray(Expr array, Expr leftIndex, Expr rightIndex, Expr step, Token rightBracket) {
      this.array = array;
      this.leftIndex = leftIndex;
      this.rightIndex = rightIndex;
      this.step = step;
      this.rightBracket = rightBracket;
    }

    @Override
    <R> R accept(Visitor<R> visitor) {
      return visitor.visitSliceArrayExpr(this);
    }

    final Expr array;
    final Expr leftIndex;
    final Expr rightIndex;
    final Expr step;
    final Token rightBracket;
  }

  abstract <R> R accept(Visitor<R> visitor);
}
