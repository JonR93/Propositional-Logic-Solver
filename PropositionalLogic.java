
// HW #3
// DATE: 10/17/13
// DESCRIPTION: Write a program that tells you if the expression entered is a tautology or not.

import java.util.Scanner;
public class PropositionalLogic 
{
    public static void main(String[] args) 
    {
        Scanner user = new Scanner(System.in);
        String infix = "", postfix = "";
        
        // usable tokens
        System.out.println("Tokens:\n>: implication\n<: biconditional\n&: conjunction\nv: disjunction\n~: negation\n");
        
        //get a logical expression from the user
        System.out.println("Enter a logical expression:");
        infix = user.next();
        //infix = "(P&Q)>(PvQ)"; //tautology example
        //infix = "(P&~P)"; //contradiction example
        //infix = "(P>R)&(Q>R)"; //contingency example
        System.out.println("Infix expression: " + infix); 
        postfix = postfix(infix); //convert expression to postfix
        System.out.println("Postfix expression: " + postfix); //display postfix expression
        
        System.out.println("P\tQ\tR\t"+infix); //print a truth table
        System.out.println("-------------------------------------"); 
        System.out.println(infix+" is a "+evaluate(postfix)); // evaluate the postfix expression
    }
    
    private static String postfix(String infix)
    {
        LLStackADT stack = new LLStackADT(); //operator stack
        stack.push('#'); //initialize stack with # token
        
        String postfix = "";
        char c; //current opperand/opperator being processed
        
        
        
        for(int i=0; i<infix.length(); i++)
        {
            c = Character.toUpperCase(infix.charAt(i)); //process the infix expression, one opperand/opperator at a time
            if(c=='P'||c=='Q'||c=='R') //add propositions to the postfix
            {
                postfix += c;
            }
            else if(c=='>'||c=='<'||c=='V'||c=='&'||c=='~') //add operators to the postfix
            {
                while(stack.ontop()!='#' && priority(stack.ontop())>= priority(c)) //check priority
                {
                    postfix += (char)stack.pop(); //add appropriate operator to the postfix
                }
                stack.push(c);
            }
            else if(c=='(') //push '(' on the stack
            {
                stack.push(c);
            }
            else if(c==')') //when ')' is processed, pop the rest of the stack to the postfix
            {
                while(stack.ontop()!='#')
                {
                    c = (char)stack.pop();
                    if(c!='(')
                    {
                        postfix += c;
                    }
                }
            }
        }
        while(stack.ontop()!='#') //pop whats left on the stack
            {
                c = (char)stack.pop();
                postfix += c;
            }
        return postfix;
    }
    
    public static int priority(int token)
    {
        if(token=='<') //biconditional
            return 1;
        if(token=='>') //implication
            return 2;
        if(token=='V') //disjunction
            return 3;
        if(token=='&') //conjunction
            return 4;
        if(token=='~') //negation
            return 5;
        if(token=='(') //open bracket
            return 0;
            
        return 0; 
    }
    
    public static String evaluate(String postfix)
    {
        LLStackADT stack = new LLStackADT(); //value stack
        stack.push('#'); //initialize stack with # token
        
        char c; // current opperand/opperator being processed
        char t='T',f='F'; // true and false
        int p,q; //propositional variables
        int tCount=0, fCount=0; //counters for how many times True and False appear

        for(int set=0; set<8; set++)
        {
            for(int i=0; i<postfix.length(); i++)
            {
                c = postfix.charAt(i); //process the posfix expression
                if(set==0 && (c=='P' || c=='Q' || c=='R'))
                {
                    stack.push(t);
                }
                else if(set==1 && (c=='P' || c=='Q' || c=='R'))
                {
                    if(c=='P')
                        stack.push(t);
                    if(c=='Q')
                        stack.push(f);
                    if(c=='R')
                        stack.push(t);
                }
                else if(set==2 && (c=='P' || c=='Q' || c=='R'))
                {
                    if(c=='P')
                        stack.push(f);
                    if(c=='Q')
                        stack.push(t);
                    if(c=='R')
                        stack.push(t);
                    }
                else if(set==3 && (c=='P' || c=='Q' || c=='R'))
                {
                    if(c=='P')
                        stack.push(f);
                    if(c=='Q')
                        stack.push(f);
                    if(c=='R')
                        stack.push(t);
                }
                else if(set==4 && (c=='P' || c=='Q' || c=='R'))
                {
                    if(c=='P')
                        stack.push(t);
                    if(c=='Q')
                        stack.push(t);
                    if(c=='R')
                        stack.push(f);
                }
                else if(set==5 && (c=='P' || c=='Q' || c=='R'))
                {
                    if(c=='P')
                        stack.push(t);
                    if(c=='Q')
                        stack.push(f);
                    if(c=='R')
                        stack.push(f);
                }
                else if(set==6 && (c=='P' || c=='Q' || c=='R'))
                {
                    if(c=='P')
                        stack.push(f);
                    if(c=='Q')
                        stack.push(t);
                    if(c=='R')
                        stack.push(f);
                }
                else if(set==7 && (c=='P' || c=='Q' || c=='R'))
                {
                    stack.push(f);
                }
                else if(c=='>') //evaluate an implication
                {
                    q = stack.pop();
                    p = stack.pop();
                    if(p=='T' && q=='T')
                        stack.push(t); //T -> T = T
                    else if(p=='T' && q=='F')
                        stack.push(f); //T -> F = F
                    else if(p=='F' && q=='T')
                        stack.push(t); //F -> T = T
                    else if(p=='F' && q=='F')
                        stack.push(t); //F -> F = T
                }   
                else if(c=='~') //evaluate a negation
                {
                    p = stack.pop();
                    if(p=='T')
                        stack.push(f); // ~T = F
                    else if(p=='F')
                        stack.push(t); // ~F = T
                }
                else if(c=='<') //evaluate a biconditional
                {
                    q = stack.pop();
                    p = stack.pop();
                    if(p=='T' && q=='T')
                        stack.push(t); //T <--> T = T
                    else if(p=='T' && q=='F')
                        stack.push(f); //T <--> F = F
                    else if(p=='F' && q=='T')
                        stack.push(f); //F <--> T = F
                    else if(p=='F' && q=='F')
                        stack.push(t); //F <--> F = T
                }
                else if(c=='&') //evaluate a conjunction
                {
                    q = stack.pop();
                    p = stack.pop();
                    if(p=='T' && q=='T')
                        stack.push(t); //T & T = T
                    else if(p=='T' && q=='F')
                        stack.push(f); //T & F = F
                    else if(p=='F' && q=='T')
                        stack.push(f); //F & T = F
                    else if(p=='F' && q=='F')
                        stack.push(f); //F & F = F
                }
                else if(c=='V') //evaluate a disjunction
                {
                    q = stack.pop();
                    p = stack.pop();
                    if(p=='T' && q=='T')
                        stack.push(t); //T v T = T
                    else if(p=='T' && q=='F')
                        stack.push(t); //T v F = T
                    else if(p=='F' && q=='T')
                        stack.push(t); //F v T = T
                    else if(p=='F' && q=='F')
                        stack.push(f); //F v F = F
                }
            }
            c = (char)stack.pop(); //pop truth value of the current case
            if(set==0)
                System.out.print("T\tT\tT\t");
            if(set==1)
                System.out.print("T\tF\tT\t");
            if(set==2)
                System.out.print("F\tT\tT\t");
            if(set==3)
                System.out.print("F\tF\tT\t");
            if(set==4)
                System.out.print("T\tT\tF\t");
            if(set==5)
                System.out.print("T\tF\tF\t");
            if(set==6)
                System.out.print("F\tT\tF\t");
            if(set==7)
                System.out.print("F\tF\tF\t");
                
            System.out.println(c); //print truth value for user entered expression
            if(c=='T') 
                tCount++; //count truths
            else
                fCount++; //count falses
        }
        //Determine the type of the logical expression
        if(tCount==8)//all cases are true
            return "tautology.";
        else if(fCount==8) //all cases are false
            return "contradiction.";
        else //mixed case
            return "contingency.";
    } 
}



