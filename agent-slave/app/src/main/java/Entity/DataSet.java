package Entity;

import java.util.*;

public class DataSet {


    private int access;

    private int Major_version;
    private  String class_name;
    private String Super_class;
    private String[] interfaces;
    private String source_name;
    private ArrayList<FieldValue> fieldValues = new ArrayList<FieldValue>();

    private ArrayList<Methodvalue> methodvalues =new ArrayList<Methodvalue>();


    public ArrayList<FieldValue> getFieldValues() {
        return fieldValues;
    }

    public void setFieldValues(FieldValue fieldValue) {
        this.fieldValues.add(fieldValue);
    }

    public ArrayList<Methodvalue> getMethodvalues() {
        return methodvalues;
    }

    public void setMethodvalues(Methodvalue methodvalue) {
        this.methodvalues.add(methodvalue);
    }

    public int getMajor_version() {
        return Major_version;
    }

    public void setMajor_version(int major_version) {
        Major_version = major_version;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getSuper_class() {
        return Super_class;
    }

    public void setSuper_class(String super_class) {
        Super_class = super_class;
    }

    public String getSource_name() {
        return source_name;
    }

    public void setSource_name(String source_name) {
        this.source_name = source_name;
    }

    public String[] getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(String[] interfaces) {
        this.interfaces = interfaces;
    }


    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public void printDataset(){

        System.out.println("Access : "+ Flag.getaccessFlag(access));
        System.out.println("Class name: " + class_name);
        System.out.println("Source name: "+ source_name);
        System.out.println("Major Version: " + Major_version);
        System.out.println("Super class: " + Super_class);


        System.out.println("interface");
        for(String inter : interfaces){
            System.out.println("  "+ inter);
        }

        System.out.println("Field_value");
        System.out.format("%30s %25s %40s %30s\r\n","access", "name","desc","value");
        for(FieldValue fieldValue:fieldValues){
            fieldValue.printset();
        }
        System.out.println("\r\nMethod_value");
        System.out.format("%30s %25s %80s \r\n","access", "name","desc");
        for(Methodvalue methodvalue:methodvalues){
            methodvalue.printset();
        }

    }
}