package com.taoyuanx.test;

import nudt.web.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Scanner;
/*
public class UserTest
{
    @Autowired
    UserService userService;


    public  void manu() throws Exception{
        System.out.println("----------------------------------------");
        System.out.println("----------------MODE--------------------");
        System.out.println("1: create");
        System.out.println("2: retrieve");
        System.out.println("3: update");
        System.out.println("4: delete");

        Scanner input = new Scanner(System.in);
        System.out.print("Key in the selection : ");

        String selectionString = input.next();

        boolean isValid = StringUtils.isNumeric(selectionString);
        if(isValid) {
            int option = Integer.parseInt(selectionString);
            if(option==1){

                System.out.print("Please input the entry dn: ");
                Scanner inputScanner = new Scanner(System.in);
                String dn = inputScanner.next();
                System.out.println("entry dn: " + dn);

                System.out.print("Please input the entry cn: ");
                inputScanner = new Scanner(System.in);
                String cn = inputScanner.next();
                System.out.println("entry cn: " + cn);

                System.out.print("Please input the p12 cert location: ");
                byte[] p12certData = getCertDataFromPath();


                System.out.println("p12 cert data read: " + new String(p12certData) );



                System.out.print("Please input the public cert location: ");
                byte[] publicCertData =getCertDataFromPath();


                System.out.println("public cert data read: " + new String(publicCertData));



                System.out.print("Please input the private cert location: ");
                byte[] privateCertData = getCertDataFromPath();


                System.out.println("private cert data read: " + new String(privateCertData));



                userService.createWithPersistObject(dn, cn, p12certData, publicCertData, privateCertData);
            }else if(option ==2){

                System.out.print("Please input the entry dn: ");
                Scanner inputScanner = new Scanner(System.in);
                String dn = inputScanner.next();

                System.out.println("entry dn: " + dn);


                System.out.print("Please input the filter(if do not have, input n): ");
                inputScanner = new Scanner(System.in);
                String filter = inputScanner.next();

                if(filter.equalsIgnoreCase("n")){
                    filter = null;
                }else{
                    System.out.println("entry filter: " + filter);
                }

                userService.search(dn, filter);


            }else if(option ==3){

                System.out.print("Please input the entry dn: ");
                Scanner inputScanner = new Scanner(System.in);
                String dn = inputScanner.next();

                System.out.println("entry dn: " + dn);

                System.out.print("Please input the entry cn: ");
                inputScanner = new Scanner(System.in);
                String cn = inputScanner.next();

                System.out.println("entry cn: " + cn);

                System.out.print("Please input the new entry dn(if do not want to update, input n): ");
                inputScanner = new Scanner(System.in);
                String newDn = inputScanner.next();

                if(newDn.equalsIgnoreCase("n")){
                    newDn = null;
                }else{
                    System.out.println("New entry dn: " + newDn);
                }

                System.out.print("Please input the new cn(if do not want to update, input n): ");
                inputScanner = new Scanner(System.in);
                String newCn = inputScanner.next();

                if(newCn.equalsIgnoreCase("n")){
                    newCn = null;
                }else{
                    System.out.println("New cn: " + newCn);
                }


                System.out.println("For below options, if no update, please input n or any other invalid path");

                System.out.print("Please input the p12 cert location: ");
                inputScanner = new Scanner(System.in);
                String certLocation = inputScanner.next();
                byte[] p12certData = getCertData(certLocation);

                if(p12certData!=null){
                    System.out.println("p12 cert data read: " + new String(p12certData) );
                }




                System.out.print("Please input the public cert location: ");
                inputScanner = new Scanner(System.in);
                certLocation = inputScanner.next();
                byte[] publicCertData = getCertData(certLocation);

                if(publicCertData!=null){
                    System.out.println("public cert data read: " + new String(publicCertData));
                }

                System.out.print("Please input the private cert location: ");
                inputScanner = new Scanner(System.in);
                certLocation = inputScanner.next();
                byte[] privateCertData = getCertData(certLocation);

                if(privateCertData!=null){
                    System.out.println("private cert data read: " + new String(privateCertData));
                }




                //userService.update(dn, cn, newDn, newCn, p12certData, publicCertData, privateCertData);

            }else if(option ==4){
                System.out.println("WARN: DELETE will delete all the sub-entries too, if exist.");
                System.out.print("Please input the entry dn: ");
                Scanner inputScanner = new Scanner(System.in);
                String dn = inputScanner.next();

                System.out.println("entry dn: " + dn);

                //userService.delete(dn);

            }else{
                System.out.println("Invalid option: " + selectionString);

            }
        } else {
            System.out.println("Invalid option: " + selectionString);

        }


    }

    public  byte[] getCertDataFromPath(){

        Scanner inputScanner = new Scanner(System.in);
        String certLocation = inputScanner.next();

        byte[] data = null;
        try{
            data = userService.readDataFromFile(certLocation);

        }catch(Exception e){
            e.printStackTrace();
            data = null;
        }
        if(data==null){
            System.out.println("Invalid cert path: " + certLocation);
            System.out.print("Please enter again: ");
            return getCertDataFromPath();
        }

        return data;
    }

     byte[] getCertData(String certLocation){

        byte[] data = null;
        try{
            data = userService.readDataFromFile(certLocation);
        }catch(Exception e){
            e.printStackTrace();
            data = null;
        }
        return data;
    }

}
*/
