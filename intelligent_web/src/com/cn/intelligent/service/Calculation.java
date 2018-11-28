package com.cn.intelligent.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Calculation {
	private final int len;
	private final int max;
	private final int type;
	private String expression;
	private int result;
	private int index;
	private final int ran = 101;
	private final static String ADD = "+";
	private final static String SUB = "-";
	private final static String MULTIPLY = "x";
	private final static String DIVISION = "÷";
	
	public Calculation(int _len, int _max, int _type) {
		this.len = _len;
		this.max = _max;
		this.type = _type;
		initialize();
	}
	
	private void initialize() {
		this.result = RandomNextInt(this.max);
		this.expression = this.result + "";
		this.index = 1;
	}
	
	private int RandomNextInt(int bound) {
		if (bound < 1) {
			return 0;
		}
		Random ran = new Random();
		return ran.nextInt(bound);
	}
	
	private List<Integer> ys(int num) {
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 2; i < num; i++) {
			if(num%i == 0) {
				list.add(i);
			}
		}
		return list;
	}
	
	private void add() {
		int num = RandomNextInt(this.max - this.result);
		if (RandomNextInt(ran)%2 == 0) {
			this.expression = this.expression + ADD + num;
		} else {
			this.expression = num + ADD + this.expression;
		}
		this.result += num;
		this.index++;
	}
	
	private void sub() {
		int num = RandomNextInt(this.max);
		if (num > this.result) {
			if (this.index > 1) {
				this.expression = num + SUB + "(" + this.expression + ")";
			} else {
				this.expression = num + SUB + this.expression;
			}
			this.result = num - this.result;
		} else {
			this.expression = this.expression + SUB + num;
			this.result -= num;
		}
		this.index++;
	}
	
	private void multiply() {
		int num;
		if (this.result == 0) {
			if (RandomNextInt(ran)%10 > 0) {
				return;
			}
			num = RandomNextInt(this.max);
		} else {
			num = RandomNextInt(this.max/this.result);
		}
		
		if (num == 0 && RandomNextInt(ran)%10 > 0) {
			return;
		}
		
		if (this.index > 1) {
			this.expression = "(" + this.expression + ")";
		}
		if (RandomNextInt(ran)%2 == 0) {
			this.expression = this.expression + MULTIPLY + num;
		} else {
			this.expression = num + MULTIPLY + this.expression;
		}
		
		this.result *= num;
		this.index++;
	}
	
	private void division1() {
		int num;
		if(this.result == 0) {
			if (RandomNextInt(ran)%10 > 0) {
				return;
			}
			num = RandomNextInt(this.max);
		}else {
			List<Integer> list = ys(this.result);
			if (list.size() == 0) {
				return;
			}
			num = list.get(RandomNextInt(list.size()));
		}
		
		if (num == 0 && RandomNextInt(ran)%10 > 0) {
			return;
		}
		
		if (this.index > 1) {
			this.expression = "(" + this.expression + ")";
		}
		this.expression = this.expression + DIVISION + num;
		this.result /= num;
		this.index++;
	}
	
	private void division2() {
		if(this.result == 0) {
			return;
		}
		int num = RandomNextInt(this.max/this.result);
		
		if (num == 0 && RandomNextInt(ran)%10 > 0) {
			return;
		}
		
		if (this.index > 1) {
			this.expression = "(" + this.expression + ")";
		}
		this.expression = (num*this.result) + DIVISION + this.expression;
		this.result = num;
		this.index++;
	}
	
	private void division() {
		if (RandomNextInt(ran)%2 == 0) {
			division1();
		} else {
			division2();
		}
	}
	
	private void calType0() {
		while(this.index < this.len) {
			switch (RandomNextInt(ran)%2) {
			case 0:
				add();
				break;
			case 1:
				sub();
				break;
			default:
				break;
			}
		}
	}
	
	private void calType1() {
		while(this.index < this.len) {
			switch (RandomNextInt(ran)%4) {
			case 0:
				add();
				break;
			case 1:
				sub();
				break;
			case 2:
				multiply();
				break;
			case 3:
				division();
				break;
			default:
				break;
			}
		}
	}
	
	private void cal() {
		switch (this.type) {
		case 0:
			calType0();
			break;
		case 1:
			calType1();
			break;
		default:
			break;
		}
	}
	
	public Set<String> getData() {
		Set<String> set = new HashSet<String>();
		while (set.size() < 40) {
			this.cal();
			set.add(this.expression+"="+this.result);
			this.initialize();
		}
		return set;
	}
	
	public static void main(String[] args) {
		Calculation cal = new Calculation(2, 100, 1);
		Map<String,Integer> map = new HashMap<String, Integer>();
		while (map.keySet().size() < 40) {
			cal.cal();
			map.put(cal.expression, cal.result);
			cal.initialize();
		}
		
		for (String key : map.keySet()) {
			System.out.println(key+"="+map.get(key));
		}
		
	}
	
}
