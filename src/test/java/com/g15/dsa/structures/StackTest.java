package com.g15.dsa.structures;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.EmptyStackException;

class StackTest {

    private Stack<String> stack;

    @BeforeEach
    void setUp() { stack = new Stack<>(); }

    // === NORMAL CASES ===
    @Test void pushAndPeek() { stack.push("A"); assertEquals("A", stack.peek()); }
    @Test void pushAndPop() { stack.push("A"); stack.push("B"); assertEquals("B", stack.pop()); }
    @Test void lifoOrder() { stack.push("A"); stack.push("B"); stack.push("C"); assertEquals("C", stack.pop()); assertEquals("B", stack.pop()); }
    @Test void size() { stack.push("X"); stack.push("Y"); assertEquals(2, stack.size()); }
    @Test void peekDoesNotRemove() { stack.push("Z"); stack.peek(); assertEquals(1, stack.size()); }
    @Test void search_found() { stack.push("a"); stack.push("b"); stack.push("c"); assertEquals(2, stack.search("b")); }
    @Test void search_notFound() { stack.push("a"); assertEquals(-1, stack.search("z")); }

    // === BOUNDARY CASES ===
    @Test void emptyIsEmpty() { assertTrue(stack.isEmpty()); }
    @Test void singlePushPop() { stack.push("only"); assertEquals("only", stack.pop()); assertTrue(stack.isEmpty()); }
    @Test void clearStack() { stack.push("a"); stack.push("b"); stack.clear(); assertTrue(stack.isEmpty()); assertEquals(0, stack.size()); }
    @Test void toStringEmpty() { assertEquals("[]", stack.toString()); }

    // === INVALID INPUT CASES ===
    @Test void popEmpty() { assertThrows(EmptyStackException.class, () -> stack.pop()); }
    @Test void peekEmpty() { assertThrows(EmptyStackException.class, () -> stack.peek()); }
}
