package cs445.a2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.lang.Math;

/**
 * This class uses two stacks to evaluate an infix arithmetic expression from an
 * InputStream. It should not create a full postfix expression along the way; it
 * should convert and evaluate in a pipelined fashion, in a single pass.
 */
public class InfixExpressionEvaluator {
    // Tokenizer to break up our input into tokens
    StreamTokenizer tokenizer;

    // Stacks for operators (for converting to postfix) and operands (for
    // evaluating)
    StackInterface<Character> operatorStack;
    StackInterface<Double> operandStack;

    /**
     * Initializes the evaluator to read an infix expression from an input
     * stream.
     * @param input the input stream from which to read the expression
     */
    public InfixExpressionEvaluator(InputStream input) {
        // Initialize the tokenizer to read from the given InputStream
        tokenizer = new StreamTokenizer(new BufferedReader(
                        new InputStreamReader(input)));

        // StreamTokenizer likes to consider - and / to have special meaning.
        // Tell it that these are regular characters, so that they can be parsed
        // as operators
        tokenizer.ordinaryChar('-');
        tokenizer.ordinaryChar('/');

        // Allow the tokenizer to recognize end-of-line, which marks the end of
        // the expression
        tokenizer.eolIsSignificant(true);

        // Initialize the stacks
        operatorStack = new ArrayStack<Character>();
        operandStack = new ArrayStack<Double>();
    }

    /**
     * Parses and evaluates the expression read from the provided input stream,
     * then returns the resulting value
     * @return the value of the infix expression that was parsed
     */
    public Double evaluate() throws ExpressionError {
        // Get the first token. If an IO exception occurs, replace it with a
        // runtime exception, causing an immediate crash.
        try {
            tokenizer.nextToken();
			switch(tokenizer.ttype){
				case '+':
                case '-':
                case '*':
                case '/':
                case '^':
				throw new ExpressionError("Expression begins with operator");
			}
				
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Continue processing tokens until we find end-of-line
        while (tokenizer.ttype != StreamTokenizer.TT_EOL) {
            // Consider possible token types
            switch (tokenizer.ttype) {
                case StreamTokenizer.TT_NUMBER:
                    // If the token is a number, process it as a double-valued
                    // operand
					try{
					tokenizer.nextToken();
					switch(tokenizer.ttype){
					case StreamTokenizer.TT_NUMBER:
					//if(tokenizer.ttype == StreamTokenizer.TT_NUMBER ){
						throw new ExpressionError("Two operands in a row");
					case '(':
					case '<':
						throw new ExpressionError("Missing operator between opearnd and parentheses");
					
					}
                    }catch(IOException e) {
					throw new RuntimeException(e);
					}
					tokenizer.pushBack();
					
					handleOperand((double)tokenizer.nval);
					
                    break;
                case '+':
                case '-':
                case '*':
                case '/':
                case '^':
                    // If the token is any of the above characters, process it
                    // is an operator
					handleOperator((char)tokenizer.ttype);
					try{
					tokenizer.nextToken();
					switch (tokenizer.ttype){
						case '+':
						case '-':
						case '*':
						case '/':
						case '^':
							throw new ExpressionError("Two operators in a row");
					}
					}catch(IOException e) {
						throw new RuntimeException(e);
					}
					tokenizer.pushBack();
					
                    break;
                case '(':
                case '<':
                    // If the token is open bracket, process it as such. Forms
                    // of bracket are interchangeable but must nest properly.
                    handleOpenBracket((char)tokenizer.ttype);
					try{
					tokenizer.nextToken();
					switch(tokenizer.ttype){
						case ')':
						case '>':
							throw new ExpressionError("Empty brackets");
						
						case '+':
						case '-':
						case '*':
						case '/':
						case '^':
							throw new ExpressionError("Operator after open bracket");
					}
					}catch(IOException e) {
						throw new RuntimeException(e);
					}
					tokenizer.pushBack();
					
                    break;
                case ')':
                case '>':
                    // If the token is close bracket, process it as such. Forms
                    // of bracket are interchangeable but must nest properly.
                    handleCloseBracket((char)tokenizer.ttype);
					try{
					tokenizer.nextToken();
					if(tokenizer.ttype == StreamTokenizer.TT_NUMBER)
						throw new ExpressionError("Operand after closed bracket");
					}catch(IOException e){
						throw new RuntimeException(e);
					}
					tokenizer.pushBack();
					
                    break;
                case StreamTokenizer.TT_WORD:
                    // If the token is a "word", throw an expression error
                    throw new ExpressionError("Unrecognized token: " +
                                    tokenizer.sval);
                default:
                    // If the token is any other type or value, throw an
                    // expression error
                    throw new ExpressionError("Unrecognized token: " +
                                    String.valueOf((char)tokenizer.ttype));
            }

            // Read the next token, again converting any potential IO exception
            try {
                tokenizer.nextToken();
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }

        // Almost done now, but we may have to process remaining operators in
        // the operators stack
        handleRemainingOperators();

        // Return the result of the evaluation
        // TODO: Fix this return statement
        return operandStack.pop();
    }

    /**
     * This method is called when the evaluator encounters an operand. It
     * manipulates operatorStack and/or operandStack to process the operand
     * according to the Infix-to-Postfix and Postfix-evaluation algorithms.
     * @param operand the operand token that was encountered
     */
    void handleOperand(double operand) {
    
		operandStack.push(operand);
	
    }

    /**
     * This method is called when the evaluator encounters an operator. It
     * manipulates operatorStack and/or operandStack to process the operator
     * according to the Infix-to-Postfix and Postfix-evaluation algorithms.
     * @param operator the operator token that was encountered
     */
	 
	 private int precedenceValue(char operator)
	 {
		 int op = 0;
		if(operator == '^')
			op = 3;
		else if(operator == '*' || operator == '/')
			op = 2;
		else if(operator == '+'|| operator == '-')
			op = 1;
		 
		return op;
	 }
		
	 void handleOperator(char operator) {
		
		if(!operatorStack.isEmpty())
		{
			int stackOp = precedenceValue(operatorStack.peek());
			int op = precedenceValue(operator);
		
		while(!operatorStack.isEmpty() && op <= stackOp)
		{			
			char topOperator = operatorStack.pop();
			evaluator(topOperator);
			if(!operatorStack.isEmpty())
				stackOp = precedenceValue(operatorStack.peek());
			else
				stackOp = 5;
		}
		}
		operatorStack.push(operator);
		
    }
	
	private void evaluator(char operator)
	{
			double num2 = operandStack.pop();
			double num1 = operandStack.pop();
			double value = 0;
			
			if(operator == '+')
			{
				value = num1 + num2;
				operandStack.push(value);
			}
			else if(operator == '-')
			{
				value = num1 - num2;
				operandStack.push(value);
			}
			else if(operator == '*')
			{
				value = num1 * num2;
				operandStack.push(value);
			}
			else if(operator == '/')
			{
				value = num1 / num2;
				operandStack.push(value);
			}
			else if(operator == '^')
			{
				value = Math.pow(num1, num2);
				operandStack.push(value);
			}
	}

    /**
     * This method is called when the evaluator encounters an open bracket. It
     * manipulates operatorStack and/or operandStack to process the open bracket
     * according to the Infix-to-Postfix and Postfix-evaluation algorithms.
     * @param openBracket the open bracket token that was encountered
     */
    void handleOpenBracket(char openBracket) {
		operatorStack.push(openBracket);
    }

    /**
     * This method is called when the evaluator encounters a close bracket. It
     * manipulates operatorStack and/or operandStack to process the close
     * bracket according to the Infix-to-Postfix and Postfix-evaluation
     * algorithms.
     * @param closeBracket the close bracket token that was encountered
     */
    void handleCloseBracket(char closeBracket) {
    
		char open;

		boolean found = false;
		
		if(closeBracket == '>')
			open = '<';
		else
			open = '(';
		
		while(found != true && !operatorStack.isEmpty())
		{
			char op = operatorStack.pop();
			if(closeBracket == ')')
			{
				if(op == '<')
				{
				throw new ExpressionError("Mismatched brackets");
				}
			
			}
			else if(closeBracket == '>')
			{
				if(op == '(')
				{
					throw new ExpressionError("Mismatched brackets");
				}
			}
			if(op == open)
			{
				found = true;
			}
			else
			{
				evaluator(op);
			}
		}
		if(found == false)
		{
			throw new ExpressionError("No open bracket");
			
		}
	}

    /**
     * This method is called when the evaluator encounters the end of an
     * expression. It manipulates operatorStack and/or operandStack to process
     * the operators that remain on the stack, according to the Infix-to-Postfix
     * and Postfix-evaluation algorithms.
     */
    void handleRemainingOperators() {
    		double num2 = 0, num1 = 0, value = 0;
			
			while(operatorStack.isEmpty() !=  true)
			{
			
			if(!operandStack.isEmpty())
				num2 = operandStack.pop();
			if(!operandStack.isEmpty())
				num1 = operandStack.pop();
			
			char operator = operatorStack.pop();
			
			if(operator == '+')
			{
				value = num1 + num2;
				operandStack.push(value);
			}
			else if(operator == '-')
			{
				value = num1 - num2;
				operandStack.push(value);
			}
			else if(operator == '*')
			{
				value = num1 * num2;
				operandStack.push(value);
			}
			else if(operator == '/')
			{
				value = num1 / num2;
				operandStack.push(value);
			}
			else if(operator == '^')
			{
				value = Math.pow(num1, num2);
				operandStack.push(value);
			}
			else if(operator == '(' || operator == '<')
			{
				throw new ExpressionError("Missing closing bracket");
			}
			}
	
    }


    /**
     * Creates an InfixExpressionEvaluator object to read from System.in, then
     * evaluates its input and prints the result.
     * @param args not used
     */
    public static void main(String[] args) {
        System.out.println("Infix expression:");
        InfixExpressionEvaluator evaluator =
                        new InfixExpressionEvaluator(System.in);
        Double value = null;
        try {
            value = evaluator.evaluate();
        } catch (ExpressionError e) {
            System.out.println("ExpressionError: " + e.getMessage());
        }
        if (value != null) {
            System.out.println(value);
        } else {
            System.out.println("Evaluator returned null");
        }
    }

}

