#include "Calculator.h"

int Calculator::add(int a, int b) {
    return a + b;
}

int Calculator::subtract(int a, int b) {
    return a - b;
}

int Calculator::multiply(int a, int b) {
    return a * b;
}

float Calculator::divide(float a, float b) {
    if (b == 0) {
        // Avoid division by zero
        return 0;
    }
    return a / b;
}