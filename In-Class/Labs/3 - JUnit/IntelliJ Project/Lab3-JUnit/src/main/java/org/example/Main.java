package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Person p = new Person();
        String testName = "Derek";
        p.setName(testName);
        if (testName.equals(p.getName())) {
            System.out.println("Person Get/Set Name: Pass");
        }
        else
        {
            System.out.println("Person Get/Set Name: FAIL!");
        }
    }
}