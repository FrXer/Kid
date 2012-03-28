package com.kid.math;

import java.util.Random;
import java.util.Stack;

public class Util {
	static Stack<Character> operatorStack = new Stack<Character>();
	static Stack<Integer> numberStack = new Stack<Integer>();

	/**
	 * 传入需要解析的字符串，返回计算结果(此处因为时间问题，省略合法性验证)
	 * 
	 * @param expression
	 *            需要进行技术的表达式
	 * @return 计算结果
	 */
	public static int caculate(String expression) {

		String tempString;
		// 2.循环开始解析字符串，当字符串解析完，且符号栈为空时，则计算完成
		StringBuffer tempNum = new StringBuffer();// 用来临时存放数字字符串(当为多位数时)
		StringBuffer expressionString = new StringBuffer().append(expression).append('#');// 用来保存，提高效率

		while (expressionString.length() != 0) {
			tempString = expressionString.substring(0, 1);
			expressionString.delete(0, 1);

			if (isNum(tempString)) {
				// 当为数字时
				tempNum = tempNum.append(tempString);// 将读到的这一位数接到以读出的数后(当不是个位数的时候)

			} else {
				// 为操作符时
				// 1.此时的tempNum内即为需要操作的数，取出数，压栈，并且清空tempNum
				if (!"".equals(tempNum.toString())) {
					int num = Integer.parseInt(tempNum.toString());
					numberStack.push(num);
					tempNum = new StringBuffer();
				}
				// 用当前取得的运算符与运算符栈顶运算符比较优先级：
				// 若高于，入栈；
				// 若等于，运算符栈顶元素出栈，取出操作数运算；
				// 若小于，运算符栈顶元素出栈，运算，将结果入操作数栈。

				// 判断当前运算符与运算符栈顶元素优先级，取出元素，进行计算(因为优先级可能小于栈顶元素，还小于第二个元素等等，需要用循环判断)
				while (!compareOperator(tempString.charAt(0)) && (!operatorStack.empty())) {
					int a = (int) numberStack.pop();// 第二个运算数
					int b = (int) numberStack.pop();// 第一个运算数
					char ope = operatorStack.pop();
					int result = 0;// 运算结果
					switch (ope) {
					case '+':
						result = b + a;
						numberStack.push(result);
						break;
					case '-':
						result = b - a;
						numberStack.push(result);
						break;
					case '×':
						result = b * a;
						numberStack.push(result);
						break;
					case '÷':
						result = b / a;
						numberStack.push(result);
						break;
					}
					System.out.println("result " + result + "is pushed into numStack");

				}
				// 计算完后，当前操作符号放入操作符栈
				if (tempString.charAt(0) != '#') {
					if (!operatorStack.empty()) {
						System.out.println("lastElement is " + operatorStack.lastElement().toString());
					}
					operatorStack.push(new Character(tempString.charAt(0)));
					// 当栈顶为'('，而当前元素为')'时，则是括号内以算完，去掉括号
					if (tempString.charAt(0) == ')') {
						operatorStack.pop();
						operatorStack.pop();
					}
				}
			}
		}
		int i = numberStack.pop();
		return i;
	}

	/**
	 * 判断传入的字符是不是0-9的数字
	 * 
	 * @param str
	 *            传入的字符串
	 * @return
	 */
	private static boolean isNum(String temp) {
		return temp.matches("[0-9]");
	}

	/**
	 * 比较当前操作符与栈顶元素操作符优先级，如果比栈顶元素优先级高，则返回true，否则返回false
	 * 
	 * @param operatorCurrent
	 *            需要进行比较的字符
	 * @return 比较结果 true代表比栈顶元素优先级高，false代表比栈顶元素优先级低
	 */
	static boolean compareOperator(char operatorCurrent) {
		// 当为空时，显然 当前优先级最低，返回高
		if (operatorStack.empty()) {
			return true;
		}

		char last = (char) operatorStack.lastElement();

		// 如果栈顶为'('显然，优先级最低，')'不可能为栈顶。
		if (last == '(') {
			return true;
		}

		switch (operatorCurrent) {
		case '#':
			// 结束符
			return false;
		case '(':
			// '('优先级最高,返回true
			return true;
		case ')':
			// ')'优先级最低，
			return false;
		case '×': {
			// '*/'优先级只比'+-'高
			if (last == '+' || last == '-')
				return true;
			else
				return false;
		}
		case '÷': {
			if (last == '+' || last == '-')
				return true;
			else
				return false;
		}
		// '+-'为最低，一直返回false
		case '+':
			return false;
		case '-':
			return false;
		}
		return true;
	}

	public static CharSequence generateRandomExpression(int range, boolean correct) {
		String expression = "";
		int result = 0;

		Random random = new Random();
		int randomInt1 = random.nextInt(range) + 1;
		int randomInt2 = random.nextInt(range) + 1;

		// Let Int2 < Int1
		while (randomInt2 >= randomInt1) {
			randomInt2 = random.nextInt(range) + 1;
		}
		int wrongFactor = random.nextInt(9) + 1;

		char op = getRandomOperation();

		if (correct) {
			// Generate correct expression
			if (op != '÷') {
				expression = String.valueOf(randomInt1) + op + String.valueOf(randomInt2);
				result = Util.caculate(expression);
			} else {
				expression = String.valueOf(randomInt1 * randomInt2) + op + String.valueOf(randomInt2);
				result = randomInt1;
			}
		} else {
			// Generate wrong expression
			if (op != '÷') {
				expression = String.valueOf(randomInt1 + wrongFactor) + op + String.valueOf(randomInt2);
				result = Util.caculate(String.valueOf(randomInt1) + op + String.valueOf(randomInt2));
			} else {
				expression = String.valueOf((randomInt1 + wrongFactor) * randomInt2) + op + String.valueOf(randomInt2);
				result = randomInt1;
			}
		}
		// System.out.printf("correct? %b, randomInt1 %d, randomInt2 %d, randomInt3 %d and expression %s = %s ", correct, randomInt1, randomInt2, wrongFactor, expression, result);
		return expression + " = " + result;

	}

	private static char getRandomOperation() {
		Random random = new Random();
		int randomOperation = (random.nextInt() + 1) % 4;
		char op = '+';
		// Get operation
		switch (randomOperation) {
		case 0:
			op = '+';
			break;
		case 1:
			op = '-';
			break;
		case 2:
			op = '×';
			break;
		case 3:
			op = '÷';
			break;
		}
		return op;
	}

	public static void main(String args[]) {
		int t = caculate("37+4*(401-5-2*10*3)*100");
		System.out.println(t);
	}
}
