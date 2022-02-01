package com.eliotlash.molang;

import com.eliotlash.molang.expressions.MolangExpression;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MolangParserTest {
    MolangParser parser;

    @BeforeEach
    void setUp() {
        parser = new MolangParser();
    }

    @Test
    void setValue() throws MolangException {
        MolangExpression expr = parser.parseOneLine("foo + 1");
        parser.setValue("foo", 1.0);
        assertEquals(2.0, expr.get());
    }

    @Test
    void getVariable() throws MolangException {
        assertNotNull(parser.getVariable("bar"));
    }

    @Test
    void parseJson() throws MolangException {
        JsonPrimitive primitive = new JsonPrimitive("2 + 2");
        MolangExpression expr = parser.parseJson(primitive);
        assertEquals(4.0, expr.get());
    }

    @Test
    void parseExpression() throws MolangException {
        MolangExpression expr = parser.parseExpression("temp.baz = 1; temp.baz + 1;");
        assertEquals(2.0, expr.get());
    }

    @Test
    void parseOneLine() throws MolangException {
        MolangExpression expr = parser.parseOneLine("2 + 2");
        assertEquals(4.0, expr.get());
    }

    @Test
    void isOperator() {
        assertTrue(parser.isOperator("+"));
        assertTrue(parser.isOperator("-"));
        assertTrue(parser.isOperator("/"));
        assertTrue(parser.isOperator("*"));
        assertTrue(parser.isOperator("%"));
        assertFalse(parser.isOperator("0"));
    }
}
